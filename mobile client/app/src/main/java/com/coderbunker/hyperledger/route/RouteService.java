package com.coderbunker.hyperledger.route;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RouteService {

    public static List<String> convertJson(JSONArray data) throws JSONException {
        List<String> result = new ArrayList<>();
        final int size = data.length();
        for (int idx = 0; idx < size; idx++) {
            result.add(data.getString(idx));
        }
        return result;
    }

    public static String getRoute(List<String> data) {
        StringBuilder result = new StringBuilder();
        for (String str : data) {
            result.append(str);
            result.append("\n");
        }
        return result.toString();
    }
}
