package utility;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.shaktipumplimited.shaktikusum.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import activity.CameraActivity2;
import activity.CustomProgressDialog;
import bean.ImageModel;
import bean.SurveyListModel;

/**
 * Created by Administrator on 1/3/2017.
 */
public class CustomUtility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final String PERMISSIONS_FILE_PICKER = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static Context appContext;
    static boolean connected;
    static long  mLastClickTime;
    private static final String PREFERENCE = "DealLizard";
    public static   String current_date, current_time;
    Calendar calander = null;
    public static  SimpleDateFormat simpleDateFormat = null;
    @SuppressLint("StaticFieldLeak")
    public static CustomProgressDialog progressDialog;


    public static void ShowToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isDateTimeAutoUpdate(Context mContext) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (android.provider.Settings.Global.getInt(mContext.getContentResolver(), android.provider.Settings.Global.AUTO_TIME) == 1) {
                    return true;
                }
            } else {
                if (android.provider.Settings.System.getInt(mContext.getContentResolver(), android.provider.Settings.Global.AUTO_TIME) == 1) {
                    return true;
                }
            }


        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showSettingsAlert(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
        // Setting Dialog Title
        alertDialog.setTitle("Date & Time Settings");
        // Setting Dialog Message
        alertDialog.setMessage("Please enable automatic date and time setting");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // on pressing cancel button

//        alertDialog.setNegativeButton("Cancel", newworkorder DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });

        // Showing Alert Message
        alertDialog.show();
        //alertDialog.setCancelable(cancellable);
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;


        return capitalize(manufacturer) + "--" + model;
//        if (model.startsWith(manufacturer)) {
//            return capitalize(model);
//        } else {
//            return capitalize(manufacturer) + "--" + model;
//        }


    }

    static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                     if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String formateDate(String date) {
        String formatedDate = "";
        try {
            SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
            Date mDate = formate.parse(date);
            SimpleDateFormat appFormate = new SimpleDateFormat("dd.MM.yyyy");

            formatedDate = appFormate.format(mDate);
            Log.i("Result", "mDate " + formatedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }


    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion < 29) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    // for username string preferences
    public static void setSharedPreference(Context context, String name,
                                           String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }


    public static boolean disableButtonTwoSecs() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
            return true;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return false;
    }

    public static void saveArrayList(Context context, List<ImageModel> imageArrayList,String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(imageArrayList);
        editor.putString(name, json);
        editor.commit();
    }

    public static void saveSurveyArrayList(Context context, List<SurveyListModel.Response> surveyList, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(surveyList);
        editor.putString(name, json);
        editor.commit();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void clearSharedPrefrences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.clear();
            prefsEditor.apply();

    }

    public static boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 9 && phone.length() <= 13;
        }
        return false;
    }

    public static String getCurrentDate() {
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date = simpleDateFormat.format(new Date());
        return current_date.trim();
    }


    public static void showProgressDialogue(Activity context) {
        progressDialog = new CustomProgressDialog(context);
          progressDialog.setCancelable(false);
          if(!context.isFinishing()) {
              progressDialog.show();
          }
      }



    public static void hideProgressDialog(Context context) {
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public static String getBase64FromBitmap(Context context,String Imagepath) {
        String imageString=" ";
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(Imagepath);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageString;

    }


    public static void deleteArrayList(Context context,String name){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(name).commit();
        editor.apply();

    }

    public static void removeValueFromSharedPref(Context context,String name){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(name).commit();
        editor.apply();

    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                "Title", null);
        return Uri.parse(path);
    }



    public static boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public static boolean checkLocationPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.TIRAMISU) {
            return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        } else {
            return true;
        }
    }
    public static String currentVersionName(){
        double release=Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));
        String codeName="Unsupported";//below Jelly Bean
        if(release >= 4.1 && release < 4.4) codeName = "Jelly Bean";
        else if(release < 5)   codeName="Kit Kat";
        else if(release < 6)   codeName="Lollipop";
        else if(release < 7)   codeName="Marshmallow";
        else if(release < 8)   codeName="Nougat";
        else if(release < 9)   codeName="Oreo";
        else if(release < 10)  codeName="Pie";
        else if(release >= 10) codeName="Android "+((int)release);//since API 29 no more candy code names
        return codeName;
    }

    public static String currentVersionAPI(){
        double release=Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));
        String codeName="Unsupported";//below Jelly Bean
        if(release >= 4.1 && release < 4.4) codeName = "Jelly Bean";
        else if(release < 5)   codeName="Kit Kat";
        else if(release < 6)   codeName="Lollipop";
        else if(release < 7)   codeName="Marshmallow";
        else if(release < 8)   codeName="Nougat";
        else if(release < 9)   codeName="Oreo";
        else if(release < 10)  codeName="Pie";
        else if(release >= 10) codeName="Android "+((int)release);//since API 29 no more candy code names
        return ""+release;
    }

    public static File saveFile(Bitmap bitmap, String name, String folder) {

        String firstname = CustomUtility.getUserFirstName(name);
        Log.e("fname=====>",firstname);
        File file = new File(getMediaFilePath(folder,firstname));
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getMediaFilePath(String folder, String name) {



        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Shakti Kusum App");

        File dir = new File(root.getAbsolutePath() + "/"+folder+"/" + name); //it is my root directory

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a media file name
        return dir.getPath() + File.separator + "IMG_"+ Calendar.getInstance().getTimeInMillis() +".jpg";
    }


    public static String getDeviceId(Context mContext) {
        String androidId;
        androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        return androidId;
    }

    public static String getUserFirstName(String fullname){
        String firstName;
        String[] fullNameArray = fullname.split("\\s+");
        if(fullNameArray.length>1) {
            StringBuilder firstNameBuilder = new StringBuilder();
            for (int i = 0; i < fullNameArray.length - 1; i++) {
                firstNameBuilder.append(fullNameArray[i]);
                if(i != fullNameArray.length - 2){
                    firstNameBuilder.append(" ");
                }
            }
            firstName = firstNameBuilder.toString();
        } else {
            firstName = fullNameArray[0];
        }
        return firstName;
    }

    public static String getAddressFromLatLng(CameraActivity2 context, double latitude, double longitude) {
        String address = "";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            if (CustomUtility.isInternetOn(context)) {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (!addresses.isEmpty()) {

                    address = "Latitude : " + String.valueOf(latitude) + "\n" + "Longitude : " + String.valueOf(longitude) + "\n" + "Address : " + addresses.get(0).getAddressLine(0) + ","
                            + addresses.get(0).getAdminArea() + " " + addresses.get(0).getPostalCode() + "," + addresses.get(0).getCountryName();

                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return address;
    }
    public static boolean isAlphaNumeric(String str)
    {
        // Regex to check string is alphanumeric or not.
        String regex = "^[0-9a-zA-Z]+$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }

}
