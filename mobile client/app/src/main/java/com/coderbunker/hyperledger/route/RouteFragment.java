package com.coderbunker.hyperledger.route;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.coderbunker.hyperledger.App;
import com.coderbunker.hyperledger.qrcode.QRCodeActivity;
import com.coderbunker.hyperledger.test.Mock;
import com.coderbunker.hyperledger.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RouteFragment extends Fragment implements RouteAdapter.IListener {

    private RecyclerView list;
    private RouteAdapter adapter;

    private Button submitButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_route, container, false);
        Log.d(App.TAG, "Root fragment init");

//        submitButton = (Button) view.findViewById(R.id.submit);
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(App.TAG, "Click on submit button");
//                // TODO save current item
//            }
//        });

        adapter = new RouteAdapter();
        list = (RecyclerView) view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        list.setHasFixedSize(true);
        list.setAdapter(adapter);
        adapter.setListener(this);
        setData();
        return view;
    }

    private void setData() {
        try {
            adapter.updateModel(
                    Route.getRoute(
                            new JSONObject(new Mock().getJson()).getJSONArray("chains"),
                            new RouteService()
                    )
            );
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to set data", e);
        }
    }

    @Override
    public void onClick(Route route) {
        Log.d(App.TAG, "CLick on route");
        try {
            QRCodeActivity.start(getContext(), route.getJson().toString());
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to get json", e);
        }
    }
}
