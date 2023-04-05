package com.shaktipumplimited.shaktikusum.debugapp.GlobalValue;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {

    Context mcontext;
    SharedPreferences sharePrefs;
    SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "NRKKitchen";
    public static final String LIST_OF_SORTED_DATA_ID = "json_list_sorted_data_id";
    public final static String PREFERENCE_FILE = "preference_file";


    public SharedPreferencesUtil() {
    }
    public static void setData(Context context,String mKey, String mItem) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(mKey, mItem);
        editor.commit();
    }

    public static String getData(Context context,String mKey) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(mKey, null);
        return text;
    }

    public static void setFloatData(Context context,String mKey, float mItem) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putFloat(mKey, mItem);
        editor.apply();    }

    public static float getRatingData(Context context,String mKey) {
        SharedPreferences settings;
        float text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getFloat(mKey, 0);
        return text;
    }


    public static void putPref(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putPref(Context context, String key, float value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void putPref(Context context, String key, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putPref(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getPrefStr(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static int getPrefForInt(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, -1);
    }

    public static boolean getPrefForBool(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }


    public static float getPrefForFloat(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getFloat(key, 0);
    }

    public static void clearPref(Context aContext, String aKey){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(aContext);
        preferences.edit().remove(aKey).commit();
    }

    public static int getPrefForCount(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, 0);
    }
    ////////////////////////////////////////





}
