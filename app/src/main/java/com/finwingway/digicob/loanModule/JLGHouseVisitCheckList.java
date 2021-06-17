package com.finwingway.digicob.loanModule;

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

import com.finwingway.digicob.R;
import com.finwingway.digicob.adapters.JLGLoanPageAdapter;

public class JLGHouseVisitCheckList extends Fragment {

    View rootView;
    public static ViewPager viewPagerHouseVisit;
    TabLayout tabLayout;
    JLGLoanPageAdapter loanPageAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_layout_house_visit, container, false);
        setRetainInstance(true);
        viewPagerHouseVisit = rootView.findViewById(R.id.loan_viewpager_hvisit);
        tabLayout = rootView.findViewById(R.id.tablayout_loan_hvisit);

        loanPageAdapter = new JLGLoanPageAdapter(getChildFragmentManager());
        loanPageAdapter.addFragment(new JLGHouseVisitAdapterOne(), "General Details");
        loanPageAdapter.addFragment(new JLGHouseVisitAdapterTwo(), "Remittance Details");
//        loanPageAdapter.addFragment(new TransactionLoanAdapterTwoSplit(StrTrnsType), "Remittance Details");
//        loanPageAdapter.addFragment(new TransactionLoanAdapterThree(StrTrnsType), "Other Details");

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        viewPagerHouseVisit.setAdapter(loanPageAdapter);
        viewPagerHouseVisit.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPagerHouseVisit);
        return rootView;
    }

}
