package com.finwingway.digicob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.softland.palmtecandro.palmtecandro;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.finwingway.digicob.login.ip_global;

public class Receipt_Loan_closing extends Activity {
    String[] receiptArray,
            ParticularArray, amountArray;
    private TableRow row;
    private TextView fixedView;
    private TextView textViewAmounts;
    private TableLayout alert_scrollablePart;
    private TableLayout alert_fixedColumn, alert_header;
    SweetAlertDialog dialog, progressDialog, errorDialog;
    JSONParser jsonParser = new JSONParser();
    protected static final String TAG = "TAG";
    public static com.leopard.api.Printer ptr;
    private int iRetVal = 0;
    String BILL = "";
    public static final int DEVICE_NOTCONNECTED = -100;
    private ImageButton print, sms;
    String[] slNoArray, trDateArray, cashArray;
    private String StrMsg, rtnMsg, smem_no, scust_no, sname, remtnc_amnt, loanAcc_no,
            sloan_acc, sclosingDate, seffctDate, svoucher_no, strans_id, sTrnsDate, sTrnsTime;
    TextView mem_noname_textView, cus_noname_textView, name_textView, amnt_textView,
            tv_loanAccNo, tv_closingDate, tv_effct_date, tv_voucher_no, tv_trans_id,
            tv_trans_date, tv_trans_time;
    private TextView textViewColon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        setContentView(R.layout.receipt_loan_closing);
        Intent intent = getIntent();
        smem_no = intent.getStringExtra("member_no");
        scust_no = intent.getStringExtra("customer_no");
        sname = intent.getStringExtra("name");
        sloan_acc = intent.getStringExtra("loan_acc");
        sclosingDate = intent.getStringExtra("closing_date");
        seffctDate = intent.getStringExtra("effective_date");
        svoucher_no = intent.getStringExtra("voucher_no");
        strans_id = intent.getStringExtra("trans_id");
        sTrnsDate = intent.getStringExtra("TrnsDate");
        sTrnsTime = intent.getStringExtra("TrnsTime");

        remtnc_amnt = intent.getStringExtra("amount");
        ParticularArray = intent.getStringArrayExtra("ParticularArray");
        amountArray = intent.getStringArrayExtra("amountArray");

        mem_noname_textView = (TextView) findViewById(R.id.receipt_mem_no_textView);
        cus_noname_textView = (TextView) findViewById(R.id.receipt_cus_no_textView);
        name_textView = (TextView) findViewById(R.id.receipt_name_textView);

        tv_loanAccNo = (TextView) findViewById(R.id.receipt_lon_acc_tv);
        tv_closingDate = (TextView) findViewById(R.id.receipt_cls_date_tv);
        tv_effct_date = (TextView) findViewById(R.id.receipt_effct_date_tv);
        tv_voucher_no = (TextView) findViewById(R.id.receipt_voucher_no_tv);
        tv_trans_id = (TextView) findViewById(R.id.receipt_trans_id_tv);
        tv_trans_date = (TextView) findViewById(R.id.receipt_date_tv);
        tv_trans_time = (TextView) findViewById(R.id.receipt_time_tv);

        amnt_textView = (TextView) findViewById(R.id.receipt_amnt_textView);

        mem_noname_textView.setText(smem_no);
        cus_noname_textView.setText(scust_no);
        name_textView.setText(sname);

        tv_loanAccNo.setText(sloan_acc);
        tv_closingDate.setText(sclosingDate);
        tv_effct_date.setText(seffctDate);
        tv_voucher_no.setText(svoucher_no);
        tv_trans_id.setText(strans_id);
        tv_trans_date.setText(sTrnsDate);
        tv_trans_time.setText(sTrnsTime);

        amnt_textView.setText(remtnc_amnt);

//        new getInstallmentDetails().execute();

        print = (ImageButton) findViewById(R.id.loan_printReceiptBtn);
        sms = (ImageButton) findViewById(R.id.loan_smsReceiptbtn);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Receipt_Loan_closing.this, "Print will not work in this device.!!", Toast.LENGTH_SHORT).show();
//                new PrintTask().execute();
//===========================================================================================================
////                try {
////                    OutputStream outSt = BluetoothComm.mosOut;
////                    InputStream inSt = BluetoothComm.misIn;
////                    ptr = new com.leopard.api.Printer(Act_Main.setupInstance, outSt, inSt);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////                new PrintTask().execute();
//
//                try {
//                    if (mChatService != null) {
//                        if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
//                            new PrintTask().execute();
//                        }
//                    } else {
//                        new SweetAlertDialog(Receipt_Loan_closing.this, SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText("BT Device Not Connected")
//                                .setContentText("please connect the device")
//                                .setCancelText("NO")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        startActivity(new Intent(Receipt_Loan_closing.this, BluetoothChat.class));
//                                        sweetAlertDialog.dismiss();
//                                    }
//                                }).show();
//                    }
//                    //                    new PrintTask().execute();
//                } catch (Exception e) {
//                }
//===========================================================================================================
            }
        });

        createTable();
    }

    //===============================================================================================================

    @SuppressLint("StaticFieldLeak")
    public class PrintTask extends AsyncTask<Integer, Integer, Integer> {
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

//                Calendar c = Calendar.getInstance();
//                @SuppressLint("SimpleDateFormat")
//                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                String CurrentDate = df.format(c.getTime());

                BILL = "\n       THIRU-KOCHI URBAN NIDHI LIMITED          \n\n";
//              BILL = "\n             Bank Name               \n\n";
                BILL = BILL + "Transaction Date: " + sTrnsDate + "\n";
                BILL = BILL + "Transaction Time: " + sTrnsTime + "\n";
                BILL = BILL + "Member Number   : " + smem_no + "\n";
                BILL = BILL + "Customer Number : " + scust_no + "\n";
                BILL = BILL + "Name            : " + sname + "\n";

                BILL = BILL + "Loan Acc No     : " + sloan_acc + "\n";
                BILL = BILL + "Closing Date    : " + sclosingDate + "\n";
                BILL = BILL + "Effective Date  : " + seffctDate + "\n";
                BILL = BILL + "Voucher Number  : " + svoucher_no + "\n";
                BILL = BILL + "Transaction ID  : " + strans_id + "\n\n";

                BILL = BILL + "Principal Amount: " + amountArray[0] + "\n";
                BILL = BILL + "Interest        : " + amountArray[1] + "\n";
                BILL = BILL + "Penal Interest  : " + amountArray[2] + "\n";

                BILL = BILL + "\n";
                BILL = BILL + "Remittance Amount: " + remtnc_amnt + "\n\n";
                BILL = BILL + "Thank you for Banking with us...\n";
//                BILL = BILL + "      Keep Smiling :)\n";
                BILL = BILL
                        + "------------------------------\n\n\n";

//===========================================================================================================
                Log.e("PrintTask: ", BILL);
//                DataToPrint.setPrintData(BILL);
//===========================================================================================================

//                String empty = BILL;//+ "\n" + "\n" + "\n" + "\n" + "\n"+ "\n";
//
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
            Print(BILL);
            dialog.dismiss();
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

    //===============================================================================================================

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


    public void createTable() {
        alert_fixedColumn = (TableLayout) findViewById(R.id.receipt_fixed_column);
        alert_scrollablePart = (TableLayout) findViewById(R.id.receipt_scrollable_part);
        alert_header = (TableLayout) findViewById(R.id.receipt_table_header);
        TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
        int[] scrollableColumnWidths = new int[]{20, 20, 20, 20, 20};
        int[] scrollableColumnWidthsColon = new int[]{5, 5, 5, 5, 5};
        int fixedRowHeight = 50;
        int fixedHeaderHeight = 60;

        int length = ParticularArray.length;
//        int length = 3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 5, 2, 5);
        for (int i = 0; i < length; i++) {
            fixedView = makeTableRowWithText(ParticularArray[i], 35, fixedRowHeight);
            fixedView.setTextSize(24);
            fixedView.setGravity(Gravity.CENTER_VERTICAL);
            fixedView.setLayoutParams(params);
            fixedView.setTextColor(Color.BLACK);
            alert_fixedColumn.addView(fixedView);

            row = new TableRow(this);
            row.setLayoutParams(wrapWrapTableRowParams);
//            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setGravity(Gravity.CENTER);
//            row.setBackgroundColor(Color.WHITE);

            textViewColon = makeTableRowWithColon(":", scrollableColumnWidthsColon[4], fixedRowHeight);
            textViewColon.setPadding(0, 0, 0, 0);
            textViewColon.setTextColor(Color.BLACK);

            textViewAmounts = makeTableRowWithColon(String.valueOf(amountArray[i]), scrollableColumnWidths[4], fixedRowHeight);
            textViewAmounts.setPadding(5, 0, 0, 0);
            textViewAmounts.setTextColor(Color.BLACK);
            row.addView(textViewColon);
            row.addView(textViewAmounts);
            alert_scrollablePart.addView(row);
        }
    }

    public TextView makeTableRowWithColon(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels) {
        TextView recyclableTextView;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(this);
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setGravity(Gravity.CENTER | Gravity.LEFT);
        recyclableTextView.setTextSize(20);
        recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setHeight(fixedHeightInPixels);
        return recyclableTextView;
    }

    public TextView makeTableRowWithText(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels) {
        TextView recyclableTextView;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(getBaseContext());
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setGravity(Gravity.CENTER_VERTICAL);
        recyclableTextView.setTextSize(20);
        recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setHeight(fixedHeightInPixels);
        return recyclableTextView;
    }

    private class getInstallmentDetails extends AsyncTask<String, String, String> {
        private String api_url = ip_global + "/getLoanInstallmentDetails";

        @Override
        protected void onPreExecute() {
            progressDialog.setTitleText("Loading");
            progressDialog.setContentText("please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", loanAcc_no);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("LoanInstlmntDtils", String.valueOf(data));
                if (data.has("account")) {
                    StrMsg = data.getJSONObject("account").getString("error");
                    return rtnMsg = "0";
                }
                if (data.has("TransactionDetails")) {
                    JSONArray jArry = data.getJSONArray("TransactionDetails");
                    int aryCnt = jArry.length();
                    slNoArray = new String[aryCnt];
                    trDateArray = new String[aryCnt];
                    cashArray = new String[aryCnt];
                    for (int i = 0; i < aryCnt; i++) {
                        slNoArray[i] = jArry.getJSONObject(i).getString("Sl_No");
                        trDateArray[i] = jArry.getJSONObject(i).getString("Tr_Date");
                        cashArray[i] = jArry.getJSONObject(i).getString("Cash");
                    }
                    rtnMsg = "ok";
                }
            } catch (Exception e) {
                e.printStackTrace();
                rtnMsg = "error";
                return rtnMsg;
            }
            return rtnMsg;
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result.equals("0")) {
//                new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                errorDialog.setTitleText("Error")
                        .setContentText(StrMsg)
                        .show();
            }
            if (result.equals("error")) {
//                new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                errorDialog.setTitleText("Error")
                        .setContentText("Some error occurred!")
                        .show();
            }
            if (result.equals("ok")) {
                createTable();
            }
        }
    }


}
