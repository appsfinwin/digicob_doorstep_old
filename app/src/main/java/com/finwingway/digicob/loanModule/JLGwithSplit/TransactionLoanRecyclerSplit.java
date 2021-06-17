package com.finwingway.digicob.loanModule.JLGwithSplit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.finwingway.digicob.R;
import com.finwingway.digicob.loanModule.JLGSplitSupportClasses.JLGTransRcylrAdptrModelSplit;

import java.util.ArrayList;

public class TransactionLoanRecyclerSplit extends RecyclerView.Adapter<TransactionLoanRecyclerSplit.MyViewHolder> {

    public static ArrayList<JLGTransRcylrAdptrModelSplit> dataSet;
    Activity activity;

    TransactionLoanRecyclerSplit(Activity activity, ArrayList<JLGTransRcylrAdptrModelSplit> data) {
        this.activity = activity;
        this.dataSet = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView tvCustId;
        TextView tvCustName;
        TextView tvAccNo;
        TextView tvPrincipalBal;
        TextView tvPrincipalDue;
        TextView tvInterest;
        TextView tvPenalInterest;
        TextView tvTotalInterest;
        EditText edtRemittance;
        CheckBox CbSelect_item;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tvCustId = (TextView) itemView.findViewById(R.id.tv_cust_id);
            tvCustName = (TextView) itemView.findViewById(R.id.tv_cust_name);
            tvAccNo = (TextView) itemView.findViewById(R.id.tv_acc_no_split);
            tvPrincipalBal = (TextView) itemView.findViewById(R.id.tv_principal);
            tvPrincipalDue = (TextView) itemView.findViewById(R.id.tv_principal_due);
            tvInterest = (TextView) itemView.findViewById(R.id.tv_interest);
            tvPenalInterest = (TextView) itemView.findViewById(R.id.tv_penal_interest);
            tvTotalInterest = (TextView) itemView.findViewById(R.id.tv_total_interest);
            edtRemittance = (EditText) itemView.findViewById(R.id.edt_remittance);
            CbSelect_item = (CheckBox) itemView.findViewById(R.id.check_select);
        }
    }

    @Override
    public int getItemCount() {
//        return list.size();
        return dataSet.size();
    }

    @Override
    public TransactionLoanRecyclerSplit.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row_trans_split, parent, false);
        // set the view's size, margins, paddings and layout parameters
        TransactionLoanRecyclerSplit.MyViewHolder vh = new TransactionLoanRecyclerSplit.MyViewHolder(v);
        // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final TransactionLoanRecyclerSplit.MyViewHolder holder, final int listPosition) {
        final JLGTransRcylrAdptrModelSplit model = dataSet.get(listPosition);
        final String StrCustId = dataSet.get(listPosition).getSplitCustId();
        final String StrCustName = dataSet.get(listPosition).getSplitCustName();
        final String StrAccountNo = dataSet.get(listPosition).getSplitAccountNo();
        final String StrPrincipal = dataSet.get(listPosition).getSplitPrincipalBlnc();
        final String StrPrincipalDue = dataSet.get(listPosition).getSplitPrincipalDue();
        final String StrInterest = dataSet.get(listPosition).getSplitInterest();
        final String StrPenalInterest = dataSet.get(listPosition).getSplitPenalInterest();
        final String StrTotalInterest = dataSet.get(listPosition).getSplitTotalInterest();
        final String StrRemittance = dataSet.get(listPosition).getSplitRemittance();

        holder.tvCustId.setText(StrCustId);
        holder.tvCustName.setText(StrCustName);
        holder.tvAccNo.setText(StrAccountNo);
        holder.tvPrincipalBal.setText(StrPrincipal);
        holder.tvPrincipalDue.setText(StrPrincipalDue);
        holder.tvInterest.setText(StrInterest);
        holder.tvPenalInterest.setText(StrPenalInterest);
        holder.tvTotalInterest.setText(StrTotalInterest);
        holder.edtRemittance.setText(StrRemittance);

        holder.CbSelect_item.setChecked(dataSet.get(listPosition).getSplitSelected());
        holder.CbSelect_item.setTag(listPosition);
        holder.CbSelect_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.CbSelect_item.getTag();
                Log.e("CbSelect_item: ", dataSet.get(pos).getSplitCustName());
                if (dataSet.get(pos).getSplitSelected()) {
                    dataSet.get(pos).setSplitSelected(false);
                    dataSet.get(pos).setSplitRemittance("0.0");
                    notifyDataSetChanged();
                } else {
                    dataSet.get(pos).setSplitSelected(true);
                    calcRemitncAmnt(pos);
                }
            }
        });

        holder.edtRemittance.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                dataSet.get(listPosition).setSplitRemittance(s.toString());
            }
        });

    }

    public static Double addRemitncAmntSplit() {
        double total_amnt = 0.0;
        try {
            for (int i = 0; i < dataSet.size(); i++) {
                total_amnt = total_amnt + Double.parseDouble(dataSet.get(i).getSplitRemittance());
            }
        } catch (Exception ignored) {
        }
        return total_amnt;
    }

    private Double calcRemitncAmnt(int pos) {
        double total_amnt = 0.0;
        try {
            total_amnt = Double.parseDouble(dataSet.get(pos).getSplitPrincipalBlnc()) +
                    Double.parseDouble(dataSet.get(pos).getSplitPrincipalDue()) +
                    Double.parseDouble(dataSet.get(pos).getSplitTotalInterest());
            dataSet.get(pos).setSplitRemittance(String.valueOf(total_amnt));
            notifyDataSetChanged();
        } catch (Exception ignored) {
        }
        return total_amnt;
    }


}