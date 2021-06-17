package com.finwingway.digicob;

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

import com.finwingway.digicob.sil.DataToPrint;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.finwingway.digicob.login.ip_global;

public class Receipt_Loan_old extends Activity {
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
    private int iRetVal;
    public static final int DEVICE_NOTCONNECTED = -100;
    private ImageButton print, sms;
    String[] slNoArray, trDateArray, cashArray;
    private String StrMsg, rtnMsg, smem_no, scust_no, sname, remtnc_amnt, loanAcc_no;
    TextView mem_noname_textView, cus_noname_textView, name_textView, amnt_textView;
    private TextView textViewColon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        setContentView(R.layout.receipt_loan);
        Intent intent = getIntent();
        smem_no = intent.getStringExtra("member_no");
        scust_no = intent.getStringExtra("customer_no");
        sname = intent.getStringExtra("name");

        remtnc_amnt = intent.getStringExtra("amount");
        ParticularArray = intent.getStringArrayExtra("ParticularArray");
        amountArray = intent.getStringArrayExtra("amountArray");

//        loanAcc_no = intent.getStringExtra("loan_acc");
//        receiptArray = intent.getStringArrayExtra("receiptArray");

        mem_noname_textView = (TextView) findViewById(R.id.receipt_mem_no_textView);
        cus_noname_textView = (TextView) findViewById(R.id.receipt_cus_no_textView);
        name_textView = (TextView) findViewById(R.id.receipt_name_textView);
        amnt_textView = (TextView) findViewById(R.id.receipt_amnt_textView);

        mem_noname_textView.setText(smem_no);
        cus_noname_textView.setText(scust_no);
        name_textView.setText(sname);
        amnt_textView.setText(remtnc_amnt);

//        new getInstallmentDetails().execute();

        print = (ImageButton) findViewById(R.id.loan_printReceiptBtn);
        sms = (ImageButton) findViewById(R.id.loan_smsReceiptbtn);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    OutputStream outSt = BluetoothComm.mosOut;
//                    InputStream inSt = BluetoothComm.misIn;
//                    ptr = new com.leopard.api.Printer(Act_Main.setupInstance, outSt, inSt);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                new PrintTask().execute();

                try {
//                    if (mChatService != null) {
//                        if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
//                            new PrintTask().execute();
//                        }
//                    } else {
//                        new SweetAlertDialog(Receipt_Loan.this, SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText("BT Device Not Connected")
//                                .setContentText("please connect the device")
//                                .setCancelText("NO")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        startActivity(new Intent(Receipt_Loan.this, BluetoothChat.class));
//                                        sweetAlertDialog.dismiss();
//                                    }
//                                }).show();
//                    }

                    new PrintTask().execute();

                } catch (Exception e) {

                }
            }
        });

//        for (int i = 0; i < receiptArray.length; i++) {
//            Log.e("receiptArrayReceipt => ", receiptArray[i]);
//        }
        createTable();
    }

    //===============================================================================================================

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
                String BILL = "";

                BILL = "\n             Bank Name               \n\n";
                /*BILL = BILL + "Date             : " + deposit_date + "\n";
                BILL = BILL + "Account Number   : " + account_number + "\n";
                BILL = BILL + "Name             : " + name +"\n";
                BILL = BILL + "Mobile Number    : " + mobile +"\n";
                BILL = BILL
                        + "------------------------------";
                BILL = BILL + "\n\n";
                BILL = BILL + "Opening Balance  : " + old_balance+"\n";
                BILL = BILL + "Deposited Amount : " + deposit_amount+"\n";
                BILL = BILL + "Current Balance  : " + current_balance+"\n\n";*/

//                for (int i = receiptArray.length - 1; i >= 0; i--) {
//                    BILL = BILL + receiptArray[i] + "\n";
//                }

                BILL = BILL + "Date            : " + "\n";
                BILL = BILL + "Member Number   : " + smem_no + "\n";
                BILL = BILL + "Customer Number : " + scust_no + "\n";
                BILL = BILL + "Name            : " + sname + "\n\n";

                BILL = BILL + "Principal Amount: " + amountArray[0] + "\n";
                BILL = BILL + "Interest        : " + amountArray[1] + "\n";
                BILL = BILL + "Penal Interest  : " + amountArray[2] + "\n";

//                for (int i = 0; i < ParticularArray.length; i++) {
//                    BILL = BILL + ParticularArray[i] + " : " + amountArray[i] + "\n";
//                }

                BILL = BILL + "\n";
                BILL = BILL + "Remittance Amount: " + remtnc_amnt + "\n\n";
                BILL = BILL + "Thank you for Banking with us...\n";
//                BILL = BILL + "      Keep Smiling :)\n";
                BILL = BILL
                        + "------------------------------\n\n\n";

                Log.e("PrintTask: ", BILL);
                DataToPrint.setPrintData(BILL);

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
            dialog.dismiss();
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

    //===============================================================================================================

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

            /*row = new TableRow(getBaseContext());
            row.setLayoutParams(wrapWrapTableRowParams);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);
            textViewAmounts =makeTableRowWithText(" : "+String.valueOf(b[i]),scrollableColumnWidths[4], fixedRowHeight);
            textViewAmounts.setPadding(5,0,0,0);
            textViewAmounts.setTextColor(Color.RED);
            row.addView(textViewAmounts);
            alert_scrollablePart.addView(row);*/
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


//    public void createTable() {
//        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
//        TableRow tbrow0 = new TableRow(this);
//        TextView tv0 = new TextView(this);
//        tv0.setText(" Sl.No ");
//        tv0.setTextColor(Color.WHITE);
//        tbrow0.addView(tv0);
//
////        TextView tv1 = new TextView(this);
////        tv1.setText(" Trans Date ");
////        tv1.setTextColor(Color.WHITE);
////        tbrow0.addView(tv1);
//
//        TextView tv2 = new TextView(this);
//        tv2.setText(" Trans Date ");
//        tv2.setTextColor(Color.WHITE);
//        tbrow0.addView(tv2);
//        TextView tv3 = new TextView(this);
//        tv3.setText(" Amount ");
//        tv3.setTextColor(Color.WHITE);
//        tbrow0.addView(tv3);
//        stk.addView(tbrow0);
//        for (int i = 0; i < slNoArray.length; i++) {
//            TableRow tbrow = new TableRow(this);
//            TextView t1v = new TextView(this);
//            t1v.setText("" + i);
//            t1v.setTextColor(Color.WHITE);
//            t1v.setGravity(Gravity.CENTER);
//            tbrow.addView(t1v);
//
////            TextView t2v = new TextView(this);
////            t2v.setText("" + slNoArray[i]);
////            t2v.setTextColor(Color.WHITE);
////            t2v.setGravity(Gravity.CENTER);
////            tbrow.addView(t2v);
//
//            TextView t3v = new TextView(this);
//            t3v.setText("" + trDateArray[i]);
//            t3v.setTextColor(Color.WHITE);
//            t3v.setGravity(Gravity.CENTER);
//            tbrow.addView(t3v);
//            TextView t4v = new TextView(this);
//            t4v.setText("" + cashArray[i]); //i * 15 / 32 * 10
//            t4v.setTextColor(Color.WHITE);
//            t4v.setGravity(Gravity.CENTER);
//            tbrow.addView(t4v);
//            stk.addView(tbrow);
//        }
//    }


//    private void CreateTable() {
//
//        TableLayout header = (TableLayout) rootView.findViewById(R.id.table_header);
//        TableLayout scrollablePart = (TableLayout) rootView.findViewById(R.id.scrollable_part);
//        TableLayout fixedColumn = (TableLayout) rootView.findViewById(R.id.fixed_column);
//        TableRow header_row = new TableRow(getContext());
//
//        TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
//        int[] scrollableColumnWidths = new int[]{20, 20, 20, 20, 20};
//        int fixedRowHeight = 50;
//        int fixedHeaderHeight = 60;
//        loan_table_view.setVisibility(VISIBLE);
//        //header (fixed vertically)
//
//        TextView textView1 = makeTableRowWithText("Particular", fixedColumnWidths[0], fixedHeaderHeight);
//        textView1.setTextColor(Color.BLACK);
//        TextView textView2 = makeTableRowWithText("Current Receipt", fixedColumnWidths[0], fixedHeaderHeight);
//        textView2.setTextColor(Color.BLACK);
//        TextView textView3 = makeTableRowWithText("Total Received", fixedColumnWidths[0], fixedHeaderHeight);
//        textView3.setTextColor(Color.BLACK);
//        TextView textView4 = makeTableRowWithText("Balance", fixedColumnWidths[0], fixedHeaderHeight);
//        textView4.setTextColor(Color.BLACK);
//        TextView textView5 = makeTableRowWithText("Overdue", fixedColumnWidths[0], fixedHeaderHeight);
//        textView5.setTextColor(Color.BLACK);
//        header_row.removeAllViews();
//        header_row.setLayoutParams(wrapWrapTableRowParams);
//        header_row.setGravity(Gravity.CENTER_VERTICAL);
//        header_row.setGravity(Gravity.CENTER);
//        header_row.addView(textView1);
//        header_row.addView(textView2);
//        header_row.addView(textView3);
//        header_row.addView(textView4);
//        header_row.addView(textView5);
//
//      /*  header_row.addView(makeTableRowWithText("Total Received", fixedColumnWidths[2], fixedHeaderHeight));
//        header_row.addView(makeTableRowWithText("Balance", fixedColumnWidths[3], fixedHeaderHeight));
//        header_row.addView(makeTableRowWithText("Overdue", fixedColumnWidths[4], fixedHeaderHeight));*/
//
//        header.removeAllViews();
//        header.setVisibility(GONE);
//        header.setVisibility(VISIBLE);
//        header.addView(header_row);
//        //header (fixed horizontally)
//
//        int length = particularArray.length;
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.setMargins(2, 5, 2, 5);
//        fixedColumn.removeAllViews();
//        scrollablePart.removeAllViews();
//
//        for (int i = 0; i < length; i++) {
//            TextView fixedView = makeTableRowWithText(particularArray[i], scrollableColumnWidths[0], fixedRowHeight);
//            fixedView.setTextSize(18);
//            fixedView.setGravity(Gravity.CENTER);
//            fixedView.setBackgroundResource(R.drawable.cell_shape);
//            fixedView.setLayoutParams(params);
//            fixedView.setTextColor(Color.WHITE);
//
//            fixedColumn.setVisibility(GONE);
//            fixedColumn.setVisibility(VISIBLE);
//            fixedColumn.addView(fixedView);
//
//            TableRow row = new TableRow(getContext());
//            row.setVisibility(GONE);
//            row.setVisibility(VISIBLE);
//            row.setLayoutParams(wrapWrapTableRowParams);
//            row.setGravity(Gravity.CENTER_VERTICAL);
//            row.setGravity(Gravity.CENTER);
//            row.setBackgroundColor(Color.WHITE);
//            TextView receivableTextView = makeTableRowWithText(String.valueOf(StrCurrentRecpt[i]), scrollableColumnWidths[1], fixedRowHeight);
//            receivableTextView.setPadding(5, 0, 0, 0);
//            receivableTextView.setBackgroundResource(R.drawable.cell_shape_column);
//
//            TextView total_textView = makeTableRowWithText(StrTotalRecvd[i], scrollableColumnWidths[2], fixedRowHeight);
//            total_textView.setBackgroundResource(R.drawable.cell_shape_column);
//
//            TextView balance_textView = makeTableRowWithText(StrBalance[i], scrollableColumnWidths[3], fixedRowHeight);
//            balance_textView.setBackgroundResource(R.drawable.cell_shape_column);
//
//            TextView textView = makeTableRowWithText(StrOverdue[i], scrollableColumnWidths[4], fixedRowHeight);
//            textView.setBackgroundResource(R.drawable.cell_shape_column);
//
//            textView.setTextColor(Color.RED);
//            row.addView(receivableTextView);
//            row.addView(total_textView);
//            row.addView(balance_textView);
//            row.addView(textView);
//
//            scrollablePart.setVisibility(GONE);
//            scrollablePart.setVisibility(VISIBLE);
//            scrollablePart.addView(row);
//        }
//    }


//    public void createTable() {
//        alert_fixedColumn = (TableLayout) findViewById(R.id.receipt_fixed_column);
//        //alert_scrollablePart = (TableLayout)findViewById(R.id.receipt_scrollable_part);
//        alert_header = (TableLayout) findViewById(R.id.receipt_table_header);
//        TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
//        int[] scrollableColumnWidths = new int[]{20, 20, 20, 20, 20};
//        int fixedRowHeight = 50;
//        int fixedHeaderHeight = 60;
//
//        int length = receiptArray.length;
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.setMargins(2, 5, 2, 5);
//        for (int i = 0; i < length; i++) {
//            fixedView = makeTableRowWithText(receiptArray[i], 60, fixedRowHeight);
//            fixedView.setTextSize(24);
//            fixedView.setGravity(Gravity.CENTER_VERTICAL);
//            fixedView.setLayoutParams(params);
//            fixedView.setTextColor(Color.BLACK);
//            alert_fixedColumn.addView(fixedView);
//            /*row = new TableRow(getBaseContext());
//            row.setLayoutParams(wrapWrapTableRowParams);
//            row.setGravity(Gravity.CENTER_VERTICAL);
//            row.setGravity(Gravity.CENTER);
//            row.setBackgroundColor(Color.WHITE);
//            textViewAmounts =makeTableRowWithText(" : "+String.valueOf(b[i]),scrollableColumnWidths[4], fixedRowHeight);
//            textViewAmounts.setPadding(5,0,0,0);
//            textViewAmounts.setTextColor(Color.RED);
//            row.addView(textViewAmounts);
//            alert_scrollablePart.addView(row);*/
//        }
//    }


}
