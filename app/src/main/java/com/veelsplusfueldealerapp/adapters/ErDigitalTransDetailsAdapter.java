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
import com.veelsplusfueldealerapp.models.ErDigitalTransModel;
import com.veelsplusfueldealerapp.models.PaymentModelLocal;

import org.w3c.dom.Text;

import java.util.List;

public class ErDigitalTransDetailsAdapter extends RecyclerView.Adapter<ErDigitalTransDetailsAdapter.MyViewHolder> {
    private static final String TAG = "ErDigitalTransDetailsAd";
    private Context context;
    private int selectedPosition;
    private List<PaymentModelLocal> transList;


    public ErDigitalTransDetailsAdapter(Context context, List<PaymentModelLocal> transList) {
        this.context = context;
        this.transList = transList;

    }


    public ErDigitalTransDetailsAdapter() {
    }

    @NonNull
    @Override
    public ErDigitalTransDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_digital_trans, parent, false);

        return new ErDigitalTransDetailsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ErDigitalTransDetailsAdapter.MyViewHolder holder, final int position) {
        selectedPosition = position;

        PaymentModelLocal erDigitalTransModel = transList.get(position);
        holder.textViewTransid.setText(erDigitalTransModel.getTransId());
        holder.textViewTransDate.setText(erDigitalTransModel.getPaymentType());
        holder.textViewTransAmount.setText(erDigitalTransModel.getAmount());
        String serialNo = String.valueOf((position + 1));
        holder.textViewSerialNo.setText(serialNo);
    }


    @Override
    public int getItemCount() {
        return transList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTransAmount, textViewTransDate, textViewTransid, textViewSerialNo;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewSerialNo = itemView.findViewById(R.id.textview_serial_no);
            textViewTransAmount = itemView.findViewById(R.id.textview_trans_amount);
            textViewTransDate = itemView.findViewById(R.id.textview_trans_date);
            textViewTransid = itemView.findViewById(R.id.textview_transId);
        }

    }
}
