package com.veelsplusfueldealerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.models.FuelPriceModel;

import java.util.List;

public class FuelPriceListAdapter extends RecyclerView.Adapter<FuelPriceListAdapter.MyViewHolder> {
    private static final String TAG = "FuelPriceListAdapter";
    CommonCodeManager commonCodeManager;
    private Context context;
    private int selectedPosition;
    private List<FuelPriceModel> fuelPriceList;

    public FuelPriceListAdapter(Context context, List<FuelPriceModel>
            fuelPriceList) {
        this.context = context;
        this.fuelPriceList = fuelPriceList;
        commonCodeManager = new CommonCodeManager(context);
    }


    public FuelPriceListAdapter() {
    }

    @NonNull
    @Override
    public FuelPriceListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_pricelist, parent, false);

        return new FuelPriceListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FuelPriceListAdapter.MyViewHolder holder, final int position) {

        selectedPosition = position;
        FuelPriceModel fuelPriceModel = fuelPriceList.get(position);

        holder.textViewProdCategory.setText(fuelPriceModel.getProductCategory());
        holder.textViewProdName.setText(fuelPriceModel.getProductName());
        holder.textViewFpriceDate.setText(fuelPriceModel.getProductPriceDate());
        holder.textViewFuelPrice.setText(fuelPriceModel.getProductSellingPrice());
    }

    @Override
    public int getItemCount() {
        return fuelPriceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProdCategory, textViewProdName, textViewFpriceDate,textViewFuelPrice;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewProdCategory = itemView.findViewById(R.id.textview_prod_category);
            textViewProdName = itemView.findViewById(R.id.textview_prod_name);
            textViewFpriceDate = itemView.findViewById(R.id.textview_fprice_date);
            textViewFuelPrice = itemView.findViewById(R.id.textview_fuel_price);
        }

    }
}
