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
import com.finwingway.digicob.adapters.SplitClosingPageAdapter;

public class MainClosingLoan extends Fragment {

    View rootView;
    String StrClsgType = "";
    static ViewPager viewPagerClsng;
    TabLayout tabLayout;
    TextView tvHead;
    SplitClosingPageAdapter clsngPageAdapter;
    int tabCount = 3;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_main_jlgclsng, container, false);
        setRetainInstance(true);
        viewPagerClsng = rootView.findViewById(R.id.loan_viewpager_jlgclng);
        tabLayout = rootView.findViewById(R.id.tablayout_loan_jlgclng);
        tvHead = rootView.findViewById(R.id.tvhead);

        try {
            Bundle extras = getArguments();
            assert extras != null;
            StrClsgType = extras.getString("CLS_TYPE", "");
            Log.e("StrTrnsType:CLS_TYPE ", StrClsgType);
        } catch (Exception ignored) {
        }

        if (StrClsgType.equals("SPLIT")) {
            tvHead.setText("JLG SPLIT CLOSING");
        } else {
            tvHead.setText("JLG CLOSING");
        }

        clsngPageAdapter = new SplitClosingPageAdapter(getChildFragmentManager());
        clsngPageAdapter.addFragment(new ClosingAdapterOne(), "General Details");
        clsngPageAdapter.addFragment(new ClosingAdapterTwoSplit(StrClsgType), "Remittance Details");
        clsngPageAdapter.addFragment(new ClosingAdapterThree(StrClsgType), "Other Details");
        tabCount = 3;

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        viewPagerClsng.setAdapter(clsngPageAdapter);
        viewPagerClsng.setOffscreenPageLimit(tabCount);
        tabLayout.setupWithViewPager(viewPagerClsng);
        return rootView;
    }


}
