package com.coderbunker.hyperledger.communication;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class NetworkService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NetworkService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // TODO show notification to run service permanently into background
    }
}
