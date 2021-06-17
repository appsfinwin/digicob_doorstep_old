package com.finwingway.digicob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter_account_details_loan extends BaseAdapter {
    private String[] acc_name, cus_id, acc_no, loan_scheme;
    Context context;
    private static LayoutInflater layoutInflater = null;

    public Adapter_account_details_loan(Context context, String[] acc_name, String[] cus_id, String[] acc_number,
                                   String[] _scheme) {
        this.context = context;
        this.acc_name = acc_name;
        this.cus_id = cus_id;
        this.acc_no = acc_number;
        this.loan_scheme = _scheme;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return acc_name.length;
    }

    @Override
    public Object getItem(int position) {
        return acc_no[position];
    }

    public Object getCusId(int position) {
        return cus_id[position];
    }

    public Object getAccName(int position) {
        return acc_name[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = layoutInflater.inflate(R.layout.acc_details_row_list_view_loan, null);
        TextView name_TextView = (TextView) vi.findViewById(R.id.list_row_name);
        TextView cus_id_TextView = (TextView) vi.findViewById(R.id.cus_id);
        TextView acc_no_TextView = (TextView) vi.findViewById(R.id.list_row_acc_no);
        TextView scheme_TextView = (TextView) vi.findViewById(R.id.list_row_scheme);
        name_TextView.setText(acc_name[position]);
        cus_id_TextView.setText(cus_id[position]);
        acc_no_TextView.setText(acc_no[position]);
        scheme_TextView.setText(loan_scheme[position]);
        return vi;
    }


}
