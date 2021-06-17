package com.finwingway.digicob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by AnVin on 4/13/2017.
 */

public class Adapter_account_details extends BaseAdapter {
    private String[] acc_name, cus_id, acc_no, acc_phone;
    Context context;
    private static LayoutInflater layoutInflater = null;

    public Adapter_account_details(Context context, String[] acc_name, String[] cus_id, String[] acc_number,
                                   String[] acc_mobile) {
        this.context = context;
        this.acc_name = acc_name;
        this.cus_id = cus_id;
        this.acc_no = acc_number;
        this.acc_phone = acc_mobile;
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
            vi = layoutInflater.inflate(R.layout.acc_details_row_list_view, null);
        TextView name_TextView = (TextView) vi.findViewById(R.id.list_row_name);
        TextView cus_id_TextView = (TextView) vi.findViewById(R.id.cus_id);
        TextView acc_no_TextView = (TextView) vi.findViewById(R.id.list_row_acc_no);
        TextView phone_TextView = (TextView) vi.findViewById(R.id.list_row_phone);
        name_TextView.setText(acc_name[position]);
        cus_id_TextView.setText(cus_id[position]);
        acc_no_TextView.setText(acc_no[position]);
        phone_TextView.setText(acc_phone[position]);
        return vi;
    }
}
