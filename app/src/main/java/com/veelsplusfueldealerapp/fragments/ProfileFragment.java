package com.veelsplusfueldealerapp.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.HomeActivity;
import com.veelsplusfueldealerapp.adapters.MyProfileTabAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.RetrofitInstanceWithoutToken;
import com.veelsplusfueldealerapp.models.UserProfileModel;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    TextView textviewUserName, textViewContact, textViewDealerName, textViewAddress;
    ProgressDialog progressDialog;
    CommonCodeManager commonCodeManager;
    CircleImageView profilePic;
    private View view;
    private FloatingActionButton imageviewProfile;
    private File photoFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        CommonCodeManager commonCodeManager = new CommonCodeManager(getActivity());

        UserProfileModel userProfileModel = commonCodeManager.getUserProfileInfo(getActivity());
        textviewUserName.setText(userProfileModel.getUsername());
        textViewContact.setText(userProfileModel.getUserPhone());
        textViewAddress.setText(userProfileModel.getDealerAddress());
        textViewDealerName.setText(userProfileModel.getCompanyName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_profile_fragment, container, false);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        initUI();
        return view;
    }

    private void initUI() {
        commonCodeManager = new CommonCodeManager(getActivity());
        profilePic = view.findViewById(R.id.profilePic);

        textviewUserName = view.findViewById(R.id.textview_username_profile);
        textViewAddress = view.findViewById(R.id.textview_address);
        textViewContact = view.findViewById(R.id.textview_contact);
        textViewDealerName = view.findViewById(R.id.textview_dealer_name);


        imageviewProfile = view.findViewById(R.id.fab);
        imageviewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission();

            }
        });

    }

    private void requestStoragePermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getActivity(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            selectImage(getActivity());

                          /*  Intent intent = new Intent(getActivity(), CameraGallaryActivity.class);
                            intent.putExtra("labelforbutton", labelForButton);
                            getActivity().startActivity(intent);*/

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getResources().getString(R.string.need_permission));
        builder.setMessage(getActivity().getResources().getString(R.string.permission));
        builder.setPositiveButton(getActivity().getResources().getString(R.string.goto_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getActivity().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose File To Upload");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    photoFile = createImageFile();
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(cameraIntent, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void uploadImage(byte[] imageBytes, final Context context) {


        Handler handler1 = new Handler(getActivity().getMainLooper());
        handler1.post(new Runnable() {
            public void run() {
                progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
                progressDialog.setMessage(getActivity().getResources().getString(R.string.upload_in_progress));
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            }
        });

        GetDataService service = RetrofitInstanceWithoutToken.getRetrofitInstance(getActivity()).create(GetDataService.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        MultipartBody.Part body = null;

        Call<ResponseBody> call = null;
        //check for all three apis


        Handler handler = new Handler(getActivity().getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                //textViewResult3.setText("Please wait upload in progress!");
            }
        });
        body = MultipartBody.Part.createFormData("image", "imageProfile.jpg", requestFile);
        call = service.uploadFile(body);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();

                        }
                    });
                    JSONObject mainObject = new JSONObject(response.body().string());
                    final String jsonData = response.body().toString();
                    Log.d(TAG, "onResponse:uploadFile :  " + jsonData);
                    final String result = mainObject.getString("image");
                    Log.d(TAG, "onResponse: result : "+result);
                    if (!result.isEmpty()) {
                        updateImageResult(context, "Uploaded", result);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();

                        }
                    });
                    updateImageResult(context, getActivity().getResources().getString(R.string.unable_to_upload), "");

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Handler handler = new Handler(context.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();

                    }
                });
                updateImageResult(context, context.getResources().getString(R.string.unable_to_connect), "");

            }


        });

    }

    private void updateImageResult(Context context, String uploadResult, String imageName) {


        //textViewResult3.setText(uploadResult);
        apiCallForSendUploadedImageLinkToServer(context, imageName);


        Toast.makeText(getActivity(), uploadResult, Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:

                    if (resultCode == RESULT_OK) {
                        if (data != null && data.getExtras() != null) {
                            try {
                                final Uri imageToencode = data.getData();

                                if (imageToencode != null) {
                                    InputStream imageStream = getActivity().getContentResolver().openInputStream(imageToencode);
                                    final Bitmap imageToEcode1 = BitmapFactory.decodeStream(imageStream);


                                } else {
                                    final Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());


                                }

                            } catch (Exception e) {
                            }
                        } else {
                            try {
                                final Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                takenImage.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                                byte[] imageArray = stream.toByteArray();
                                profilePic.setImageBitmap(takenImage);

                                //upload image
                                UploadImageAsycTask asycTask = new UploadImageAsycTask(getActivity());
                                asycTask.execute(imageArray);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } else {
                        Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                profilePic.setImageBitmap(bitmap);
                                //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                                byte[] imageArray = stream.toByteArray();
                                UploadImageAsycTask asycTask = new UploadImageAsycTask(getActivity());

                                asycTask.execute(imageArray);
                            }
                        }

                    }
                    break;
            }
        }
    }

    private File createImageFile() {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File newFile = new File(storageDir + "/" + "upload.jpg");
        return newFile;
    }

    private void apiCallForSendUploadedImageLinkToServer(final Context context,
                                                         String imageName) {


        Handler handler1 = new Handler(getActivity().getMainLooper());
        handler1.post(new Runnable() {
            public void run() {
                progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
                progressDialog.setMessage(getActivity().getResources().getString(R.string.upload_in_progress));
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            }
        });

        String[] personDetails = commonCodeManager.getEssentialsForDealer(getActivity());


        GetDataService service = RetrofitInstanceWithoutToken.getRetrofitInstance(getActivity()).create(GetDataService.class);
        Call<ResponseBody> call = null;
        //check for all three apis


        Handler handler = new Handler(getActivity().getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                //textViewResult3.setText("Please wait upload in progress!");
            }
        });
        call = service.updateProfilePicture(personDetails[1], imageName);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    final String jsonData = response.body().toString();
                    Log.d(TAG, "onResponse:apiCallForSendUploadedImageLinkToServer :  " + jsonData);
                    JSONObject mainObject = new JSONObject(response.body().string());
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();

                        }
                    });
                    final String status = mainObject.getString("status").trim();
                    if (!status.isEmpty() && status.equals("OK")) {
                        updateImageResultForFinal("Uploaded", "Uploaded1");

                    }else{
                        Log.d(TAG, "onResponse: updateProfilePicture :else part ");
                        /*else {
                        updateImageResult("Unable to upload, Please try later", "Uploaded1");

                    }*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();

                        }
                    });
                    updateImageResultForFinal(getActivity().getResources().getString(R.string.unable_to_upload), "");

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Handler handler = new Handler(context.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();

                    }
                });
                updateImageResultForFinal(getActivity().getResources().getString(R.string.need_permission), "");

            }


        });

    }

    private void updateImageResultForFinal(String uploadResult, String podLink) {

        Toast.makeText(getActivity(), uploadResult, Toast.LENGTH_SHORT).show();

    }

    public class UploadImageAsycTask extends AsyncTask<byte[], Void, String> {
        private static final String TAG = "UploadDocumentAsycTask";
        private Context context;

        public UploadImageAsycTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(byte[]... bytes) {
            Log.d(TAG, "doInBackground: ");
            byte[] imageArray = bytes[0];
            uploadImage(imageArray, context);
            return null;
        }

    }


}