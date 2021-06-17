package com.finwingway.digicob;

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

import com.finwingway.digicob.support.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.VISIBLE;

public class Account_Search extends Activity {

    Spinner acc_type_spinner, branch_spinner;
    String acc_type, acc_name, acc_nme, cus_id, error, acc_number_result, account_name, account_number, account_account_phone, branch_id, customer_id;
    EditText acc_name_editText;
    TextView tvwrng_msg;
    Button search_btn, search_cancel;
    public static String ip = login.ip_global;
    String[] CustomernameArray, CustomerIDArray, CustomerAccountNumberArray, CustomerMobileNumberArray;
    ListView acc_details_listView;
    JSONParser jsonParser;
    Dialog progressDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__search);
        jsonParser = new JSONParser();
        tvwrng_msg = (TextView) findViewById(R.id.tv_wrng_msg);
        acc_name_editText = (EditText) findViewById(R.id.search_acc_name_editText);
        search_btn = (Button) findViewById(R.id.search_search_btn);
        search_cancel = (Button) findViewById(R.id.search_cancel_btn);
        acc_details_listView = (ListView) findViewById(R.id.search_acc_details_list_view);
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialogue_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //progressBar=(ProgressBar)progressDialog.findViewById(R.id.progressBar);

        //acc_type_id = getResources().getStringArray(R.array.cashdeposit_acc_type_id);
        branch_spinner = (Spinner) findViewById(R.id.search_branch_spinner);
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

        acc_type_spinner = (Spinner) findViewById(R.id.search_acc_type_spinner);
        ArrayAdapter accTypeAdapter = new ArrayAdapter<String>
                (getBaseContext(), R.layout.spinner_list_item, login.arrayAccountType);
        acc_type_spinner.setAdapter(accTypeAdapter);
        acc_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                acc_type = login.arrayAccountTypeId[position];
//                Toast.makeText(getBaseContext(),acc_type, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connDetector = new ConnectionDetector(getApplicationContext());
                if (connDetector.isnetAvailable()) {
                    acc_name = acc_name_editText.getText().toString();
                    acc_details_listView.setAdapter(null);
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

    private class getAccountNumbers extends AsyncTask<String, String, String> {
        private String api_getCustomers = ip + "/getCustUnderAgent";

        @Override
        public void onPreExecute() {
            progressDialog.setTitle("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("agent_id", "null");
            hashMap.put("branch_id", "null");
            hashMap.put("name", acc_name);
            hashMap.put("acc_type", acc_type);
            hashMap.put("acc_cat", "null");
            hashMap.put("branch_id", branch_id);
//            Log.e( "getAccountNumbers: ",hashMap.toString() );
            JSONObject jsonObject = jsonParser.makeHttpRequest(api_getCustomers, "POST", hashMap);
            try {
                String json_result = jsonObject.toString();
                Log.e("Acc search: ", json_result);
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
                acc_details_listView.setAdapter(adapter_account_details);
                acc_details_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        }
    }
}
