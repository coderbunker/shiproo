package com.coderbunker.hyperledger.shipper;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coderbunker.hyperledger.R;

public class ShipperFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_shipper, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
