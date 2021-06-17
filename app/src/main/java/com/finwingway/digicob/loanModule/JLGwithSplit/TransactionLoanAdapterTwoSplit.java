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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.finwingway.digicob.R;
import com.finwingway.digicob.adapters.JLGTransRcylrAdptrModel;
import com.finwingway.digicob.loanModule.JLGSplitSupportClasses.JLGTransRcylrAdptrModelSplit;
import com.finwingway.digicob.loanModule.JLGSplitSupportClasses.JLGTransRcylrAdptrModelSplitDebit;
import com.finwingway.digicob.loanModule.JLGSplitSupportClasses.TransactionLoanRecyclerSplitDebit;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class TransactionLoanAdapterTwoSplit extends Fragment {
    Button next, prev;
    View rootView;
    ViewPager viewPagerTrans = MainTransactionLoan.viewPagerTrans;
    RequestQueue requestQueue;
    public static ArrayList<JLGTransRcylrAdptrModelSplit> dataitemSplit;
    public static ArrayList<JLGTransRcylrAdptrModelSplitDebit> dataitemSplitDebit;
    public static ArrayList<JLGTransRcylrAdptrModel> dataitem;
    RecyclerView recyclerView;
    static String TrType;
    TransactionLoanRecycler loanRecyclerAdapter;
    TransactionLoanRecyclerSplit loanRecyclerAdapterSplit;
    TransactionLoanRecyclerSplitDebit loanRecyclerAdapterSplitDebit;

    @SuppressLint("ValidFragment")
    public TransactionLoanAdapterTwoSplit(String strTrnsType) {
        TrType = strTrnsType;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.loan_jlg_transaction_two, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
        dataitemSplit = new ArrayList<JLGTransRcylrAdptrModelSplit>();
        dataitemSplitDebit = new ArrayList<JLGTransRcylrAdptrModelSplitDebit>();
        dataitem = new ArrayList<JLGTransRcylrAdptrModel>();

        // get the reference of RecyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientaion
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        next = rootView.findViewById(R.id.btn_trans_nxt_two);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TrType.equals("SPLIT")) {
                    if (TransactionLoanAdapterOne.StrSubTransTypeCode.equals("Dr")) {
                        TransactionLoanGetSet.setTotalRemitncAmnt(
                                String.valueOf(TransactionLoanRecyclerSplitDebit.addRemitncAmntSplitDebit()));
                    } else {
                        TransactionLoanGetSet.setTotalRemitncAmnt(
                                String.valueOf(TransactionLoanRecyclerSplit.addRemitncAmntSplit()));
                    }
                } else {
                    TransactionLoanGetSet.setTotalRemitncAmnt(
                            String.valueOf(TransactionLoanRecycler.addRemitncAmnt()));
                }
                viewPagerTrans.setCurrentItem(2, true);
            }
        });
        prev = rootView.findViewById(R.id.btn_pre_two);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerTrans.setCurrentItem(0, true);
            }
        });

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("populate_data"));
    }

    //========================================================================================================
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String intentRslt = intent.getStringExtra("data");
                if (!TextUtils.isEmpty(intentRslt)) {
                    if (intentRslt.equals("clear_grp_data")) {
                        clearData();
                    } else {
                        if (TrType.equals("SPLIT")) {
                            if (TransactionLoanAdapterOne.StrSubTransTypeCode.equals("Dr")) {
                                populateDataSplitDebit();
                            } else {
                                populateDataSplit();
                            }
                        } else {
                            populateData();
                        }
                    }
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

    public void clearData() {
        if (TrType.equals("SPLIT")) {
            if (TransactionLoanAdapterOne.StrSubTransTypeCode.equals("Cr")) {
                dataitemSplitDebit.clear();
                loanRecyclerAdapterSplitDebit = new TransactionLoanRecyclerSplitDebit(getActivity(), dataitemSplitDebit);
                loanRecyclerAdapterSplitDebit.notifyDataSetChanged();
                recyclerView.setAdapter(loanRecyclerAdapterSplitDebit);
            } else {
                dataitemSplit.clear();
                loanRecyclerAdapterSplit = new TransactionLoanRecyclerSplit(getActivity(), dataitemSplit);
                loanRecyclerAdapterSplit.notifyDataSetChanged();
                recyclerView.setAdapter(loanRecyclerAdapterSplit);
            }
        } else {
            dataitem.clear();
            loanRecyclerAdapter = new TransactionLoanRecycler(getActivity(), dataitem);
            loanRecyclerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(loanRecyclerAdapter);
        }
    }

    public void populateData() {
        dataitem.clear();
        for (int i = 0; i < TransactionLoanAdapterOne.datCount; i++) {
            dataitem.add(new JLGTransRcylrAdptrModel(
                    TransactionLoanAdapterOne.StrCustomerIDAry[i],
                    TransactionLoanAdapterOne.StrCustomerNameAry[i],
                    TransactionLoanAdapterOne.StrPrincipalBalanceAry[i],
                    TransactionLoanAdapterOne.StrInterestAry[i],
                    TransactionLoanAdapterOne.StrPenalInterestAry[i],
                    TransactionLoanAdapterOne.StrTotalInterestAry[i],
                    TransactionLoanAdapterOne.StrRemittanceAry[i]));
        }
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        loanRecyclerAdapter = new TransactionLoanRecycler(getActivity(), dataitem);
        loanRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(loanRecyclerAdapter); // set the Adapter to RecyclerView
    }

    public void populateDataSplit() {
        dataitemSplit.clear();
        for (int i = 0; i < TransactionLoanAdapterOne.datCount; i++) {
            dataitemSplit.add(new JLGTransRcylrAdptrModelSplit(
                    TransactionLoanAdapterOne.StrCustomerIDAry[i],
                    TransactionLoanAdapterOne.StrCustomerNameAry[i],
                    TransactionLoanAdapterOne.StrAccountNumberAry[i],
                    TransactionLoanAdapterOne.StrPrincipalBalanceAry[i],
                    TransactionLoanAdapterOne.StrPrincipalDueAry[i],
                    TransactionLoanAdapterOne.StrInterestAry[i],
                    TransactionLoanAdapterOne.StrPenalInterestAry[i],
                    TransactionLoanAdapterOne.StrTotalInterestAry[i],
                    TransactionLoanAdapterOne.StrRemittanceAry[i]));
        }
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        loanRecyclerAdapterSplit = new TransactionLoanRecyclerSplit(getActivity(), dataitemSplit);
        loanRecyclerAdapterSplit.notifyDataSetChanged();
        recyclerView.setAdapter(loanRecyclerAdapterSplit); // set the Adapter to RecyclerView
    }

    public void populateDataSplitDebit() {
        dataitemSplitDebit.clear();
        for (int i = 0; i < TransactionLoanAdapterOne.datCount; i++) {
            dataitemSplitDebit.add(new JLGTransRcylrAdptrModelSplitDebit(
                    TransactionLoanAdapterOne.StrAccountNumberAryDr[i],
                    TransactionLoanAdapterOne.StrCustomerIDAryDr[i],
                    TransactionLoanAdapterOne.StrCustomerNameAryDr[i],
                    TransactionLoanAdapterOne.StrAmountAryDr[i]));
        }
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        loanRecyclerAdapterSplitDebit = new TransactionLoanRecyclerSplitDebit(getActivity(), dataitemSplitDebit);
        loanRecyclerAdapterSplitDebit.notifyDataSetChanged();
        recyclerView.setAdapter(loanRecyclerAdapterSplitDebit); // set the Adapter to RecyclerView
    }

    //===============================================================================================


}
