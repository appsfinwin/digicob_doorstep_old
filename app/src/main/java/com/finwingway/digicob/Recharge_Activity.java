package com.finwingway.digicob;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import com.finwingway.digicob.support.ConnectionDetector;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by AnVin on 3/1/2017.
 */

public class Recharge_Activity extends Fragment {
    public static String ip = login.ip_global;
    final String api_url = ip + "/getAccountHolder";
    final String api_getMaster = ip + "/getMasters";
    final String api_getOperatorlist = ip + "/getOperatorList";
    final String api_getCircle = ip + "/getCircleList";
    final String api_rechargeMobile = ip + "/RechargeMobile";
    SweetAlertDialog dialog;
    String accountNumber;
    String customerId;
    JSONParser jsonParser;
    String name, mobile;
    TextView acc_no_textView, nameTextView, mobileTextView;
    public Spinner spinner_masters, spinner_operator, spinner_circle;
    public String[] mastersArray, masterArray;
    public String cir_id, cir_name, opr_id, opr_name;
    public String[] circleArray, circleListArray, operatorArray, operatorsArray;
    EditText amountEditText, mobileNumberEditText, acc_noEditText;
    Button okbtn;
    String operatorType, operator, circle;
    String getamount, getmobileNumber, status, result;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        final View rootView = inflater.inflate(R.layout.recharge_layout, container, false);
        dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        jsonParser = new JSONParser();

        okbtn = (Button) rootView.findViewById(R.id.re_okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc_noEditText = (EditText) rootView.findViewById(R.id.re_account_number_editText);
                accountNumber = acc_noEditText.getText().toString();
                new getAccountHolder().execute();
                new getMasters().execute();
                new getCircle().execute();
            }
        });
       /* acc_no_textView=(TextView)rootView.findViewById(R.id.recharge_accountNumberTextView);
        nameTextView=(TextView)rootView.findViewById(R.id.rechargeNametextView);
        mobileTextView=(TextView)rootView.findViewById(R.id.rechargemobileTextView);*/


        spinner_masters = (Spinner) rootView.findViewById(R.id.recharge_typeSpinner);
        spinner_operator = (Spinner) rootView.findViewById(R.id.recharge_operatorSpinner);
        spinner_circle = (Spinner) rootView.findViewById(R.id.recharge_circleSpinner);
        Button recharge = (Button) rootView.findViewById(R.id.rechargeButton);
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectionDetector connDetector = new ConnectionDetector(getContext());
                if (connDetector.isnetAvailable()) {
                    amountEditText = (EditText) rootView.findViewById(R.id.recharge_amountEditText);
                    mobileNumberEditText = (EditText) rootView.findViewById(R.id.recharge_mobileNumber_editText);
                    getamount = amountEditText.getText().toString();
                    getmobileNumber = mobileNumberEditText.getText().toString();
                    new getRechargeMobile().execute(getamount, getmobileNumber);
                } else {
                    Toast.makeText(getContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;

    }


    private class getAccountHolder extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            dialog.setTitleText("Getting Account info..");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", accountNumber);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                String JsonData = data.toString();
                JSONObject reader = new JSONObject(JsonData);
                JSONObject jobj = reader.getJSONObject("account");
                JSONObject jobj1 = jobj.getJSONObject("data");
                name = jobj1.getString("NAME");
                mobile = jobj1.getString("MOBILE");
                customerId = jobj1.getString("CUST_ID");

            } catch (Exception e) {
                e.printStackTrace();
                return "accountHolderError";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String url) {
            if (url != "accountHolderError") {
                dialog.dismiss();
              /*  acc_no_textView.setText(accountNumber);
                nameTextView.setText(introName);
                mobileTextView.setText(mobile);*/
            } else {
                dialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Error in Connection")
                        .show();
            }
        }
    }
    /*////////////////////////////////////GET MASTERS/////////////////////////////////*/

    class getMasters extends AsyncTask<String, String, String> {
        String master_name, master_value;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("CAT_ID", "RCHG_TYPE");
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_getMaster, "POST", hashMap);
                JSONObject masters = data.getJSONObject("masters");
                JSONArray jsonArray = masters.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonArray.toString());
                int length = jsonArray.length();
                mastersArray = new String[length];
                for (int i = 0; i < length; i++) {
                    JSONObject arrayObject = jsonArray.getJSONObject(i);
                    master_name = arrayObject.getString("NAME");
                    master_value = arrayObject.getString("VALUE");
                    mastersArray[i] = master_name + "," + master_value;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "accountHolderError";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String url) {
            if (url != "accountHolderError") {
                Log.d("mastersTitleArray", Arrays.toString(mastersArray));
                int length = mastersArray.length;
                masterArray = new String[length];
                for (int i = 0; i < length; i++) {
                    int indSize = mastersArray[i].lastIndexOf(",");
                    masterArray[i] = mastersArray[i].substring(0, indSize);
                    Log.e("trim", masterArray[i]);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.spinner_list_item, masterArray);
                spinner_masters.setAdapter(adapter);
                spinner_masters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        int pos = parent.getSelectedItemPosition();
                        Log.v("item", (String) parent.getItemAtPosition(position));
                        String value = mastersArray[pos];
                        operatorType = value.substring(value.lastIndexOf(',') + 1);
//                        Toast.makeText(getContext(),operatorType,Toast.LENGTH_LONG).show();
                        new getOperatorList().execute(operatorType);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });

            } else {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Error in Connection")
                        .show();
            }
        }
    }


    /*////////////////////////////////////GET OPERATOR/////////////////////////////////*/


    private class getOperatorList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            HashMap<String, String> hashMap = new HashMap<>();
//            hashMap.put("introtype",type);
            hashMap.put("type", type);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_getOperatorlist, "POST", hashMap);
                JSONArray jsonArray = data.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonArray.toString());
                int length = jsonArray.length();
                operatorArray = new String[length];
                for (int i = 0; i < length; i++) {
                    JSONObject arrayObject = jsonArray.getJSONObject(i);
                    opr_id = arrayObject.getString("opr_id");
                    opr_name = arrayObject.getString("opr_name");
                    operatorArray[i] = opr_name + "," + opr_id;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "accountHolderError";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String url) {
            if (url != "accountHolderError") {
                Log.d("mastersTitleArray", Arrays.toString(operatorArray));
                int length = operatorArray.length;
                operatorsArray = new String[length];
                for (int i = 0; i < length; i++) {
                    int indSize = operatorArray[i].lastIndexOf(",");
                    operatorsArray[i] = operatorArray[i].substring(0, indSize);
                    Log.e("trim", operatorsArray[i]);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.spinner_list_item, operatorsArray);
                spinner_operator.setAdapter(adapter);
                spinner_operator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        int pos = parent.getSelectedItemPosition();
                        Log.v("item", (String) parent.getItemAtPosition(position));
//                        String value=circleArray[pos];
                        String value = operatorArray[pos];
                        operator = value.substring(value.lastIndexOf(',') + 1);
//                        Toast.makeText(getContext(),operator,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });
            } else {
//                new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Error!")
//                        .setContentText("Error in Connection")
//                        .show();
            }
        }
    }


    /*////////////////////////////////////GET CIRCLE/////////////////////////////////*/


    class getCircle extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", accountNumber);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_getCircle, "POST", hashMap);
                JSONArray jsonArray = data.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonArray.toString());
                int length = jsonArray.length();
                circleArray = new String[length];
                for (int i = 0; i < length; i++) {
                    JSONObject arrayObject = jsonArray.getJSONObject(i);
                    cir_id = arrayObject.getString("cir_id");
                    cir_name = arrayObject.getString("cir_name");
                    circleArray[i] = cir_name + "," + cir_id;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "accountHolderError";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String url) {
            if (url != "accountHolderError") {

                Log.d("mastersTitleArray", Arrays.toString(circleArray));
                int length = circleArray.length;
                circleListArray = new String[length];
                for (int i = 0; i < length; i++) {
                    int indSize = circleArray[i].lastIndexOf(",");
                    circleListArray[i] = circleArray[i].substring(0, indSize);
                    Log.e("trim", circleListArray[i]);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.spinner_list_item, circleListArray);
                spinner_circle.setAdapter(adapter);
                spinner_circle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        int pos = parent.getSelectedItemPosition();
                        Log.v("item", (String) parent.getItemAtPosition(position));
                        String value = circleArray[pos];
                        circle = value.substring(value.lastIndexOf(',') + 1);
//                        Toast.makeText(getContext(),circle,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });

            } else {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Error in Connection")
                        .show();
            }
        }
    }


    /*////////////////////////////////////RECHARGE MOBILE/////////////////////////////////*/

    class getRechargeMobile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            dialog.setTitleText("Processing..")
                    .show();
        }

        @Override
        protected String doInBackground(String... params) {
            String amountVal = params[0];
            String mobileVal = params[1];
            Log.e("amount&Mobile", amountVal + mobileVal);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", accountNumber);
            hashMap.put("agent_id", "0");
            hashMap.put("amount", amountVal);
            hashMap.put("circle", circle);
            hashMap.put("customer_id", customerId);
            hashMap.put("mobile", mobileVal);
            hashMap.put("Operator", operator);
            hashMap.put("recharge_type", operatorType);
            Log.e("HshMap", hashMap.toString());
            try {
                JSONObject rechargedata = jsonParser.makeHttpRequest(api_rechargeMobile, "POST", hashMap);
                Log.e("getRechargeMobile=>", rechargedata.toString());
                status = rechargedata.getString("status");
                result = rechargedata.getString("msg");
                Log.e("Status:", status + "Result:" + result);
                if (status == "1") {
                    return result; //"Recharge Pending..";
                }
                if (status == "0") {
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "accountHolderError";
            }

            return "";
        }

        @Override
        protected void onPostExecute(String url) {
//            Log.e("url=",url);
            if (url != "accountHolderError") {
                dialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Result")
                        .setContentText(url)
                        .show();
            } else {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Error in Connection")
                        .show();
            }
        }
    }

}
