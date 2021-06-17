package com.finwingway.digicob.loanModule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.finwingway.digicob.R;

public class SearchAdapterGroup extends BaseAdapter {
    private String[] grp_name, grp_code, branch, center;
    Context context;
    private static LayoutInflater layoutInflater = null;

    public SearchAdapterGroup(Context context, String[] grpName, String[] grpCode, String[] Branch, String[] Center) {
        this.context = context;
        this.grp_name = grpName;
        this.grp_code = grpCode;
        this.branch = Branch;
        this.center = Center;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return grp_name.length;
    }
    @Override
    public Object getItem(int position) {
        return grp_code[position];
    }

    public Object getGrpName(int position) {
        return grp_name[position];
    }
    public Object getGrpCode(int position) {
        return grp_code[position];
    }
    public Object getBranchGrp(int position) {
        return branch[position];
    }
    public Object getCenterGrp(int position) {
        return center[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = layoutInflater.inflate(R.layout.search_details_list_grp, null);
        TextView Tvgrp_name = vi.findViewById(R.id.tv_grp_name);
        TextView Tvgrp_code = vi.findViewById(R.id.tv_grp_cod);
        TextView Tvbranch = vi.findViewById(R.id.tv_brn_name);
        TextView Tvcenter = vi.findViewById(R.id.tv_cntr_name);
        Tvgrp_name.setText(grp_name[position]);
        Tvgrp_code.setText(grp_code[position]);
        Tvbranch.setText(branch[position]);
        Tvcenter.setText(center[position]);
        if (position % 2 == 1) {
            vi.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
            vi.setBackgroundColor(Color.parseColor("#e6e6e6"));
        }
        return vi;
    }
}
