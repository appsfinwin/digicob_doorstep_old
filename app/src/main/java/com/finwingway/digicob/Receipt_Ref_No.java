package com.finwingway.digicob;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.finwingway.digicob.sil.BluetoothChat;
import com.finwingway.digicob.sil.BluetoothChatService;
import com.finwingway.digicob.sil.DataToPrint;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static com.finwingway.digicob.sil.BluetoothChat.mChatService;

/**
 * Created by ADSS on 8/14/2017.
 */

public class Receipt_Ref_No extends Fragment {
    private TextView name_textview,mobile_textview,ac_type_textview,ref_id_textview;
    private ImageButton sms,print;
    public static com.leopard.api.Printer ptr;
    SweetAlertDialog dialog;
    private String name,mobile,ac_type,ref_id;
    @Nullable
    @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.ref_id_layout,container,false);
        dialog=new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
        name_textview=(TextView) rootView.findViewById(R.id.receipt_ref_name_textview);
        mobile_textview=(TextView) rootView.findViewById(R.id.receipt_ref_mobile_name_textview);
        ac_type_textview=(TextView) rootView.findViewById(R.id.receipt_ref_ac_type_name_textview);
        ref_id_textview=(TextView) rootView.findViewById(R.id.receipt_ref_id_textview);
        sms=(ImageButton)rootView.findViewById(R.id.receipt_ref_sms_btn);
        print=(ImageButton)rootView.findViewById(R.id.receipt_ref_print_btn);

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mChatService != null) {
                    if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
                        new Print().execute();
                    }
                } else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("BT Device Not Connected")
                            .setContentText("please connect the device")
                            .setCancelText("NO")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(getActivity(), BluetoothChat.class));
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                }

//                if(connectionStatus){
//                    try {
//                        OutputStream outSt = BluetoothComm.mosOut;
//                        InputStream inSt = BluetoothComm.misIn;
//                        ptr = new com.leopard.api.Printer(Act_Main.setupInstance, outSt, inSt);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    new Print().execute();
//                }else {
//                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("BT Device Not Connected")
//                            .setContentText("are you sure you want to continue?")
//                            .setCancelText("NO")
//                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismiss();
//                                }
//                            })
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    try {
//                                        OutputStream outSt = BluetoothComm.mosOut;
//                                        InputStream inSt = BluetoothComm.misIn;
//                                        ptr = new com.leopard.api.Printer(Act_Main.setupInstance, outSt, inSt);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    new Print().execute();
//                                    sweetAlertDialog.dismiss();
//                                }
//                            }).show();
//                }
            }
        });

        name=getArguments().getString("name");
        mobile=getArguments().getString("phone");
        ac_type=getArguments().getString("ac_type");
        ref_id=getArguments().getString("ref_id");

        name_textview.setText(name);
        mobile_textview.setText(mobile);
        ac_type_textview.setText(ac_type);
        ref_id_textview.setText(ref_id);

        return rootView;
    }


    public class Print extends AsyncTask<Integer, Integer, Integer> {
        private int iRetVal;
        private static final int DEVICE_NOTCONNECTED = -100;
        /* displays the progress dialog until background task is completed*/
        @Override
        protected void onPreExecute() {
            dialog.setTitleText("Processing");
            dialog.setContentText("Please wait..");
            dialog.show();
            super.onPreExecute();
        }
        /* Task of EnterTextAsyc performing in the background*/
        @Override
        protected Integer doInBackground(Integer... params) {
            try {
//                ptr.iFlushBuf();


                String BILL = "";
                BILL = "\n             Bank Name               \n\n";
                BILL = BILL + "Name             : " + name + "\n";
                BILL = BILL + "Mobile Number   : " + mobile+ "\n";
                BILL = BILL + "Account Type             : " + ac_type +"\n";
                BILL = BILL + "Reference ID    : " + ref_id+"\n";
                BILL = BILL
                        + "------------------------------";
                BILL = BILL + "Thank you for Banking with us...\n";
                BILL = BILL + "      Keep Smiling :)\n";
                BILL = BILL
                        + "------------------------------\n\n\n";


                //========================================================================================================================
                DataToPrint.setPrintData(BILL);
                //=======================================================================================================================

//                String empty = BILL ;//+ "\n" + "\n" + "\n" + "\n" + "\n"+ "\n";

//                ptr.iPrinterAddData(Printer.PR_FONTSMALLNORMAL, empty);
//                ptr.iPrinterAddData(com.leopard.api.Printer.PR_FONTLARGENORMAL, " \n \n \n \n \n \n");
//                iRetVal = ptr.iStartPrinting(1);
            } catch (NullPointerException e) {
                iRetVal = DEVICE_NOTCONNECTED;
                return iRetVal;
            }
            return iRetVal;
        }
        /* This displays the status messages of EnterTextAsyc in the dialog box */
        @Override
        protected void onPostExecute(Integer result) {
            dialog.dismiss();
            BluetoothChat.printData();
            if (iRetVal == DEVICE_NOTCONNECTED) {
            } else if (iRetVal == com.leopard.api.Printer.PR_SUCCESS) {
            } else if (iRetVal == com.leopard.api.Printer.PR_PLATEN_OPEN) {
            } else if (iRetVal == com.leopard.api.Printer.PR_PAPER_OUT) {
            } else if (iRetVal == com.leopard.api.Printer.PR_IMPROPER_VOLTAGE) {
            } else if (iRetVal == com.leopard.api.Printer.PR_FAIL) {
            } else if (iRetVal == com.leopard.api.Printer.PR_PARAM_ERROR) {
            } else if (iRetVal == com.leopard.api.Printer.PR_NO_RESPONSE) {
            } else if (iRetVal== com.leopard.api.Printer.PR_DEMO_VERSION) {
            } else if (iRetVal== com.leopard.api.Printer.PR_INVALID_DEVICE_ID) {
            } else if (iRetVal== com.leopard.api.Printer.PR_ILLEGAL_LIBRARY) {
            }
            super.onPostExecute(result);
        }
    }

}
