package com.coderbunker.hyperledger;


import android.support.v4.app.Fragment;
import android.util.Log;

import com.coderbunker.hyperledger.communication.TcpClient;
import com.coderbunker.hyperledger.communication.TcpClient.OnMessageReceived;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseFragment extends Fragment implements OnMessageReceived {

    @Override
    public void messageReceived(String message) {
        try {
            JSONObject json  = new JSONObject(message);
            Storage.saveToken(getContext(), json.getString("token"));
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to process server response", e);
        }
    }
}
