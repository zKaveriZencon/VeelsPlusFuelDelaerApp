package com.veelsplusfueldealerapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.util.LogTime;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.models.FuelPriceModel;
import com.veelsplusfueldealerapp.models.MappedCorporatesModel;

import java.util.List;

public class MappedCorporateAccountsAdapter extends RecyclerView.Adapter<MappedCorporateAccountsAdapter.MyViewHolder> {
    private static final String TAG = "MappedCorporateAccounts";
    CommonCodeManager commonCodeManager;
    private Context context;
    private int selectedPosition;
    private List<MappedCorporatesModel> mappedCorpsDetailsList;

    public MappedCorporateAccountsAdapter(Context context, List<MappedCorporatesModel>
            mappedCorpsDetailsList) {
        this.context = context;
        this.mappedCorpsDetailsList = mappedCorpsDetailsList;
        commonCodeManager = new CommonCodeManager(context);
    }


    public MappedCorporateAccountsAdapter() {
    }

    @NonNull
    @Override
    public MappedCorporateAccountsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_mapped_acc, parent, false);

        return new MappedCorporateAccountsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MappedCorporateAccountsAdapter.MyViewHolder holder, final int position) {

        selectedPosition = position;
        MappedCorporatesModel mappedCorpModel = mappedCorpsDetailsList.get(position);
        int serilNovalue = position + 1;

        String serialNo = "" + serilNovalue + ".";

        holder.textViewCreditLimit.setText(mappedCorpModel.getCreditLimit());
        holder.textViewTotalBilledAmt.setText(mappedCorpModel.getTotalBilledAmt());
        holder.textViewPaidAmt.setText(mappedCorpModel.getPaidAmt());
        holder.textViewPendingAmt.setText(mappedCorpModel.getPendingAmt());
        holder.textViewCorpName.setText(mappedCorpModel.getCorporateName());
        holder.textViewSerilNo.setText(serialNo);

        Log.d(TAG, "MyViewHolder: textViewPendingAmt : " + holder.textViewPendingAmt.getText().toString());

        String creditLimit = mappedCorpModel.getCreditLimit();
        String pendingAmt = mappedCorpModel.getFinalPendingAmt();

        Log.d(TAG, "onBindViewHolder: pendingAmt : " + pendingAmt);
        Log.d(TAG, "onBindViewHolder: creditLimit : " + creditLimit);

        if (creditLimit != null && !creditLimit.isEmpty() && !creditLimit.equals("null") && !creditLimit.equals("undefined")) {
            if (pendingAmt != null && !pendingAmt.isEmpty() && !pendingAmt.equals("null") && !pendingAmt.equals("undefined") && !pendingAmt.contains("CR")) {

                if (Double.parseDouble(pendingAmt) > Double.parseDouble(creditLimit)) {
                    holder.textViewPendingAmt.setTextColor(Color.RED);
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return mappedCorpsDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCreditLimit, textViewTotalBilledAmt, textViewPaidAmt, textViewPendingAmt,
                textViewCorpName, textViewSerilNo;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewCreditLimit = itemView.findViewById(R.id.textview_credit_limit);
            textViewTotalBilledAmt = itemView.findViewById(R.id.textview_total_billed_amt);
            textViewPaidAmt = itemView.findViewById(R.id.textview_paid_amt);
            textViewPendingAmt = itemView.findViewById(R.id.textview_pending_amt);

            textViewCorpName = itemView.findViewById(R.id.textview_corpname);
            textViewSerilNo = itemView.findViewById(R.id.textview_srno_mca);


        }

    }
}
