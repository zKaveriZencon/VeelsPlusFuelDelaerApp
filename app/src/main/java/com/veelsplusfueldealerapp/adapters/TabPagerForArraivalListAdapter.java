package com.veelsplusfueldealerapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.veelsplusfueldealerapp.fragments.FleetListFragment;
import com.veelsplusfueldealerapp.fragments.NonFleetListFragment;


public class TabPagerForArraivalListAdapter extends FragmentPagerAdapter {

    int tabCount;

    public TabPagerForArraivalListAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FleetListFragment();
            case 1:
                return new NonFleetListFragment();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return tabCount;
    }
}