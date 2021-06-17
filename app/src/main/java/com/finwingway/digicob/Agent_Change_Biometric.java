package com.finwingway.digicob;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.finwingway.digicob.leopard.Act_Main;
import com.finwingway.digicob.leopard.bluetooth.BluetoothComm;
import com.leopard.api.FPS;
import com.leopard.api.FpsConfig;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;
import static com.finwingway.digicob.MainActivity.connectionStatus;
import static com.finwingway.digicob.leopard.Act_Main.mBT;
import static com.finwingway.digicob.leopard.Act_Main.mGP;
import static com.finwingway.digicob.login.authorizedForbiometric;
import static com.finwingway.digicob.login.connectionStatusLogin;
import static com.finwingway.digicob.login.ip_global;
import static com.finwingway.digicob.login.serviceIntent;


public class Agent_Change_Biometric extends Fragment {

    private Button biometric_btn;
    ImageButton change_biometric_next_btn,bio_exitbtn,bio_tryagainbtn;
    private String pass;
    private SweetAlertDialog dialogWarning,dialog;
    public static final int DEVICE_NOTCONNECTED = -100;
    private int iRetVal;
    OutputStream outputStream;
    InputStream inputstream;
    FPS fps;
    FpsConfig fpsconfig = new FpsConfig();
    private byte[] brecentminituaedata = {};
    SweetAlertDialog pDialog;
    JSONParser jsonParser=new JSONParser();
    public static boolean fromchangeBio=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_agent__change__biometric,container,false);
        biometric_btn=(Button)rootView.findViewById(R.id.change_bio_btn);
        bio_exitbtn=(ImageButton) rootView.findViewById(R.id.change_bio_exit_btn);
        bio_tryagainbtn=(ImageButton) rootView.findViewById(R.id.change_bio_tryagain_btn);
        change_biometric_next_btn=(ImageButton)rootView.findViewById(R.id.change_bio_next_btn);
        dialogWarning=new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE);
        dialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        pDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        change_biometric_next_btn.setVisibility(GONE);
        change_biometric_next_btn.setEnabled(false);

        bio_tryagainbtn.setVisibility(GONE);
        bio_tryagainbtn.setEnabled(false);
        bio_tryagainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CaptureFingerAsyn().execute();
            }
        });

        bio_exitbtn.setVisibility(GONE);
        bio_exitbtn.setEnabled(false);
        bio_exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGP.closeConn();
                mBT.disable();
                getActivity().stopService(serviceIntent);
                System.exit(0);
            }
        });


        try {
            outputStream = BluetoothComm.mosOut;
            inputstream = BluetoothComm.misIn;
            fps = new FPS(Act_Main.setupInstance, outputStream, inputstream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        change_biometric_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().getSupportFragmentManager().beginTransaction().remove(Agent_Change_Biometric.this).commit();
            }
        });

        if(connectionStatusLogin && authorizedForbiometric){
            fromchangeBio=false;
            biometric_btn.setEnabled(false);
            new CaptureFingerAsyn().execute();
            authorizedForbiometric=false;
        }

        biometric_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionStatus){
                    final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    LayoutInflater layoutInflater=getLayoutInflater(null);
                    final View view=layoutInflater.inflate(R.layout.alert_confirm_pass,null);
                    final EditText bio_username_editText=(EditText)view.findViewById(R.id.bio_username_edittext);
                    final EditText bio_password_editText=(EditText)view.findViewById(R.id.bio_password_edittext);
                    builder.setView(view);
                    builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new passwordCheckAsync().execute(bio_username_editText.getText().toString(),
                                    bio_password_editText.getText().toString());

                        }
                    });
                    builder.setNegativeButton("CANCEL",null);
                    builder.show();
                }else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("BT Device Not Connected")
                            .setContentText("please connect the device")
                            .setCancelText("NO")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(getContext(), Act_Main.class));
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                }
            }
        });

        return rootView;
    }


    public class CaptureFingerAsyn extends AsyncTask<Integer, Integer, Integer> {
        /* displays the progress dialog until background task is completed */
        @Override
        protected void onPreExecute() {
            dialog.setTitleText("Processing")
                    .setContentText("Place your finger on FP Scanner..").show();
            super.onPreExecute();
        }
        /* Task of CaptureFinger performing in the background */
        @Override
        protected Integer doInBackground(Integer... params) {

            try {
                brecentminituaedata = new byte[3500];
                fpsconfig = new FpsConfig(0, (byte) 0x0F);
                brecentminituaedata = fps.bFpsCaptureMinutiae(fpsconfig);
                iRetVal = fps.iGetReturnCode();
            } catch (Exception e) {
                e.printStackTrace();
                iRetVal = DEVICE_NOTCONNECTED;
                Log.e("iretVal in Exception : ",String.valueOf(iRetVal));
                return iRetVal;
            }
            try {
                createFile(brecentminituaedata);
            } catch (NullPointerException e) {
                return iRetVal;
            }
            return iRetVal;
        }

        /*
         * This function sends message to handler to display the status messages
         * of Diagnose in the dialog box
         */
        @Override
        protected void onPostExecute(Integer result) {
            dialog.dismiss();
            if (iRetVal == DEVICE_NOTCONNECTED) {
                deletefile();
                dialogWarning.setTitleText("Error")
                        .setContentText("Device not connected").show();
                bio_exitbtn.setEnabled(true);
                bio_exitbtn.setVisibility(View.VISIBLE);
                bio_tryagainbtn.setEnabled(true);
                bio_tryagainbtn.setVisibility(View.VISIBLE);

            } else if (iRetVal == FPS.SUCCESS) {
                change_biometric_next_btn.setVisibility(View.VISIBLE);
                biometric_btn.setText("SUCCESSFULLY UPDATED BIOMETRIC");
                biometric_btn.setTextColor(Color.GREEN);
                if(fromchangeBio){
                    change_biometric_next_btn.setEnabled(false);
                }else{
                    change_biometric_next_btn.setEnabled(true);
                }

                bio_exitbtn.setEnabled(false);
                bio_exitbtn.setVisibility(View.GONE);
            }else if (iRetVal == FPS.NO_FINGER) {
                deletefile();
                dialogWarning.setTitleText("Error")
                        .setContentText("No finger identified").show();
                bio_exitbtn.setEnabled(true);
                bio_exitbtn.setVisibility(View.VISIBLE);
                bio_tryagainbtn.setEnabled(true);
                bio_tryagainbtn.setVisibility(View.VISIBLE);
            } else if (iRetVal == FPS.FPS_INACTIVE_PERIPHERAL) {
                deletefile();
                dialogWarning.setTitleText("Error")
                        .setContentText("Peripheral is inactive").show();
                bio_exitbtn.setEnabled(true);
                bio_exitbtn.setVisibility(View.VISIBLE);
                bio_tryagainbtn.setEnabled(true);
                bio_tryagainbtn.setVisibility(View.VISIBLE);
            } else if (iRetVal == FPS.TIME_OUT) {
                deletefile();
                dialogWarning.setTitleText("Error")
                        .setContentText("Capture finger time out").show();
                bio_exitbtn.setEnabled(true);
                bio_exitbtn.setVisibility(View.VISIBLE);
                bio_tryagainbtn.setEnabled(true);
                bio_tryagainbtn.setVisibility(View.VISIBLE);
            } else if (iRetVal == FPS.FAILURE) {
                deletefile();
                dialogWarning.setTitleText("Error")
                        .setContentText("Capture finger failed").show();
                bio_exitbtn.setEnabled(true);
                bio_exitbtn.setVisibility(View.VISIBLE);
                bio_tryagainbtn.setEnabled(true);
                bio_tryagainbtn.setVisibility(View.VISIBLE);
            } else if (iRetVal == FPS.PARAMETER_ERROR) {
                deletefile();
                dialogWarning.setTitleText("Error")
                        .setContentText("Parameter error").show();
                bio_exitbtn.setEnabled(true);
                bio_exitbtn.setVisibility(View.VISIBLE);
                bio_tryagainbtn.setEnabled(true);
                bio_tryagainbtn.setVisibility(View.VISIBLE);
            } else if (iRetVal == FPS.FPS_INVALID_DEVICE_ID) {
                deletefile();
                dialogWarning.setTitleText("Error")
                        .setContentText("Connected  device is not license authenticated.").show();
                bio_exitbtn.setEnabled(true);
                bio_exitbtn.setVisibility(View.VISIBLE);
                bio_tryagainbtn.setEnabled(true);
                bio_tryagainbtn.setVisibility(View.VISIBLE);
            } else if (iRetVal == FPS.FPS_ILLEGAL_LIBRARY) {
                deletefile();
                dialogWarning.setTitleText("Error")
                        .setContentText("Library not valid").show();
                bio_exitbtn.setEnabled(true);
                bio_exitbtn.setVisibility(View.VISIBLE);
                bio_tryagainbtn.setEnabled(true);
                bio_tryagainbtn.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(result);
        }
    }

    public void createFile(byte[] newbyte){
        try{
            File file = new File(getActivity().getFilesDir(),"94!!4&$75!@#$$06%@1=4");
            file.createNewFile();
//write the bytes in file
            if(file.exists())
            {
                OutputStream fo = new FileOutputStream(file);
                fo.write(newbyte);
                fo.close();
                System.out.println("file created: "+file);
                Log.e("FILE","CREATED"+file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletefile(){
        File file=new File(getActivity().getFilesDir()+File.separator+"94!!4&$75!@#$$06%@1=4");
        file.delete();
        Log.e("filepath=",file.toString()+" Deleted");
    }
    @Override
    public void onResume() {
      /*  try {
            outputStream = BluetoothComm.mosOut;
            inputstream = BluetoothComm.misIn;
            fps = new FPS(Act_Main.setupInstance, outputStream, inputstream);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        super.onResume();
    }

    private class passwordCheckAsync extends AsyncTask<String, String, String> {
        private String error="YES";
        private String msg="Something went Wrong!";
        private  String api_url = ip_global+"/userLogin";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            String name = args[0];
            String password = args[1];
            try {
                // Building Parameters
                HashMap<String,String> h1=new HashMap<>();
                h1.put("username", name);
                h1.put("password", password);
                JSONObject json= jsonParser.makeHttpRequest(api_url,"POST", h1);
                Log.e("Create Response 1", json.toString());
                String JsonData=json.toString();
                JSONObject reader=new JSONObject(JsonData);
                JSONObject jobj=reader.getJSONObject("user");
                if(jobj.has("data")){
                    error="NO";
                }
                if(jobj.has("error")){
                    msg=jobj.getString("error");
                    error="YES";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return error;

            }
            return error;
        }

        protected void onPostExecute(String val) {
            pDialog.dismiss();
            if(val.equals("YES")){
                new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Invalid Password")
                        .setContentText(msg)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
            if(val.equals("NO")){
                new CaptureFingerAsyn().execute();
                change_biometric_next_btn.setImageResource(R.drawable.success_btn_bg);
                change_biometric_next_btn.setEnabled(false);
            }
        }
    }
}
