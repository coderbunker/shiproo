package com.coderbunker.hyperledger;


import android.content.Context;
import android.preference.PreferenceManager;

import com.coderbunker.hyperledger.parcel.Location;

public class Storage {

    private static final String LOCATION_FROM = "LOCATION_FROM";
    private static final String LOCATION_TO = "LOCATION_TO";

    public static void addLocation(Context context, Location location) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LOCATION_FROM, location.getLocationFrom())
                .putString(LOCATION_TO, location.getLocationTo())
                .commit();
    }
}
