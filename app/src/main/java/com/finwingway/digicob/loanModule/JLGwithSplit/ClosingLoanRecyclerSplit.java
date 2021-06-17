package com.finwingway.digicob.loanModule.JLGwithSplit;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.finwingway.digicob.R;
import com.finwingway.digicob.loanModule.JLGSplitSupportClasses.JLGClosingRcylrAdptrModelSplit;

import java.util.ArrayList;

public class ClosingLoanRecyclerSplit extends RecyclerView.Adapter<ClosingLoanRecyclerSplit.MyViewHolder> {

    public static ArrayList<JLGClosingRcylrAdptrModelSplit> dataSet;
    Activity activity;

    public ClosingLoanRecyclerSplit(Activity activity, ArrayList<JLGClosingRcylrAdptrModelSplit> data) {
        this.activity = activity;
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row_closing_split, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ClosingLoanRecyclerSplit.MyViewHolder vh = new ClosingLoanRecyclerSplit.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int listPosition) {
        final JLGClosingRcylrAdptrModelSplit model = dataSet.get(listPosition);
        final String StrCustId = dataSet.get(listPosition).getSplitCustId();
        final String StrCustName = dataSet.get(listPosition).getSplitCustName();
        final String StrAccountNo = dataSet.get(listPosition).getSplitAccountNo();
        final String StrPrincipal = dataSet.get(listPosition).getSplitPrincipalBlnc();
        final String StrInterest = dataSet.get(listPosition).getSplitInterest();
        final String StrPenalInterest = dataSet.get(listPosition).getSplitPenalInterest();
        final String StrTotalInterest = dataSet.get(listPosition).getSplitTotalInterest();
        final String StrRemittance = dataSet.get(listPosition).getSplitRemittance();

        holder.tvCustId.setText(StrCustId);
        holder.tvCustName.setText(StrCustName);
        holder.tvAccNo.setText(StrAccountNo);
        holder.tvPrincipalBal.setText(StrPrincipal);
        holder.tvInterest.setText(StrInterest);
        holder.tvPenalInterest.setText(StrPenalInterest);
        holder.tvTotalInterest.setText(StrTotalInterest);
        holder.edtRemittance.setText(StrRemittance);

        holder.CbSelect_item.setChecked(dataSet.get(listPosition).getSplitSelected());
        // holder.checkBox.setTag(R.integer.btnplusview, convertView)imarosh;
        holder.CbSelect_item.setTag(listPosition);
        holder.CbSelect_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.CbSelect_item.getTag();
                Log.e("CbSelect_item: ", dataSet.get(pos).getSplitCustName());
                if (dataSet.get(pos).getSplitSelected()) {
                    dataSet.get(pos).setSplitSelected(false);
                } else {
                    dataSet.get(pos).setSplitSelected(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustId;
        TextView tvCustName;
        TextView tvAccNo;
        TextView tvPrincipalBal;
        TextView tvInterest;
        TextView tvPenalInterest;
        TextView tvTotalInterest;
        EditText edtRemittance;
        CheckBox CbSelect_item;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tvCustName = (TextView) itemView.findViewById(R.id.tv_cust_name_cs);
            tvCustId = (TextView) itemView.findViewById(R.id.tv_cust_id_cs);
            tvAccNo = (TextView) itemView.findViewById(R.id.tv_acc_no_split_cs);
            tvPrincipalBal = (TextView) itemView.findViewById(R.id.tv_principal_cs);
            tvInterest = (TextView) itemView.findViewById(R.id.tv_interest_cs);
            tvPenalInterest = (TextView) itemView.findViewById(R.id.tv_penal_interest_cs);
            tvTotalInterest = (TextView) itemView.findViewById(R.id.tv_total_interest_cs);
            edtRemittance = (EditText) itemView.findViewById(R.id.edt_remittance_cs);
            CbSelect_item = (CheckBox) itemView.findViewById(R.id.check_select_cs);
        }
    }

    public static Double addRemitncAmntSplit() {
        double total_amnt = 0;
        try {
            for (int i = 0; i < dataSet.size(); i++) {
                total_amnt = total_amnt + Double.parseDouble(dataSet.get(i).getSplitRemittance());
            }
        } catch (Exception ignored) {
        }
        return total_amnt;
    }

}
