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
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.models.ArraivalVehiclesListModel;
import com.veelsplusfueldealerapp.models.FleetListModel;

import org.w3c.dom.Text;

import java.util.List;

public class FleetArrivalListAdapter extends RecyclerView.Adapter<FleetArrivalListAdapter.MyViewHolder> {
    private static final String TAG = "VehiclesArraivalsListAd";
    CommonCodeManager commonCodeManager;
    private Context context;
    private int selectedPosition;
    private List<FleetListModel> fleetListModelList;

    public FleetArrivalListAdapter(Context context, List<FleetListModel> fleetListModelList) {
        this.context = context;
        this.fleetListModelList = fleetListModelList;

        commonCodeManager = new CommonCodeManager(context);
    }


    public FleetArrivalListAdapter() {
    }

    @NonNull
    @Override
    public FleetArrivalListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_fleet_arrivals, parent, false);

        return new FleetArrivalListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FleetArrivalListAdapter.MyViewHolder holder, final int position) {
        selectedPosition = position;
        final FleetListModel fleetListModel = fleetListModelList.get(position);

        holder.textViewCustName.setText(fleetListModel.getCustomerName());
        holder.textViewDate.setText(fleetListModel.getDate());
        holder.textViewDriverNo.setText(fleetListModel.getDriverNo());
        holder.textViewVehicleNo.setText(fleetListModel.getVehicleNo());

        final String transacStatus = fleetListModel.getTransactionStatus().toLowerCase().trim();
        Log.d(TAG, "onBindViewHolder:transacStatus: " + transacStatus);
        if (transacStatus.equals("complete")) {
            holder.imageviewCreditStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.completed));
        }

        holder.layoutFleetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonCodeManager.saveFuelCreditId(context, fleetListModel.getFuelCreditId());
                commonCodeManager.saveFuelCreditDetailsForView(context, fleetListModel.getFuelCreditId(), "corporate");
                if (transacStatus.equals("complete")) {
                    commonCodeManager.saveDetailsForCreditReq(context,
                            fleetListModel.getFuelCreditId(), "forview");

                } else {
                    commonCodeManager.saveDetailsForCreditReq(context,
                            fleetListModel.getFuelCreditId(), "");

                }
                Intent earnIntent = new Intent(context, ManualEntryFormActivity.class);
                context.startActivity(earnIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return fleetListModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCustName, textViewDriverNo, textViewDate, textViewVehicleNo;
        ImageView imageviewCreditStatus;
        private LinearLayout layoutFleetCard;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            layoutFleetCard = itemView.findViewById(R.id.layout_fleet_card);
            textViewCustName = itemView.findViewById(R.id.textview_cust_name_fleet);
            textViewDriverNo = itemView.findViewById(R.id.textview_driver_no_fleet);
            textViewDate = itemView.findViewById(R.id.textview_date_fa_fleet);
            textViewVehicleNo = itemView.findViewById(R.id.textview_vehicle_no_fleet);
            imageviewCreditStatus = itemView.findViewById(R.id.imageview_credit_status);
        }

    }
}
