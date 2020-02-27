package com.inu.amadda.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String FILE_NAME = "AMADDA_SharedPreferences";
    private static PreferenceManager instance;

    public static PreferenceManager getInstance() {
        if (null == instance) {
            instance = new PreferenceManager();
        }
        return instance;
    }

    public void putSharedPreference(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putSharedPreference(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putSharedPreference(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getSharedPreference(Context context, String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, defaultValue);
    }

    public int getSharedPreference(Context context, String key, int defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, defaultValue);
    }

    public boolean getSharedPreference(Context context, String key, boolean defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultValue);
    }

    public void deleteSharedPreference(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }

    public void resetSharedPreference(Context context) {
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
