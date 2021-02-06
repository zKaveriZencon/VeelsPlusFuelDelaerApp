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
import com.veelsplusfueldealerapp.models.NonFleetListModel;
import com.veelsplusfueldealerapp.models.NonFleetModel;
import com.veelsplusfueldealerapp.models.NonFleetModel;

import java.util.List;

public class NonFleetArrivalListAdapter extends RecyclerView.Adapter<NonFleetArrivalListAdapter.MyViewHolder> {
    private static final String TAG = "VehiclesArraivalsListAd";
    CommonCodeManager commonCodeManager;
    private Context context;
    private int selectedPosition;
    private List<NonFleetModel> nonFleetList;

    public NonFleetArrivalListAdapter(Context context,
                                      List<NonFleetModel> nonFleetList) {
        this.context = context;
        this.nonFleetList = nonFleetList;

        commonCodeManager = new CommonCodeManager(context);
    }


    public NonFleetArrivalListAdapter() {
    }

    @NonNull
    @Override
    public NonFleetArrivalListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_non_fleet_arrivals, parent, false);

        return new NonFleetArrivalListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NonFleetArrivalListAdapter.MyViewHolder holder, final int position) {
        selectedPosition = position;

        final NonFleetModel NonFleetModel = nonFleetList.get(position);

        holder.textViewCustName.setText(NonFleetModel.getDriverNo());
        holder.textViewDate.setText(NonFleetModel.getDate());
        holder.textViewPersonName.setText(NonFleetModel.getPersonName());
        holder.textViewVehicleNo.setText(NonFleetModel.getVehicleNo());

        final String transacStatus = NonFleetModel.getTransactionStatus().toLowerCase().trim();

        if (transacStatus.equals("complete")) {
            holder.imageViewNonFleetStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.completed));
        }

        holder.layoutNonFleetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonCodeManager.saveFuelCreditId(context, NonFleetModel.getFuelCreditId());
                commonCodeManager.saveFuelCreditDetailsForView(context, NonFleetModel.getFuelCreditId(), "person");

                if (transacStatus.equals("complete")) {
                    commonCodeManager.saveDetailsForCreditReq(context,
                            NonFleetModel.getFuelCreditId(), "forview");

                } else {
                    commonCodeManager.saveDetailsForCreditReq(context,
                            NonFleetModel.getFuelCreditId(), "");

                }
                Intent earnIntent = new Intent(context, ManualEntryFormActivity.class);
                context.startActivity(earnIntent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return nonFleetList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewNonFleetStatus;
        private LinearLayout layoutNonFleetCard;
        private TextView textViewCustName, textViewPersonName, textViewDate, textViewVehicleNo;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            layoutNonFleetCard = itemView.findViewById(R.id.layout_non_fleet_card);
            textViewCustName = itemView.findViewById(R.id.textview_custname_non);
            textViewDate = itemView.findViewById(R.id.textview_date_non);
            textViewPersonName = itemView.findViewById(R.id.textview_person_name_non);
            imageViewNonFleetStatus = itemView.findViewById(R.id.imageview_nonfleet_status);
            textViewVehicleNo = itemView.findViewById(R.id.textview_vehicle_no_nfa);


        }

    }
}
