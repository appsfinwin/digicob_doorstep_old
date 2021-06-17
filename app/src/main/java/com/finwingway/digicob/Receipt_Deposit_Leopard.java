package com.finwingway.digicob;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.finwingway.digicob.leopard.Act_Main;
import com.finwingway.digicob.leopard.bluetooth.BluetoothComm;

import java.io.InputStream;
import java.io.OutputStream;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.softland.palmtecandro.palmtecandro;

/**
 * Created by AnVin on 1/7/2017.
 */

public class Receipt_Deposit_Leopard extends Activity {
    TextView accountNO, Name, Mobile, oldBal, DepositAmount, currentBal, depositDate, depositTime, DepositAmount_Caption, transaction_id;
    String account_number, name, mobile, old_balance, deposit_amount, current_balance, deposit_date, tran_id, print_string = "Amount           : ";
    SweetAlertDialog dialog;
    protected static final String TAG = "TAG";
    public static com.leopard.api.Printer ptr;
    private int iRetVal = 0;
    public static final int DEVICE_NOTCONNECTED = -100;
    static String BILL = "";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.receipt_layout_deposit);
        Intent intent = getIntent();
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);

        try {
            OutputStream outSt = BluetoothComm.mosOut;
            InputStream inSt = BluetoothComm.misIn;
            ptr = new com.leopard.api.Printer(Act_Main.setupInstance, outSt, inSt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DepositAmount_Caption = (TextView) findViewById(R.id.deposit_amount_caption);

        account_number = intent.getStringExtra("account_number");
        name = intent.getStringExtra("introName");
        mobile = intent.getStringExtra("mobile");
        old_balance = intent.getStringExtra("old_balance");
        deposit_amount = intent.getStringExtra("deposit_amount");
        current_balance = intent.getStringExtra("current_balance");
        deposit_date = intent.getStringExtra("deposit_date");
        tran_id = intent.getStringExtra("tran_id");

        try {
            String withdrawal = intent.getStringExtra("withdrawal");
            if (withdrawal.equals("WITH")) {
                DepositAmount_Caption.setText("Withdrawal Amount    ");
                print_string = "Withdrawal Amount: ";
                deposit_date = intent.getStringExtra("withdrawalDate");
                deposit_amount = intent.getStringExtra("withdrawalAmount");
            } else {
                print_string = "Deposited Amount : ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        accountNO = (TextView) findViewById(R.id.accNOReceipt);
        Name = (TextView) findViewById(R.id.nameReceipt);
        Mobile = (TextView) findViewById(R.id.MobileReceipttxt);
        oldBal = (TextView) findViewById(R.id.oldBalanceReceipt);
        DepositAmount = (TextView) findViewById(R.id.DepositedAmountReceipt);
        currentBal = (TextView) findViewById(R.id.CurrentBalanceReceipt);
        depositDate = (TextView) findViewById(R.id.date_receipt);
        depositTime = (TextView) findViewById(R.id.timeReceipt);
        transaction_id = (TextView) findViewById(R.id.receipt_deposit_transaction_no);

        accountNO.setText(account_number);
        Name.setText(name);
        Mobile.setText(mobile);
        oldBal.setText(old_balance);
        DepositAmount.setText(deposit_amount);
        currentBal.setText(current_balance);
        depositDate.setText(deposit_date);
        transaction_id.setText(tran_id);

//        ImageButton blPrinter = findViewById(R.id.bluetooth_btn_print);
//        blPrinter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                if (mChatService != null) {
////                    if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
////                        Toast.makeText(getApplicationContext(), "Printer is already connected.", Toast.LENGTH_LONG).show();
////                    }
////                } else {
//                Intent intent = new Intent(getApplicationContext(), BluetoothChat.class);
//                startActivity(intent);
//                }
//            }
//        });

        ImageButton print = (ImageButton) findViewById(R.id.printReceiptBtn);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Receipt_Deposit_Leopard.this, "Print will not work in this device.!!", Toast.LENGTH_SHORT).show();
//                new EnterTextTask().execute();

//===========================================================================================================
////                if (connectionStatus) {
////                    new EnterTextTask().execute();
////                } else {
////                    new SweetAlertDialog(Receipt_Deposit_Leopard.this, SweetAlertDialog.WARNING_TYPE)
////                            .setTitleText("BT Device Not Connected")
////                            .setContentText("please connect the device")
////                            .setCancelText("NO")
////                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
////                                @Override
////                                public void onClick(SweetAlertDialog sweetAlertDialog) {
////                                    startActivity(new Intent(Receipt_Deposit_Leopard.this, Act_Main.class));
////                                    sweetAlertDialog.dismiss();
////                                }
////                            }).show();
////                }
//                try {
//                    if (mChatService != null) {
//                        if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
//                            new EnterTextTask().execute();
//                        }
//                    } else {
//                        new SweetAlertDialog(Receipt_Deposit_Leopard.this, SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText("BT Device Not Connected")
//                                .setContentText("please connect the device")
//                                .setCancelText("NO")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        startActivity(new Intent(Receipt_Deposit_Leopard.this, BluetoothChat.class));
//                                        sweetAlertDialog.dismiss();
//                                    }
//                                }).show();
//                    }
//
//                } catch (Exception e) {
//                }
            }
        });

//===========================================================================================================

//        ImageButton sms = (ImageButton) findViewById(R.id.smsReceiptbtn);
//        sms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    public class EnterTextTask extends AsyncTask<Integer, Integer, Integer> {
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
                BILL = "\n        THIRU-KOCHI URBAN NIDHI LIMITED          \n\n";
//              BILL = "\n             Bank Name               \n\n";
                BILL = BILL + "Date             : " + deposit_date + "\n";
                BILL = BILL + "Tran ID          : " + tran_id + "\n";
                BILL = BILL + "Account Number   : " + account_number + "\n";
                BILL = BILL + "Name             : " + name + "\n";
                BILL = BILL + "Mobile Number    : " + mobile + "\n";
                BILL = BILL
                        + "------------------------------";
                BILL = BILL + "\n\n";
                BILL = BILL + "Opening Balance  : " + old_balance + "\n";
                BILL = BILL + print_string + deposit_amount + "\n";
                BILL = BILL + "Current Balance  : " + current_balance + "\n\n";
                BILL = BILL + "Thank you for Banking with us...\n";
                BILL = BILL + "------------------------------\n\n\n";

                // ========================================================================================================================
//                DataToPrint.setPrintData(BILL);
                //=======================================================================================================================
//                String empty = BILL;//+ "\n" + "\n" + "\n" + "\n" + "\n"+ "\n";

//                ptr.iPrinterAddData(Printer.PR_FONTSMALLNORMAL, empty); //PR_FONTSMALLNORMAL
//                ptr.iPrinterAddData(com.leopard.api.Printer.PR_FONTLARGENORMAL, " \n \n \n \n \n \n");
//                iRetVal = ptr.iStartPrinting(1);
            } catch (NullPointerException e) {
//                iRetVal = DEVICE_NOTCONNECTED;
                return iRetVal;
            }
            return iRetVal;
        }

        /* This displays the status messages of EnterTextAsyc in the dialog box */
        @Override
        protected void onPostExecute(Integer result) {
            Print(BILL);
            dialog.dismiss();

//            BluetoothChat.printData();
//            if (iRetVal == DEVICE_NOTCONNECTED) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_SUCCESS) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_PLATEN_OPEN) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_PAPER_OUT) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_IMPROPER_VOLTAGE) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_FAIL) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_PARAM_ERROR) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_NO_RESPONSE) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_DEMO_VERSION) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_INVALID_DEVICE_ID) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_ILLEGAL_LIBRARY) {
//            }
            super.onPostExecute(result);
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    //==============================================================================

    @Override
    protected void onStart() {
        super.onStart();
//        palmtecandro.jnidevOpen(115200);
    }

    private void Print(String input) {
        int iLen = 0;
        int iCount = 0;
        int iPos = 0;

        byte[] _data = input.getBytes();
        iLen = _data.length;
        iLen += 4;
        final int[] dataArr = new int[iLen];
        dataArr[0] = (byte) 0x1B;
        dataArr[1] = (byte) 0x21;
        dataArr[2] = (byte) 0x00;
        iCount = 3;

        for (; iCount < iLen - 1; iCount++, iPos++) {
            dataArr[iCount] = _data[iPos];
        }
        dataArr[iCount] = (byte) 0x0A;

        palmtecandro.jnidevDataWrite(dataArr, iLen);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        palmtecandro.jnidevClose();
    }

    //==============================================================================

}
