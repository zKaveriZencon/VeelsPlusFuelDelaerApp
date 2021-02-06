package com.veelsplusfueldealerapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.veelsplusfueldealerapp.R;


public class AccountInfoFragment extends Fragment {
    private static final String TAG = "MyInfoFragment";
    private RecyclerView mRecyclerInfo;
    private View view;
    private TextView textViewLicenseNo;
    private ImageView imageviewLicense;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_my_acc_info, container, false);

        initUI();
        return view;
    }

    private void initUI() {
    }
}