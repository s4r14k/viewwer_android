package com.priscilla.viewwer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SharedPref {
    private static SharedPreferences mSharedPref;

    /* All keys used on application
    * Si tu as besoin d'autres clés tu les mets ici. J'ai déjà implémenté tous les méthodes read/write
    * pour les données de type String, Integer et Boolean
    * */

    public static final String PREFS_LOGIN = "PREFS_LOGIN";
    public static final boolean IsUserLoggedIn = false;


    public static final boolean IsChecked = false;
    /* End Keys */

    /*
    * Usage:
    * Etape 1: Mets le code d'initialisation une fois seulement dans onCreate du MainActivity
    *          NB: la classe App est un singleton et qui te fournit le context de l'application
    *          SharedPref.init(App.context);
    *
    * Etape 2:
    *    - Write data example: SharedPref.write(SharedPref.ID, "XXXX");
    *    - Read data example: String myID = SharedPref.read(SharedPref.ID, "DEFAULT_IF_NOTHING_FOUND");
    * */
    private SharedPref()
    {
    }

    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

//    public static HashSet<String> getCookies(Context context) {
//        SharedPreferences mcpPreferences = getSKSharedPreferences(context);
//        return (HashSet<String>) mcpPreferences.getStringSet("cookies", new HashSet<String>());
//    }
//
//    public static boolean setCookies(Context context, HashSet<String> cookies) {
//        SharedPreferences mcpPreferences = getSKSharedPreferences(context);
//        SharedPreferences.Editor editor = mcpPreferences.edit();
//        return editor.putStringSet("cookies", cookies).commit();
//    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static boolean read(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public static Integer read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).commit();
    }

    public static void clear(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public static void remove(String key) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.remove(key);
        prefsEditor.apply();


    }














}
