package com.coderbunker.hyperledger.parcel;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.coderbunker.hyperledger.R;

public class ParcelDetailsFragment extends Fragment implements IParcel {

    private EditText locationFrom;
    private EditText locationTo;
    private Spinner spinner;
    private EditText weight;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_parcel, container, false);
        locationFrom = (EditText) view.findViewById(R.id.sendFrom);
        locationTo = (EditText) view.findViewById(R.id.sendTo);
        spinner = (Spinner) view.findViewById(R.id.size);
        // TODO replace spinner with mask to offer dimensions of item
        weight = (EditText) view.findViewById(R.id.weight);
        return view;
    }

    @Override
    public void updateParcelInfo() {
        ParcelService service = ParcelService.getInstance();
        service.setSize(weight.getText().toString());
        service.setWeight("");
        service.setParcelSend(locationFrom.getText().toString());
        service.setParcelReceive(locationTo.getText().toString());

    }
}
