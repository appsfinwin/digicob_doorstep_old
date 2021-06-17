package com.finwingway.digicob.loanModule.JLGwithSplit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.finwingway.digicob.Account_Search;
import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.loanModule.Search_group_created;
import com.finwingway.digicob.login;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ClosingAdapterOne extends Fragment {
    Button next;
    View rootView;
    ViewPager viewPagerClsng = MainClosingLoan.viewPagerClsng;
    public static String ip = login.ip_global;
    JSONParser jsonParser;
    RequestQueue requestQueue;
    Spinner spinerTransType, spinerSubTransType;
    SweetAlertDialog proDialog, errorDialog;

    EditText EdtAccnumber, EdtSelectGrp;
    TextView //tvSelectGrp,//tvEffectDate, tvDate, ;
            Tv_acc_no, Tv_branch, Tv_loan_type, Tv_scheme, Tv_loan_amount,
            Tv_loan_date, Tv_due_date, Tv_roi;
    Button btnEffectDate, btnDate, btnSearchGrp;
    ImageButton btnGroup, btnSearchAcc;
    String responseMsg, Message;

    public static int datCount;
    public static String StrTransType, StrTransTypeCode, StrSubTransType, StrEffectDate, StrDate, StrSelectGrp,
            StrAccountNo, StrType, StrBranch, StrScheme, StrSch_Code,
            StrLn_LoanAmount, StrLn_LoanDate, StrLn_DueDate, StrLn_IntRate;

    public static String[] arrayTransTypeCode = {"null"}, arrayTransTypeName = {"null"}, arraySubTransCode = {"null"},
            arraySubTransName = {"null"}, arrayInstrmntCode = {"null"}, arrayInstrmntName = {"null"},
            StrAccountNumberAry, StrCustomerIDAry, StrCustomerNameAry, StrPrincipalBalanceAry, StrPrincipalDueAry,
            StrInterestAry, StrPenalInterestAry, StrTotalInterestAry, StrRemittanceAry, StrIsClosingAry;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_jlg_closing_one, container, false);
        jsonParser = new JSONParser();
        requestQueue = Volley.newRequestQueue(getContext());
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);

        EdtAccnumber = rootView.findViewById(R.id.edt_clsng_accno);
        btnEffectDate = rootView.findViewById(R.id.btn_clsng_effectdate);
        btnDate = rootView.findViewById(R.id.btn_clsng_date);
        EdtSelectGrp = rootView.findViewById(R.id.edt_clsng_select_grp);

        Tv_acc_no = rootView.findViewById(R.id.tv_acc_no_cls);
        Tv_branch = rootView.findViewById(R.id.tv_branch_cls);
        Tv_loan_type = rootView.findViewById(R.id.tv_loan_type_cls);
        Tv_scheme = rootView.findViewById(R.id.tv_scheme_cls);
        Tv_loan_amount = rootView.findViewById(R.id.tv_loan_amount_cls);
        Tv_loan_date = rootView.findViewById(R.id.tv_loan_date_cls);
        Tv_due_date = rootView.findViewById(R.id.tv_due_date_cls);
        Tv_roi = rootView.findViewById(R.id.tv_roi_cls);


        btnSearchGrp = rootView.findViewById(R.id.btn_sbmt_clsng);
        btnSearchGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String grpAccNo = EdtSelectGrp.getText().toString();
                if (!TextUtils.isEmpty(grpAccNo)) {
                    new getGroupDetails().execute(grpAccNo);
                } else {
                    Toast.makeText(getContext(), "Please enter a Group Account No", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSearchAcc = rootView.findViewById(R.id.btn_search_clsg_accno);
        btnSearchAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent debit_intent = new Intent(getActivity(), Account_Search.class);
                startActivityForResult(debit_intent, 1045);
            }
        });

        btnGroup = rootView.findViewById(R.id.btn_clsng_select_grp);
        btnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent debit_intent = new Intent(getActivity(), Search_group_created.class);
                startActivityForResult(debit_intent, 1015);
            }
        });

        spinerTransType = rootView.findViewById(R.id.spinner_trans_type_clsng);
        spinerTransType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrTransType = arrayTransTypeName[position];
                StrTransTypeCode = arrayTransTypeCode[position];
                TransactionMode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinerSubTransType = rootView.findViewById(R.id.spinner_sub_trans_type_clsng);
        spinerSubTransType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrSubTransType = arraySubTransCode[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        next = rootView.findViewById(R.id.btn_clsng_nxt);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

        new getDropdownList().execute();

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String CurrentDate = df.format(c.getTime());
        btnEffectDate.setText(CurrentDate);
        btnDate.setText(CurrentDate);

        return rootView;
    }


    public void TransactionMode() {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (StrTransType.equals("Transfer")) {
            EdtAccnumber.setFocusableInTouchMode(true);
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                EdtAccnumber.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_style));
                btnSearchAcc.setVisibility(View.VISIBLE);
            } else {
                EdtAccnumber.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_style));
                btnSearchAcc.setVisibility(View.VISIBLE);
            }
        } else {
            EdtAccnumber.setFocusable(false);
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                EdtAccnumber.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_style_disble));
                btnSearchAcc.setVisibility(View.GONE);
            } else {
                EdtAccnumber.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_style_disble));
                btnSearchAcc.setVisibility(View.GONE);
            }
        }
    }

    public void setData() {
        StrSelectGrp = EdtSelectGrp.getText().toString();
        if (!TextUtils.isEmpty(StrSelectGrp)) {
            try {
                String StrAccno = EdtAccnumber.getText().toString();
                if (!TextUtils.isEmpty(StrAccno)) {
                    ClosingAdapterGetSet.setTransAccountNo(StrAccno);
                }
            } catch (Exception ignored) {
            }

            ClosingAdapterGetSet.setTransType(StrTransTypeCode);
            ClosingAdapterGetSet.setEffctDate(btnEffectDate.getText().toString());
            ClosingAdapterGetSet.setDate(btnDate.getText().toString());
            ClosingAdapterGetSet.setSubTransType(StrSubTransType);

            viewPagerClsng.setCurrentItem(1, true);
        } else {
            Toast.makeText(getContext(), "Please select a Group Account..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1015) {
                if (resultCode == RESULT_OK) {
                    String resultGrpName = data.getStringExtra("result_Accno");
                    EdtSelectGrp.setText(resultGrpName);
                    new getGroupDetails().execute(resultGrpName);
                } else if (resultCode == RESULT_CANCELED) {
                    EdtSelectGrp.setText(null);
                }
            }
            if (requestCode == 1045) {
                if (resultCode == RESULT_OK) {
                    String resultGrpName = data.getStringExtra("result_acc_no");
                    EdtAccnumber.setText(resultGrpName);
                } else if (resultCode == RESULT_CANCELED) {
                    EdtAccnumber.setText(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getDropdownList extends AsyncTask<String, String, String> {
        private String api_url = ip + "/RefCodeMaster";

        @Override
        public void onPreExecute() {
            proDialog.setTitleText("Please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Brcode", "");
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "GET", hashMap);
                Log.e("getDropdownList", jsonObject.toString());
                responseMsg = jsonObject.getString("status");
                if (responseMsg.equals("1")) {
                    //-----------------Trans type
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("Mode"));
                    Log.e("Trans Mode=>", jsonArray.toString());
                    arrayTransTypeCode = new String[jsonArray.length()];
                    arrayTransTypeName = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arrayObject = jsonArray.getJSONObject(i);
                        arrayTransTypeCode[i] = arrayObject.getString("Code");
                        arrayTransTypeName[i] = arrayObject.getString("Name");
                    }
                    //-----------------Sub Trans Sector
                    JSONArray sectorArray = new JSONArray(jsonObject.getString("SubTranType"));
                    Log.e("SubTranType=>", sectorArray.toString());
                    arraySubTransCode = new String[sectorArray.length()];
                    arraySubTransName = new String[sectorArray.length()];
                    for (int i = 0; i < sectorArray.length(); i++) {
                        JSONObject secArrayObject = sectorArray.getJSONObject(i);
                        arraySubTransCode[i] = secArrayObject.getString("Code");
                        arraySubTransName[i] = secArrayObject.getString("Name");
                    }
                    //-----------------InstrumentType Sector
                    JSONArray InstrmntTypeArray = new JSONArray(jsonObject.getString("InstrumentType"));
                    Log.e("SubTranType=>", InstrmntTypeArray.toString());
                    arrayInstrmntCode = new String[InstrmntTypeArray.length()];
                    arrayInstrmntName = new String[InstrmntTypeArray.length()];
                    for (int i = 0; i < InstrmntTypeArray.length(); i++) {
                        JSONObject secArrayObject = InstrmntTypeArray.getJSONObject(i);
                        arrayInstrmntCode[i] = secArrayObject.getString("Code");
                        arrayInstrmntName[i] = secArrayObject.getString("Name");
                    }
                } else if (responseMsg.equals("0")) {
                    Message = jsonObject.getString("msg");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseMsg;
        }

        @Override
        public void onPostExecute(final String result) {
            proDialog.dismiss();
            switch (result) {
                case "0":
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Error!!")
                            .setContentText(Message)
                            .show();
                    break;
                case "exception":
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!!")
                            .setContentText("Something went wrong!")
                            .show();
                    break;
                case "1":
                    ArrayAdapter brachAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayTransTypeName);
                    spinerTransType.setAdapter(brachAdapter);

                    ArrayAdapter brachAdapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arraySubTransName);
                    spinerSubTransType.setEnabled(false);
                    spinerSubTransType.setClickable(false);
                    spinerSubTransType.setAdapter(brachAdapter2);

                    try {
                        int spinnerPosition = brachAdapter2.getPosition("Cr");
                        spinerSubTransType.setSelection(spinnerPosition);
                    } catch (Exception ignore) {
                    }

                    break;
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class getGroupDetails extends AsyncTask<String, String, String> {
        private String api_url = ip + "/JLGTransGroupSelect";

        @Override
        public void onPreExecute() {
            proDialog.setTitle("Please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Accno", params[0]);
            hashMap.put("SubTranType", StrSubTransType);
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                responseMsg = jsonObject.getString("status");
                Log.e("Closing GroupSelect", jsonObject.toString());
                if (responseMsg.equals("1")) {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    JSONObject arrayObject = jsonArray.getJSONObject(0);
                    StrAccountNo = arrayObject.getString("Account No");
                    StrType = arrayObject.getString("Type");
                    StrBranch = arrayObject.getString("Branch");
                    StrScheme = arrayObject.getString("Scheme");
                    StrSch_Code = arrayObject.getString("Sch_Code");
                    StrLn_LoanAmount = arrayObject.getString("Ln_LoanAmount");
                    StrLn_LoanDate = arrayObject.getString("Ln_LoanDate");
                    StrLn_DueDate = arrayObject.getString("Ln_DueDate");
                    StrLn_IntRate = arrayObject.getString("Ln_IntRate");

                    JSONArray jsonArray1 = new JSONArray(jsonObject.getString("dat"));
                    datCount = jsonArray1.length();
                    StrAccountNumberAry = new String[datCount];
                    StrCustomerIDAry = new String[datCount];
                    StrCustomerNameAry = new String[datCount];
                    StrPrincipalBalanceAry = new String[datCount];
                    StrPrincipalDueAry = new String[datCount];
                    StrInterestAry = new String[datCount];
                    StrPenalInterestAry = new String[datCount];
                    StrTotalInterestAry = new String[datCount];
                    StrRemittanceAry = new String[datCount];
                    StrIsClosingAry = new String[datCount];

                    for (int i = 0; i < datCount; i++) {
                        JSONObject arrayObject1 = jsonArray1.getJSONObject(i);
                        StrAccountNumberAry[i] = arrayObject1.getString("Account Number");
                        StrCustomerIDAry[i] = arrayObject1.getString("Customer ID");
                        StrCustomerNameAry[i] = arrayObject1.getString("Customer Name");
                        StrPrincipalBalanceAry[i] = arrayObject1.getString("Principal Balance");
                        StrPrincipalDueAry[i] = arrayObject1.getString("Principal Due");
                        StrInterestAry[i] = arrayObject1.getString("Interest");
                        StrPenalInterestAry[i] = arrayObject1.getString("Penal Interest");
                        StrTotalInterestAry[i] = arrayObject1.getString("Total Interest");
                        StrRemittanceAry[i] = arrayObject1.getString("Remittance");
                        StrIsClosingAry[i] = arrayObject1.getString("IsClosing");
                    }
                } else if (responseMsg.equals("0")) {
                    Message = jsonObject.getString("msg");
                }
            } catch (JSONException e) {
                responseMsg = "0";
                proDialog.dismiss();
                e.printStackTrace();
            }
            return responseMsg;
        }

        @Override
        public void onPostExecute(final String result) {
            proDialog.dismiss();
            if (result.equals("1")) {
                Tv_acc_no.setText(StrAccountNo);
                Tv_branch.setText(StrBranch);
                Tv_loan_type.setText(StrType);
                Tv_scheme.setText(StrScheme);
                Tv_loan_amount.setText(StrLn_LoanAmount);
                Tv_loan_date.setText(StrLn_LoanDate);
                Tv_due_date.setText(StrLn_DueDate);
                Tv_roi.setText(StrLn_IntRate);

                TransactionLoanGetSet.setSchCode(StrSch_Code);

                Intent intent = new Intent("populate_data_cls");
                intent.putExtra("data", "grp_data_cls");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

            } else if (result.equals("0")) {
                EdtSelectGrp.setText(null);
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText(Message)
                        .show();
            }
        }
    }


}
