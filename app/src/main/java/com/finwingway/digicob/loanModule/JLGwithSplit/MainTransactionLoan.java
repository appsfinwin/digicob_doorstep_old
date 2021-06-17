package com.finwingway.digicob.loanModule.JLGwithSplit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finwingway.digicob.R;
import com.finwingway.digicob.adapters.JLGLoanPageAdapter;

public class MainTransactionLoan extends Fragment {
    View rootView;
    public static ViewPager viewPagerTrans;
    JLGLoanPageAdapter loanPageAdapter;
    TabLayout tabLayout;
    TextView tvHead;
    String StrTrnsType = "";
    int tabCount = 3;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_main_layout_transac, container, false);
        setRetainInstance(true);
        viewPagerTrans = rootView.findViewById(R.id.loan_viewpager_tran);
        tabLayout = rootView.findViewById(R.id.tablayout_loan_tran);
        tvHead = rootView.findViewById(R.id.tvhead);

        try {
//            bundle.putString("TRNS_TYPE", "SPLIT");
            Bundle extras = getArguments();
            assert extras != null;
            StrTrnsType = extras.getString("TRNS_TYPE", "");
            Log.e("StrTrnsType:TRNS_TYPE ", StrTrnsType);
        } catch (Exception ignored) {
        }

        if (StrTrnsType.equals("SPLIT")) {
            tvHead.setText("JLG SPLIT TRANSACTION");
        } else {
            tvHead.setText("JLG TRANSACTION");
        }

        loanPageAdapter = new JLGLoanPageAdapter(getChildFragmentManager());
        loanPageAdapter.addFragment(new TransactionLoanAdapterOne(StrTrnsType), "General Details");
        loanPageAdapter.addFragment(new TransactionLoanAdapterTwoSplit(StrTrnsType), "Remittance Details");
        loanPageAdapter.addFragment(new TransactionLoanAdapterThree(StrTrnsType), "Other Details");
        tabCount = 3;

        viewPagerTrans.setAdapter(loanPageAdapter);
        viewPagerTrans.setOffscreenPageLimit(tabCount);
        tabLayout.setupWithViewPager(viewPagerTrans);

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
