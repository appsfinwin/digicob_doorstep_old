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
import com.finwingway.digicob.adapters.JLGTransRcylrAdptrModel;

import java.util.ArrayList;

public class TransactionLoanRecycler extends RecyclerView.Adapter<TransactionLoanRecycler.MyViewHolder> {

    public static ArrayList<JLGTransRcylrAdptrModel> dataSet;
    Activity activity;

    public TransactionLoanRecycler(Activity activity, ArrayList<JLGTransRcylrAdptrModel> data) {
        this.activity = activity;
        this.dataSet = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView tvCustId;
        TextView tvCustName;
        TextView tvPrincipal;
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
            tvPrincipal = (TextView) itemView.findViewById(R.id.tv_principal);
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row_trans, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int listPosition) {
        final JLGTransRcylrAdptrModel model = dataSet.get(listPosition);
        final String StrCustId = dataSet.get(listPosition).getCustId();
        final String StrCustName = dataSet.get(listPosition).getCustName();
        final String StrPrincipal = dataSet.get(listPosition).getPrincipal();
        final String StrInterest = dataSet.get(listPosition).getInterest();
        final String StrPenalInterest = dataSet.get(listPosition).getPenalInterest();
        final String StrTotalInterest = dataSet.get(listPosition).getTotalInterest();
        final String StrRemittance = dataSet.get(listPosition).getRemittance();

        holder.tvCustId.setText(StrCustId);
        holder.tvCustName.setText(StrCustName);
        holder.tvPrincipal.setText(StrPrincipal);
        holder.tvInterest.setText(StrInterest);
        holder.tvPenalInterest.setText(StrPenalInterest);
        holder.tvTotalInterest.setText(StrTotalInterest);
        holder.edtRemittance.setText(StrRemittance);
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
                    dataSet.get(pos).setRemittance("0.0");
                    notifyDataSetChanged();
                } else {
                    dataSet.get(pos).setSelected(true);
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
                dataSet.get(listPosition).setRemittance(s.toString());
            }
        });
    }

    public static Double addRemitncAmnt() {
        double total_amnt = 0.0;
        try {
            for (int i = 0; i < dataSet.size(); i++) {
                total_amnt = total_amnt + Double.parseDouble(dataSet.get(i).getRemittance());
            }
        } catch (Exception ignored) {
        }
        return total_amnt;
    }

    private Double calcRemitncAmnt(int pos) {
        double total_amnt = 0.0;
        try {
            total_amnt = Double.parseDouble(dataSet.get(pos).getPrincipal()) +
                    Double.parseDouble(dataSet.get(pos).getTotalInterest());
            dataSet.get(pos).setRemittance(String.valueOf(total_amnt));

            notifyDataSetChanged();

        } catch (Exception ignored) {
        }
        return total_amnt;
    }


}


