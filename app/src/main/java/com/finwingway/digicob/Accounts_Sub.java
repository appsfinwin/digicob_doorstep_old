package com.finwingway.digicob;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.finwingway.digicob.adapters.TabsAdapter;

/**
 * Created by Anvin on 12/20/2016.
 */

public class Accounts_Sub extends Fragment{
    public static ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_accounts_layout_sub, container, false);
        setRetainInstance(true);
        viewPager = (ViewPager) rootView.findViewById(R.id.accounts_viewpager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);

        TabsAdapter tabsAdapter = new TabsAdapter(getChildFragmentManager());
        tabsAdapter.addFragment(new Accounts_Tab_Fragment_Customer(),"Customer");
        tabsAdapter.addFragment(new Accounts_Tab_Fragment_Introduction(), "Introduction");
        tabsAdapter.addFragment(new Accounts_Tab_Fragment_KYC(22), "KYC");
        tabsAdapter.addFragment(new Accounts_Tab_Fragment_AccountOpening(), "Open Account");

        viewPager.setAdapter(tabsAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);

        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
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

