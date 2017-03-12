package com.coderbunker.hyperledger.qrcode;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.coderbunker.hyperledger.App;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeActivity extends Activity implements ZXingScannerView.ResultHandler {

    public static final String EXTRA_BROADCAST = "EXTRA_BROADCAST";

    private static final String EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE";
    private static final String EXTRA_QR_CODE = "EXTRA_QR_CODE";

    private ZXingScannerView scannerView;

    public static Intent prepareBarCode(Context context, final int code) {
        Intent intent = new Intent(context, BarcodeActivity.class);
        intent.putExtra(EXTRA_REQUEST_CODE, code);
        return intent;
    }

    public static CodeEntity getCodeFromResult(Intent intent) {
        CodeEntity codeEntity = (CodeEntity) intent.getExtras().get(EXTRA_QR_CODE);
        return codeEntity;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.d(App.TAG, "Bar code activity receives result");

        Intent intent = new Intent(EXTRA_BROADCAST);
        intent.putExtra(
                EXTRA_QR_CODE,
                new CodeEntity(rawResult.getText())
        );
        setResult(RESULT_OK, intent);
//        LocalBroadcastManager.getInstance(this)
//                .sendBroadcast(intent);

        finish();
    }
}
