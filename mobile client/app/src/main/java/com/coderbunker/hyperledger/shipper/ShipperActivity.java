package com.coderbunker.hyperledger.shipper;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.coderbunker.hyperledger.App;
import com.coderbunker.hyperledger.R;
import com.coderbunker.hyperledger.parcel.ParcelService;
import com.coderbunker.hyperledger.parcel.ParcelUtil;
import com.coderbunker.hyperledger.qrcode.BarcodeActivity;
import com.coderbunker.hyperledger.qrcode.QRCodeActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ShipperActivity extends AppCompatActivity {

    private static final int EXTRA_CODE = 1000;
    private static final int CODE_SCAN_BARCODE = 1001;
    private static final int ZXING_CAMERA_PERMISSION = 1002;

    private TextView info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_shipper);
        Log.d(App.TAG, "ShipperFragment run");
        info = (TextView) findViewById(R.id.info);
        View view = findViewById(R.id.container);
        view.findViewById(R.id.scan_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open the scanner
                scanQRCode();
            }
        });
        view.findViewById(R.id.change_ownership).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show qr code
                showQRCode();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(App.TAG, "activity onActivityResult");
        if (RESULT_CANCELED == resultCode) {
            return;
        }
        info.setText(
                BarcodeActivity.getCodeFromResult(data)
                        .getCode()
        );
    }

    public void launchActivity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            startActivityForResult(BarcodeActivity.prepareBarCode(this, 10), CODE_SCAN_BARCODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchActivity();
                } else {
                    Toast.makeText(
                            this,
                            "Please grant camera permission to use the QR Scanner",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                return;
        }
    }

    private void scanQRCode() {
        launchActivity();
    }

    private void showQRCode() {
        String json = null;
        try {
            // TODO replace with real data
            json = ParcelService.getInstance().getJson().toString();
            QRCodeActivity.start(this, json);
        } catch (JSONException e) {
            Log.e(App.TAG, "Failed to obtain json", e);
        }
    }

}
