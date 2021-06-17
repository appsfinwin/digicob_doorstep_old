package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.finwingway.digicob.Adapter_account_details;
import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.VISIBLE;

@SuppressLint("Registered")
public class Search_customer extends Activity {

    Spinner branch_spinner;
    String StrSearch, acc_nme, cus_id, error, acc_number_result, account_name, account_number, account_account_phone, branch_id, customer_id;
    EditText EdtSearch_cusid;
    TextView tvwrng_msg;
    Button search_btn, search_cancel;
    public static String ip = login.ip_global;
    String[] CustomernameArray, CustomerIDArray, CustomerAccountNumberArray, CustomerMobileNumberArray;
    ListView details_listView;
    JSONParser jsonParser;
    Dialog progressDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__search);
        jsonParser = new JSONParser();
        tvwrng_msg = (TextView) findViewById(R.id.tv_wrng_msg_cus);
        EdtSearch_cusid = (EditText) findViewById(R.id.edt_search_cusid);
        search_btn = (Button) findViewById(R.id.btn_search_cus);
        search_cancel = (Button) findViewById(R.id.btn_cancel_cus);
        details_listView = (ListView) findViewById(R.id.search_details_cus);
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialogue_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //progressBar=(ProgressBar)progressDialog.findViewById(R.id.progressBar);

        //acc_type_id = getResources().getStringArray(R.array.cashdeposit_acc_type_id);
        branch_spinner = (Spinner) findViewById(R.id.spinner_branch_cus);
        ArrayAdapter brachAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_list_item, login.arrayBrachName);
        branch_spinner.setAdapter(brachAdapter);
        try {
            branch_spinner.setSelection(brachAdapter.getPosition(login.branch_name));
        } catch (Exception ignore) {
        }
        branch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch_id = login.arrayBrachId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrSearch = EdtSearch_cusid.getText().toString();
                details_listView.setAdapter(null);
                new getAccountNumbers().execute();
            }
        });
        search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JLGCreateGroup.FindBtnClickd = true;
                finish();
            }
        });
    }

    private class getAccountNumbers extends AsyncTask<String, String, String> {
        private String api_getCustomers = ip + "/getCustUnderAgentbyCustId";

        @Override
        public void onPreExecute() {
            progressDialog.setTitle("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("agent_id", login.agent_code);
            hashMap.put("branch_id", branch_id);
            hashMap.put("custid", StrSearch);

            Log.e( "getCstUdrAgtCustId",hashMap.toString() );
            JSONObject jsonObject = jsonParser.makeHttpRequest(api_getCustomers, "POST", hashMap);
            try {
                String json_result = jsonObject.toString();
                JSONObject data = new JSONObject(json_result);
                JSONObject customer_list = data.getJSONObject("customer_list");
                if (customer_list.has("error")) {
                    error = customer_list.getString("error");
                    return "error";
                }
                if (customer_list.has("data")) {
                    JSONArray jsonArray = customer_list.getJSONArray("data");
                    Log.e("JSONArray=>", jsonArray.toString());
                    CustomernameArray = new String[jsonArray.length()];
                    CustomerIDArray = new String[jsonArray.length()];
                    CustomerAccountNumberArray = new String[jsonArray.length()];
                    CustomerMobileNumberArray = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arrayObject = jsonArray.getJSONObject(i);
                        account_name = arrayObject.getString("CUST_NAME");
                        CustomernameArray[i] = account_name;
                        Log.e("introName", account_name);
                        customer_id = arrayObject.getString("CUST_ID");
                        CustomerIDArray[i] = customer_id;
                        Log.e("introName", customer_id);
                        account_number = arrayObject.getString("ACC_NO");
                        CustomerAccountNumberArray[i] = account_number;
                        Log.e("number", account_number);
                        account_account_phone = arrayObject.getString("MOBILE");
                        CustomerMobileNumberArray[i] = account_account_phone;
                        Log.e("phone", account_account_phone);
                    }
                    return "ok";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(final String result) {
            progressDialog.dismiss();
            if (result.equals("ok")) {
                final Adapter_account_details adapter_account_details
                        = new Adapter_account_details(getBaseContext(), CustomernameArray, CustomerIDArray,
                        CustomerAccountNumberArray, CustomerMobileNumberArray);
                details_listView.setAdapter(adapter_account_details);
                details_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getBaseContext(), adapter_account_details.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                        acc_number_result = adapter_account_details.getItem(position).toString();
                        cus_id = adapter_account_details.getCusId(position).toString();
                        acc_nme = adapter_account_details.getAccName(position).toString();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("result_acc_no", acc_number_result);
                        resultIntent.putExtra("result_debit_acc_no", acc_number_result);
                        resultIntent.putExtra("result_cus_id", cus_id);
                        resultIntent.putExtra("result_acc_name", acc_nme);
                        setResult(RESULT_OK, resultIntent);
                        /*setResult(1002,resultIntent);
                        setResult(1003,resultIntent);
                        setResult(1004,resultIntent);
                        setResult(1005,resultIntent);
                        setResult(1006,resultIntent);
                        setResult(1007,resultIntent);*/
                        finish();
                    }
                });
                tvwrng_msg.setVisibility(View.INVISIBLE);
                details_listView.setVisibility(VISIBLE);
            }
            if (result.equals("error")) {
                if (error.equals("No results found!")) {
                    details_listView.setVisibility(View.INVISIBLE);
                    tvwrng_msg.setVisibility(VISIBLE);
                    tvwrng_msg.setText(error);
                } else {
                    Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

}
