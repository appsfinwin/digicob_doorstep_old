package com.finwingway.digicob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class Transactions extends Fragment implements View.OnClickListener {

    ImageButton cashWithdrawal, transfer, neft, dailyDeposit, cashDeposit, loan, loanClosing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_transaction_layout, container, false);
        dailyDeposit = (ImageButton) rootView.findViewById(R.id.dailyDepositbtn);
        dailyDeposit.setOnClickListener(this);
        cashDeposit = (ImageButton) rootView.findViewById(R.id.cashDepositbtn);
        cashDeposit.setOnClickListener(this);
        cashWithdrawal = (ImageButton) rootView.findViewById(R.id.cashWithdrawelbtn);
        cashWithdrawal.setOnClickListener(this);
        transfer = (ImageButton) rootView.findViewById(R.id.transferBtn);
        transfer.setOnClickListener(this);
        neft = (ImageButton) rootView.findViewById(R.id.neft_transferBtn);
        neft.setOnClickListener(this);
        loan = (ImageButton) rootView.findViewById(R.id.loan_btn);
        loan.setOnClickListener(this);
        loanClosing = (ImageButton) rootView.findViewById(R.id.loan_clsng_btn);
        loanClosing.setOnClickListener(this);


        return rootView;
    }

    public void onClick(View view) {
        if (view == dailyDeposit) {

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            Transactions_Daily_Deposit transactions_cashDepositCustomer = new Transactions_Daily_Deposit();
            ft.replace(R.id.content_frame, transactions_cashDepositCustomer);
            ft.commit();

        }
        if (view == cashDeposit) {
            FragmentManager fm = getFragmentManager();
            Transactions_Cash_Deposit transactions_neft_transfer = new Transactions_Cash_Deposit();
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            ft.replace(R.id.content_frame, transactions_neft_transfer);
            ft.commit();
        }
        if (view == cashWithdrawal) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            Transactions_CashWithdrawal transactions_cashWithdrawal = new Transactions_CashWithdrawal();
            ft.replace(R.id.content_frame, transactions_cashWithdrawal);
            ft.commit();
        }
        if (view == transfer) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            Transactions_Transfer transactions_deposit = new Transactions_Transfer();
            ft.replace(R.id.content_frame, transactions_deposit);
            ft.commit();
        }
        if (view == neft) {
            FragmentManager fm = getFragmentManager();
            Transactions_Neft_Transfer transactions_neft_transfer = new Transactions_Neft_Transfer();
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            ft.replace(R.id.content_frame, transactions_neft_transfer);
            ft.commit();
        }
        if (view == loan) {
//            Loan loan = new Loan();
            FragmentManager fm = getFragmentManager();
            LoanFragment loan = new LoanFragment();
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            ft.replace(R.id.content_frame, loan);
            ft.commit();
        }
        if (view == loanClosing) {
            FragmentManager fm = getFragmentManager();
            LoanFragmentClosing loan = new LoanFragmentClosing();
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            ft.replace(R.id.content_frame, loan);
            ft.commit();
        }

    }

}
