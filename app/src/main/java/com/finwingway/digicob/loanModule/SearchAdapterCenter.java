package com.finwingway.digicob.loanModule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.finwingway.digicob.R;

public class SearchAdapterCenter extends BaseAdapter {
    private String[] cntr_name, cntr_code, branch_name, maker_id;
    Context context;
    private static LayoutInflater layoutInflater = null;

    public SearchAdapterCenter(Context context, String[] cntrName, String[] cntrCode, String[] branchName, String[] makerId) {
        this.context = context;
        this.cntr_name = cntrName;
        this.cntr_code = cntrCode;
        this.branch_name = branchName;
        this.maker_id = makerId;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cntr_name.length;
    }

    @Override
    public Object getItem(int position) {
        return cntr_code[position];
    }

    public Object getCenterName(int position) {
        return cntr_name[position];
    }

    public Object getBranchName(int position) {
        return branch_name[position];
    }

    public Object getMakerID(int position) {
        return maker_id[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = layoutInflater.inflate(R.layout.search_details_list_view, null);
        TextView txtcntr_name = vi.findViewById(R.id.list_cntr_name);
        TextView txtcntr_cod = vi.findViewById(R.id.list_cntr_cod);
        TextView txtbrn_name = vi.findViewById(R.id.list_brn_name);
        TextView txtmaker_id = vi.findViewById(R.id.list_maker_id);
        txtcntr_name.setText(cntr_name[position]);
        txtcntr_cod.setText(cntr_code[position]);
        txtbrn_name.setText(branch_name[position]);
        txtmaker_id.setText(maker_id[position]);
        if (position % 2 == 1) {
            vi.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
            vi.setBackgroundColor(Color.parseColor("#e6e6e6"));
        }
        return vi;
    }
}
