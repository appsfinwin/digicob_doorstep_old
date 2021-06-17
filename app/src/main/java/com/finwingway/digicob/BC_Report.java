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

public class BC_Report extends Fragment implements View.OnClickListener{
    ImageButton dailybtn,datebtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_bc_report, container, false);
        dailybtn=(ImageButton)rootView.findViewById(R.id.bcreport_daily_btn);
        datebtn=(ImageButton)rootView.findViewById(R.id.bcreport_date2date_btn);
        dailybtn.setOnClickListener(this);
        datebtn.setOnClickListener(this);
        return rootView;
    }
    public void onClick(View view){
        if(view == dailybtn){
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            BC_Report_Daily_Report bc_report_daily_report=new BC_Report_Daily_Report();
            ft.replace(R.id.content_frame,bc_report_daily_report).addToBackStack(null);
            ft.commit();
        }
        if(view == datebtn){
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            BC_Report_Date_Date_Report bc_report_date_date_report=new BC_Report_Date_Date_Report();
            ft.replace(R.id.content_frame,bc_report_date_date_report).addToBackStack(null);;
            ft.commit();
        }
    }
}
