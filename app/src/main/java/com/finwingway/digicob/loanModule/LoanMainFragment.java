package com.finwingway.digicob.loanModule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.finwingway.digicob.R;
import com.finwingway.digicob.loanModule.JLGwithSplit.MainTransactionLoan;
import com.finwingway.digicob.loanModule.JLGwithSplit.MainClosingLoan;

public class LoanMainFragment extends Fragment implements View.OnClickListener {

    //  ImageButton btnCrtGroup, btnCrtCenter;
    Button btnCrtGroup, btnCrtCenter, btnCrtLoan, btnPendingList, btnTrnsctn, btnClosing, btnTrnsctnSplit,
            btnClosingSplit, btnHouseVisit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_loan_layout, container, false);
        btnCrtCenter = rootView.findViewById(R.id.btn_crt_center);
        btnCrtCenter.setOnClickListener(this);
        btnCrtGroup = rootView.findViewById(R.id.btn_crt_group);
        btnCrtGroup.setOnClickListener(this);
        btnCrtLoan = rootView.findViewById(R.id.btn_crt_loan);
        btnCrtLoan.setOnClickListener(this);
        btnPendingList = rootView.findViewById(R.id.btn_pending_list);
        btnPendingList.setOnClickListener(this);
        btnTrnsctn = rootView.findViewById(R.id.btn_jlg_trnsctn);
        btnTrnsctn.setOnClickListener(this);
        btnClosing = rootView.findViewById(R.id.btn_jlg_closing);
        btnClosing.setOnClickListener(this);
        btnTrnsctnSplit = rootView.findViewById(R.id.btn_jlg_trnsctn_split);
        btnTrnsctnSplit.setOnClickListener(this);
        btnClosingSplit = rootView.findViewById(R.id.btn_jlg_closing_split);
        btnClosingSplit.setOnClickListener(this);
        btnHouseVisit = rootView.findViewById(R.id.btn_jlg_house_visit);
        btnHouseVisit.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == btnCrtCenter) {
            FragmentManager fm = getFragmentManager();
            JLGCreateCenter createCenter = new JLGCreateCenter();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            ft.replace(R.id.content_frame, createCenter);
            ft.commit();
        }
        if (v == btnCrtGroup) {
            FragmentManager fm = getFragmentManager();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            JLGCreateGroup loanCreateGroup = new JLGCreateGroup();
            ft.replace(R.id.content_frame, loanCreateGroup);
            ft.commit();
        }
        if (v == btnCrtLoan) {
            FragmentManager fm = getFragmentManager();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            JLGLoanCreation JLGloanCreation = new JLGLoanCreation();
            ft.replace(R.id.content_frame, JLGloanCreation);
            ft.commit();
        }
        if (v == btnPendingList) {
            FragmentManager fm = getFragmentManager();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            JLGPendingLoanList JLGPendingList = new JLGPendingLoanList();
            ft.replace(R.id.content_frame, JLGPendingList);
            ft.commit();
        }
        if (v == btnTrnsctn) { //***********************************************************************************
            FragmentManager fm = getFragmentManager();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            MainTransactionLoan loanTransac = new MainTransactionLoan();
            ft.replace(R.id.content_frame, loanTransac);
            ft.commit();
        }
        if (v == btnClosing) { //JLG Closing ==============================================================
            FragmentManager fm = getFragmentManager();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            MainClosingLoan loanClsng = new MainClosingLoan();
            ft.replace(R.id.content_frame, loanClsng);
            ft.commit();
        }
        if (v == btnTrnsctnSplit) { //********************************************************************************
            Bundle bundle = new Bundle();
            bundle.putString("TRNS_TYPE", "SPLIT");

            FragmentManager fm = getFragmentManager();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            MainTransactionLoan loanTransac = new MainTransactionLoan();
            loanTransac.setArguments(bundle);
            ft.replace(R.id.content_frame, loanTransac);
            ft.commit();
        }
        if (v == btnClosingSplit) { //JLG Split Closing ====================================================
            Bundle bundle = new Bundle();
            bundle.putString("CLS_TYPE", "SPLIT");

            FragmentManager fm = getFragmentManager();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            MainClosingLoan loanClsngSplt = new MainClosingLoan();
            loanClsngSplt.setArguments(bundle);
            ft.replace(R.id.content_frame, loanClsngSplt);
            ft.commit();
        }
        if (v == btnHouseVisit) {
            FragmentManager fm = getFragmentManager();
            assert fm != null;
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            JLGHouseVisitCheckList frag = new JLGHouseVisitCheckList();
            ft.replace(R.id.content_frame, frag);
            ft.commit();
        }
    }


}
