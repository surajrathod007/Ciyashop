package com.example.ciyashop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.ciyashop.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 16/2/16.
 */
public class Utils {

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            Log.e("TAG", "Exception: " + ex.getMessage());
        }
    }

    public static void enableTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
            } finally {
                is.close();
            }
            str = sb.toString();
        }
        return str;
    }

    public static boolean resetExternalStorageMedia(Context context,
                                                    String filePath) {
        try {
            if (Environment.isExternalStorageEmulated())
                return (false);
            Uri uri = Uri.parse("file://" + new File(filePath));
            Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, uri);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return (false);
        }
        return (true);
    }

    public static void notifyMediaScannerService(Context context,
                                                 String filePath) {
        MediaScannerConnection.scanFile(context, new String[]{filePath},
                null, (path, uri) -> {
                    Debug.i("ExternalStorage", "Scanned " + path + ":");
                    Debug.i("ExternalStorage", "-> uri=" + uri);
                });
    }

    public static void setPref(Context c, String pref, String val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.apply();
    }

    public static String getPref(Context c, String pref, String val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(pref,
                val);
    }

    public static void setPref(Context c, String pref, boolean val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putBoolean(pref, val);
        e.apply();
    }

    public static boolean getPref(Context c, String pref, boolean val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(
                pref, val);
    }

    public static void delPref(Context c, String pref) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.remove(pref);
        e.apply();
    }

    public static void setPref(Context c, String pref, int val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putInt(pref, val);
        e.apply();
    }

    public static int getPref(Context c, String pref, int val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getInt(pref,
                val);
    }

    public static void setPref(Context c, String pref, long val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putLong(pref, val);
        e.apply();
    }

    public static long getPref(Context c, String pref, long val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getLong(pref,
                val);
    }

    public static void setPref(Context c, String file, String pref, String val) {
        SharedPreferences settings = c.getSharedPreferences(file,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = settings.edit();
        e.putString(pref, val);
        e.apply();
    }

    public static String getPref(Context c, String file, String pref, String val) {
        return c.getSharedPreferences(file, Context.MODE_PRIVATE).getString(
                pref, val);
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static void sendExceptionReport(Exception e) {
        e.printStackTrace();
        try {
            // Writer result = new StringWriter();
            // PrintWriter printWriter = new PrintWriter(result);
            // e.printStackTrace(printWriter);
            // String stacktrace = result.toString();
            // new CustomExceptionHandler(c, URLs.URL_STACKTRACE)
            // .sendToServer(stacktrace);
        } catch (Exception e1) {
            e1.printStackTrace();
            Log.e("TAG", "Exception: " + e1.getMessage());
        }
    }

    public static int getDeviceWidth(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.widthPixels;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }
        return 480;
    }

    public static int getDeviceHeight(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.heightPixels;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }
        return 800;
    }

    public static void hideKeyBoard(Context c, View v) {
        InputMethodManager imm = (InputMethodManager) c
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static String parseCalendarFormat(Calendar c, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return sdf.format(c.getTime());
    }

    public static String parseTime(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return sdf.format(new Date(time));
    }

    public static Date parseTime(String time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String parseTime(String time, String fromPattern,
                                   String toPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromPattern,
                Locale.getDefault());
        try {
            Date d = sdf.parse(time);
            sdf = new SimpleDateFormat(toPattern, Locale.getDefault());
            if (d != null) {
                return sdf.format(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String nullSafe(String content) {
        if (content == null) {
            return "";
        }
        return content;
    }

    public static String nullSafe(String content, String defaultStr) {
        if (content.isEmpty()) {
            return defaultStr;
        }
        return content;
    }

    public static String nullSafeDash(String content) {
        if (content.isEmpty()) {
            return "-";
        }
        return content;
    }

    public static String nullSafe(int content, String defaultStr) {
        if (content == 0) {
            return defaultStr;
        }
        return "" + content;
    }

    public static Typeface getRobotoRegular(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(),
                    "fonts/Roboto-Regular.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Typeface getRobotoLight(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(),
                    "fonts/Roboto-Thin.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Typeface getRobotoBold(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(),
                    "fonts/Roboto-Bold.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Typeface getCondensedNormal(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(),
                    "fonts/RobotoCondensed-Regular.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isGPSProviderEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isNetworkProviderEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static boolean isLocationProviderEnabled(Context context) {
        return (isGPSProviderEnabled(context) || isNetworkProviderEnabled(context));
    }

    public static ArrayList<String> asList(String str) {
        return new ArrayList<>(Arrays.asList(str
                .split("\\s*,\\s*")));
    }

    public static String implode(ArrayList<String> data) {
        try {
            String devices = "";
            for (String iterable_element : data) {
                devices = String.format("%s,%s", devices, iterable_element);
            }

            if (devices.length() > 0 && devices.startsWith(",")) {
                devices = devices.substring(1);
            }
            return devices;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getExtension(String urlPath) {
        if (urlPath.contains(".")) {
            return urlPath.substring(urlPath.lastIndexOf(".") + 1);
        }
        return "";
    }


    public static boolean isInternetConnected(Context mContext) {
        boolean outcome = false;
        try {
            if (mContext != null) {
                ConnectivityManager cm = (ConnectivityManager) mContext
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
                for (NetworkInfo tempNetworkInfo : networkInfos) {
                    if (tempNetworkInfo.isConnected()) {
                        outcome = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outcome;
    }

    public static void showDialog(final Context c, String title, String message) {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(c)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.hint_ok, (dialog1, which) -> dialog1.dismiss())
                .setNegativeButton(R.string.hint_cancel, (dialog12, which) -> dialog12.dismiss()).create();
        dialog.show();
        Button nButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nButton.setTextColor(c.getResources().getColor(R.color.colorPrimary));
        Button yButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        yButton.setTextColor(c.getResources().getColor(R.color.colorPrimary));
    }

    public static void clearLoginCredentials(Activity c) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(c).edit();
        editor.clear();
        editor.apply();
//        FacebookSdk.sdkInitialize(c);
//        LoginManager.getInstance().logOut();
    }


    public static void printHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(Constant.PACKAGE_NAME,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                if (md != null) {
                    md.update(signature.toByteArray());
                }
                if (md != null) {
                    Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("hashkey_error", e.toString());
        }
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
