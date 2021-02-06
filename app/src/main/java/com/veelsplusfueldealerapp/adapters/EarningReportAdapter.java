package com.veelsplusfueldealerapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.veelsplusfueldealerapp.fragments.ErCashFragment;
import com.veelsplusfueldealerapp.fragments.ErCreditFragment;
import com.veelsplusfueldealerapp.fragments.ErDigitalFragment;
import com.veelsplusfueldealerapp.fragments.ErInfoFragment;


public class EarningReportAdapter extends FragmentPagerAdapter {

    int tabCount;

    public EarningReportAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ErInfoFragment();
            case 1:
                return new ErCashFragment();
            case 2:
                return new ErDigitalFragment();
            case 3:
                return new ErCreditFragment();

            default:
                return null;
        }
    }
/*
case 2:
        return new LRFreightFragment();
*/

    @Override
    public int getCount() {
        return tabCount;
    }
}