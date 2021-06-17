package com.finwingway.digicob.inauguration;

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

import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;
import com.finwingway.digicob.sil.BluetoothChat;
import com.finwingway.digicob.sil.BluetoothChatService;
import com.finwingway.digicob.sil.DataToPrint;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import static com.finwingway.digicob.sil.BluetoothChat.mChatService;

public class Acc_Statement_List extends Activity {
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
    private int iRetVal;
    public static final int DEVICE_NOTCONNECTED = -100;
    String BILL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaaaa_inaugu_statement_lay);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        new AsyncMiniStatement().execute();
        context = this;
        tl = (TableLayout) findViewById(R.id.inau_maintable);

        //=======================================================================
//// Initialize the BluetoothChatServic to perform bluetooth connections
//        if (mChatService == null)
//            mChatService = new BluetoothChatServic(this, mHandler);
//        // Initialize the buffer for outgoing messages
//        mOutStringBuffer = new StringBuffer("");
        //=======================================================================

        ImageButton blPrinter = findViewById(R.id.inau_bltooth_btn_printmstmnt);
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

        ImageButton print = (ImageButton) findViewById(R.id.inau_MiniStatementprintReceiptBtn);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mChatService != null) {
                        if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
                            new PrintTask().execute();
                        }
                    } else {
                        new SweetAlertDialog(Acc_Statement_List.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("BT Device Not Connected")
                                .setContentText("please connect the device")
                                .setCancelText("NO")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        startActivity(new Intent(Acc_Statement_List.this, BluetoothChat.class));
                                        sweetAlertDialog.dismiss();
                                    }
                                }).show();
                    }

                } catch (Exception e) {

                }
            }
        });
    }


    class AsyncMiniStatement extends AsyncTask<String, String, String> {
        @Override
        public void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading..");
            pDialog.setCancelable(false);
            pDialog.show();

//            intent = getIntent();
//            account_number = intent.getStringExtra("acc_no");
            account_numberTextView = (TextView) findViewById(R.id.inau_accNOMiniStatement);
            name = (TextView) findViewById(R.id.inau_nameMiniStatement);
            mobile = (TextView) findViewById(R.id.inau_MobileMiniStatementtxt);
            balance = (TextView) findViewById(R.id.inau_currentBalanceMiniStatement);

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMapAmount = new HashMap<>();
            try {
                String file_path = getApplicationContext().getFilesDir().getPath();
                Log.e("file_path: ", file_path);
//                String JsonDataString = UtilClass.readJsonFromDisk(getApplicationContext(), file_path + "/" + file_name_userdetails);R.raw.ministmnt
//                String JsonDataString = UtilClass.readJsonFromDisk(getApplicationContext(), String.valueOf(Uri.parse("android.resource://" + getPackageName() + R.raw.ministmnt)));
                String JsonDataString = UtilClass.readJsonAsStringFromDisk(getApplicationContext(), R.raw.ministmnt);

                Log.e("JsonDataString: ", JsonDataString);


//                hashMapAmount.put("account_no", "133509");
//                JSONObject jsonObject = jsonParser.makeHttpRequest(depositUrl, "POST", hashMapAmount);
//                String JsonDataString = jsonObject.toString();
//                JSONObject reader = new JSONObject(JsonDataString);

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
                new SweetAlertDialog(Acc_Statement_List.this, SweetAlertDialog.ERROR_TYPE)
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

                    BILL = "                Bank Name               \n\n";
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

                        BILL = BILL + pdate + " " + pParticular + "   " + pDebitorCre + "  " + pamount + "  " + pbalance + "\n";
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
                DataToPrint.setPrintData(BILL);
                //=======================================================================================================================

//                ptr.iPrinterAddData(Printer.PR_FONTSMALLNORMAL, BILL);//PR_FONTSMALLNORMAL
//                ptr.iPrinterAddData(com.leopard.api.Printer.PR_FONTLARGENORMAL, " \n \n \n \n \n \n");//PR_FONTLARGENORMAL
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
            pDialog.dismiss();
            BluetoothChat.printData();

            if (iRetVal == DEVICE_NOTCONNECTED) {
            } else if (iRetVal == com.leopard.api.Printer.PR_SUCCESS) {
            } else if (iRetVal == com.leopard.api.Printer.PR_PLATEN_OPEN) {
            } else if (iRetVal == com.leopard.api.Printer.PR_PAPER_OUT) {
            } else if (iRetVal == com.leopard.api.Printer.PR_IMPROPER_VOLTAGE) {
            } else if (iRetVal == com.leopard.api.Printer.PR_FAIL) {
            } else if (iRetVal == com.leopard.api.Printer.PR_PARAM_ERROR) {
            } else if (iRetVal == com.leopard.api.Printer.PR_NO_RESPONSE) {
            } else if (iRetVal == com.leopard.api.Printer.PR_DEMO_VERSION) {
            } else if (iRetVal == com.leopard.api.Printer.PR_INVALID_DEVICE_ID) {
            } else if (iRetVal == com.leopard.api.Printer.PR_ILLEGAL_LIBRARY) {
            }
            super.onPostExecute(result);
        }
    }

}
