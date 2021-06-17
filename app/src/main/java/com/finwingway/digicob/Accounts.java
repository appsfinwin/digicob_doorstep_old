package com.finwingway.digicob;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class Accounts extends Fragment implements View.OnClickListener {
    ImageButton newAccount, kycupdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_accounts_layout, container, false);
        newAccount = (ImageButton) rootView.findViewById(R.id.newAccountBtn);
        newAccount.setOnClickListener(this);
        kycupdate = (ImageButton) rootView.findViewById(R.id.kycUpdateBtn);
        kycupdate.setOnClickListener(this);
        ImageButton QuickOpen = (ImageButton) rootView.findViewById(R.id.quickOpeningBtn);
        QuickOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
                Accounts_QuickOpening llf = new Accounts_QuickOpening();
                ft.replace(R.id.content_frame, llf);
                ft.commit();
            }
        });
        return rootView;
    }

    public void onClick(View view) {
        if (view == newAccount) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            Accounts_Sub llf = new Accounts_Sub();
            ft.replace(R.id.content_frame, llf);
            ft.commit();
        }
        if (view == kycupdate) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
            Accounts_KYCUpdate accounts_kycUpdate = new Accounts_KYCUpdate();
            ft.replace(R.id.content_frame, accounts_kycUpdate);
            ft.commit();

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
