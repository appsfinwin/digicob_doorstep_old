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
import com.finwingway.digicob.loanModule.JLGSplitSupportClasses.JLGClosingRcylrAdptrModel;

import java.util.ArrayList;

public class ClosingLoanRecycler extends RecyclerView.Adapter<ClosingLoanRecycler.MyViewHolder> {

    public static ArrayList<JLGClosingRcylrAdptrModel> dataSet;
    Activity activity;

    public ClosingLoanRecycler(Activity activity, ArrayList<JLGClosingRcylrAdptrModel> data) {
        this.activity = activity;
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row_closing, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ClosingLoanRecycler.MyViewHolder vh = new ClosingLoanRecycler.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int listPosition) {
        final JLGClosingRcylrAdptrModel model = dataSet.get(listPosition);
        final String StrCustId = dataSet.get(listPosition).getCustId();
        final String StrCustName = dataSet.get(listPosition).getCustName();
        final String StrPrincipal = dataSet.get(listPosition).getPrincipalBlnc();
        final String StrInterest = dataSet.get(listPosition).getInterest();
        final String StrPenalInterest = dataSet.get(listPosition).getPenalInterest();
        final String StrTotalInterest = dataSet.get(listPosition).getTotalInterest();
        final String StrRemittance = dataSet.get(listPosition).getRemittance();

        holder.tvCustId.setText(StrCustId);
        holder.tvCustName.setText(StrCustName);
        holder.tvPrincipalBal.setText(StrPrincipal);
        holder.tvInterest.setText(StrInterest);
        holder.tvPenalInterest.setText(StrPenalInterest);
        holder.tvTotalInterest.setText(StrTotalInterest);
        holder.edtRemittance.setText(StrRemittance);

        holder.CbSelect_item.setChecked(dataSet.get(listPosition).getSelected());
        // holder.checkBox.setTag(R.integer.btnplusview, convertView)imarosh;
        holder.CbSelect_item.setTag(listPosition);
        holder.CbSelect_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.CbSelect_item.getTag();
                Log.e("CbSelect_item: ", dataSet.get(pos).getCustName());
                if (dataSet.get(pos).getSelected()) {
                    dataSet.get(pos).setSelected(false);
                } else {
                    dataSet.get(pos).setSelected(true);
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
        TextView tvPrincipalBal;
        TextView tvInterest;
        TextView tvPenalInterest;
        TextView tvTotalInterest;
        EditText edtRemittance;
        CheckBox CbSelect_item;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tvCustName = (TextView) itemView.findViewById(R.id.tv_cust_name_c);
            tvCustId = (TextView) itemView.findViewById(R.id.tv_cust_id_c);
            tvPrincipalBal = (TextView) itemView.findViewById(R.id.tv_principal_c);
            tvInterest = (TextView) itemView.findViewById(R.id.tv_interest_c);
            tvPenalInterest = (TextView) itemView.findViewById(R.id.tv_penal_interest_c);
            tvTotalInterest = (TextView) itemView.findViewById(R.id.tv_total_interest_c);
            edtRemittance = (EditText) itemView.findViewById(R.id.edt_remittance_c);
            CbSelect_item = (CheckBox) itemView.findViewById(R.id.check_select_c);
        }
    }

    public static Double addRemitncAmnt() {
        double total_amnt = 0;
        try {
            for (int i = 0; i < dataSet.size(); i++) {
                total_amnt = total_amnt + Double.parseDouble(dataSet.get(i).getRemittance());
            }
        } catch (Exception ignored) {
        }
        return total_amnt;
    }
}
