package com.finwingway.digicob.loanModule.JLGSplitSupportClasses;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.finwingway.digicob.R;
import com.finwingway.digicob.loanModule.JLGwithSplit.TransactionLoanAdapterThree;

import java.util.ArrayList;
import java.util.HashMap;

import static com.finwingway.digicob.loanModule.JLGwithSplit.TransactionLoanAdapterThree.ACCNO_COLUMN;
import static com.finwingway.digicob.loanModule.JLGwithSplit.TransactionLoanAdapterThree.AMOUNT_COLUMN;
import static com.finwingway.digicob.loanModule.JLGwithSplit.TransactionLoanAdapterThree.BNK_CHRGS_COLUMN;

public class ListviewAdapterBankCharge extends BaseAdapter {

    public static ArrayList<HashMap<String, String>> list;
    Activity activity;

    public ListviewAdapterBankCharge(Activity activity, ArrayList<HashMap<String, String>> list) {
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
//        TextView mSNo;
        TextView mAccNo;
        TextView mBnkChrgs;
        TextView mAmount;
        Button mRemove;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_bnk_chrgs_row, null);
            holder = new ViewHolder();
//            holder.mSNo =  convertView.findViewById(R.id.txt_sNo);
            holder.mAccNo =  convertView.findViewById(R.id.txt_accno_bc);
            holder.mBnkChrgs =  convertView.findViewById(R.id.txt_bnkchrgs_bc);
            holder.mAmount = convertView.findViewById(R.id.txt_amount_bc);
            holder.mRemove =  convertView.findViewById(R.id.btn_rmov_bc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        holder.mAccNo.setText(map.get(ACCNO_COLUMN));
        holder.mBnkChrgs.setText(map.get(BNK_CHRGS_COLUMN));
        holder.mAmount.setText(map.get(AMOUNT_COLUMN));

        holder.mRemove.setTag(position);
        holder.mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionLoanAdapterThree.removeFromList(position);
            }
        });

        return convertView;
    }

}

