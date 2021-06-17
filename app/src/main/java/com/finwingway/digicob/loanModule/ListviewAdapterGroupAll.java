package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.finwingway.digicob.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.finwingway.digicob.loanModule.AllLoanGroup.ALL_BRNAME;
import static com.finwingway.digicob.loanModule.AllLoanGroup.ALL_CNTRCOD;
import static com.finwingway.digicob.loanModule.AllLoanGroup.ALL_GRPCOD;
import static com.finwingway.digicob.loanModule.AllLoanGroup.ALL_GRPNAME;
import static com.finwingway.digicob.loanModule.AllLoanGroup.ALL_MAKERID;
import static com.finwingway.digicob.loanModule.AllLoanGroup.ALL_MAKINGTIM;

public class ListviewAdapterGroupAll extends BaseAdapter {

    static final String _CANCEL = "Cancel";

    private ArrayList<HashMap<String, String>> list;
    Activity activity;

    public ListviewAdapterGroupAll(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tvBranchName;
        TextView tvCenterCode;
        TextView tvGroupName;
        TextView tvGroupCode;
        TextView tvMakerId;
        TextView tvMakingTime;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.loan_all_group_row, null);
            holder = new ViewHolder();
            holder.tvBranchName = convertView.findViewById(R.id.txt_gro_brnh_nam);
            holder.tvCenterCode = convertView.findViewById(R.id.txt_gro_cntr_cod);
            holder.tvGroupCode = convertView.findViewById(R.id.txt_gro_cod);
            holder.tvGroupName = convertView.findViewById(R.id.txt_gro_name);
            holder.tvMakerId = convertView.findViewById(R.id.txt_gro_mak_id);
            holder.tvMakingTime = convertView.findViewById(R.id.txt_gro_mak_tim);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        holder.tvBranchName.setText(map.get(ALL_BRNAME));
        holder.tvCenterCode.setText(map.get(ALL_CNTRCOD));
        holder.tvGroupCode.setText(map.get(ALL_GRPCOD));
        holder.tvGroupName.setText(map.get(ALL_GRPNAME));
        holder.tvMakerId.setText(map.get(ALL_MAKERID));
        holder.tvMakingTime.setText(map.get(ALL_MAKINGTIM));
//        holder.mRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#e6e6e6"));
        }

        return convertView;
    }

}
