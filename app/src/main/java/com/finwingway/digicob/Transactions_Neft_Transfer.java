package com.finwingway.digicob;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static android.graphics.Color.RED;
import static android.view.View.GONE;

/**
 * Created by AnVin on 4/10/2017.
 */

public class Transactions_Neft_Transfer extends Fragment {
    public static String ip = login.ip_global;
    final String api_url = ip + "/getAccountHolder";
    LinearLayout beneficiary_details, bene_list, credit_details, debit_details;
    SweetAlertDialog dialog;
    JSONParser jsonParser;
    Button debit_ok_btn, add_new_ben, add_bene;
    TextView neft_debit_acc_no, neft_debit_name, neft_debit_mobile, neft_debit_balance,
            neft_credit_acc_no, neft_credit_name, neft_credit_mobile, neft_credit_balance;
    String acc_no, name, mobile, balance, debit_acc, cust_id;
    EditText debit_acc_no, b_name, b_acc_no, b_phone, b_ifsc;
    //Spinner ben_list;
    Spinner ben_list;
    String[] benidArray, benArray;
    String beneid, sbid, sbname, sbacc_no, sbifsc, bname, bphone, bacc_no, bifsc, msg, status, error;
    ImageButton cancel_btn, acc_search_img_btn;
    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_neft_transfer_layout, container, false);

        jsonParser = new JSONParser();
        debit_details = (LinearLayout) rootView.findViewById(R.id.linear_debit_details);
        beneficiary_details = (LinearLayout) rootView.findViewById(R.id.linear_beneficiary_details);
        credit_details = (LinearLayout) rootView.findViewById(R.id.linear_credit_details);
        bene_list = (LinearLayout) rootView.findViewById(R.id.linear_bene_list);
        debit_details.setVisibility(GONE);
        beneficiary_details.setVisibility(GONE);
        credit_details.setVisibility(GONE);
        bene_list.setVisibility(GONE);

        ben_list = (Spinner) rootView.findViewById(R.id.neft_ben_list_spinner);
        neft_debit_acc_no = (TextView) rootView.findViewById(R.id.neft_debit_accountNumberTextView);
        neft_debit_name = (TextView) rootView.findViewById(R.id.neft_debit_NametextView);
        neft_debit_mobile = (TextView) rootView.findViewById(R.id.neft_debit_mobileTextView);
        neft_debit_balance = (TextView) rootView.findViewById(R.id.neft_debit_balance);
        neft_credit_acc_no = (TextView) rootView.findViewById(R.id.neft_credit_accountNumberTextView);
        neft_credit_name = (TextView) rootView.findViewById(R.id.neft_credit_NametextView);
        neft_credit_mobile = (TextView) rootView.findViewById(R.id.neft_credit_mobileTextView);
        neft_credit_balance = (TextView) rootView.findViewById(R.id.neft_credit_balance);

        debit_acc_no = (EditText) rootView.findViewById(R.id.neft_debit_account_no_editText);
        b_name = (EditText) rootView.findViewById(R.id.neft_beneNameEditText);
        b_acc_no = (EditText) rootView.findViewById(R.id.neft_beneAccNoEditText);
        b_phone = (EditText) rootView.findViewById(R.id.neft_beneMobEditText);
        b_ifsc = (EditText) rootView.findViewById(R.id.neft_beneIfscEditText);
        acc_search_img_btn = (ImageButton) rootView.findViewById(R.id.neft_acc_search);
        acc_search_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Account_Search.class);
                startActivityForResult(intent, 1002);
            }
        });
        cancel_btn = (ImageButton) rootView.findViewById(R.id.neft_cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        add_new_ben = (Button) rootView.findViewById(R.id.neft_addnew_Benefitiary_btn);
        add_new_ben.setOnClickListener(new View.OnClickListener() {
            boolean ben = false;

            @Override
            public void onClick(View v) {
                if (!ben) {
                    beneficiary_details.setVisibility(View.VISIBLE);
                    add_new_ben.setBackgroundColor(RED);
                    ben = true;
                } else {
                    beneficiary_details.setVisibility(View.GONE);
                    add_new_ben.setBackgroundResource(android.R.drawable.btn_default);
                    ben = false;
                }
            }
        });
        debit_ok_btn = (Button) rootView.findViewById(R.id.neft_debit_ok_btn);
        debit_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debit_acc = debit_acc_no.getText().toString();
                new getAccountHolder().execute();
            }
        });
        add_bene = (Button) rootView.findViewById(R.id.neft_add_bene_btn);
        add_bene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bname = b_name.getText().toString();
                bacc_no = b_acc_no.getText().toString();
                bphone = b_phone.getText().toString();
                bifsc = b_ifsc.getText().toString();
                new addBen().execute();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002) {
            String result = null;
            try {
                result = data.getStringExtra("result_acc_no");
            } catch (Exception e) {
                e.printStackTrace();
            }
            debit_acc_no.setText(result);
        }
    }

    class getAccountHolder extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            dialog.setTitleText("Getting Account info..");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", debit_acc);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                String JsonData = data.toString();
                JSONObject reader = new JSONObject(JsonData);
                JSONObject jobj = reader.getJSONObject("account");
                if (jobj.has("data")) {
                    JSONObject jobj1 = jobj.getJSONObject("data");
                    name = jobj1.getString("NAME");
                    mobile = jobj1.getString("MOBILE");
                    balance = jobj1.getString("CURRENT_BALANCE");
                    cust_id = jobj1.getString("CUST_ID");
                    msg = "ok";
                }
                if (jobj.has("error")) {
                    error = jobj.getString("error");
                    msg = "error";
                }

            } catch (Exception e) {
                e.printStackTrace();
                msg = "accountHolderError";
                error = "Something went Wrong!";
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String url) {
            dialog.dismiss();
            if (url == "ok") {
                debit_details.setVisibility(View.VISIBLE);
                neft_debit_acc_no.setText(debit_acc);
                neft_debit_name.setText(name);
                neft_debit_mobile.setText(mobile);
                neft_debit_balance.setText(balance);
                new getList().execute();
                bene_list.setVisibility(View.VISIBLE);
            }
            if ((url == "accountHolderError") || (url == "error")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText(error)
                        .show();
            }
        }
    }

    public class getList extends AsyncTask<String, String, String> {

        final String benurl = ip + "/custSenderRegisteredReceiversList";

        @Override
        protected void onPreExecute() {
            dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            dialog.setTitleText("Loading");
            dialog.setCancelable(false);
            dialog.setContentText("Fetching Beneficiary Details..").show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMapAmount = new HashMap<>();
            hashMapAmount.put("customer_id", cust_id);
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(benurl, "POST", hashMapAmount);
                String JsonDataString = jsonObject.toString();
                JSONObject reader = new JSONObject(JsonDataString);
                String status = reader.getString("status");
                Log.e("STATUS=", status);
                if (status.equals("1")) {
                    error = "ok";
                    final JSONArray jsonArray = reader.getJSONArray("data");
                    Log.e("JSONArray=>", jsonArray.toString());
                    int length = jsonArray.length() + 1;
                    benidArray = new String[length];
                    benidArray[0] = "none";
                    for (int i = 1; i < length; i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i - 1);
                        Log.e("JData", jsonData.toString());
                        sbid = jsonData.getString("receiverid");
                        sbname = jsonData.getString("receiver_name");
                        benidArray[i] = sbname + "," + sbid;
                        sbacc_no = jsonData.getString("receiver_accountno");
                        sbifsc = jsonData.getString("receiver_ifsccode");
                    }
                }
                if (status.equals("0")) {
                    error = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                error = "error";
            }
            return error;
        }

        @Override
        public void onPostExecute(String url) {
            dialog.dismiss();
            if (url == "ok") {
                Log.e("ARRAY=>", Arrays.toString(benidArray));
                int length = benidArray.length;
                benArray = new String[length];
                benArray[0] = "-Select Beneficiary-";
                for (int i = 1; i < length; i++) {
                    int indSize = benidArray[i].lastIndexOf(",");
                    benArray[i] = benidArray[i].substring(0, indSize);
                    Log.e("trim", benArray[i]);
                }
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.spinner_list_item, benArray);
                ben_list.setAdapter(adapter);
                ben_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        if (position == 0) {
//                            Toast.makeText(getContext(), "Select One!", Toast.LENGTH_SHORT).show();
                        }
                        Log.v("item", (String) parent.getItemAtPosition(position));
                        //Toast.makeText(getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                        int pos = parent.getSelectedItemPosition();
                        Log.v("item", (String) parent.getItemAtPosition(position));
                        String value = benidArray[pos];
                        beneid = value.substring(value.lastIndexOf(',') + 1);
//                        Toast.makeText(getContext(),beneid,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });
            }
            if (url == "error") {
                benArray = new String[1];
                benArray[0] = "-No Beneficiary-";
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.spinner_list_item, benArray);
                ben_list.setAdapter(adapter);
            }
        }
    }

    public class addBen extends AsyncTask<String, String, String> {
        final String benurl = ip + "/custCreateBeneficiary";

        @Override
        protected void onPreExecute() {
            dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            dialog.setTitleText("Loading");
            dialog.setCancelable(false);
            dialog.setContentText("Generating OTP").show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMapAmount = new HashMap<>();
            try {
                hashMapAmount.put("customer_id", cust_id);
                hashMapAmount.put("ben_name", bname);
                hashMapAmount.put("ben_mobile", bphone);
                hashMapAmount.put("ben_account_no", bacc_no);
                hashMapAmount.put("ben_ifscode", bifsc);
                JSONObject jsonObject = jsonParser.makeHttpRequest(benurl, "POST", hashMapAmount);
                //Log.e("Hashmap",hashMapAmount.toString());
                String JsonDataString = jsonObject.toString();
                JSONObject result = new JSONObject(JsonDataString);
                Log.e("ResultBen", result.toString());
                status = result.getString("status");
                Log.e("ResultStatus", status);
                msg = result.getString("msg");
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
            if (status.equals("1")) {
                return "ok";
            } else {
                return "error";
            }
        }

        @Override
        public void onPostExecute(String url) {
            dialog.dismiss();
            if (url == "error") {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(msg)
                        .show();
            }
            if (url == "ok") {
                beneficiary_details.setVisibility(GONE);
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!!")
                        .setContentText("Select Beneficiary from List").show();
                new getList().execute();
            }
        }
    }


}
