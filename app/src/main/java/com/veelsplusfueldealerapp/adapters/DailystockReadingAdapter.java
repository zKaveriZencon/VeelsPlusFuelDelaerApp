package com.veelsplusfueldealerapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.DailyStockReadingActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.models.DailyStockMgModel;

import java.util.List;

public class DailystockReadingAdapter extends RecyclerView.Adapter<DailystockReadingAdapter.MyViewHolder> {
    private static final String TAG = "InventoryDetailsListAda";
    CommonCodeManager commonCodeManager;
    private Context context;
    private int selectedPosition;
    private List<DailyStockMgModel> dailyList;


    public DailystockReadingAdapter(Context context, List<DailyStockMgModel> dailyList) {
        this.context = context;
        this.dailyList = dailyList;
        commonCodeManager = new CommonCodeManager(context);

    }


    public DailystockReadingAdapter() {
    }

    @NonNull
    @Override
    public DailystockReadingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_daily_inventory, parent, false);

        return new DailystockReadingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DailystockReadingAdapter.MyViewHolder holder, final int position) {
        selectedPosition = position;
        final DailyStockMgModel dailyStockMgModel = dailyList.get(position);

        holder.textViewDate.setText(dailyStockMgModel.getOpenDipReadDate());
        holder.textViewOpenRead.setText(dailyStockMgModel.getOpenDipScaleReading());
        holder.textViewCloseRead.setText(dailyStockMgModel.getCloseDipScaleReading());
        holder.textViewTankNo.setText(dailyStockMgModel.getTankNo());
        String status = dailyStockMgModel.getStockCheckStatus().toLowerCase().trim();

        if (status.equals("complete")) {
            holder.imageViewStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.completed));
        }

        holder.layoutDSRCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commonCodeManager.saveFuelTankRefuelId(context, dailyStockMgModel.getFuelTankRefuelId());

                String status = dailyStockMgModel.getStockCheckStatus().toLowerCase().trim();
                if (status.equals("complete")) {
                    Log.d(TAG, "onClick: status : " + status);
                    commonCodeManager.saveDailyStockOnCardClick(context, dailyStockMgModel.getFuelTankRefuelId(), "updatestock", "completed");
                    Intent intent = new Intent(context, DailyStockReadingActivity.class);
                    context.startActivity(intent);

                } else {
                    commonCodeManager.saveDailyStockOnCardClick(context, dailyStockMgModel.getFuelTankRefuelId(), "updatestock", "");

                    Intent intent = new Intent(context, DailyStockReadingActivity.class);
                    context.startActivity(intent);
                }

            }
        });
    }


    @Override
    public int getItemCount() {

        return dailyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutDSRCard;
        TextView textViewDate, textViewTankNo, textViewOpenRead, textViewCloseRead;
        ImageView imageViewStatus;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            layoutDSRCard = itemView.findViewById(R.id.layout_dsr_card);
            textViewDate = itemView.findViewById(R.id.textView_date_dsr);
            textViewTankNo = itemView.findViewById(R.id.textview_tank_no_dsr);
            textViewOpenRead = itemView.findViewById(R.id.textview_open_dip_dsr);
            textViewCloseRead = itemView.findViewById(R.id.textView_close_dip_dsr);
            imageViewStatus = itemView.findViewById(R.id.imageview_dsr_status);
        }

    }
}
