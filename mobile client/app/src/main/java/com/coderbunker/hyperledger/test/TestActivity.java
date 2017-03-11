package com.coderbunker.hyperledger.test;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.coderbunker.hyperledger.R;
import com.coderbunker.hyperledger.route.RouteFragment;

public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, new RouteFragment())
                .commit();
    }
}
