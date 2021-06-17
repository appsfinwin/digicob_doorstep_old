package com.finwingway.digicob.loanModule.JLGwithSplit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.finwingway.digicob.R;
import com.finwingway.digicob.loanModule.JLGSplitSupportClasses.JLGTransRcylrAdptrModelSplit;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class TransactionLoanAdapterAddChargs extends Fragment {
    static String TR_SLNO, TR_CUSID, TR_CUSNAME, TR_ACC_NO,
            TR_PRINCIPAL, TR_PRINCIPAL_DUE, TR_INTEREST, TR_PENALINTRST,
            TR_TOTALINTRST, TR_REMITTANCE;

    Button next, prev;
    View rootView;
    ViewPager viewPagerTrans = MainTransactionLoan.viewPagerTrans;
    RequestQueue requestQueue;
    public static ArrayList<JLGTransRcylrAdptrModelSplit> dataitem;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_jlg_trnsctn_add_chrg, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        dataitem = new ArrayList<JLGTransRcylrAdptrModelSplit>();

        populateData();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_addchrg);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        TransactionLoanRecyclerSplit customAdapter = new TransactionLoanRecyclerSplit(getActivity(), dataitem);
        customAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

        next = rootView.findViewById(R.id.btn_trans_nxt_two);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerTrans.setCurrentItem(3, true);
            }
        });
        prev = rootView.findViewById(R.id.btn_pre_two);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerTrans.setCurrentItem(1, true);
            }
        });

        return rootView;
    }

    public void populateData() {
        TR_SLNO = "1";
        TR_CUSID = "133055";
        TR_CUSNAME = "FAHAD";
        TR_ACC_NO = "Acc";
        TR_PRINCIPAL = "200000";
        TR_PRINCIPAL_DUE = "10202";
        TR_INTEREST = "8";
        TR_PENALINTRST = "180";
        TR_TOTALINTRST = "540";
        TR_REMITTANCE = "";

        dataitem.clear();
        for (int i = 0; i < 2; i++) {
            dataitem.add(new JLGTransRcylrAdptrModelSplit(TR_CUSID, TR_CUSNAME + i, TR_ACC_NO, TR_PRINCIPAL,
                    TR_PRINCIPAL_DUE, TR_INTEREST, TR_PENALINTRST, TR_TOTALINTRST, TR_REMITTANCE));
        }
    }

}
