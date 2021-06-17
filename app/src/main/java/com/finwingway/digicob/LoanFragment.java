package com.finwingway.digicob;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.finwingway.digicob.login.ip_global;

public class LoanFragment extends Fragment {

   /* String [] particular={"Principal Amount","Interest","Penal Interest","Other Charges"};
    int [] total_receivable={800000,289689,50002,200};
    int [] total_received={265043,273494,4021,0};
    int [] balance={534957,16195,981,200};
    int [] overdue={188290,16195,981,200};
    int [] current_receipt={};*/

    private TableLayout alert_fixedColumn, alert_header;
    private TableLayout alert_scrollablePart;
    private TableRow row;
    private TextView fixedView;
    private TextView textViewAmounts;
    private TextView textViewColon;

    private Button acc_submit_btn, remitance_submit_btn;
    private EditText loan_no_editText, remitance_amnt_edit_text;
    private ImageButton loan_no_search_img_btn, cancel_btn;
    private TextView mem_no_textView, cus_no_textView, name_textView, date_of_loan_textView, interest_textView,
            loan_amount_textView, loan_no_textView, loan_type_textView, period_textView, txt_totalBalance;
    private LinearLayout loan_deatils_linear, loan_remitance_linear, loan_table_view;
    private String remitance_amnt, loan_acc_no;
    LayoutInflater finalInflater;
    //    int[] b;
    SweetAlertDialog progressDialog;
    JSONParser jsonParser = new JSONParser();
    private String smem_no, scust_no, sname, sloan_no, sloan_date, sinterest_rate, sloan_amnt, sloan_type, sloan_period, sloan_Status;
    public String[] particularArray, receiptArray,
            StrTotalRecvd, StrBalance, StrOverdue, StrCurrentRecpt,
            ParticularArray, amountArray;

    //    public int[] intBalance;
    //    public int[] receivableArray, receivedArray, balanceArray, overdueArray;
    private String error = "error", StrMsg, StrAccNo, StrMsgEr, StrDate, StrTime;
    RelativeLayout loan_main_relative;
    View rootView;
    View viewBar;
    double d_amnt = 0;

    //    @SuppressLint("RestrictedApi")
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan, container, false);
        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        finalInflater = getLayoutInflater(null);

        acc_submit_btn = (Button) rootView.findViewById(R.id.loan_no_submit_btn);
        remitance_submit_btn = (Button) rootView.findViewById(R.id.loan_remitance_amnt_submit_btn);

        loan_no_editText = (EditText) rootView.findViewById(R.id.loan_no_edit_text);
        remitance_amnt_edit_text = (EditText) rootView.findViewById(R.id.loan_remitance_amount_editText);

        loan_no_search_img_btn = (ImageButton) rootView.findViewById(R.id.loan_no_search_img_btn);
        cancel_btn = (ImageButton) rootView.findViewById(R.id.loan_CANCELbtn);

        mem_no_textView = (TextView) rootView.findViewById(R.id.loan_member_no_textview);
        cus_no_textView = (TextView) rootView.findViewById(R.id.loan_cus_no_textview);
        name_textView = (TextView) rootView.findViewById(R.id.loan_name_textview);
        date_of_loan_textView = (TextView) rootView.findViewById(R.id.loan_date_textview);
        interest_textView = (TextView) rootView.findViewById(R.id.loan_interest_textview);
        loan_amount_textView = (TextView) rootView.findViewById(R.id.loan_amount_textview);
        loan_no_textView = (TextView) rootView.findViewById(R.id.loan_loan_no_textview);
        loan_type_textView = (TextView) rootView.findViewById(R.id.loan_type_textview);
        period_textView = (TextView) rootView.findViewById(R.id.loan_period_textview);

        txt_totalBalance = (TextView) rootView.findViewById(R.id.loan_total_balance);
        viewBar = (View) rootView.findViewById(R.id.view_bar);

        loan_deatils_linear = (LinearLayout) rootView.findViewById(R.id.linear_search_deposit);
        loan_remitance_linear = (LinearLayout) rootView.findViewById(R.id.linear_amount_deposit);
        loan_table_view = (LinearLayout) rootView.findViewById(R.id.loan_table_view);
        loan_table_view.setVisibility(GONE);
        loan_deatils_linear.setVisibility(View.GONE);
        loan_remitance_linear.setVisibility(View.GONE);

        acc_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loan_acc_no = loan_no_editText.getText().toString();
                new getLoanAccountHolder().execute();
                /*   setCustomerDeatils();
                 */
            }
        });

        loan_no_search_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("LN_schm", "");
                Intent debit_intent = new Intent(getActivity(), Loan_Number_Search.class);
                startActivityForResult(debit_intent, 1043);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });


        remitance_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remitance_amnt = remitance_amnt_edit_text.getText().toString();
                if (TextUtils.isEmpty(remitance_amnt)) {
                    Toast.makeText(getContext(), "Enter Remittance Amount.", Toast.LENGTH_SHORT).show();
                    return;
                }
                double balance_amt = Double.parseDouble(remitance_amnt);

                if (balance_amt <= 0) {
                    Toast.makeText(getContext(), "Amount must be higher than zero.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (balance_amt > d_amnt) {
                    Toast.makeText(getContext(), "Entered Amount is higher than remittance amount.", Toast.LENGTH_SHORT).show();
                } else if (d_amnt == balance_amt) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Do you want to close this loan.?")
                            .setContentText("if you want to close the loan move to Loan closing window")
                            .setCancelText("NO")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("LN_ACC", loan_no_editText.getText().toString());
                                    Fragment newFragment = new LoanFragmentClosing();
                                    newFragment.setArguments(bundle);
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    // Replace whatever is in the fragment_container view with this fragment,
                                    // and add the transaction to the back stack
                                    transaction.replace(R.id.content_frame, newFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            }).show();
                } else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View dialogView = finalInflater.inflate(R.layout.alert_loan_deposit_layout, null);

                    alert_fixedColumn = (TableLayout) dialogView.findViewById(R.id.alert_fixed_column);
                    alert_scrollablePart = (TableLayout) dialogView.findViewById(R.id.alert_scrollable_part);
                    alert_header = (TableLayout) dialogView.findViewById(R.id.alert_table_header);

                    createTable();
                    TextView amount = (TextView) dialogView.findViewById(R.id.remitance_amount_alert_textView);
                    amount.setText(remitance_amnt_edit_text.getText().toString());
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setNegativeButton("CANCEL", null);
                    dialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            if (connectionStatus) {
                            new submitLoan().execute();
//                            } else {
//                                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                                        .setTitleText("BT Device Not Connected")
//                                        .setContentText("please connect the device")
//                                        .setCancelText("NO")
//                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                            @Override
//                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                                startActivity(new Intent(getContext(), Act_Main.class));
//                                                sweetAlertDialog.dismiss();
//                                            }
//                                        }).show();
//                            }
                        }
                    });
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        return rootView;
    }

    //util method
    //=========================================================================================================

    public TextView makeTableRowWithText(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels) {
        TextView recyclableTextView;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(getContext());
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setGravity(Gravity.CENTER);
        recyclableTextView.setTextSize(20);
        recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setHeight(fixedHeightInPixels);
        return recyclableTextView;
    }

    public TextView makeTableRowWithColon(String text, int widthInPercentOfScreenWidth, int fixedHeightInPixels) {
        TextView recyclableTextView;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        recyclableTextView = new TextView(getContext());
        recyclableTextView.setText(text);
        recyclableTextView.setTextColor(Color.BLACK);
        recyclableTextView.setGravity(Gravity.CENTER | Gravity.LEFT);
        recyclableTextView.setTextSize(20);
        recyclableTextView.setWidth(widthInPercentOfScreenWidth * screenWidth / 100);
        recyclableTextView.setHeight(fixedHeightInPixels);
        return recyclableTextView;
    }

    public void createTable() {
        TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
        int[] scrollableColumnWidths = new int[]{20, 20, 20, 20, 20};
        int[] scrollableColumnWidthsColon = new int[]{5, 5, 5, 5, 5};
        int fixedRowHeight = 50;
        int fixedHeaderHeight = 60;

        int length = particularArray.length;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 5, 2, 5);
        for (int i = 0; i < length; i++) {
            fixedView = makeTableRowWithText(particularArray[i], 35, fixedRowHeight);
            fixedView.setTextSize(18);
            fixedView.setGravity(Gravity.CENTER_VERTICAL);
            fixedView.setLayoutParams(params);
            fixedView.setTextColor(Color.BLACK);
            alert_fixedColumn.addView(fixedView);
            row = new TableRow(getContext());
            row.setLayoutParams(wrapWrapTableRowParams);
//            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);

            textViewColon = makeTableRowWithColon(":", scrollableColumnWidthsColon[4], fixedRowHeight);
            textViewColon.setPadding(0, 0, 0, 0);
            textViewColon.setTextColor(Color.BLACK);

            textViewAmounts = makeTableRowWithColon(String.valueOf(StrBalance[i]), scrollableColumnWidths[4], fixedRowHeight);
            textViewAmounts.setPadding(5, 0, 0, 0);
            textViewAmounts.setTextColor(Color.RED);
            row.addView(textViewColon);
            row.addView(textViewAmounts);
            alert_scrollablePart.addView(row);
        }
    }

    //=========================================================================================================

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1043 && resultCode == RESULT_OK) {
            try {
                viewBar.setVisibility(GONE);
                loan_deatils_linear.setVisibility(GONE);
                loan_table_view.setVisibility(GONE);
                loan_remitance_linear.setVisibility(GONE);
                loan_no_editText.setText(null);
                remitance_amnt_edit_text.setText(null);

                String result_acc_no = data.getStringExtra("result_loan_acc_no");
                loan_no_editText.setText(result_acc_no);
                loan_acc_no = result_acc_no;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 1043 && resultCode == RESULT_CANCELED) {
            viewBar.setVisibility(GONE);
            loan_deatils_linear.setVisibility(GONE);
            loan_table_view.setVisibility(GONE);
            loan_remitance_linear.setVisibility(GONE);
            loan_no_editText.setText(null);
            remitance_amnt_edit_text.setText(null);
        }
        if (requestCode == 1005 && resultCode == RESULT_CANCELED) {
            viewBar.setVisibility(GONE);
            loan_deatils_linear.setVisibility(GONE);
            loan_table_view.setVisibility(GONE);
            loan_remitance_linear.setVisibility(GONE);
            loan_no_editText.setText(null);
            remitance_amnt_edit_text.setText(null);
        }
    }

    //=========================================================================================================
    //==============================================================================================

    @SuppressLint("StaticFieldLeak")
    private class getLoanAccountHolder extends AsyncTask<String, String, String> {
        private String api_url = ip_global + "/getLoanAccountHolder";

        @Override
        protected void onPreExecute() {
            if (loan_acc_no.equals("")) {
                loan_acc_no = loan_no_editText.getText().toString();
            } else {
            }
            progressDialog.setTitleText("Loading");
            progressDialog.setContentText("please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", loan_acc_no);
            hashMap.put("demandDate", "2019-05-28T06:11:16.381Z");
            hashMap.put("flag", "");
            hashMap.put("branch_id", login.branch_id);
            hashMap.put("sch_code", "");
//            hashMap.put("agent", login.agent_code);
            try {
                JSONObject jobj = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("getLoanAccountHolder = ", String.valueOf(jobj));
                if (jobj.has("account")) {
                    JSONObject jobj1 = jobj.getJSONObject("account");
                    if (jobj1.has("error")) {
                        StrMsgEr = jobj1.getJSONObject("error").getString("msg");
                        error = "invalid";
                    } else if (jobj1.has("Closed")) {
                        StrMsgEr = jobj1.getJSONObject("Closed").getString("msg");
                        error = "closed";
                    }
                } else {
//                "ACNO"
                    smem_no = jobj.getString("Member_No");
                    scust_no = jobj.getString("CUST_NO");
                    sname = jobj.getString("Name");
                    sloan_no = jobj.getString("LoanNo");
                    sloan_date = jobj.getString("LoanDate");
                    sinterest_rate = jobj.getString("InterestRate");
                    sloan_amnt = jobj.getString("LoanAmount");
                    sloan_type = jobj.getString("LoanType");
                    sloan_period = jobj.getString("LoanPeriod");
                    sloan_Status = jobj.getString("Status");

                    JSONArray recArray = jobj.getJSONArray("Receiptdetails");
                    int arCount = recArray.length();
                    particularArray = new String[arCount];
                    StrTotalRecvd = new String[arCount];
                    StrBalance = new String[arCount];
                    StrOverdue = new String[arCount];
                    StrCurrentRecpt = new String[arCount];
                    for (int j = 0; j < recArray.length(); j++) {
                        JSONObject ingredObject = recArray.getJSONObject(j);
                        particularArray[j] = ingredObject.getString("Particular");
                        StrTotalRecvd[j] = ingredObject.getString("Total Received");
                        StrBalance[j] = ingredObject.getString("Balance");
                        StrOverdue[j] = ingredObject.getString("Overdue");
                        StrCurrentRecpt[j] = ingredObject.getString("Current Receipt");
                    }
                    error = "ok";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return error = "error";
            }
            return error;
        }

        @Override
        protected void onPostExecute(String url) {
            progressDialog.dismiss();
            if (url.equals("error")) {
                Toast.makeText(getContext(), "Some error Occurred!", Toast.LENGTH_SHORT).show();
            } else if (url.equals("closed")) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Already closed!")
                        .setContentText(StrMsgEr)
                        .show();
                viewBar.setVisibility(GONE);
                loan_deatils_linear.setVisibility(GONE);
                loan_deatils_linear.setVisibility(View.GONE);
                loan_remitance_linear.setVisibility(View.GONE);
                loan_table_view.setVisibility(GONE);
            } else if (url.equals("invalid")) {
                viewBar.setVisibility(GONE);
                loan_deatils_linear.setVisibility(GONE);
                loan_deatils_linear.setVisibility(View.GONE);
                loan_remitance_linear.setVisibility(View.GONE);
                loan_table_view.setVisibility(GONE);
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Warning!")
                        .setContentText(StrMsgEr)
                        .show();
//                Toast.makeText(getContext(), StrMsgEr, Toast.LENGTH_SHORT).show();
            } else if (url.equals("ok")) {
                setCustomerDeatils();
                viewBar.setVisibility(VISIBLE);
                loan_deatils_linear.setVisibility(VISIBLE);
                CreateTable();
                loan_deatils_linear.setVisibility(View.VISIBLE);
                loan_remitance_linear.setVisibility(View.VISIBLE);

                double total_amnt = 0;
                for (String blncAmnt : StrBalance) {
                    double amnt = Double.parseDouble(blncAmnt);
                    total_amnt = total_amnt + amnt;
                }
                txt_totalBalance.setText(String.format("%.2f", total_amnt));
                d_amnt = total_amnt;

                if (sloan_Status.equals("C")) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Already closed!")
                            .setContentText("This Loan Acc was already closed!")//pending for authorisation
                            .show();
                }
            }
        }
    }

    public void setCustomerDeatils() {
        mem_no_textView.setText(smem_no);
        cus_no_textView.setText(scust_no);
        name_textView.setText(sname);
        date_of_loan_textView.setText(sloan_date);
        interest_textView.setText(sinterest_rate);
        loan_amount_textView.setText(sloan_amnt);
        loan_no_textView.setText(sloan_no);
        loan_type_textView.setText(sloan_type);
        period_textView.setText(sloan_period);
    }

    private void CreateTable() {

        TableLayout header = (TableLayout) rootView.findViewById(R.id.table_header);
        TableLayout scrollablePart = (TableLayout) rootView.findViewById(R.id.scrollable_part);
        TableLayout fixedColumn = (TableLayout) rootView.findViewById(R.id.fixed_column);
        TableRow header_row = new TableRow(getContext());

        TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int[] fixedColumnWidths = new int[]{40, 20, 20, 20, 20};
        int[] scrollableColumnWidths = new int[]{40, 40, 40, 40, 40};
//        int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
//        int[] scrollableColumnWidths = new int[]{20, 20, 20, 20, 20};
        int fixedRowHeight = 50;
        int fixedHeaderHeight = 120;//60;
        loan_table_view.setVisibility(VISIBLE);
        //header (fixed vertically)

        TextView textView1 = makeTableRowWithText("Particular", fixedColumnWidths[0], fixedHeaderHeight);
        textView1.setTextColor(Color.BLACK);
        TextView textView2 = makeTableRowWithText("Current \nReceipt", fixedColumnWidths[0], fixedHeaderHeight);
        textView2.setTextColor(Color.BLACK);
        TextView textView3 = makeTableRowWithText("Total \nReceived", fixedColumnWidths[0], fixedHeaderHeight);
        textView3.setTextColor(Color.BLACK);
        TextView textView4 = makeTableRowWithText("Balance", fixedColumnWidths[0], fixedHeaderHeight);
        textView4.setTextColor(Color.BLACK);
        TextView textView5 = makeTableRowWithText("Overdue", fixedColumnWidths[0], fixedHeaderHeight);
        textView5.setTextColor(Color.BLACK);
        header_row.removeAllViews();
        header_row.setLayoutParams(wrapWrapTableRowParams);
        header_row.setGravity(Gravity.CENTER_VERTICAL);
        header_row.setGravity(Gravity.CENTER);
        header_row.addView(textView1);
        header_row.addView(textView2);
        header_row.addView(textView3);
        header_row.addView(textView4);
        header_row.addView(textView5);

      /*  header_row.addView(makeTableRowWithText("Total Received", fixedColumnWidths[2], fixedHeaderHeight));
        header_row.addView(makeTableRowWithText("Balance", fixedColumnWidths[3], fixedHeaderHeight));
        header_row.addView(makeTableRowWithText("Overdue", fixedColumnWidths[4], fixedHeaderHeight));*/

        header.removeAllViews();
        header.setVisibility(GONE);
        header.setVisibility(VISIBLE);
        header.addView(header_row);
        //header (fixed horizontally)

        int length = particularArray.length;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 5, 2, 5);
        fixedColumn.removeAllViews();
        scrollablePart.removeAllViews();

        for (int i = 0; i < length; i++) {
            TextView fixedView = makeTableRowWithText(particularArray[i], scrollableColumnWidths[0], fixedRowHeight);
            fixedView.setTextSize(18);
            fixedView.setGravity(Gravity.CENTER);
            fixedView.setBackgroundResource(R.drawable.cell_shape);
            fixedView.setLayoutParams(params);
            fixedView.setTextColor(Color.WHITE);

            fixedColumn.setVisibility(GONE);
            fixedColumn.setVisibility(VISIBLE);
            fixedColumn.addView(fixedView);

            TableRow row = new TableRow(getContext());
            row.setVisibility(GONE);
            row.setVisibility(VISIBLE);
            row.setLayoutParams(wrapWrapTableRowParams);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);
            TextView receivableTextView = makeTableRowWithText(String.valueOf(StrCurrentRecpt[i]), scrollableColumnWidths[1], fixedRowHeight);
            receivableTextView.setPadding(5, 0, 0, 0);
            receivableTextView.setBackgroundResource(R.drawable.cell_shape_column);

            TextView total_textView = makeTableRowWithText(StrTotalRecvd[i], scrollableColumnWidths[2], fixedRowHeight);
            total_textView.setBackgroundResource(R.drawable.cell_shape_column);

            TextView balance_textView = makeTableRowWithText(StrBalance[i], scrollableColumnWidths[3], fixedRowHeight);
            balance_textView.setBackgroundResource(R.drawable.cell_shape_column);

            TextView textView = makeTableRowWithText(StrOverdue[i], scrollableColumnWidths[4], fixedRowHeight);
            textView.setBackgroundResource(R.drawable.cell_shape_column);

            textView.setTextColor(Color.RED);
            row.addView(receivableTextView);
            row.addView(total_textView);
            row.addView(balance_textView);
            row.addView(textView);

            scrollablePart.setVisibility(GONE);
            scrollablePart.setVisibility(VISIBLE);
            scrollablePart.addView(row);
        }
    }

    //===============================

    @SuppressLint("StaticFieldLeak")
    private class submitLoan extends AsyncTask<String, String, String> {
        private String api_url = ip_global + "/LoanReceiptCash";

        @Override
        protected void onPreExecute() {
            progressDialog.setTitleText("Loading");
            progressDialog.setContentText("please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", loan_acc_no);
            hashMap.put("deposit_amount", remitance_amnt);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("submitLoan: ", String.valueOf(data));
                JSONObject jobj = data.getJSONObject("receipt");
                if (jobj.has("data")) {
//                    StrMsg = jobj.getJSONObject("data").getString("msg");
                    JSONObject jobj1 = jobj.getJSONObject("data");
                    StrMsg = jobj1.getString("msg");
                    StrAccNo = jobj1.getString("AccNo");
                    StrDate = jobj1.getString("Date");
                    StrTime = jobj1.getString("Time");

                    JSONArray JArray = jobj.getJSONObject("data").getJSONArray("dat");
                    int cnt = JArray.length();
                    ParticularArray = new String[cnt];
                    amountArray = new String[cnt];
                    for (int i = 0; i < cnt; i++) {
                        ParticularArray[i] = JArray.getJSONObject(i).getString("Particular");
                        amountArray[i] = JArray.getJSONObject(i).getString("Current Receipt");
                    }

                    error = "ok";
                } else if (jobj.has("error")) {
                    StrMsgEr = jobj.getJSONObject("error").getString("msg");
                    error = "invalid";
                } else if (jobj.has("Closed")) {
                    StrMsgEr = jobj.getJSONObject("Closed").getString("msg");
                    error = "Closed";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return error = "error";
            }
            return error;
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result.equals("error")) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Some error occurred!")
                        .show();
            } else if (result.equals("Closed")) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Already closed!")
                        .setContentText(StrMsgEr)
                        .show();
            }
            if (result.equals("ok")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Payment Successful")
                        .setContentText("Your payment was successful")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(getActivity(), Receipt_Loan.class);
                                intent.putExtra("member_no", smem_no);
                                intent.putExtra("customer_no", sloan_no);
                                intent.putExtra("name", sname);
                                intent.putExtra("TrnsDate", StrDate);
                                intent.putExtra("TrnsTime", StrTime);
                                intent.putExtra("loan_acc", StrAccNo);
                                intent.putExtra("amount", remitance_amnt);
                                intent.putExtra("ParticularArray", ParticularArray);
                                intent.putExtra("amountArray", amountArray);
                                startActivityForResult(intent, 1005);

                                sweetAlertDialog.dismiss();
                            }
                        }).show();

            }
        }
    }

    //==============================================================================================
    //=========================================================================================================

}
