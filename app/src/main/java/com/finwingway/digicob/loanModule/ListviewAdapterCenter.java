package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.finwingway.digicob.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.finwingway.digicob.loanModule.JLGCreateCenter.BRID;
import static com.finwingway.digicob.loanModule.JLGCreateCenter.BRNAME;
import static com.finwingway.digicob.loanModule.JLGCreateCenter.CNTRCOD;
import static com.finwingway.digicob.loanModule.JLGCreateCenter.CNTRNME;
import static com.finwingway.digicob.loanModule.JLGCreateCenter.ConfirmUpdateCntr;
import static com.finwingway.digicob.loanModule.JLGCreateCenter._UPDATE;
import static com.finwingway.digicob.loanModule.JLGCreateCenter.removeFromListCntr;

public class ListviewAdapterCenter extends BaseAdapter {

    JLGCreateCenter loanCreateCenter;

    static final String _CANCEL = "Cancel";
    private ArrayList<HashMap<String, String>> list;
    Activity activity;

    public ListviewAdapterCenter(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.list = list;
        loanCreateCenter = new JLGCreateCenter();
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
        TextView tvCenterName;
        TextView tvBranchId;
        TextView tvMakerId;
        TextView tvMakingTime;
        Button mRemove;
        Button mUpdate;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row_center, null);
            holder = new ViewHolder();
            holder.tvBranchName = convertView.findViewById(R.id.txt_branch_nme);
            holder.tvBranchId = convertView.findViewById(R.id.txt_branch_id);
            holder.tvCenterCode = convertView.findViewById(R.id.txt_center_code);
            holder.tvCenterName = convertView.findViewById(R.id.txt_center_name);
            holder.mRemove = convertView.findViewById(R.id.btn_rmov);
            holder.mUpdate = convertView.findViewById(R.id.btn_updtcntr);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        holder.tvBranchName.setText(map.get(BRNAME));
        holder.tvBranchId.setText(map.get(BRID));
        holder.tvCenterCode.setText(map.get(CNTRCOD));
        holder.tvCenterName.setText(map.get(CNTRNME));
        holder.mUpdate.setText(map.get(_UPDATE));
        holder.mUpdate.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.gray_btn_bg_color), PorterDuff.Mode.MULTIPLY);
        holder.mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                removeFromListCntr(position);
                removeFromListCntr(holder.tvCenterCode.getText().toString(), holder.tvCenterName.getText().toString(), holder.tvBranchId.getText().toString());
            }
        });
        holder.mUpdate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                ConfirmUpdateCntr(holder.tvCenterCode.getText().toString(), holder.tvCenterName.getText().toString(), holder.tvBranchId.getText().toString(), holder.mUpdate.getText().toString());
                holder.mUpdate.setText(_CANCEL);
                holder.mUpdate.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

            }
        });

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#e6e6e6"));
        }

        return convertView;
    }

}
