package com.veelsplusfueldealerapp.commonclasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.veelsplusfueldealerapp.activities.AddProductReceiptActivity;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class CommonTaskManager {
    private static final String TAG = "CommonTaskManager";
    Context context;

    public CommonTaskManager(Context context) {
        this.context = context;
    }

    public void sleepProcess(final DialogInterface dialog) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 300);
    }


    public String[] getCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        String day = (String) DateFormat.format("dd", currentDate);
        String monthString = (String) DateFormat.format("MMM", currentDate);
        String year = (String) DateFormat.format("yyyy", currentDate);
        String finalDateString = day + " " + monthString + "," + year;

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        String currentDate1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String[] dateArray = {currentDate1, dayOfTheWeek};
        return dateArray;
    }

    public String getCurrentDateNew() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        return fDate;
    }

    public String[] getBeforeAfterTodaysDates() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, +1);

        String[] dates = {dateFormat.format(cal.getTime()), dateFormat.format(cal1.getTime())};
        Log.d(TAG, "getBeforeAfterTodaysDates:start date : " + dateFormat.format(cal.getTime()));
        Log.d(TAG, "getBeforeAfterTodaysDates:end date : " + dateFormat.format(cal1.getTime()));


        return dates;
    }

    public String getCurrentDateTime() {
        Date currentDate = Calendar.getInstance().getTime();
        String day = (String) DateFormat.format("dd", currentDate);
        String monthString = (String) DateFormat.format("MMM", currentDate);
        String year = (String) DateFormat.format("yyyy", currentDate);
        String finalDateString = day + " " + monthString + "," + year;

        String currentDate1 = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(new Date());
        String currentTime1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String newDateTime = currentDate1 + " " + currentTime1;
        return newDateTime;
    }

    public String getCurrentDateTimeNewFormat() {
        Date currentDate = Calendar.getInstance().getTime();
        String day = (String) DateFormat.format("dd", currentDate);
        String monthString = (String) DateFormat.format("MMM", currentDate);
        String year = (String) DateFormat.format("yyyy", currentDate);
        String finalDateString = day + " " + monthString + "," + year;

        String currentDate1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String newDateTime = currentDate1 + " " + currentTime1;
        return newDateTime;
       /* Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        Log.e(TAG, "onCreate:date formatted string: " + sdf.format(c.getTime()));
        String formatedDate = sdf.format(c.getTime());
        return formatedDate;*/
    }

    public String getCurrentDateTimeTestFormat() {
        Date currentDate = Calendar.getInstance().getTime();
        String day = (String) DateFormat.format("dd", currentDate);
        String monthString = (String) DateFormat.format("MMM", currentDate);
        String year = (String) DateFormat.format("yyyy", currentDate);
        String finalDateString = day + " " + monthString + "," + year;

        String currentDate1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String newDateTime = currentDate1 + " " + currentTime1;
        return newDateTime;
    }


    public String getBatchIdDetails() {
        Date currentDate = Calendar.getInstance().getTime();
        String day = (String) DateFormat.format("dd", currentDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());
        String month = dateFormat.format(date);

        int num = generator.nextInt(99999) + 99999;
        if (num < 100000 || num > 999999) {
            num = generator.nextInt(99999) + 99999;
        }
        Log.d(TAG, "getBatchIdDetails: month : " + dateFormat.format(date));
        Log.d(TAG, "getBatchIdDetails: day : " + day);
        Log.d(TAG, "getBatchIdDetails: hour : " + hour);
        Log.d(TAG, "getBatchIdDetails: random no : " + num);

        String batchId = month + day + hour + num;
        Log.d(TAG, "getBatchIdDetails: batchid : " + batchId);

        return batchId;
    }

    public String getCurrentTime() {
        String currentTime1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        return currentTime1;
    }

    public void dismissProgressDialog(final Context context, final ProgressDialog progressDialog) {
        Handler handler1 = new Handler(context.getMainLooper());
        handler1.post(new Runnable() {
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();

                }
            }
        });
    }

    public void dismissDialogWithToast(final Context context, final ProgressDialog progressDialog, final String message) {
        Handler handler1 = new Handler(context.getMainLooper());
        handler1.post(new Runnable() {
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();

                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void showToast(final Context context, final String message) {
        Handler handler1 = new Handler(context.getMainLooper());
        handler1.post(new Runnable() {
            public void run() {

                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean compareDates(Context context) {
        boolean isLogout = false;
        try {
            String logoutDatePref = getSessionTimer(context);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            Date currentDateTime = df.parse(currentTime);
            Date logoutDatetime = df.parse(logoutDatePref);

            int result = currentDateTime.compareTo(logoutDatetime);


            //compare current time and logout time

            if (result > 0) {
                isLogout = true;
            } else if (result < 0) {


            } else {

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isLogout;
    }

    public String getSessionTimer(Context context) {
        String LOAD_PREF = Constants.TIMER_SESSION;
        SharedPreferences prefs = context.getSharedPreferences(LOAD_PREF, MODE_PRIVATE);
        String logouttime = prefs.getString("logouttime", "");
        return logouttime;
    }

    public void showAlertDialogWithOneButtonHandler(final Context context,
                                                    final String title,
                                                    final String message,
                                                    final String button1) {
        Handler handler1 = new Handler(context.getMainLooper());
        handler1.post(new Runnable() {
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(button1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                final AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(true);
                alertDialog.show();
            }
        });
    }

    public void showAlertDialogWithTwoButtons(final Context context,
                                              final String title,
                                              final String message,
                                              final String button1, final String button2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(button1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(button2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

    }

    /*public static int generatePin() throws Exception {
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());

        int num = generator.nextInt(99999) + 99999;
        if (num < 100000 || num > 999999) {
            num = generator.nextInt(99999) + 99999;
            if (num < 100000 || num > 999999) {
                throw new Exception("Unable to generate PIN at this time..");
            }
        }
        return num;
    }*/
}
