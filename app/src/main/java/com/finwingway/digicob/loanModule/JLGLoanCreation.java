package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.adapters.PageAdapter;
import com.finwingway.digicob.login;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class JLGLoanCreation extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static ViewPager viewPager;
    SweetAlertDialog proDialog, errorDialog;
    JSONParser jsonParser;
    String responseMsg, Message;
    public static String ip = login.ip_global;

    PageAdapter pageAdapter;
    TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.loan_main_layout, container, false);
        jsonParser = new JSONParser();
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);

        setRetainInstance(true);
        viewPager = rootView.findViewById(R.id.loan_viewpager);
        tabLayout = rootView.findViewById(R.id.tablayout_loan);

        pageAdapter = new PageAdapter(getChildFragmentManager());
        pageAdapter.addFragment(new JLGLoanCreationAdapterOne(), "General Details");
        pageAdapter.addFragment(new JLGLoanCreationAdapterTwo(), "Select Group");
        pageAdapter.addFragment(new JLGLoanCreationAdapterThree(), "Loan Details");

        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        return rootView;
    }



}
