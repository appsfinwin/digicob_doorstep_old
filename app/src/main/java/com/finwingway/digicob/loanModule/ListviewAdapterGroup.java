package com.finwingway.digicob.loanModule;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.finwingway.digicob.R;
import java.util.ArrayList;
import java.util.HashMap;

import static com.finwingway.digicob.loanModule.JLGCreateGroup.ACCNO_COLUMN;
import static com.finwingway.digicob.loanModule.JLGCreateGroup.CUSTID_COLUMN;
import static com.finwingway.digicob.loanModule.JLGCreateGroup.NAME_COLUMN;
import static com.finwingway.digicob.loanModule.JLGCreateGroup.SLNO_COLUMN;
import static com.finwingway.digicob.loanModule.JLGCreateGroup.removeFromList;

public class ListviewAdapterGroup extends BaseAdapter {

    private ArrayList<HashMap<String, String>> list;
    Activity activity;
//    private static final String SLNO_COLUMN = "SlNo";
//    private static final String ACCNO_COLUMN = "AccNo";
//    private static final String CUSTID_COLUMN = "CustID";
//    private static final String NAME_COLUMN = "Name";

    public ListviewAdapterGroup(Activity activity, ArrayList<HashMap<String, String>> list) {
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
        TextView mSNo;
        TextView mCustid;
        TextView mAccNo;
        TextView mName;
        Button mRemove;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder();
            holder.mSNo =  convertView.findViewById(R.id.txt_sNo);
            holder.mAccNo =  convertView.findViewById(R.id.txt_accno);
            holder.mCustid =  convertView.findViewById(R.id.txt_custid);
            holder.mName = convertView.findViewById(R.id.txt_name);
            holder.mRemove =  convertView.findViewById(R.id.btn_rmov);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        holder.mSNo.setText(map.get(SLNO_COLUMN));
        holder.mAccNo.setText(map.get(ACCNO_COLUMN));
        holder.mCustid.setText(map.get(CUSTID_COLUMN));
        holder.mName.setText(map.get(NAME_COLUMN));

        holder.mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromList(Integer.parseInt(holder.mSNo.getText().toString())-1);
            }

        });


        return convertView;
    }

}
