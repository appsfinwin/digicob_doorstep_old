package com.finwingway.digicob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.finwingway.digicob.support.ConnectionDetector;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import static android.view.View.VISIBLE;
import static com.finwingway.digicob.login.ip_global;

public class Loan_Number_Search extends Activity {

    private Spinner acc_type_spinner, branch_spinner;
    private String acc_type, acc_name = "null", cus_id, error, acc_number_result, account_name,
            account_number, account_account_phone, branch_id, customer_id,
            StrloanSchm = "null";
    SweetAlertDialog proDialog;
    private EditText acc_name_editText;
    private TextView tvwrng_msg;
    private Button search_btn, search_cancel;
    public static String ip = ip_global;
    private String[] CustomernameArray, CustomerIDArray, CustomerAccNumberArray, CustomerSch_NameArray;
    String[] SchCodeArray, SchNameArray;
    String rtnMsg = "error", responseMsg, StrSchCode;
    private ListView acc_details_listView;
    private JSONParser jsonParser;
    private Dialog progressDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_loan_number_search);
        proDialog = new SweetAlertDialog(Loan_Number_Search.this, SweetAlertDialog.PROGRESS_TYPE);
        jsonParser = new JSONParser();
        tvwrng_msg = (TextView) findViewById(R.id.tv_wrng_msg_lon);
        acc_name_editText = (EditText) findViewById(R.id.loan_search_acc_name_editText);
        search_btn = (Button) findViewById(R.id.loan_search_search_btn);
        search_cancel = (Button) findViewById(R.id.loan_search_cancel_btn);
        acc_details_listView = (ListView) findViewById(R.id.loan_search_acc_details_list_view);
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialogue_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //progressBar=(ProgressBar)progressDialog.findViewById(R.id.progressBar);

        try {
            Bundle extras = getIntent().getExtras();
            assert extras != null;
            StrloanSchm = extras.getString("LN_schm", "null");
        } catch (Exception ignored) {
        }

        //acc_type_id = getResources().getStringArray(R.array.cashdeposit_acc_type_id);
        branch_spinner = (Spinner) findViewById(R.id.loan_search_branch_spinner);
        ArrayAdapter brachAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_list_item, login.arrayBrachName);
        branch_spinner.setAdapter(brachAdapter);
        branch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch_id = login.arrayBrachId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        acc_type_spinner = (Spinner) findViewById(R.id.loan_search_scheme_spinner);
//        ArrayAdapter accTypeAdapter = new ArrayAdapter<String>
//                (getBaseContext(), R.layout.spinner_list_item, login.arrayAccountType);
//        acc_type_spinner.setAdapter(accTypeAdapter);
        acc_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                StrSchCode = SchNameArray[position];
                StrSchCode = SchCodeArray[position];
                Log.e("scheme_spinner: ", StrSchCode);

//                acc_type = login.arrayAccountTypeId[position];
//                Toast.makeText(getBaseContext(), acc_type, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new getDropdownScheme().execute();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connDetector = new ConnectionDetector(getApplicationContext());
                if (connDetector.isnetAvailable()) {
                    acc_name = acc_name_editText.getText().toString();
                    new getAccountNumbers().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                case "0":
                    Toast.makeText(Loan_Number_Search.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    break;
                case "1":
                    if (SchNameArray != null && SchNameArray.length > 0) {
                        ArrayAdapter SchemeAdapter = new ArrayAdapter<String>(Loan_Number_Search.this, R.layout.spinner_list_item, SchNameArray);
                        acc_type_spinner.setAdapter(SchemeAdapter);

                        if (!TextUtils.isEmpty(StrloanSchm) && SchCodeArray != null && SchCodeArray.length > 0) {
                            int indexSchm = Arrays.asList(SchCodeArray).indexOf(StrloanSchm);
                            acc_type_spinner.setSelection(indexSchm);
                        }
                    }
                    break;
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getAccountNumbers extends AsyncTask<String, String, String> {
        private String api_getCustomers = ip + "/getLoanCustUnderAgent";

        @Override
        public void onPreExecute() {
            progressDialog.setTitle("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("agent_id", login.agent_code);
            hashMap.put("branch_id", login.branch_id);//branch_id
            hashMap.put("acno", acc_name);
            hashMap.put("sch_code", StrSchCode);//StrSchCode   StrloanSchm

            Log.e("hashMap=>", String.valueOf(hashMap));
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_getCustomers, "POST", hashMap);
                Log.e("getLoanCustUnderAgent >", String.valueOf(jsonObject));
                JSONObject customer_list = jsonObject.getJSONObject("CustomerList");
                if (customer_list.has("error")) {
                    error = customer_list.getString("error");
                    rtnMsg = "error";
                }
                if (customer_list.has("data")) {
                    JSONArray jsonArray = customer_list.getJSONArray("data");
                    CustomerIDArray = new String[jsonArray.length()];
                    CustomernameArray = new String[jsonArray.length()];
                    CustomerAccNumberArray = new String[jsonArray.length()];
                    CustomerSch_NameArray = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arrayObject = jsonArray.getJSONObject(i);
                        CustomerIDArray[i] = arrayObject.getString("Cust_Id");
                        CustomernameArray[i] = arrayObject.getString("Cust_Name");
                        CustomerAccNumberArray[i] = arrayObject.getString("Ln_GlobalAccNo");
                        CustomerSch_NameArray[i] = arrayObject.getString("Sch_Name");
                    }
                    rtnMsg = "ok";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                rtnMsg = "0";
            } catch (Exception e) {
                e.printStackTrace();
                rtnMsg = "0";
            }

//                JSONObject data = new JSONObject(json_result);
//                if (data.has("masters")) {
//                    error = data.getJSONObject("masters").getString("msg");
//                    return "error";
//                }
//                JSONObject customer_list = data.getJSONObject("customer_list");
//                if (customer_list.has("error")) {
//                    error = customer_list.getString("error");
//                    return "error";
//                }
//                if (customer_list.has("data")) {
//                    JSONArray jsonArray = customer_list.getJSONArray("data");
//                    Log.e("JSONArray=>", jsonArray.toString());
//                    CustomernameArray = new String[jsonArray.length()];
//                    CustomerIDArray = new String[jsonArray.length()];
//                    CustomerAccountNumberArray = new String[jsonArray.length()];
//                    CustomerMobileNumberArray = new String[jsonArray.length()];
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject arrayObject = jsonArray.getJSONObject(i);
//                        account_name = arrayObject.getString("CUST_NAME");
//                        CustomernameArray[i] = account_name;
//                        Log.e("introName", account_name);
//                        customer_id = arrayObject.getString("CUST_ID");
//                        CustomerIDArray[i] = customer_id;
//                        Log.e("introName", customer_id);
//                        account_number = arrayObject.getString("ACC_NO");
//                        CustomerAccountNumberArray[i] = account_number;
//                        Log.e("number", account_number);
//                        account_account_phone = arrayObject.getString("MOBILE");
//                        CustomerMobileNumberArray[i] = account_account_phone;
//                        Log.e("phone", account_account_phone);
//                    }
//                    return "ok";
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            return rtnMsg;
        }

        @Override
        public void onPostExecute(final String result) {
            progressDialog.dismiss();
            if (result.equals("ok")) {
                final Adapter_account_details_loan adapter_account_details
                        = new Adapter_account_details_loan(getBaseContext(), CustomernameArray, CustomerIDArray,
                        CustomerAccNumberArray, CustomerSch_NameArray);
                acc_details_listView.setAdapter(adapter_account_details);
                acc_details_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getBaseContext(), adapter_account_details.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                        acc_number_result = adapter_account_details.getItem(position).toString();
                        cus_id = adapter_account_details.getCusId(position).toString();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("result_loan_acc_no", acc_number_result);
                        resultIntent.putExtra("result_loan_cus_id", cus_id);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
                tvwrng_msg.setVisibility(View.INVISIBLE);
                acc_details_listView.setVisibility(VISIBLE);
            }
            if (result.equals("error")) {
                if (error.equals("No results found!")) {
                    acc_details_listView.setVisibility(View.INVISIBLE);
                    tvwrng_msg.setVisibility(VISIBLE);
                    tvwrng_msg.setText(error);
                } else {
                    Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
            if (result.equals("0")) {
                Toast.makeText(Loan_Number_Search.this, "Server error occurred!", Toast.LENGTH_SHORT).show();
            }


        }
    }

}
