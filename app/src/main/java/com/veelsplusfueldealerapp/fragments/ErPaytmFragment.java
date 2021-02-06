package com.veelsplusfueldealerapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.veelsplusfueldealerapp.R;


public class ErPaytmFragment extends Fragment {
    private static final String TAG = "ErCashFragment";
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_paytm_frag_er, container, false);
        return view;
    }

}
