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

/**
 * Created by ADSS on 6/14/2017.
 */

public class Agent_Management extends Fragment {
    private ImageButton password_btn,biometric_btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.agent_management,container,false);
        password_btn=(ImageButton)rootView.findViewById(R.id.agent_change_pass_btn);
        biometric_btn=(ImageButton)rootView.findViewById(R.id.agent_change_bio_btn);

        password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction().addToBackStack(null);
                Agent_Change_Password agent_change_password=new Agent_Change_Password();
                ft.replace(R.id.content_frame,agent_change_password);
                ft.commit();
            }
        });
        biometric_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Agent_Change_Biometric.fromchangeBio=true;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
                Agent_Change_Biometric agent_change_biometric=new Agent_Change_Biometric();
                ft.replace(R.id.content_frame,agent_change_biometric);
                ft.commit();
            }
        });

        return rootView;
    }
}
