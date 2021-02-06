package com.veelsplusfueldealerapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.veelsplusfueldealerapp.fragments.AccountInfoFragment;
import com.veelsplusfueldealerapp.fragments.MyInfoFragment;


public class MyProfileTabAdapter extends FragmentPagerAdapter {

    int tabCount;

    public MyProfileTabAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MyInfoFragment();
            case 1:
                return new AccountInfoFragment();
           /* case 2:
                return new InsuranceFragment();*/
            default:
                return new MyInfoFragment();
        }
    }


    @Override
    public int getCount() {
        return tabCount;
    }
}