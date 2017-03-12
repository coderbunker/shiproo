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
import com.coderbunker.hyperledger.Storage;
import com.coderbunker.hyperledger.qrcode.QRCodeActivity;

import org.json.JSONException;

public class ParcelInsuranceFragment extends Fragment implements IParcel {

    private EditText insurance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_parcel_insurance, container, false);
        insurance = (EditText) view.findViewById(R.id.insurance);
        view.findViewById(R.id.confirm)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(App.TAG, "Update information about parcel");
                        try {
                            String json  = ParcelService.getInstance().getJson().toString();
                            QRCodeActivity.start(getContext(), json);
                        } catch (JSONException e) {
                            Log.e(App.TAG, "Failed to obtain json", e);
                        }
                    }
                });
        return view;
    }

    @Override
    public void updateParcelInfo() {
        ParcelService parcelService = ParcelService.getInstance();
        parcelService.setInsurance(insurance.getText().toString());
    }
}
