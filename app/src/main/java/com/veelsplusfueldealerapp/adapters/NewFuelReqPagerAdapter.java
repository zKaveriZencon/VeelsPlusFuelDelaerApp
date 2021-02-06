package com.veelsplusfueldealerapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.veelsplusfueldealerapp.fragments.CorporateFragmentForFuelReq;
import com.veelsplusfueldealerapp.fragments.NewCorporateFragmentForFuelReq;
import com.veelsplusfueldealerapp.fragments.PersonFragmentForFuelReq;

public class NewFuelReqPagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public NewFuelReqPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new NewCorporateFragmentForFuelReq();
            case 1:
                return new PersonFragmentForFuelReq();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabCount;
    }
}