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
import com.veelsplusfueldealerapp.activities.UpdateProductReceiptActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.models.InventoryDetailsListModel;

import java.util.List;

public class InventoryDetailsListAdapter extends RecyclerView.Adapter<InventoryDetailsListAdapter.MyViewHolder> {
    private static final String TAG = "InventoryDetailsListAda";
    CommonCodeManager commonCodeManager;
    private Context context;
    private int selectedPosition;
    private List<InventoryDetailsListModel> inventoryList;


    public InventoryDetailsListAdapter(Context context, List<InventoryDetailsListModel> inventoryList) {
        this.context = context;
        this.inventoryList = inventoryList;
        commonCodeManager = new CommonCodeManager(context);
    }


    public InventoryDetailsListAdapter() {
    }

    @NonNull
    @Override
    public InventoryDetailsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_inventory, parent, false);

        return new InventoryDetailsListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InventoryDetailsListAdapter.MyViewHolder holder, final int position) {
        selectedPosition = position;
        final InventoryDetailsListModel inventoryDetailsListModel = inventoryList.get(position);
        holder.textViewDate.setText(inventoryDetailsListModel.getOpenMetDate());
        holder.textViewQuantity.setText(inventoryDetailsListModel.getQuantity());
        holder.textViewTank.setText(inventoryDetailsListModel.getTankNo());
        holder.textviewProductPl.setText(inventoryDetailsListModel.getProductCategory());
        holder.testRefuelID.setText(inventoryDetailsListModel.getRefuelId());

        final String status = inventoryDetailsListModel.getDecanStatus().toLowerCase().trim();
        if (status.equals("complete")) {
            holder.imageViewProductStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.completed));
        }


        holder.layoutUpdateInventoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commonCodeManager.saveFuelTankRefuelId(context, inventoryDetailsListModel.getFuelTankRefuelId());

                if (status.equals("complete")) {

                    commonCodeManager.saveFuelInventoryOnCardClick(context, "forview", inventoryDetailsListModel.getFuelTankRefuelId());
                    Intent intent = new Intent(context, UpdateProductReceiptActivity.class);
                    context.startActivity(intent);
                } else {
                    commonCodeManager.saveFuelInventoryOnCardClick(context, "", inventoryDetailsListModel.getFuelTankRefuelId());

                    Intent intent = new Intent(context, UpdateProductReceiptActivity.class);
                    context.startActivity(intent);
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutUpdateInventoryCard;
        TextView textViewQuantity, textViewDate, textViewTank, textviewProductPl, testRefuelID;
        ImageView imageViewProductStatus;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            layoutUpdateInventoryCard = itemView.findViewById(R.id.layout_card_update_inventory);
            textViewDate = itemView.findViewById(R.id.textview_date_il);
            textViewQuantity = itemView.findViewById(R.id.textview_quantity_il);
            textViewTank = itemView.findViewById(R.id.textview_tank_no_il);
            imageViewProductStatus = itemView.findViewById(R.id.imageview_product_status);
            textviewProductPl = itemView.findViewById(R.id.textview_product_pl);
            testRefuelID = itemView.findViewById(R.id.test_refuelid);

        }
    }
}
