package test.project.firestore_sample.controls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.apache.commons.math3.util.Precision;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import test.project.firestore_sample.MainActivity;
import test.project.firestore_sample.OrderDetailsFragment;
import test.project.firestore_sample.R;

// Created by ioBirdOussama on 09/04/2017.

@SuppressWarnings("WeakerAccess")
public class Utils {
    private Utils() {}

    /**
     * Check if textInputLayout contains editText view. If so, then set text value to the view.
     *
     * @param textInputLayout wrapper for the editText view where the text value should be set.
     * @param text            text value to display.
     */
    public static void setTextToInputLayout(TextInputLayout textInputLayout, String text) {
        if (textInputLayout != null && textInputLayout.getEditText() != null) {
            textInputLayout.getEditText().setText(text);
            if (textInputLayout.isErrorEnabled()) {
                textInputLayout.setErrorEnabled(false);
            }
        }
    }

    /**
     * Check if textInputLayout contains editText view. If so, then return text value of the view.
     *
     * @param textInputLayout wrapper for the editText view.
     * @return text value of the editText view.
     */
    public static String getTextFromInputLayout(TextInputLayout textInputLayout) {
        if (textInputLayout != null && textInputLayout.getEditText() != null && !TextUtils.isEmpty(textInputLayout.getEditText().getText().toString())) {
            return textInputLayout.getEditText().getText().toString();
        } else {
            return null;
        }
    }

    public static String displayPrice(double priceValue, boolean displayCurrency, String countryID) {
        if (TextUtils.isEmpty(countryID)) {
            countryID = "FR";
        }
        NumberFormat numberFormat;
        if (displayCurrency) {
            numberFormat = NumberFormat.getCurrencyInstance(new Locale(Locale.getDefault().getLanguage(), countryID));
        } else {
            numberFormat = NumberFormat.getInstance(new Locale(Locale.getDefault().getLanguage(), countryID));
        }
        try {
            return numberFormat.format(priceValue);
        } catch (Exception ex) {
            FirebaseCrashlytics.getInstance().recordException(ex);

            numberFormat = NumberFormat.getCurrencyInstance(new Locale(Locale.getDefault().getLanguage()));
            return numberFormat.format(priceValue);
        }
    }

    public static void setPreference(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * A method to download json data from url
     */
    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        } finally {
            if (iStream != null) {
                iStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }

    //Method used for creating a popup window
    //asking the user if he want to proceed the phone call
    public static void createPhoneCallDialog(Activity mActivity, final String phoneNumber, Object restaurant) {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
            AlertDialog phoneCallPopup;

            @SuppressLint("InflateParams") View dialogLayout = LayoutInflater.from(mActivity).inflate(R.layout.custom_dialog_layout, null);

            //Set the alertDialog message
            TextView title = dialogLayout.findViewById(R.id.title);
            title.setText(mActivity.getString(R.string.phone_call, phoneNumber));
            //set the alertDialog negative button text
            TextView negativeBtn = dialogLayout.findViewById(R.id.negative_message);
            negativeBtn.setText(R.string.cancel_call);
            //set the alertDialog positive button text
            TextView positiveBtn = dialogLayout.findViewById(R.id.positive_message);
            positiveBtn.setText(R.string.yes_call);
            //Assign the custom view to the alertDialog
            dialogBuilder.setView(dialogLayout);
            //create the AlertDialog object
            phoneCallPopup = dialogBuilder.create();

            //Implement listener for positive button click
            AlertDialog finalPhoneCallPopup = phoneCallPopup;
            positiveBtn.setOnClickListener(new OnSingleClickListener() {
                @Override
                protected void onSingleClick() {
                    //Clean whitespaces from phone number
                    String phone = phoneNumber.replaceAll(" ", "");
                    try {
                        //log product click event
                        try {
                            Bundle params = new Bundle();
                            try {
                                params.putString("store_name", "My Store");
                                params.putString("user_id", Constants.USER_PROFILE_ID);
                                Utils.incrementCounter(Constants.STORES_PROFILES+"/"+"STORE_ID", Constants.PHONE_CALL_COUNT, 1, null);
                                FirebaseAnalytics.getInstance(Objects.requireNonNull(mActivity)).logEvent("phone_call_click", params);
                            } catch (Exception ignored) {}


                        } catch (Exception exx) { FirebaseCrashlytics.getInstance().recordException(exx); }
                        TelephonyManager telManager = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
                        //if the device is a phone and the sim card state equals "ready"
                        if (telManager != null && telManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE
                                && telManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(Constants.TEL + phone));
                            mActivity.startActivity(intent);
                        } else {
                            ((MainActivity) mActivity).displayMessage(R.string.needs_sim_card, android.R.color.holo_orange_light, Constants.TIME_TWO_SECONDS);
                        }
                    } catch (SecurityException e) {
                        FirebaseCrashlytics.getInstance().recordException(e);
                        if (ActivityCompat.checkSelfPermission(mActivity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            //Ask for call_phone permission
                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{android.Manifest.permission.CALL_PHONE}, Constants.EIGHT);
                        } else {
                            ((MainActivity) mActivity).displayMessage(R.string.needs_sim_card, android.R.color.holo_orange_light, Constants.TIME_TWO_SECONDS);
                        }
                    }
                    finalPhoneCallPopup.dismiss();
                }
            });
            negativeBtn.setOnClickListener(view -> finalPhoneCallPopup.dismiss());


            //Adjust the dialog window size depending on the device size
            //noinspection ConstantConditions
            phoneCallPopup.getWindow().setLayout(((Double) (Constants.widthAndHeightInDp(mActivity).first * 0.6d)).intValue(), RelativeLayout.LayoutParams.WRAP_CONTENT);
            phoneCallPopup.show();
        } catch (Exception ignored) {}
    }

    //used to check if a given Map is empty
    public static boolean isMapEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    //method used to create an alertdialog with a text field
    public static void createAlertDialog(Activity mActivity, final String message, String positiveButton, String negativeButton, int layoutId, Callable<Void> positiveFun, Callable<Void> negativeFun) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
        AlertDialog alertDialog;

        @SuppressLint("InflateParams") View dialogLayout = LayoutInflater.from(mActivity).inflate(layoutId, null);


        //Set the alert dialog message
        TextView title = dialogLayout.findViewById(R.id.title);
        title.setText(message);
        //set the alert dialog negative button text
        TextView negativeBtn = dialogLayout.findViewById(R.id.negative_message);
        negativeBtn.setText(negativeButton);
        //set the alert dialog positive button text
        TextView positiveBtn = dialogLayout.findViewById(R.id.positive_message);
        positiveBtn.setText(positiveButton);
        //Assign the custom view to the alertDialog
        dialogBuilder.setView(dialogLayout);
        //create the AlertDialog object
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);

        //Implement listener for positive button click
        AlertDialog popup = alertDialog;
        positiveBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick() {
                try {
                    positiveFun.call();
                } catch (Exception e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
                popup.dismiss();
            }
        });
        negativeBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick() {
                try {
                    negativeFun.call();
                } catch (Exception e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
                popup.dismiss();
            }
        });

        //Adjust the dialog window size depending on the device size
        try {
            //noinspection ConstantConditions
            alertDialog.getWindow().setLayout(((Double) (Constants.widthAndHeightInDp(mActivity).first * 0.6d)).intValue(), RelativeLayout.LayoutParams.WRAP_CONTENT);
        } catch (Exception ignored) {}

        alertDialog.show();
    }

    public static double roundPriceToChosenScale(double priceValue, int digitsNumber) {
        return new BigDecimal(priceValue).setScale(digitsNumber , BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //Method used to increment a counter in the database
    public static void incrementCounter(String branch, String attribute, int step, String databaseUrl) {
        Constants.dbRef()
                .child(branch)
                .child(attribute)
                .runTransaction(new com.google.firebase.database.Transaction.Handler() {
                    @NonNull
                    @Override
                    public com.google.firebase.database.Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        if (mutableData.getValue() == null) {
                            mutableData.setValue(step);
                        } else {
                            //means we found the last ticket number value
                            //lets increment it, set it to our order and update the database.
                            Integer counter = mutableData.getValue(Integer.class);
                            mutableData.setValue(counter+step);
                        }

                        return com.google.firebase.database.Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    }
                });
    }
}