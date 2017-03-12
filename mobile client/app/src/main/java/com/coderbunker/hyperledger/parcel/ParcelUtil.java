package com.coderbunker.hyperledger.parcel;


import android.util.Log;

import com.coderbunker.hyperledger.App;
import com.coderbunker.hyperledger.route.Route;
import com.coderbunker.hyperledger.route.RouteService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ParcelUtil {

    private Route route;

    public ParcelUtil(JSONObject route) {
        try {
            this.route = Route.getRoute(route, new RouteService());
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to obtain route", e);
        }
    }

    public String getDestination() {
        StringBuilder prettyPath = new StringBuilder();
        List<String> checkpoints = route.getCheckpoints();
        for (int idx = 0; idx < checkpoints.size(); idx++) {
            prettyPath.append(checkpoints.get(idx));
            if (idx != (checkpoints.size() - 1)) prettyPath.append(" - ");
        }
        prettyPath.append("\n");
        prettyPath.append(route.getTime() + " for price " + route.getCost());
        return prettyPath.toString();
    }
}
