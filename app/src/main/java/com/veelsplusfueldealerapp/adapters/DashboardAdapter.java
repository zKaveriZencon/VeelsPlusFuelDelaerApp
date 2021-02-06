package com.veelsplusfueldealerapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.models.DashboardModel;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardModelVH> implements View.OnClickListener {

    private static final String TAG = "DashboardAdapter";
    List<DashboardModel> dashboardMenuList;
    Context context;
    int selectedPosition;

    public DashboardAdapter(Context context, List<DashboardModel> dashboardMenuList) {
        this.context = context;
        this.dashboardMenuList = dashboardMenuList;
    }

    @NonNull
    @Override
    public DashboardModelVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dashboard_row, parent, false);
        return new DashboardModelVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardModelVH holder, int position) {
        selectedPosition = position;
        DashboardModel dashboardModel = dashboardMenuList.get(position);
        holder.textviewMenuTitle1.getEditText().setText(dashboardModel.getMenuTitle());

        holder.textviewMenuitemFirst.setText(dashboardModel.getMenuitemFirst());
        holder.textviewMenuitemSecond.setText(dashboardModel.getMenuitemSecond());
        holder.textviewMenuitemThird.setText(dashboardModel.getMenuitemThird());


        boolean isExpanded = dashboardMenuList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        holder.imageViewMenuitemFirst.setOnClickListener(this);
        holder.imageViewMenuitemSecond.setOnClickListener(this);
        holder.imageViewMenuitemThird.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return dashboardMenuList.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_menuitem_first:
                if (selectedPosition == 0) {
                    Log.d(TAG, "onClick: selectedposition 0 : "+selectedPosition);
                    Toast.makeText(context, "Refuel clicked", Toast.LENGTH_SHORT).show();
                }
                if (selectedPosition == 1) {
                    Log.d(TAG, "onClick: selectedposition 1: "+selectedPosition);

                    Toast.makeText(context, "Reports clicked", Toast.LENGTH_SHORT).show();
                }
                if (selectedPosition == 2) {
                    Log.d(TAG, "onClick: selectedposition 2: "+selectedPosition);

                    Toast.makeText(context, "Inventory clicked", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageview_menuitem_second:
                break;
            case R.id.imageview_menuitem_third:
                break;

        }
    }

    class DashboardModelVH extends RecyclerView.ViewHolder {

        private static final String TAG = "DashboardModelVH";

        LinearLayout expandableLayout;
        TextView textviewMenuitemFirst, textviewMenuitemSecond, textviewMenuitemThird,
                textviewMenuitemFourth, textviewMenuTitle;
        ImageView imageViewMenuitemFirst, imageViewMenuitemSecond, imageViewMenuitemThird;
        TextInputLayout textviewMenuTitle1;

        public DashboardModelVH(@NonNull final View itemView) {
            super(itemView);
            // textviewMenuTitle = itemView.findViewById(R.id.textview_menu_title);
            textviewMenuitemFirst = itemView.findViewById(R.id.textview_menuitem_first);
            textviewMenuitemSecond = itemView.findViewById(R.id.textview_menuitem_second);
            textviewMenuitemThird = itemView.findViewById(R.id.textview_menuitem_third);
            textviewMenuTitle1 = itemView.findViewById(R.id.textview_menu_title1);

            imageViewMenuitemFirst = itemView.findViewById(R.id.imageview_menuitem_first);
            imageViewMenuitemSecond = itemView.findViewById(R.id.imageview_menuitem_second);
            imageViewMenuitemThird = itemView.findViewById(R.id.imageview_menuitem_third);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            textviewMenuTitle1.setEndIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: textviewMenuTitle clicked");
                    DashboardModel DashboardModel = dashboardMenuList.get(getAdapterPosition());
                    DashboardModel.setExpanded(!DashboardModel.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });


           /* textviewMenuTitle1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });*/
        }
    }



}
