package com.veelsplusfueldealerapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.commonclasses.Constants;
import com.veelsplusfueldealerapp.fragments.HomeFragment;
import com.veelsplusfueldealerapp.fragments.ProfileFragment;
import com.veelsplusfueldealerapp.managers.AboutAppDialog;
import com.veelsplusfueldealerapp.managers.AppUpdateCheckManager;
import com.veelsplusfueldealerapp.managers.FuelDealerApplication;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.UserProfileModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseCompatActivity {
    private static final String TAG = "HomeActivity";
    CoordinatorLayout layoutHome;
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    UserProfileModel userProfileModel;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private ImageView imageviewHamburger, imageviewLogout, imageViewTestUpdate;
    private String personID;
    private TextView textViewPageTitle, textViewNavCurrentUser;
    private MenuItem itemTrip, itemProfile, itemContact, itemLogout, itemAboutUs;
    private Locale myLocale;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        commonCodeManager = new CommonCodeManager(HomeActivity.this);

        View toolbar = findViewById(R.id.toolbar_custom_dasboard);
        imageviewHamburger = toolbar.findViewById(R.id.imageview_back_arrow);
        imageviewHamburger.setImageDrawable(getResources().getDrawable(R.drawable.hamburger));

        textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Dashboard");

        layoutHome = findViewById(R.id.layout_home);


        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigationview);
        View headerView = mNavigationView.getHeaderView(0);
        textViewNavCurrentUser = headerView.findViewById(R.id.textview_nav_header_username);
        profileImage = headerView.findViewById(R.id.profilePic);

        UserProfileModel userProfileModel = commonCodeManager.getUserProfileInfo(HomeActivity.this);
        textViewNavCurrentUser.setText(userProfileModel.getUsername());
        Log.d(TAG, "onCreate:textViewNavCurrentUser: " + userProfileModel.getUsername());

        String personImagePath = userProfileModel.getUserImagePath();
        if (!personImagePath.isEmpty()) {

            if (personImagePath.equals("undefined")) {

            } else if (personImagePath.equals("null")) {

            } else {
                String imageUrl = Constants.IMAGES_DOWNLOAD_LINK + personImagePath;
                Glide
                        .with(this)
                        .load(imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(profileImage);
            }

        }

        imageviewHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.LEFT);

            }
        });
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace
                (R.id.flContent, fragment).commit();
        initUI();

    }

    private void initUI() {
        commonCodeManager = new CommonCodeManager(HomeActivity.this);
        commonTaskManager = new CommonTaskManager(HomeActivity.this);
        // for testing
        //details as = fuelDealerId, fuelStaffId, fuelStaffPerformId, fuelVeelsId
       /* String[] details = {"34", "3", "", "VF00021"};
        CommonCodeManager commonCodeManager = new CommonCodeManager(HomeActivity.this);
        commonCodeManager.saveDealerDetailsOnLogin(HomeActivity.this,details);
*/
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigationview);


        View headerView = mNavigationView.getHeaderView(0);
        Menu menuNav = mNavigationView.getMenu();
        itemTrip = menuNav.findItem(R.id.nav_home);
        itemProfile = menuNav.findItem(R.id.nav_profile);
        //   itemContact = menuNav.findItem(R.id.nav_contact_us);
        itemLogout = menuNav.findItem(R.id.nav_logout);
        itemAboutUs = menuNav.findItem(R.id.nav_about_us);


        setupDrawerContent(mNavigationView);
        mNavigationView.getMenu().getItem(0).setChecked(true);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                textViewPageTitle.setText(R.string.nav_home);
                break;
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                textViewPageTitle.setText(R.string.nav_profile);
                break;
           /* case R.id.nav_contact_us:
                fragmentClass = ContactUsFragment.class;
                textViewPageTitle.setText(R.string.nav_contact_us);
                break;*/
            default:
                fragmentClass = HomeFragment.class;
                textViewPageTitle.setText(R.string.nav_home);

        }

        try {

            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment).commit();
        String backStateName = fragment.getClass().getName();
        fragmentTransaction.addToBackStack(backStateName);

        switch (menuItem.getItemId()) {
            case R.id.nav_about_us:
                setTitle(getResources().getString(R.string.nav_about_us));

                AboutAppDialog aboutAppDialog = new AboutAppDialog
                        (HomeActivity.this, HomeActivity.this);
                aboutAppDialog.show();
                break;
            case R.id.nav_logout:

                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage(R.string.want_To_logout);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PROFILE, MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();

                        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.NO_INFRA, MODE_PRIVATE);
                        sharedPreferences1.edit().clear().apply();

                        FuelDealerApplication.getInstance().clearApplicationData();

                     /*   SharedPreferences preferences = getSharedPreferences(Constants.PROFILE, 0);
                        preferences.edit().apply();
*/


                        Intent myIntent = new Intent(HomeActivity.this, LoginActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myIntent);

                        finish();

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();


                break;

        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppUpdateCheckManager appUpdateCheckManager = new AppUpdateCheckManager(HomeActivity.this);
        appUpdateCheckManager.checkForUpdate();

        boolean result = commonTaskManager.compareDates(HomeActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(HomeActivity.this, HomeActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }

}
