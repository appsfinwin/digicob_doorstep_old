package com.finwingway.digicob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.finwingway.digicob.login.ip_global;

public class LoanFragmentClosing extends Fragment implements DatePickerDialog.OnDateSetListener {

    static String StrEffctvDate = null, StrClsngDate = null;
    public String[] particularArray, receiptArray,
            StrTotalRecvd, StrBalance, StrOverdue, StrCurrentRecpt;
    View rootView;
    Button btnEffctvDate, btnClsngDate, btnClosingSbmt, btnLonNoSbmt;
    DatePickerDialog dpd, dpd1;
    SweetAlertDialog progressDialog, proDialog, errorDialog;
    RelativeLayout reltvLoanclsng;
    JSONParser jsonParser = new JSONParser();
    String[] SchCodeArray, SchNameArray;
    EditText edtLoanAccno;
    Spinner spinnerScheme;
    TextView txtTotalBalance, Txt_member_no, Txt_cus_no, Txt_name,
            Txt_loan_date, Txt_interest, Txt_amount,
            Txt_loan_no, Txt_loan_type, Txt_period;
    double d_amnt = 0;
    private FragmentActivity myContext;
    private TableRow row;
    private LinearLayout loan_table_view;
    private String StrloanAcc_no, smem_no, scust_no, sname, sloan_no, sloan_date, sinterest_rate, sloan_amnt, sloan_type, sloan_period, sloan_Status,
            error = "error", StrMsgEr,
            SrlonAccNo, StrSchCode,
            StrMsgC, rtnMsgC, responseMsg = "0",
            StrClsAccNo, StrClsTranNo, StrClsVoucherNo, StrDate, StrTime;
    private TextView fixedView;
    private ImageButton cancel_btn, btnSearch;
    private TextView textViewAmounts;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_closing, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        final FragmentManager fragManager = myContext.getFragmentManager();

        try {
            assert getArguments() != null;
            SrlonAccNo = getArguments().getString("LN_ACC", "");
        } catch (Exception ignored) {
        }

        Txt_member_no = rootView.findViewById(R.id.txt_lncls_member_no);
        Txt_cus_no = rootView.findViewById(R.id.txt_lncls_cus_no);
        Txt_name = rootView.findViewById(R.id.txt_lncls_name);
        Txt_loan_date = rootView.findViewById(R.id.txt_lncls_loan_date);
        Txt_interest = rootView.findViewById(R.id.txt_lncls_interest);
        Txt_amount = rootView.findViewById(R.id.txt_lncls_amount);
        Txt_loan_no = rootView.findViewById(R.id.txt_lncls_loan_no);
        Txt_loan_type = rootView.findViewById(R.id.txt_lncls_loan_type);
        Txt_period = rootView.findViewById(R.id.txt_lncls_period);

        spinnerScheme = rootView.findViewById(R.id.spnr_loan_schm);
        spinnerScheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrSchCode = SchCodeArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        new getDropdownScheme().execute();

        cancel_btn = (ImageButton) rootView.findViewById(R.id.loanclsng_CANCELbtn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        edtLoanAccno = rootView.findViewById(R.id.edt_loan_accno);
        txtTotalBalance = (TextView) rootView.findViewById(R.id.txt_lnclsg_total_blnc);
        reltvLoanclsng = (RelativeLayout) rootView.findViewById(R.id.reltvLyt_loanclsng);
        reltvLoanclsng.setVisibility(GONE);
        loan_table_view = (LinearLayout) rootView.findViewById(R.id.lncls_table_view);
        loan_table_view.setVisibility(GONE);

        btnClsngDate = rootView.findViewById(R.id.btn_clsing_date);
        btnClsngDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                dpd = DatePickerDialog.newInstance(
                        LoanFragmentClosing.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setTitle("SELECT FROM DATE");
                dpd.setThemeDark(true);
                dpd.vibrate(true);
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.show(fragManager, "Datepickerdialog");
            }
        });

        btnEffctvDate = rootView.findViewById(R.id.btn_efctv_date);
        btnEffctvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now1 = Calendar.getInstance();
                dpd1 = DatePickerDialog.newInstance(
                        LoanFragmentClosing.this,
                        now1.get(Calendar.YEAR),
                        now1.get(Calendar.MONTH),
                        now1.get(Calendar.DAY_OF_MONTH)
                );
                dpd1.setTitle("SELECT TO DATE");
                dpd1.setThemeDark(true);
                dpd1.vibrate(true);
                dpd1.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd1.show(fragManager, "Datepickerdialog");
            }
        });

        if (!TextUtils.isEmpty(SrlonAccNo)) {
            edtLoanAccno.setText(SrlonAccNo);
            StrloanAcc_no = SrlonAccNo;
            if (!TextUtils.isEmpty(StrloanAcc_no)) {
                new getLoanAccountHolder().execute();
            } else {
                Toast.makeText(getContext(), "Please enter Account No", Toast.LENGTH_SHORT).show();
            }
        }

        btnLonNoSbmt = rootView.findViewById(R.id.btn_ln_no_sbmt);
        btnLonNoSbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrloanAcc_no = edtLoanAccno.getText().toString();
                if (!TextUtils.isEmpty(StrloanAcc_no)) {
                    new getLoanAccountHolder().execute();
                } else {
                    Toast.makeText(getContext(), "Please enter Account No", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClosingSbmt = rootView.findViewById(R.id.btn_ln_closing);
        btnClosingSbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new submitLoanClosing().execute();
            }
        });

        btnSearch = rootView.findViewById(R.id.loan_cls_search_img_btn);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("LN_schm", StrSchCode);
                Intent debit_intent = new Intent(getActivity(), Loan_Number_Search.class);
                debit_intent.putExtras(bundle);
                startActivityForResult(debit_intent, 1044);
            }
        });

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String CurrentDate = df.format(c.getTime());
        btnEffctvDate.setText(CurrentDate);
        btnClsngDate.setText(CurrentDate);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
        if (view == dpd) {
            StrClsngDate = date;
            btnClsngDate.setText(date);
            btnClsngDate.setTextColor(Color.RED);
            btnClsngDate.setTextSize(18);
        }
        if (view == dpd1) {
            StrEffctvDate = date;
            btnEffctvDate.setText(date);
            btnEffctvDate.setTextColor(Color.RED);
            btnEffctvDate.setTextSize(18);
        }
    }

    //==============================================================================================

    public void setCustomerDeatils() {
        Txt_member_no.setText(smem_no);
        Txt_cus_no.setText(scust_no);
        Txt_name.setText(sname);
        Txt_loan_date.setText(sloan_date);
        Txt_interest.setText(sinterest_rate);
        Txt_amount.setText(sloan_amnt);
        Txt_loan_no.setText(sloan_no);
        Txt_loan_type.setText(sloan_type);
        Txt_period.setText(sloan_period);
    }

    private void CreateTable() {

        TableLayout header = (TableLayout) rootView.findViewById(R.id.lncls_table_header);
        TableLayout scrollablePart = (TableLayout) rootView.findViewById(R.id.lncls_scrollable_part);
        TableLayout fixedColumn = (TableLayout) rootView.findViewById(R.id.lncls_fixed_column);
        TableRow header_row = new TableRow(getContext());

        TableRow.LayoutParams wrapWrapTableRowParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int[] fixedColumnWidths = new int[]{40, 20, 20, 20, 20};
        int[] scrollableColumnWidths = new int[]{40, 40, 40, 40, 40};
//        int[] fixedColumnWidths = new int[]{20, 20, 20, 20, 20};
//        int[] scrollableColumnWidths = new int[]{20, 20, 20, 20, 20};
        int fixedRowHeight = 50;
        int fixedHeaderHeight = 120;//60
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

    //=========================================================================================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1044) {
            try {
                String result_acc_no = data.getStringExtra("result_loan_acc_no");
                edtLoanAccno.setText(result_acc_no);

                reltvLoanclsng.setVisibility(GONE);
                loan_table_view.setVisibility(GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 1044 && resultCode == RESULT_CANCELED) {
            reltvLoanclsng.setVisibility(GONE);
            loan_table_view.setVisibility(GONE);
        }
        if (requestCode == 1055 && resultCode == RESULT_CANCELED) {
            reltvLoanclsng.setVisibility(GONE);
            loan_table_view.setVisibility(GONE);
            edtLoanAccno.setText(null);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getDropdownScheme extends AsyncTask<String, String, String> {
        private String api_url = ip_global + "/getLoanScheme";

        @Override
        public void onPreExecute() {
            proDialog.setTitleText("Please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("", "");
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("getLoanScheme", jsonObject.toString());

                JSONArray SchemeArray = new JSONArray(jsonObject.getString("Scheme"));
                Log.e("Scheme=>", SchemeArray.toString());
                SchCodeArray = new String[SchemeArray.length()];
                SchNameArray = new String[SchemeArray.length()];
                for (int i = 0; i < SchemeArray.length(); i++) {
                    JSONObject SchAryObject = SchemeArray.getJSONObject(i);
                    SchCodeArray[i] = SchAryObject.getString("Sch_Code");
                    SchNameArray[i] = SchAryObject.getString("Sch_Name");
                }
                responseMsg = "1";
            } catch (JSONException e) {
                e.printStackTrace();
                return responseMsg = "0";
            } catch (Exception e) {
                e.printStackTrace();
                return responseMsg = "0";
            }
            return responseMsg;
        }

        @Override
        public void onPostExecute(final String result) {
            proDialog.dismiss();
            switch (result) {
//                case "0":
//                    proDialog.dismiss();
//                    errorDialog.setTitleText("Error!!")
//                            .setContentText(Message)
//                            .show();
//                    break;
                case "0":
                    proDialog.dismiss();
                    errorDialog.setTitleText("Error!!")
                            .setContentText("Server error occurred!")
                            .show();
                    break;
                case "1":
                    if (SchNameArray != null && SchNameArray.length > 0) {
                        ArrayAdapter SchemeAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, SchNameArray);
                        spinnerScheme.setAdapter(SchemeAdapter);
                    }
                    break;
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getLoanAccountHolder extends AsyncTask<String, String, String> {
        private String api_url = ip_global + "getLoanAccountHolder";

        @Override
        protected void onPreExecute() {
//            if (StrloanAcc_no.equals("")) {
//                StrloanAcc_no = edtLoanAccno.getText().toString();
//            } else {
//            }
            progressDialog.setTitleText("Loading");
            progressDialog.setContentText("please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", StrloanAcc_no);
            hashMap.put("demandDate", "2019-05-28T06:11:16.381Z");
            hashMap.put("flag", "");
            hashMap.put("branch_id", login.branch_id);
            hashMap.put("sch_code", StrSchCode);
//            hashMap.put("agent", login.agent_code);
            try {
                JSONObject jobj = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("getLoanAccHolder CLS", String.valueOf(jobj));
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
                error = "error";
                return error;
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
                reltvLoanclsng.setVisibility(GONE);
            } else if (url.equals("invalid")) {
                reltvLoanclsng.setVisibility(GONE);
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Warning!")
                        .setContentText(StrMsgEr)
                        .show();
//                Toast.makeText(getContext(), StrMsgEr, Toast.LENGTH_SHORT).show();
            } else if (url.equals("ok")) {
                setCustomerDeatils();
                reltvLoanclsng.setVisibility(VISIBLE);
                CreateTable();

                double total_amnt = 0;
                for (String blncAmnt : StrBalance) {
                    double amnt = Double.parseDouble(blncAmnt);
                    total_amnt = total_amnt + amnt;
                }
                txtTotalBalance.setText(String.format("%.2f", total_amnt));
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

    @SuppressLint("StaticFieldLeak")
    private class submitLoanClosing extends AsyncTask<String, String, String> {
        HashMap<String, String> hashMap = new HashMap<>();
        private String api_url = ip_global + "LoanClosing";

        @Override
        protected void onPreExecute() {
            progressDialog.setTitleText("Loading");
            progressDialog.setContentText("please wait..");
            progressDialog.show();

            hashMap.put("ClosingDate", btnClsngDate.getText().toString());
            hashMap.put("EffectiveDate", btnEffctvDate.getText().toString());
            hashMap.put("SchemeCode", StrSchCode);
            hashMap.put("AccNo", StrloanAcc_no);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("submitLoanCloasing", String.valueOf(data));
                if (data.has("error")) {
                    StrMsgC = data.getJSONObject("error").getString("msg");
                    rtnMsgC = "0";
                } else if (data.has("closed")) {
                    StrMsgC = data.getJSONObject("closed").getString("msg");
                    rtnMsgC = "closed";
                } else if (data.has("data")) {
//                    StrMsgC = data.getJSONObject("data").getString("msg");
                    JSONObject jsObj = data.getJSONObject("data");
                    StrMsgC = jsObj.getString("msg");
                    StrClsAccNo = jsObj.getString("AccNo");
                    StrClsTranNo = jsObj.getString("TranNo");
                    StrClsVoucherNo = jsObj.getString("VoucherNo");
                    StrDate = jsObj.getString("Date");
                    StrTime = jsObj.getString("Time");

                    rtnMsgC = "ok";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return rtnMsgC = "error";
            }
            return rtnMsgC;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result.equals("0")) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText(StrMsgC)
                        .show();
            } else if (result.equals("error")) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Server error occurred!")
                        .show();
            } else if (result.equals("closed")) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Already Closed")
                        .setContentText(StrMsgC)
                        .show();
            } else if (result.equals("ok")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Payment Successful")
                        .setContentText("Your payment was successful")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                                Intent intent = new Intent(getActivity(), Receipt_Loan_closing.class);
                                intent.putExtra("member_no", smem_no);
                                intent.putExtra("customer_no", sloan_no);
                                intent.putExtra("name", sname);
                                intent.putExtra("loan_acc", StrClsAccNo);
                                intent.putExtra("closing_date", btnClsngDate.getText().toString());
                                intent.putExtra("effective_date", btnEffctvDate.getText().toString());
                                intent.putExtra("voucher_no", StrClsVoucherNo);
                                intent.putExtra("trans_id", StrClsTranNo);
                                intent.putExtra("TrnsDate", StrDate);
                                intent.putExtra("TrnsTime", StrTime);
                                intent.putExtra("amount", txtTotalBalance.getText().toString());
                                intent.putExtra("ParticularArray", particularArray);
                                intent.putExtra("amountArray", StrOverdue);
                                startActivityForResult(intent, 1055);
                            }
                        }).show();

//                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("Success")
//                        .setContentText(StrMsgC)
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.cancel();
////                                StrloanAcc_no = edtLoanAccno.getText().toString();
//                                if (!TextUtils.isEmpty(StrloanAcc_no)) {
//                                    new getLoanAccountHolder().execute();
//                                }
//                            }
//                        }).show();
            }
        }
    }

    //=========================================================================================================


}
