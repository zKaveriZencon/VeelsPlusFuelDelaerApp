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
import com.veelsplusfueldealerapp.activities.OperatorShiftStartEndActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.models.OperatorDailyWorkListModel;
import com.veelsplusfueldealerapp.models.OperatorDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class AllOperatorsListAdapter extends RecyclerView.Adapter<AllOperatorsListAdapter.MyViewHolder> {
    private static final String TAG = "AllOperatorsListAdapter";
    CommonCodeManager commonCodeManager;
    private Context context;
    private int selectedPosition;
    private List<OperatorDailyWorkListModel> daliyWorkList;

    public AllOperatorsListAdapter(Context context, List<OperatorDailyWorkListModel> daliyWorkList) {
        this.context = context;
        this.daliyWorkList = daliyWorkList;
        commonCodeManager = new CommonCodeManager(context);
    }


    public AllOperatorsListAdapter() {
    }

    @NonNull
    @Override
    public AllOperatorsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_op_list, parent, false);

        return new AllOperatorsListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllOperatorsListAdapter.MyViewHolder holder, final int position) {
        selectedPosition = position;
        final OperatorDailyWorkListModel operatorDailyWorkListModel = daliyWorkList.get(position);
        holder.textViewStartTime.setText(operatorDailyWorkListModel.getStartTime());
        holder.textViewPumpNozzle.setText(operatorDailyWorkListModel.getPumpNozzle());
        holder.textViewActivityStatus.setText(operatorDailyWorkListModel.getActivityStatus());
        holder.textViewCollection.setText(operatorDailyWorkListModel.getCollectionStatus());
        holder.textViewOPName.setText(operatorDailyWorkListModel.getName());
        String status = operatorDailyWorkListModel.getActivityStatus().toLowerCase().trim();

        if (status.equals("completed")) {

            holder.textViewActivityStatus.setTextColor(context.getResources().getColor(R.color.colorLightGreenTr));

        }

        holder.layoutDailyWorkCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonCodeManager.saveFuelStaffPerformId(context, operatorDailyWorkListModel.getFuelStaffPerformId());
                String status = operatorDailyWorkListModel.getActivityStatus().toLowerCase().trim();
                List<String> params = new ArrayList<>();

                commonCodeManager.saveManagerOPList(context, "manager");

                if (status.equals("completed")) {
                    params.add(operatorDailyWorkListModel.getFuelStaffPerformId());
                    params.add("endshift");
                    params.add("completed");

                    commonCodeManager.saveParamsForManageOPShift(context, params);

                    Intent intent = new Intent(context, OperatorShiftStartEndActivity.class);
                    intent.putExtra("forendshift", "endshift");
                    intent.putExtra("fuelStaffPerformId", operatorDailyWorkListModel.getFuelStaffPerformId());
                    intent.putExtra("status", "completed");
                    context.startActivity(intent);
                } else {
                    params.add(operatorDailyWorkListModel.getFuelStaffPerformId());
                    params.add("endshift");
                    params.add("started");

                    commonCodeManager.saveParamsForManageOPShift(context, params);
                    Intent intent = new Intent(context, OperatorShiftStartEndActivity.class);
                    intent.putExtra("forendshift", "endshift");
                    intent.putExtra("fuelStaffPerformId", operatorDailyWorkListModel.getFuelStaffPerformId());
                    context.startActivity(intent);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return daliyWorkList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewStartTime, textViewPumpNozzle, textViewActivityStatus,
                textViewCollection, textViewOPName;
        LinearLayout layoutDailyWorkCard;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewStartTime = itemView.findViewById(R.id.textview_start_time_dw);
            textViewPumpNozzle = itemView.findViewById(R.id.textview_pump_dw);
            textViewActivityStatus = itemView.findViewById(R.id.textview_activity_status_dw);
            textViewCollection = itemView.findViewById(R.id.textview_coll_status_dw);
            layoutDailyWorkCard = itemView.findViewById(R.id.layout_daily_work_card);
            layoutDailyWorkCard = itemView.findViewById(R.id.layout_daily_work_card);

            textViewOPName = itemView.findViewById(R.id.textview_op_name);

        }

    }
}
