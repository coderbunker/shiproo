package com.coderbunker.hyperledger.route;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private List<String> checkpoints;
    private String cost;
    private String time;

    public Route() {
        checkpoints = new ArrayList<>();
    }

    public List<String> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<String> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static List<Route> getRoute(JSONArray jsonArray, RouteService service) throws JSONException {
        List<Route> result = new ArrayList<>();
        for (int id = 0; id < jsonArray.length(); id++) {
            result.add(
                    getRoute(
                            jsonArray.getJSONObject(id),
                            service
                    ));
        }
        return result;
    }

    public static Route getRoute(JSONObject data, RouteService service) throws JSONException {
        Route route = new Route();
        route.setCost(data.getString("cost"));
        route.setTime(data.getString("time"));
        route.setCheckpoints(
                service.convertJson(
                        data.getJSONArray("checkpoints"))
        );
        return route;
    }
}
