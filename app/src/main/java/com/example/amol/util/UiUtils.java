package com.example.amol.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class UiUtils {

    private UiUtils() {

    }

    private static String tag = UiUtils.class.getSimpleName();
//    private static String deviceId;
    private static String instanceID;

    public static boolean isEmailValidated(String str) {

        return Patterns.EMAIL_ADDRESS.matcher(str).matches() && !TextUtils.isEmpty(str);

    }


    public static boolean isStringEmpty(String str) {

        return TextUtils.isEmpty(str);
    }




     public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /*public static void fetchDeviceID(Context context) {

        String serialNumber = Build.SERIAL;
        String androidID = Settings.Secure.getString(context.getContentResolver(), "android_id");
        if (serialNumber == null || TextUtils.isEmpty(androidID)) {

            serialNumber = "0";
        }
        if (androidID == null || TextUtils.isEmpty(androidID)) {

            androidID = "0";
        }
//        deviceId = serialNumber + "-" + androidID;
         instanceID = InstanceID.getInstance(context).getId();

//        Log.e(tag, "fetchDeviceID: " + deviceId);
        Log.e(tag, "fetchDeviceID: " + instanceID);
    }*/

    public static String convertDate(String da, int sourceType) {
        SimpleDateFormat targetFormat = new SimpleDateFormat("d MMMM yyyy", Locale.US);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        if (sourceType == 1) {
            format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        }
        String dat = da;
        if ("".equalsIgnoreCase(dat)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            dat = format.format(calendar.getTime());
        }
        try {
            Date dateFormat = format.parse(dat);
            return targetFormat.format(dateFormat);
        } catch (ParseException e) {
            HelperStatic.log(tag, "" + e);
        }
        return dat;
    }

    public static String formatTime(String s) {

        String str = s;
        if (str.startsWith("0")) {
            str = str.substring(1);
        }
        str = str.replaceAll(" AM", "am");
        str = str.replaceAll(" PM", "pm");
        str = str.replaceAll(":",".");
        return str;
    }
    public static JSONObject getJSONObject(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            HelperStatic.log(tag,""+e);
        }
        return jsonObject;
    }





    public static String getDeviceId() {
        return instanceID;
    }

    public static String getDateDisplay(String time)
    {
        Log.e("Nilesh", "getDateDisplay: "+time );
        String today = new SimpleDateFormat("d MMMM yyyy").format(Calendar.getInstance().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK , 1);
        String tomorrow = new SimpleDateFormat("d MMMM yyyy").format(calendar.getTime());
        Log.e("Nilesh", "getDateDisplay: "+today+" "+today );
        if (today.equalsIgnoreCase(time))
        {
            return "Today";
        }
        else if (tomorrow.equalsIgnoreCase(time))
        {
            return "Tomorrow";
        }
        else {
            return time;
        }
    }

    public static String getFamilySendDate(String date)
    {
        SimpleDateFormat target = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat source = new SimpleDateFormat("d MMMM yyyy", Locale.US);
        try {
            Date dateFormat = source.parse(date);
            return target.format(dateFormat);
        } catch (ParseException e) {
            HelperStatic.log(tag, "" + e);
        }
        return date;
    }

    public static String getFamilyDisplayDate(String date)
    {
        SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat target = new SimpleDateFormat("d MMMM yyyy", Locale.US);
        try {
            Date dateFormat = source.parse(date);
            return target.format(dateFormat);
        } catch (ParseException e) {
            HelperStatic.log(tag, "" + e);
        }
        return date;
    }

    public static String getFeedbackDate(String date)
    {
        SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat source1 = new SimpleDateFormat("d MMMM yyyy hh:mm a", Locale.US);
        SimpleDateFormat target = new SimpleDateFormat("d MMMM yyyy", Locale.US);
        try {
            Date dateFormat = source.parse(date);
            return target.format(dateFormat);
        } catch (ParseException e) {
            try {
                Date dateFormat = source1.parse(date);
                return target.format(dateFormat);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            HelperStatic.log(tag, "" + e);
        }
        catch (NullPointerException e)
        {
            Log.e(tag, "getFeedbackDate: "+e );
        }
        return date;
    }

    public static String getFeedbackTime(String date)
    {
        if (date== null)
        {return "";}

        SimpleDateFormat source1 = new SimpleDateFormat("d MMMM yyyy hh:mm a", Locale.US);
        SimpleDateFormat target = new SimpleDateFormat("hh:mm a", Locale.US);
        try {
            Date dateFormat = source1.parse(date);
            return target.format(dateFormat);
        } catch (ParseException e) {
            HelperStatic.log(tag, "" + e);
            return date;
        }
    }

    public static String isEmptyString(String str)
    {
        if (str == null)
        {
            return "";
        }
        else if (str.isEmpty())
        {
            return "";
        }
        else if (TextUtils.isEmpty(str))
        {
            return "";
        }
        else {
            return str;
        }
    }

}
