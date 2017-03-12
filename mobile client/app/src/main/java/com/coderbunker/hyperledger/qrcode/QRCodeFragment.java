package com.coderbunker.hyperledger.qrcode;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.coderbunker.hyperledger.App;
import com.coderbunker.hyperledger.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import static android.content.Context.WINDOW_SERVICE;

public class QRCodeFragment extends Fragment {

    public static final String JSON = "JSON";

    public static void start(Context context, String json) {
        Intent intent = new Intent(context, QRCodeFragment.class);
        intent.putExtra(JSON, json);
        context.startActivity(intent);
    }

    public static Fragment newInstance(String json) {
        Fragment fragment = new QRCodeFragment();
        Bundle args = new Bundle();
        args.putString(JSON, json);
        Log.d(App.TAG, "QR code passed to fragment: " + json);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_qr_code, container, false);
        Log.d(App.TAG, "QR code: " + getArguments().getString(JSON, ""));
        init((ImageView) view.findViewById(R.id.qr_code), getArguments().getString(JSON, ""));
        return view;
    }

    public void init(ImageView imageView, String json) {
        try {
            //Find screen size
            WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3/4;

            //Encode with a QR Code image
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(
                    json,
                    null,
                    Contents.Type.TEXT,
                    BarcodeFormat.QR_CODE.toString(),
                    smallerDimension);

            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            Log.d(App.TAG, "Failed to obtain an image", e);
        }

    }
}
