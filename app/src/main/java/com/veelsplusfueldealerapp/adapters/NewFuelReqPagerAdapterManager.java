package com.veelsplusfueldealerapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.veelsplusfueldealerapp.fragments.CreateCRForCorpMgFragment;
import com.veelsplusfueldealerapp.fragments.NewCorporateFragmentForFuelReq;
import com.veelsplusfueldealerapp.fragments.PersonFragmentForFuelReq;

public class NewFuelReqPagerAdapterManager extends FragmentPagerAdapter {

    int tabCount;

    public NewFuelReqPagerAdapterManager(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new CreateCRForCorpMgFragment();
         /*   case 1:
                return new PersonFragmentForFuelReq();*/
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabCount;
    }
}