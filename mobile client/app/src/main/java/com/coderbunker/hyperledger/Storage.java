package com.coderbunker.hyperledger;


import android.content.Context;
import android.preference.PreferenceManager;

import com.coderbunker.hyperledger.parcel.Location;

public class Storage {

    private static final String LOCATION_FROM = "LOCATION_FROM";
    private static final String LOCATION_TO = "LOCATION_TO";
    private static final String TOKEN = "TOKEN";
    private static final String TRANSACTION_ID = "TRANSACTION_ID";
    private static final String LOGIN = "LOGIN";

    public static void setLoginState(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(LOGIN, true)
                .apply();
    }

    public static boolean isLogin(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(LOGIN, false);
    }

    public static void addLocation(Context context, Location location) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LOCATION_FROM, location.getLocationFrom())
                .putString(LOCATION_TO, location.getLocationTo())
                .apply();
    }

    public static void saveToken(Context context, String token) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(TOKEN, token)
                .apply();
    }

    public static String getToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(TOKEN, "");
    }

    public static void saveTransactionId(Context context, String token) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(TRANSACTION_ID, token)
                .apply();
    }

    public static String getTransactionId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(TRANSACTION_ID, "");
    }

}
