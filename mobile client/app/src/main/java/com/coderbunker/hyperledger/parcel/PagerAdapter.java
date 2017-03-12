package com.coderbunker.hyperledger.parcel;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    private Fragment company;
    private Fragment parcel;
    private Fragment insurance;

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        company = new ParcelCompanyFragment();
        parcel = new ParcelDetailsFragment();
        insurance = new ParcelInsuranceFragment();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return company;
            case 1:
                return parcel;
            case 2:
                return insurance;
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        String result = "";
        switch (position) {
            case 0:
                result = "company";
                break;
            case 1:
                result = "parcel";
                break;
            case 2:
                result = "insurance";
                break;
        }
        return result;
    }

    public void updateInfo(final int position) {
        // TODO support backwards movement when this logic becomes more complex
        int previousPosition = position - 1;
        previousPosition = previousPosition >= 0 ? previousPosition : 0;
        Fragment fragment = getItem(previousPosition);
        if (fragment instanceof IParcel) {
            ((IParcel) fragment).updateParcelInfo();
        }
    }

}
