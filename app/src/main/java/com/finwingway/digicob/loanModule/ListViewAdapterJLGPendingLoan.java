package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.finwingway.digicob.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.finwingway.digicob.loanModule.JLGPendingLoanList.PND_ACC_NO;
import static com.finwingway.digicob.loanModule.JLGPendingLoanList.PND_BRNAME;
import static com.finwingway.digicob.loanModule.JLGPendingLoanList.PND_GROUPNAME;
import static com.finwingway.digicob.loanModule.JLGPendingLoanList.PND_LOANTYP;
import static com.finwingway.digicob.loanModule.JLGPendingLoanList.PND_PERIODTYP;
import static com.finwingway.digicob.loanModule.JLGPendingLoanList.PND_PURPOSE;
import static com.finwingway.digicob.loanModule.JLGPendingLoanList.PND_SUBPURPOSE;
import static com.finwingway.digicob.loanModule.JLGPendingLoanList.removePendingList;

public class ListViewAdapterJLGPendingLoan extends BaseAdapter implements Filterable {

    private ArrayList<HashMap<String, String>> list;
    private ArrayList<HashMap<String, String>> filteredList = null;
    private ItemFilter mFilter = new ItemFilter();
    Activity activity;

    public ListViewAdapterJLGPendingLoan(Activity activity, ArrayList<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.filteredList = list;
        this.list = list;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tvAccNo;
        TextView tvLoanType;
        TextView tvBranchName;
        TextView tvPeriodType;
        TextView tvPurpose;
        TextView tvSubPurpose;
        TextView tvGrpName;
        Button btnEdit;
        Button btnRemov;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.loan_jlg_pending_loan_list_row, null);
            holder = new ViewHolder();
            holder.tvAccNo = convertView.findViewById(R.id.txt_accno);
            holder.tvLoanType = convertView.findViewById(R.id.txt_loan_typ);
            holder.tvBranchName = convertView.findViewById(R.id.txt_branch_name);
            holder.tvPeriodType = convertView.findViewById(R.id.txt_period_typ);
            holder.tvPurpose = convertView.findViewById(R.id.txt_purpose);
            holder.tvSubPurpose = convertView.findViewById(R.id.txt_sub_purpose);
            holder.tvGrpName = convertView.findViewById(R.id.txt_grp_name);
            holder.btnEdit = convertView.findViewById(R.id.btn_edt);
            holder.btnRemov = convertView.findViewById(R.id.btn_rmv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = filteredList.get(position);
        holder.tvAccNo.setText(map.get(PND_ACC_NO));
        holder.tvLoanType.setText(map.get(PND_LOANTYP));
        holder.tvBranchName.setText(map.get(PND_BRNAME));
        holder.tvPeriodType.setText(map.get(PND_PERIODTYP));
        holder.tvPurpose.setText(map.get(PND_PURPOSE));
        holder.tvSubPurpose.setText(map.get(PND_SUBPURPOSE));
        holder.tvGrpName.setText(map.get(PND_GROUPNAME));
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnRemov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePendingList(holder.tvAccNo.getText().toString());
            }
        });

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#e6e6e6"));
        }

        return convertView;
    }

    //===================  Filter  =================================

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final ArrayList<HashMap<String, String>> flist = list;
            int count = flist.size();
            final ArrayList<HashMap<String, String>> nlist = new ArrayList<HashMap<String, String>>(count);
            String filterableString;

            for (int i = 0; i < count; i++) {
                HashMap<String, String> Hshfilterable = flist.get(i);
                filterableString = Hshfilterable.get(PND_ACC_NO);
                if (filterableString.toLowerCase().startsWith(filterString)) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    hashmap.put(PND_ACC_NO, Hshfilterable.get(PND_ACC_NO));
                    hashmap.put(PND_LOANTYP, Hshfilterable.get(PND_LOANTYP));
                    hashmap.put(PND_BRNAME, Hshfilterable.get(PND_BRNAME));
                    hashmap.put(PND_PERIODTYP, Hshfilterable.get(PND_PERIODTYP));
                    hashmap.put(PND_PURPOSE, Hshfilterable.get(PND_PURPOSE));
                    hashmap.put(PND_SUBPURPOSE, Hshfilterable.get(PND_SUBPURPOSE));
                    hashmap.put(PND_GROUPNAME, Hshfilterable.get(PND_GROUPNAME));
                    nlist.add(hashmap);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<HashMap<String, String>>) results.values;
            notifyDataSetChanged();
        }
    }



}
