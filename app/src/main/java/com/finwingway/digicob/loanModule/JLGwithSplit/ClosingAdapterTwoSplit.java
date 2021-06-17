package com.finwingway.digicob.loanModule.JLGwithSplit;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.finwingway.digicob.loanModule.JLGSplitSupportClasses.JLGClosingRcylrAdptrModel;
import com.finwingway.digicob.loanModule.JLGSplitSupportClasses.JLGClosingRcylrAdptrModelSplit;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ClosingAdapterTwoSplit extends Fragment {
    Button next, prev;
    View rootView;
    ViewPager viewPagerClsng = MainClosingLoan.viewPagerClsng;
    RequestQueue requestQueue;
    public static ArrayList<JLGClosingRcylrAdptrModelSplit> dataitemSplit;
    public static ArrayList<JLGClosingRcylrAdptrModel> dataitem;
    RecyclerView recyclerView;
    static String TrType;

    @SuppressLint("ValidFragment")
    public ClosingAdapterTwoSplit(String strTrnsType) {
        TrType = strTrnsType;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.loan_jlg_clsng_two, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
        dataitemSplit = new ArrayList<JLGClosingRcylrAdptrModelSplit>();
        dataitem = new ArrayList<JLGClosingRcylrAdptrModel>();

        // get the reference of RecyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_clsng);
        // set a LinearLayoutManager with default vertical orientaion
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        next = rootView.findViewById(R.id.btn_clsng_nxt_two);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TrType.equals("SPLIT")) {
                    ClosingAdapterGetSet.setTotalRemitncAmnt(String.valueOf(ClosingLoanRecyclerSplit.addRemitncAmntSplit()));
                } else {
                    ClosingAdapterGetSet.setTotalRemitncAmnt(String.valueOf(ClosingLoanRecycler.addRemitncAmnt()));
                }
                viewPagerClsng.setCurrentItem(2, true);
            }
        });
        prev = rootView.findViewById(R.id.btn_clsng_pre_two);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerClsng.setCurrentItem(0, true);
            }
        });

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("populate_data_cls"));
    }

    //========================================================================================================

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (TrType.equals("SPLIT")) {
                    populateDataSplit();
                } else {
                    populateData();
                }
            } catch (Exception ignored) {
            }
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    //========================================================================================================

    public void populateData() {
        dataitem.clear();
        for (int i = 0; i < ClosingAdapterOne.datCount; i++) {
            dataitem.add(new JLGClosingRcylrAdptrModel(
                    ClosingAdapterOne.StrCustomerIDAry[i],
                    ClosingAdapterOne.StrCustomerNameAry[i],
//                    ClosingAdapterOne.StrPrincipalBalanceAry[i],
                    String.valueOf(1000 + i),
                    ClosingAdapterOne.StrInterestAry[i],
                    ClosingAdapterOne.StrPenalInterestAry[i],
//                    ClosingAdapterOne.StrTotalInterestAry[i],
                    String.valueOf(500 + i),
                    ClosingAdapterOne.StrRemittanceAry[i]));
        }
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        ClosingLoanRecycler customAdapter = new ClosingLoanRecycler(getActivity(), dataitem);
        customAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
    }

    public void populateDataSplit() {
        dataitemSplit.clear();
        for (int i = 0; i < ClosingAdapterOne.datCount; i++) {
            dataitemSplit.add(new JLGClosingRcylrAdptrModelSplit(
                    ClosingAdapterOne.StrCustomerIDAry[i],
                    ClosingAdapterOne.StrCustomerNameAry[i],
                    ClosingAdapterOne.StrAccountNumberAry[i],
//                    ClosingAdapterOne.StrPrincipalBalanceAry[i],
                    String.valueOf(2000 + i),
                    ClosingAdapterOne.StrInterestAry[i],
                    ClosingAdapterOne.StrPenalInterestAry[i],
//                    ClosingAdapterOne.StrTotalInterestAry[i],
                    String.valueOf(500 + i),
                    ClosingAdapterOne.StrRemittanceAry[i]));
        }

        // call the constructor of CustomAdapter to send the reference and data to Adapter
        ClosingLoanRecyclerSplit customAdapter = new ClosingLoanRecyclerSplit(getActivity(), dataitemSplit);
        customAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
    }


}
