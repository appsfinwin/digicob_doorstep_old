package com.finwingway.digicob;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.finwingway.digicob.support.ConnectionDetector;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by AnVin on 1/2/2017.
 */

public class Enquiry_Balance_Enquiry extends Fragment implements View.OnClickListener {
    Button okbtn;
    ImageButton cancelbtn;
    EditText acc_no;
    private String jdate, jaccount_no, jname, jphone, jbalance, searched_acc_no;
    SweetAlertDialog pDialog, dialogue;
    JSONParser jsonParser = new JSONParser();
    private ImageButton acc_search;
    public static String ip = login.ip_global;
    private String error = "ok", msg;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle bundle) {
        View rootView = inflator.inflate(R.layout.fragment_enquiry_balanceenquiry_layout, container, false);
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        okbtn = (Button) rootView.findViewById(R.id.balanceEnquiryOKbtn);
        okbtn.setOnClickListener(this);
        cancelbtn = (ImageButton) rootView.findViewById(R.id.balanceEnquiryCANCELbtn);
        cancelbtn.setOnClickListener(this);
        acc_no = (EditText) rootView.findViewById(R.id.accNoBalanceEnq);
        acc_search = (ImageButton) rootView.findViewById(R.id.balance_enquiry_acc_search_btn);
        acc_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Account_Search.class);
                startActivityForResult(intent, 1000);
            }
        });
        return rootView;
    }

    public void onClick(View view) {
        if (view == okbtn) {
            ConnectionDetector connDetector = new ConnectionDetector(getContext());
            if (connDetector.isnetAvailable()) {
                new AsyncBalanceEnquiry().execute();
            } else {
                Toast.makeText(getContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == cancelbtn) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        }
    }

    class AsyncBalanceEnquiry extends AsyncTask<String, String, String> {
        String account_no = acc_no.getText().toString();
        private final String depositUrl = ip + "/balanceEnqury";

        @Override
        public void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                HashMap<String, String> hashMapAmount = new HashMap<>();
                hashMapAmount.put("account_no", account_no);
                JSONObject jsonObject = jsonParser.makeHttpRequest(depositUrl, "POST", hashMapAmount);
                String JsonDataString = jsonObject.toString();
                JSONObject reader = new JSONObject(JsonDataString);
                JSONObject receipt = reader.getJSONObject("balance");
                if (receipt.has("data")) {
                    JSONObject data = receipt.getJSONObject("data");
                    jdate = data.getString("DATE");
                    jaccount_no = data.getString("ACC_NO");
                    jname = data.getString("NAME");
                    jphone = data.getString("MOBILE");
                    jbalance = data.getString("CURRENT_BALANCE");
                }
                if (receipt.has("error")) {
                    msg = receipt.getString("error");
                    error = "error";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                error = "error";
            }
            return error;
        }

        @Override
        public void onPostExecute(String url) {
            if (url.equals("error")) {
                pDialog.dismiss();
                dialogue = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                dialogue.setTitleText("Error!!");
                dialogue.setContentText(msg);
                dialogue.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        FragmentManager fm = getFragmentManager();
                        //fm.popBackStack();
                        dialogue.dismiss();
                    }
                })
                        .show();
            } else {
                pDialog.dismiss();
                Intent intent = new Intent(getActivity(), BalanceEnquiry.class);
                intent.putExtra("date", jdate);
                intent.putExtra("acc_no", jaccount_no);
                intent.putExtra("introName", jname);
                intent.putExtra("mobile", jphone);
                intent.putExtra("balance", jbalance);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            searched_acc_no = data.getStringExtra("result_acc_no");
            acc_no.setText(searched_acc_no);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
