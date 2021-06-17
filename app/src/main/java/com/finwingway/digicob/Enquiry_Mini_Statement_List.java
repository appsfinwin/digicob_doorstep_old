package com.finwingway.digicob;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//import com.finwingway.digicob.SilPrint.BluetoothChatServic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import com.finwingway.digicob.sil.BluetoothChat;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.softland.palmtecandro.palmtecandro;

/**
 * Created by AnVin on 1/18/2017.
 */

public class Enquiry_Mini_Statement_List extends Activity {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    private final String depositUrl = ip + "/getMiniStatement";

    //=======================================================================
//    public BluetoothChatService mChatService = null;
    private StringBuffer mOutStringBuffer;
    //    private final Handler mHandler = null;
//    BluetoothCha blueCht;
    SharedPreferences my_Preferences;

    private String mConnectedDeviceName = null;
    private static final boolean D = true;
    //=======================================================================

    SweetAlertDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    String account_number;
    public static String ip = login.ip_global;
    Intent intent;
    Context context;
    TableLayout tl;
    TableRow tr;
    TextView companyTV, valueTV, debit, amount, balan;
    TextView date, time, transaction_id, particular, debitorCredit, transactionAmount, accountBalance,
            date2, time2, transaction_id2, particular2, debitorCredit2, transactionAmount2, accountBalance2,
            date3, time3, transaction_id3, particular3, debitorCredit3, transactionAmount3, accountBalance3,
            date4, time4, transaction_id4, particular4, debitorCredit4, transactionAmount4, accountBalance4,
            date5, time5, transaction_id5, particular5, debitorCredit5, transactionAmount5, accountBalance5,
            date6, time6, transaction_id6, particular6, debitorCredit6, transactionAmount6, accountBalance6,
            date7, time7, transaction_id7, particular7, debitorCredit7, transactionAmount7, accountBalance7,
            date8, time8, transaction_id8, particular8, debitorCredit8, transactionAmount8, accountBalance8,
            date9, time9, transaction_id9, particular9, debitorCredit9, transactionAmount9, accountBalance9,
            date10, time10, transaction_id10, particular10, debitorCredit10, transactionAmount10, accountBalance10,
            account_numberTextView, name, mobile, balance;
    String ptxnid, pdate, ptime, pParticular, pDebitorCre, pamount, pbalance;

    public static com.leopard.api.Printer ptr;
    private int iRetVal=0;
    public static final int DEVICE_NOTCONNECTED = -100;
    String BILL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_mini_statement_layout);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        new AsyncMiniStatement().execute();
        context = this;
        tl = (TableLayout) findViewById(R.id.maintable);

        //=======================================================================
//// Initialize the BluetoothChatServic to perform bluetooth connections
//        if (mChatService == null)
//            mChatService = new BluetoothChatServic(this, mHandler);
//        // Initialize the buffer for outgoing messages
//        mOutStringBuffer = new StringBuffer("");
        //=======================================================================

        ImageButton blPrinter = findViewById(R.id.bltooth_btn_printmstmnt);
        blPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (mChatService != null) {
//                    if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
//                        Toast.makeText(getApplicationContext(), "Printer is already connected.", Toast.LENGTH_LONG).show();
//                    }
//                } else {
                Intent intent = new Intent(getApplicationContext(), BluetoothChat.class);
                startActivity(intent);
//                }
            }
        });

        ImageButton print = (ImageButton) findViewById(R.id.MiniStatementprintReceiptBtn);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Enquiry_Mini_Statement_List.this, "Print will not work in this device.!!", Toast.LENGTH_SHORT).show();
//                new PrintTask().execute();
                //===========================================================================================================
//                if (mChatService != null) {
//                    if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
//                        new PrintTask().execute();
//                    }
//                } else {
//                    new SweetAlertDialog(Enquiry_Mini_Statement_List.this, SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("BT Device Not Connected")
//                            .setContentText("please connect the device")
//                            .setCancelText("NO")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    startActivity(new Intent(Enquiry_Mini_Statement_List.this, BluetoothChat.class));
//                                    sweetAlertDialog.dismiss();
//                                }
//                            }).show();
//                }
//
//
////                try {
////                    OutputStream outSt = BluetoothComm.mosOut;
////                    InputStream inSt = BluetoothComm.misIn;
////                    ptr = new com.leopard.api.Printer(Act_Main.setupInstance, outSt, inSt);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////                if (connectionStatus) {
////                     new PrintTask().execute();
////                } else {
////                    new SweetAlertDialog(Enquiry_Mini_Statement_List.this, SweetAlertDialog.WARNING_TYPE)
////                            .setTitleText("BT Device Not Connected")
////                            .setContentText("please connect the device")
////                            .setCancelText("NO")
////                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
////                                @Override
////                                public void onClick(SweetAlertDialog sweetAlertDialog) {
////                                    startActivity(new Intent(Enquiry_Mini_Statement_List.this, Act_Main.class));
////                                    sweetAlertDialog.dismiss();
////                                }
////                            }).show();
////                }
////               /* mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
////                if (mBluetoothAdapter == null) {
////                    Toast.makeText(getBaseContext(), "Device Not Support", Toast.LENGTH_SHORT).show();
////                } else {
////                    if (!mBluetoothAdapter.isEnabled()) {
////                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
////                        startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
////                    } else {
////                        ListPairedDevices();
////                        Intent connectIntent = new Intent(Enquiry_Mini_Statement_List.this,DeviceListActivity.class);
////                        startActivityForResult(connectIntent,REQUEST_CONNECT_DEVICE);
////                    }
////                }*/
            }
        });
        //===========================================================================================================

    }// The Handler that gets information back from the BluetoothChatServic

    ///////////////////////////////////////////////////////////////////////////////////////////////////
//
//    @SuppressLint("HandlerLeak")
//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MESSAGE_STATE_CHANGE:
//                    if (D)
//                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
//                    switch (msg.arg1) {
//                        case BluetoothChatServic.STATE_CONNECTED:
//                            // setStatus( "Charge " + printcharge );
////                            txtprintername.setText("Connected to "
////                                    + mConnectedDeviceName);
////                            // mSendButton.setVisibility( View.VISIBLE );
////                            btnLayout.setVisibility(View.VISIBLE);
////                            mConversationArrayAdapter.clear();
//                            break;
//                        case BluetoothChatServic.STATE_CONNECTING:
//                            // setStatus( R.string.title_connecting );
////                            txtprintername.setText("Connecting... ");
////                            // mSendButton.setVisibility( View.INVISIBLE );
////                            btnLayout.setVisibility(View.INVISIBLE);
//                            break;
//                        case BluetoothChatServic.STATE_LISTEN:
//                        case BluetoothChatServic.STATE_NONE:
//                            // setStatus( R.string.title_not_connected );
////                            txtprintername.setText("Not Connected... ");
////                            // mSendButton.setVisibility( View.VISIBLE );
////                            btnLayout.setVisibility(View.VISIBLE);
//                            break;
//                    }
//                    break;
//                case MESSAGE_WRITE:
//                    try {
//                        byte[] writeBuf = (byte[]) msg.obj;
//                        String writeMessage = new String(writeBuf);
//                        System.out.println("mywrite:" + writeMessage);
//                        // mConversationArrayAdapter.add("Me:  "
//                        // + writeMessage);
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                    }
//                    // construct a string from the
//                    // buffer
//
//                    break;
//                case MESSAGE_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    // construct a string from the
//                    // valid bytes in the buffer
//                    System.out.println("myresp" + readBuf);
//                    String readMessage = new String(readBuf, 0, msg.arg1);
//                    System.out.println("myread:" + readMessage);
//                    /*
//                     * mConversationArrayAdapter.add( mConnectedDeviceName + ":  " +
//                     * readMessage );
//                     */
//                    Log.e("", "READ MESSEGE OUT :" + readMessage);
//
//                    if (readMessage.length() >= 5) {
//
//                        // ifbattery = true;
//                        readMessage = readMessage.trim().replace("BL=", "");
//                        int charge = 0;
//                        System.out.println("chargeinfo " + readMessage);
//                        Log.e("", "READ MESSEGE :" + readMessage);
//                        try {
//                            charge = Integer.parseInt(readMessage);
//                        } catch (Exception e) {
//                            // TODO: handle exception
//                        }
//                        // BluetoothCha.this.sendMessageTahi( receivedmsg );
//                        if (readMessage.equals("Truncating Files...")) {
//                            System.out.println("truncs");
//                        } else {
//
//                        }
//
//                        if (readMessage.equals("0")) {
//                            System.out.println("chargeinfo nocharge");
////                            txtprintername.setText("No Charge in Printer ");
//
//                        } else {
//
//                            System.out.println("chargeinfo charge yes");
////                            ifbattery = true;
//                            Toast.makeText(getApplicationContext(), "Cleared", 2000)
//                                    .show();
//
//                            // BluetoothCha.this.sendMessageTahi( receivedmsg );
//
//                        }
//                        // setStatus("Charge " + readMessage );
//                        SharedPreferences.Editor editor = my_Preferences.edit();
//                        editor.putString(Variables.PRINTERCHARGE, readMessage);
//                        editor.commit();
//
//                        // ifbattery = true;
//                        // BluetoothCha.this.sendMessageTahi( receivedmsg );
//
//                        /*
//                         * readMessage = readMessage.trim().replace( "BL=", "" );
//                         * printcharge = readMessage; // Log.e( "", "READ MESSEGE :"
//                         * + readMessage );
//                         *
//                         * SharedPreferences.Editor editor = my_Preferences.edit();
//                         * editor.putString( Variables.PRINTERCHARGE, readMessage );
//                         * editor.commit();
//                         *
//                         * if ( readMessage.contains( "0" ) ) {
//                         * txtprintername.setText( "No Charge in Printer " );
//                         *
//                         * }else {
//                         *
//                         * ifbattery = true;
//                         *
//                         * BluetoothCha.this.sendMessageTahi( receivedmsg );
//                         *
//                         * }
//                         */
//                    }
//
//                    // mSendButton.setEnabled( false
//                    // );
//                    break;
//                case MESSAGE_DEVICE_NAME:
//                    // save the connected device's
//                    // name
//                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
////                    txtprintername.setText("Connected to " + mConnectedDeviceName);
//                    Toast.makeText(getApplicationContext(),
//                            "Connected to " + mConnectedDeviceName, 500).show();
//                    break;
//                case MESSAGE_TOAST:
//                    Toast.makeText(getApplicationContext(),
//                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
//                            .show();
//                    break;
//            }
//        }
//    };

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    class AsyncMiniStatement extends AsyncTask<String, String, String> {
        @Override
        public void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading..");
            pDialog.setCancelable(false);
            pDialog.show();
            intent = getIntent();
            account_number = intent.getStringExtra("acc_no");
            account_numberTextView = (TextView) findViewById(R.id.accNOMiniStatement);
            name = (TextView) findViewById(R.id.nameMiniStatement);
            mobile = (TextView) findViewById(R.id.MobileMiniStatementtxt);
            balance = (TextView) findViewById(R.id.currentBalanceMiniStatement);

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMapAmount = new HashMap<>();
            try {
                hashMapAmount.put("account_no", account_number);
                JSONObject jsonObject = jsonParser.makeHttpRequest(depositUrl, "POST", hashMapAmount);

                Log.e("account_number", account_number);
                Log.e("AsyncMiniStmnt InBg", jsonObject.toString());

                String JsonDataString = jsonObject.toString();
                JSONObject reader = new JSONObject(JsonDataString);
                JSONObject receipt = reader.getJSONObject("mini_statement");
                final JSONObject data = receipt.getJSONObject("data");
                final JSONArray jsonArray = data.getJSONArray("TRANSACTONS");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            account_numberTextView.setText(data.getString("ACC_NO"));
                            name.setText(data.getString("NAME"));
                            mobile.setText(data.getString("MOBILE"));
                            balance.setText(data.getString("CURRENT_BALANCE"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        /** This function add the headers to the table **/
                        /** Create a TableRow dynamically **/
                        tr = new TableRow(getBaseContext());
                        tr.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        /** Creating a TextView to add to the row **/
                        TextView companyTV = new TextView(getBaseContext());
                        companyTV.setText("DATE");
                        companyTV.setTextColor(Color.RED);
                        companyTV.setGravity(Gravity.CENTER);
                        companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        companyTV.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        companyTV.setPadding(5, 5, 5, 0);
                        tr.addView(companyTV);  // Adding textView to tablerow.

                        /** Creating another textview **/
                        TextView valueTV = new TextView(getBaseContext());
                        valueTV.setText("PARTICULAR");
                        valueTV.setGravity(Gravity.CENTER);
                        valueTV.setTextColor(Color.RED);
                        valueTV.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        valueTV.setPadding(5, 5, 5, 0);
                        valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        tr.addView(valueTV); // Adding textView to tablerow.

                        /** Creating another textview **/
                        TextView debCre = new TextView(getBaseContext());
                        debCre.setText("DEBIT/CREDIT");
                        debCre.setTextColor(Color.RED);
                        debCre.setGravity(Gravity.CENTER);
                        debCre.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        debCre.setPadding(5, 5, 5, 0);
                        debCre.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        tr.addView(debCre); // Adding textView to tablerow.
                        /** Creating another textview **/
                        TextView amount = new TextView(getBaseContext());
                        amount.setText("AMOUNT");
                        amount.setTextColor(Color.RED);
                        amount.setGravity(Gravity.CENTER);
                        amount.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        amount.setPadding(5, 5, 5, 0);
                        amount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        tr.addView(amount); // Adding textView to tablerow.

                        /** Creating another textview **/
                        TextView balance = new TextView(getBaseContext());
                        balance.setText("BALANCE");
                        balance.setTextColor(Color.RED);
                        balance.setGravity(Gravity.CENTER);
                        balance.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                        balance.setPadding(5, 5, 5, 0);
                        balance.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        tr.addView(balance); // Adding textView to tablerow.

                        // Add the TableRow to the TableLayout
                        tl.addView(tr, new TableLayout.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json_data = null;
                            try {
                                json_data = jsonArray.getJSONObject(i);

                                ptxnid = json_data.getString("TXN_ID");
                                pdate = json_data.getString("TXN_DATE");
                                pParticular = json_data.getString("TRAN_TYPE");
                                pDebitorCre = json_data.getString("DORC");
                                pamount = json_data.getString("TXN_AMOUNT");
                                pbalance = json_data.getString("BALANCE");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            /** Create a TableRow dynamically **/
                            tr = new TableRow(getBaseContext());
                            tr.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));

                            /** Creating a TextView to add to the row **/
                            companyTV = new TextView(getBaseContext());
                            companyTV.setText(pdate);
                            companyTV.setGravity(Gravity.CENTER);
                            companyTV.setTextColor(Color.BLACK);
                            companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            companyTV.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            companyTV.setPadding(5, 5, 5, 5);
                            tr.addView(companyTV);  // Adding textView to tablerow.

                            /** Creating another textview **/
                            valueTV = new TextView(getBaseContext());
                            valueTV.setText(pParticular);
                            valueTV.setGravity(Gravity.CENTER);
                            valueTV.setTextColor(Color.BLACK);
                            valueTV.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            valueTV.setPadding(5, 5, 5, 5);
                            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            tr.addView(valueTV); // Adding textView to tablerow.

                            /** Creating another textview **/
                            debit = new TextView(getBaseContext());
                            debit.setText(pDebitorCre);
                            debit.setGravity(Gravity.CENTER);
                            debit.setTextColor(Color.BLACK);
                            debit.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            debit.setPadding(5, 5, 5, 5);
                            debit.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            tr.addView(debit); // Adding textView to tablerow.

                            /** Creating another textview **/
                            amount = new TextView(getBaseContext());
                            amount.setText(pamount);
                            amount.setGravity(Gravity.CENTER);
                            amount.setTextColor(Color.BLACK);
                            amount.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            amount.setPadding(5, 5, 5, 5);
                            amount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            tr.addView(amount); // Adding textView to tablerow.

                            /** Creating another textview **/
                            balance = new TextView(getBaseContext());
                            balance.setText(pbalance);
                            balance.setGravity(Gravity.CENTER);
                            balance.setTextColor(Color.BLACK);
                            balance.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            balance.setPadding(5, 5, 5, 5);
                            balance.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                            tr.addView(balance); // Adding textView to tablerow.

                            // Add the TableRow to the TableLayout
                            tl.addView(tr, new TableLayout.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
            return null;
        }

        @Override
        public void onPostExecute(String url) {
            if (url == "error") {
                pDialog.dismiss();
                new SweetAlertDialog(Enquiry_Mini_Statement_List.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText("Invalid Account Number!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .show();

            } else {
                pDialog.dismiss();
                Log.e("PostExecute-", "Executed");
            }
        }
    }


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

/*
        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);


                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Connecting to "+ mBluetoothDevice.getName());
                    pDialog.setCancelable(false);
                    pDialog.show();

                    */
/*pDialog = SweetAlertDialog.show(getBaseContext(),
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);*//*



                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(Enquiry_Mini_Statement_List.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(Enquiry_Mini_Statement_List.this, "Bluetooth activate denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
*/
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pDialog != null) {
            pDialog.dismiss();
        }
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
                    HashMap<String, String> hashMapAmount = new HashMap<>();
                    hashMapAmount.put("account_no", account_number);
                    JSONObject jsonObject = jsonParser.makeHttpRequest(depositUrl, "POST", hashMapAmount);
                    String JsonDataString = jsonObject.toString();

                    Log.e("mini_statement resp =>", JsonDataString);

                    JSONObject reader = new JSONObject(JsonDataString);
                    JSONObject receipt = reader.getJSONObject("mini_statement");
                    JSONObject data = receipt.getJSONObject("data");
                    final JSONArray jsonArray = data.getJSONArray("TRANSACTONS");

//                    BILL = "                Bank Name               \n\n";
                    BILL = "\n        THIRU-KOCHI URBAN NIDHI LIMITED          \n\n";
                    BILL = BILL + " Account Number  : " + data.getString("ACC_NO") + "\n";
                    BILL = BILL + " Account Name    : " + data.getString("NAME") + "\n";
                    BILL = BILL + " Mobile Number   : " + data.getString("MOBILE") + "\n";
                    BILL = BILL + " Current Balance : " + data.getString("CURRENT_BALANCE") + "\n\n";
//                    BILL = BILL + "   DATE    TYPE Dr/Cr AMT  BAL" + "\n\n";
                    BILL = BILL + "   DATE    Dr/Cr  AMT  BAL" + "\n\n";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json_data = jsonArray.getJSONObject(i);
                        ptxnid = json_data.getString("TXN_ID");
                        pdate = json_data.getString("TXN_DATE");
//                        pParticular = json_data.getString("TRAN_TYPE");
                        pDebitorCre = json_data.getString("DORC");
                        pamount = json_data.getString("TXN_AMOUNT");
                        pbalance = json_data.getString("BALANCE");

//                        BILL = BILL + pdate + " " + pParticular + "   " + pDebitorCre + "  " + pamount + "  " + pbalance + "\n";
                        BILL = BILL + pdate + " " + "  " + pDebitorCre + "  " + pamount + "  " + pbalance + "\n";
                                       /* BILL = BILL + "Transaction ID     : " + ptxnid + "\n";
                                        BILL = BILL + "Particular         : " + pParticular + "\n";
                                        BILL = BILL + "Debit or Credit    : " + pDebitorCre + "\n";
                                        BILL = BILL + "Transaction Amount : " + pamount + "\n";
                                        BILL = BILL + "Account Balance    : " + pbalance + "\n";
                                        BILL = BILL
                                                + "------------------------------";*/
                    }
                    BILL = BILL + "\n\n";
                    BILL = BILL + "Thank you for Banking with us...\n";
//                    BILL = BILL + "      Keep Smiling :)\n";
                    BILL = BILL
                            + "------------------------------\n\n\n";
                    Log.e("Print=>", BILL.toString());
                } catch (Exception e) {
                    Log.e("Main", "Exe ", e);
                }

                //========================================================================================================================
//                DataToPrint.setPrintData(BILL);
                //=======================================================================================================================

//                ptr.iPrinterAddData(Printer.PR_FONTSMALLNORMAL, BILL);//PR_FONTSMALLNORMAL
//                ptr.iPrinterAddData(com.leopard.api.Printer.PR_FONTLARGENORMAL, " \n \n \n \n \n \n");//PR_FONTLARGENORMAL
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
