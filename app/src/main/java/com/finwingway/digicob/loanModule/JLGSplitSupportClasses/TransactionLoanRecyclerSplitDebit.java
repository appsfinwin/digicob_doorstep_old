package com.finwingway.digicob.loanModule.JLGSplitSupportClasses;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.finwingway.digicob.R;

import java.util.ArrayList;

public class TransactionLoanRecyclerSplitDebit extends RecyclerView.Adapter<TransactionLoanRecyclerSplitDebit.MyViewHolder> {

    public static ArrayList<JLGTransRcylrAdptrModelSplitDebit> dataSet;
    Activity activity;

    public TransactionLoanRecyclerSplitDebit(Activity activity, ArrayList<JLGTransRcylrAdptrModelSplitDebit> data) {
        this.activity = activity;
        this.dataSet = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView tvAccNo;
        TextView tvCustId;
        TextView tvCustName;
        TextView tvAmount;
        CheckBox CbSelect_item;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tvAccNo = (TextView) itemView.findViewById(R.id.tv_acc_no_split_dt);
            tvCustId = (TextView) itemView.findViewById(R.id.tv_cust_id_dt);
            tvCustName = (TextView) itemView.findViewById(R.id.tv_cust_name_dt);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_amnt_dt);
            CbSelect_item = (CheckBox) itemView.findViewById(R.id.check_select_dt);
        }
    }

    @Override
    public int getItemCount() {
//        return list.size();
        return dataSet.size();
    }

    @Override
    public TransactionLoanRecyclerSplitDebit.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row_trans_split_dbt, parent, false);
        // set the view's size, margins, paddings and layout parameters
        TransactionLoanRecyclerSplitDebit.MyViewHolder vh = new TransactionLoanRecyclerSplitDebit.MyViewHolder(v);
        // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionLoanRecyclerSplitDebit.MyViewHolder holder, final int listPosition) {
        final JLGTransRcylrAdptrModelSplitDebit model = dataSet.get(listPosition);
        final String StrAccountNo = dataSet.get(listPosition).getSplitDebitAccountNo();
        final String StrCustId = dataSet.get(listPosition).getSplitDebitCustId();
        final String StrCustName = dataSet.get(listPosition).getSplitDebitCustName();
        final String StrAmount = dataSet.get(listPosition).getSplitDebitAmount();

        holder.tvAccNo.setText(StrAccountNo);
        holder.tvCustId.setText(StrCustId);
        holder.tvCustName.setText(StrCustName);
        holder.tvAmount.setText(StrAmount);

        holder.CbSelect_item.setChecked(dataSet.get(listPosition).getSplitDebitSelected());
        holder.CbSelect_item.setTag(listPosition);
        holder.CbSelect_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.CbSelect_item.getTag();
                Log.e("CbSelect_item: ", dataSet.get(pos).getSplitDebitCustName());
                if (dataSet.get(pos).getSplitDebitSelected()) {
                    dataSet.get(pos).setSplitDebitSelected(false);
                } else {
                    dataSet.get(pos).setSplitDebitSelected(true);
                }
            }
        });
    }

    public static Double addRemitncAmntSplitDebit() {
        double total_amnt = 0.0;
        try {
            for (int i = 0; i < dataSet.size(); i++) {
                if (dataSet.get(i).getSplitDebitSelected()) {
                    total_amnt = total_amnt + Double.parseDouble(dataSet.get(i).getSplitDebitAmount());
                }
            }
        } catch (Exception ignored) {
        }
        return total_amnt;
    }
}
