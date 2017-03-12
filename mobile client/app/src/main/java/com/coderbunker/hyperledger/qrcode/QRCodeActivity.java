package com.coderbunker.hyperledger.qrcode;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.coderbunker.hyperledger.R;

public class QRCodeActivity extends AppCompatActivity {

    private static final String EXTRA_JSON = "EXTRA_JSON";

    public static void start(Context context, String json) {
        Intent intent = new Intent(context, QRCodeActivity.class);
        intent.putExtra(EXTRA_JSON, json);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        String json = getIntent().getStringExtra(EXTRA_JSON);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content, QRCodeFragment.newInstance(json))
                .commit();
    }
}
