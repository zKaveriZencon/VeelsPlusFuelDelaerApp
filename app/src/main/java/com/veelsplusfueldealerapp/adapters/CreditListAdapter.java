package com.veelsplusfueldealerapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.ManualEntryFormActivity;
import com.veelsplusfueldealerapp.activities.ViewCreditRequestDetailsActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.models.ArraivalVehiclesListModel;
import com.veelsplusfueldealerapp.models.FleetListModel;

import java.util.List;

public class CreditListAdapter extends RecyclerView.Adapter<CreditListAdapter.MyViewHolder> {
    private static final String TAG = "CreditListAdapter";
    CommonCodeManager commonCodeManager;
    private Context context;
    private int selectedPosition;
    private List<FleetListModel> fleetList;


    public CreditListAdapter(Context context, List<FleetListModel> fleetList) {
        this.context = context;
        this.fleetList = fleetList;

        commonCodeManager = new CommonCodeManager(context);

    }


    public CreditListAdapter() {
    }

    @NonNull
    @Override
    public CreditListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_credit_list, parent, false);

        return new CreditListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CreditListAdapter.MyViewHolder holder, final int position) {
        selectedPosition = position;
        final FleetListModel fleetListModel = fleetList.get(position);


        //holder.textViewCustName.setText(fleetListModel.getCustomerName());
        holder.textViewDate.setText(fleetListModel.getDate());
        holder.textViewDriverNo.setText(fleetListModel.getDriverNo());
        holder.textViewVehicleNo.setText(fleetListModel.getVehicleNo());
        String transacStatus = fleetListModel.getTransactionStatus().toLowerCase().trim();
        holder.textViewCreditAmt.setText(fleetListModel.getCreditAmount());
        Log.d(TAG, "onBindViewHolder: textViewCreditAmt : " + fleetListModel.getCreditAmount());

        holder.textViewActQuantity.setText(fleetListModel.getActualCreditQuantity());
        holder.textViewProduct.setText(fleetListModel.getProductName());
        
        if (!transacStatus.equals("null") && !transacStatus.isEmpty() && transacStatus.equals("complete")) {
            holder.imageviewCreditStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.completed));

        }
        Log.d(TAG, "onBindViewHolder: fcreditId - " + fleetListModel.getFuelCreditId());
        holder.layoutFleetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonCodeManager.saveFuelCreditId(context, fleetListModel.getFuelCreditId());

              /*  Intent earnIntent = new Intent(context, ViewCreditRequestDetailsActivity.class);
                context.startActivity(earnIntent);*/
            }
        });

    }


    @Override
    public int getItemCount() {
        return fleetList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCustName, textViewDriverNo, textViewDate,
                textViewVehicleNo, textViewCreditAmt, textViewProduct, textViewActQuantity;
        ImageView imageviewCreditStatus;
        private LinearLayout layoutFleetCard;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            layoutFleetCard = itemView.findViewById(R.id.layout_credit_card);
            textViewCreditAmt = itemView.findViewById(R.id.textview_credit_amount_cr);
            textViewDriverNo = itemView.findViewById(R.id.textview_driver_no_credit);
            textViewDate = itemView.findViewById(R.id.textview_date_credit);
            textViewVehicleNo = itemView.findViewById(R.id.textview_vehicle_no_credit);
            imageviewCreditStatus = itemView.findViewById(R.id.imageview_credit_status_credit);
            textViewProduct = itemView.findViewById(R.id.textview_product_cr);
            textViewActQuantity = itemView.findViewById(R.id.textview_act_quantity);

        }

    }
}
