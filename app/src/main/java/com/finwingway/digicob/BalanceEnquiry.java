package com.finwingway.digicob;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.softland.palmtecandro.palmtecandro;

/**
 * Created by AnVin on 1/7/2017.
 */

public class BalanceEnquiry extends Activity {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    private ProgressDialog mBluetoothConnectProgressDialog;
    TextView Date, Acc_no, Name, Mobile, Balance;

    public static com.leopard.api.Printer ptr;
    private int iRetVal = 0;
    public static final int DEVICE_NOTCONNECTED = -100;
    String BILL = "";
    private String date, acc_no, name, mobile, balance;
    SweetAlertDialog pDialog;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        setContentView(R.layout.balance_enquiry_layout);
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        acc_no = intent.getStringExtra("acc_no");
        name = intent.getStringExtra("introName");
        mobile = intent.getStringExtra("mobile");
        balance = intent.getStringExtra("balance");

        Date = (TextView) findViewById(R.id.balance_date);
        Acc_no = (TextView) findViewById(R.id.balance_acc_no);
        Name = (TextView) findViewById(R.id.balance_name);
        Mobile = (TextView) findViewById(R.id.balance_mobile);
        Balance = (TextView) findViewById(R.id.balance_balance);

        Date.setText(date);
        Acc_no.setText(acc_no);
        Name.setText(name);
        Mobile.setText(mobile);
        Balance.setText(balance);


        Button print = (Button) findViewById(R.id.printReceiptBtn);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BalanceEnquiry.this, "Print will not work in this device.!!", Toast.LENGTH_SHORT).show();
//                new PrintTask().execute();
//============================================================================================

//                try {
//                    OutputStream outSt = BluetoothComm.mosOut;
//                    InputStream inSt = BluetoothComm.misIn;
//                    ptr = new com.leopard.api.Printer(Act_Main.setupInstance, outSt, inSt);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if(connectionStatus){
//                    new PrintTask().execute();
//                }else {
//                    new SweetAlertDialog(BalanceEnquiry.this, SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("BT Device Not Connected")
//                            .setContentText("please connect the device")
//                            .setCancelText("NO")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    startActivity(new Intent(BalanceEnquiry.this, Act_Main.class));
//                                    sweetAlertDialog.dismiss();
//                                }
//                            }).show();
//                }


//========================================================================================
//                try {
//                    if (mChatService != null) {
//                        if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
//                            new PrintTask().execute();
//                        }
//                    } else {
//                        new SweetAlertDialog(BalanceEnquiry.this, SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText("BT Device Not Connected")
//                                .setContentText("please connect the device")
//                                .setCancelText("NO")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        startActivity(new Intent(BalanceEnquiry.this, BluetoothChat.class));
//                                        sweetAlertDialog.dismiss();
//                                    }
//                                }).show();
//                    }
//
//                } catch (Exception e) {
//                }

//========================================================================================

               /* mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(getBaseContext(), "Device Not Support", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(BalanceEnquiry.this,DeviceListActivity.class);
                        startActivityForResult(connectIntent,REQUEST_CONNECT_DEVICE);
                    }
                }
                Thread t = new Thread() {
                    public void run() {*/
            }
              /*  };
                t.start();
            }*/


        });


        Button sms = (Button) findViewById(R.id.smsReceiptbtn);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    public class PrintTask extends AsyncTask<Integer, Integer, Integer> {
        /* displays the progress dialog until background task is completed*/
        @Override
        protected void onPreExecute() {
            pDialog.setTitleText("Processing");
            pDialog.setContentText("Please wait..");
            pDialog.show();
            super.onPreExecute();
        }

        /* Task of EnterTextAsyc performing in the background*/
        @Override
        protected Integer doInBackground(Integer... params) {
            try {
//                ptr.iFlushBuf();
                try {

//                  BILL = "\n             Bank Name             \n\n";
                    BILL = "\n        THIRU-KOCHI URBAN NIDHI LIMITED          \n\n";
                    BILL = BILL + "Deposit Date     : " + date + "\n";
                    BILL = BILL + "Account Number   : " + acc_no + "\n";
                    BILL = BILL + "Name             : " + name + "\n";
                    BILL = BILL + "Mobile Number    : " + mobile + "\n";
                    BILL = BILL
                            + "------------------------------";
                    BILL = BILL + "\n\n";
                    BILL = BILL + "Current Balance  : " + balance + "\n\n";
                    BILL = BILL + "Thank you for Banking with us...";
                    BILL = BILL
                            + "------------------------------\n\n\n";
                } catch (Exception e) {
                    Log.e("Main", "Exe ", e);
                }
//========================================================================================
//                DataToPrint.setPrintData(BILL);
//========================================================================================

//                ptr.iPrinterAddData(Printer.PR_FONTSMALLNORMAL, BILL);
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
            pDialog.dismiss();
//            if (iRetVal == DEVICE_NOTCONNECTED) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_SUCCESS) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_PLATEN_OPEN) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_PAPER_OUT) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_IMPROPER_VOLTAGE) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_FAIL) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_PARAM_ERROR) {
//            } else if (iRetVal == com.leopard.api.Printer.PR_NO_RESPONSE) {
//            } else if (iRetVal== com.leopard.api.Printer.PR_DEMO_VERSION) {
//            } else if (iRetVal== com.leopard.api.Printer.PR_INVALID_DEVICE_ID) {
//            } else if (iRetVal== com.leopard.api.Printer.PR_ILLEGAL_LIBRARY) {
//            }
            super.onPostExecute(result);
        }
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




/*
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);

                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(BalanceEnquiry.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(BalanceEnquiry.this, "Bluetooth activate denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            mBluetoothConnectProgressDialog.cancel();
            Toast.makeText(BalanceEnquiry.this, "Connecting failed", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(BalanceEnquiry.this, "DeviceConnected", Toast.LENGTH_LONG).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }*/

}
