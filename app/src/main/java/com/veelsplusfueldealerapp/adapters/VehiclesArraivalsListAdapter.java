package com.veelsplusfueldealerapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.ManualEntryFormActivity;
import com.veelsplusfueldealerapp.models.ArraivalVehiclesListModel;

import java.util.List;

public class VehiclesArraivalsListAdapter extends RecyclerView.Adapter<VehiclesArraivalsListAdapter.MyViewHolder> {
    private static final String TAG = "VehiclesArraivalsListAd";
    private Context context;
    private int selectedPosition;
    private List<ArraivalVehiclesListModel> vehiclesList;


    public VehiclesArraivalsListAdapter(Context context, List<ArraivalVehiclesListModel> vehiclesList) {
        this.context = context;
        this.vehiclesList = vehiclesList;

    }


    @NonNull
    @Override
    public VehiclesArraivalsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_fleet_arrivals, parent, false);

        return new VehiclesArraivalsListAdapter.MyViewHolder(view);
    }


    public VehiclesArraivalsListAdapter() {
    }

    @Override
    public void onBindViewHolder(@NonNull final VehiclesArraivalsListAdapter.MyViewHolder holder, final int position) {
        selectedPosition = position;
        holder.layoutFleetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent earnIntent = new Intent(context, ManualEntryFormActivity.class);
                context.startActivity(earnIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return 30;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
private LinearLayout layoutFleetCard;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            layoutFleetCard = itemView.findViewById(R.id.layout_fleet_card);
        }

    }
}
