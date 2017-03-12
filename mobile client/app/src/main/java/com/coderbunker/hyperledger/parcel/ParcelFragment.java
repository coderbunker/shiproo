package com.coderbunker.hyperledger.parcel;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coderbunker.hyperledger.App;
import com.coderbunker.hyperledger.R;

public class ParcelFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private ParcelService parcelService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_parcel_view_pager, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getFragmentManager()));
        viewPager.addOnPageChangeListener(this);
        parcelService = ParcelService.getInstance();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager.removeOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // no op
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(App.TAG, "On page selected: " + position);
        PagerAdapter pagerAdapter = (PagerAdapter) viewPager.getAdapter();
        pagerAdapter.updateInfo(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // no op
    }
}
