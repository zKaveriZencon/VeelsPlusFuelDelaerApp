package com.veelsplusfueldealerapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.models.ArraivalVehiclesListModel;
import com.veelsplusfueldealerapp.models.OperatorDailyWorkListModel;
import com.veelsplusfueldealerapp.models.OperatorInfoFragModel;

import java.util.List;

public class ErSalesInfoAdapter extends RecyclerView.Adapter<ErSalesInfoAdapter.MyViewHolder> {
    private static final String TAG = "VehiclesArraivalsListAd";
    List<OperatorInfoFragModel> paysList;
    List<String> performIDs;
    private Context context;
    private int selectedPosition;


    public ErSalesInfoAdapter(Context context, List<OperatorInfoFragModel> paysList,
                              List<String> performIds) {
        this.context = context;
        this.paysList = paysList;
        this.performIDs = performIds;

    }

    public ErSalesInfoAdapter() {
    }

    @NonNull
    @Override
    public ErSalesInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_sales_er_info, parent, false);

        return new ErSalesInfoAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ErSalesInfoAdapter.MyViewHolder holder, final int position) {
        selectedPosition = position;
        OperatorInfoFragModel operatorDailyWorkListModel = paysList.get(position);
        holder.textViewProduct.setText(operatorDailyWorkListModel.getProduct());
        holder.textViewMeterSale.setText(operatorDailyWorkListModel.getMeterSaleLiters());
        holder.textViewTotalAmount.setText(operatorDailyWorkListModel.getTotalAmount());
        holder.textViewPumpNoz.setText(operatorDailyWorkListModel.getPumpNozzle());
        if (performIDs.size() > 0)
            holder.textViewId.setText(performIDs.get(position));


    }

    @Override
    public int getItemCount() {
        return paysList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProduct, textViewMeterSale, textViewTotalAmount, textViewId, textViewPumpNoz;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            textViewProduct = itemView.findViewById(R.id.textview_product_pay);
            textViewMeterSale = itemView.findViewById(R.id.textview_metersale_pay);
            textViewTotalAmount = itemView.findViewById(R.id.textview_total_amt_pay);
            textViewId = itemView.findViewById(R.id.textview_id);
            textViewPumpNoz = itemView.findViewById(R.id.textview_pumpnoz);


        }

    }
}
