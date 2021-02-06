package com.veelsplusfueldealerapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.EarningsReportActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.models.DailySalesInfoModel;
import com.veelsplusfueldealerapp.models.OperatorDetailsModel;

import java.util.List;

public class DailySalesAdapter extends RecyclerView.Adapter<DailySalesAdapter.MyViewHolder> {
    private static final String TAG = "DailySalesAdapter";
    CommonCodeManager commonCodeManager;
    private Context context;
    private int selectedPosition;
    private List<DailySalesInfoModel> dailySalesList;


    public DailySalesAdapter(Context context, List<DailySalesInfoModel> dailySalesList) {
        this.context = context;
        this.dailySalesList = dailySalesList;
        commonCodeManager = new CommonCodeManager(context);
    }


    public DailySalesAdapter() {
    }

    @NonNull
    @Override
    public DailySalesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_daily_sales, parent, false);

        return new DailySalesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DailySalesAdapter.MyViewHolder holder, final int position) {
        selectedPosition = position;
        final DailySalesInfoModel dailySalesInfoModel = dailySalesList.get(position);

        holder.textViewDate.setText(dailySalesInfoModel.getTransacDate());
        holder.textViewProduct.setText(dailySalesInfoModel.getProduct());
        //holder.textViewPumpNz.setText(dailySalesInfoModel.getPumpNz());
        holder.textViewUnitSale.setText(dailySalesInfoModel.getUnitSale());
        holder.textViewTotalRecoveredAmt.setText(dailySalesInfoModel.getTotalRecoveredAmount());
        holder.textViewTransactionLogId.setText(dailySalesInfoModel.getAccountTransacLogId());
        holder.textViewBAtchId.setText(dailySalesInfoModel.getBatchId());

        holder.layoutDailySales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check on transaction status for view or
                Log.d(TAG, "onClick:dailySalesInfoModel.getBatchId():  " + dailySalesInfoModel.getBatchId());
                commonCodeManager.saveDailySalesCardDetailsForView(context, "viewonly");

                commonCodeManager.saveDailySalesCardEssentials(context, dailySalesInfoModel.getAccountTransacLogId(),
                        dailySalesInfoModel.getBatchId());

                Intent earnIntent = new Intent(context, EarningsReportActivity.class);
                context.startActivity(earnIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return dailySalesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutDailySales;
        TextView textViewDate, textViewProduct, textViewUnitSale, textViewTotalRecoveredAmt,
                textViewTransactionLogId, textViewBAtchId;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            layoutDailySales = itemView.findViewById(R.id.layout_daily_sales);
            textViewDate = itemView.findViewById(R.id.textview_date_dsc);
            textViewProduct = itemView.findViewById(R.id.textview_product_dsc);
            //  textViewPumpNz = itemView.findViewById(R.id.textview_pumpnz_dsc);
            textViewUnitSale = itemView.findViewById(R.id.textview_unitsale_dsc);
            textViewTotalRecoveredAmt = itemView.findViewById(R.id.textview_total_recoverd_amt);
            textViewTransactionLogId = itemView.findViewById(R.id.textview_acclogId);
            textViewBAtchId = itemView.findViewById(R.id.textview_batch_id);


        }

    }
}
