package com.coderbunker.hyperledger.parcel;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.coderbunker.hyperledger.App;
import com.coderbunker.hyperledger.R;

public class ParcelCompanyFragment extends Fragment implements IParcel {

    private EditText sendTo;
    private EditText sendFrom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_parcel_company, container, false);
        sendTo = (EditText) view.findViewById(R.id.sendTo);
        sendFrom = (EditText) view.findViewById(R.id.sendFrom);
        return view;
    }

    @Override
    public void updateParcelInfo() {
        Log.d(App.TAG, "updateParcelInfo at company");
        ParcelService service = ParcelService.getInstance();
        service.setCompanySend(sendTo.getText().toString());
        service.setCompanyReceive(sendFrom.getText().toString());
    }
}
