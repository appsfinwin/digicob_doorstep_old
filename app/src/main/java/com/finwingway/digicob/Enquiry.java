package com.finwingway.digicob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.finwingway.digicob.inauguration.AccDetails;

public class Enquiry extends Fragment implements View.OnClickListener {
    ImageButton accountStatus, miniStatement, balanceEnquiry;
    Button inauAccStatemnt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_enquiry_layout, container, false);
        accountStatus = (ImageButton) rootView.findViewById(R.id.account_status_btn);
        accountStatus.setOnClickListener(this);
        miniStatement = (ImageButton) rootView.findViewById(R.id.mini_statement_btn);
        miniStatement.setOnClickListener(this);
        balanceEnquiry = (ImageButton) rootView.findViewById(R.id.balance_enquiry_btn);
        balanceEnquiry.setOnClickListener(this);
        inauAccStatemnt = (Button) rootView.findViewById(R.id.inau_btn_acc_statement);
        inauAccStatemnt.setOnClickListener(this);

        return rootView;
    }

    public void onClick(View view) {
        if (view == accountStatus) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Enquiry_Account_Status enquiry_account_status = new Enquiry_Account_Status();
            ft.replace(R.id.content_frame, enquiry_account_status);
            ft.addToBackStack(null);
            ft.commit();

        }
        if (view == miniStatement) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Enquiry_Mini_Statement enquiry_balance_enquiry = new Enquiry_Mini_Statement();
            ft.replace(R.id.content_frame, enquiry_balance_enquiry);
            ft.addToBackStack(null);
            ft.commit();
        }
        if (view == balanceEnquiry) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Enquiry_Balance_Enquiry enquiry_balance_enquiry = new Enquiry_Balance_Enquiry();
            ft.replace(R.id.content_frame, enquiry_balance_enquiry);
            ft.addToBackStack(null);
            ft.commit();

        }
        if (view == inauAccStatemnt) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            AccDetails AccDtls = new AccDetails();
            ft.replace(R.id.content_frame, AccDtls);
            ft.addToBackStack(null);
            ft.commit();

        }

    }

}
