package com.finwingway.digicob;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.finwingway.digicob.support.ConnectionDetector;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;

/**
 * Created by AnVin on 1/2/2017.
 */

public class Enquiry_Account_Status extends Fragment implements View.OnClickListener {
    Button okbtn;
    ImageButton cancelbtn;
    public static String ip = login.ip_global;
    JSONParser jsonParser = new JSONParser();
    EditText acc_ref_id_edit_text;
    String acc_ref_id, msg, error;
    SweetAlertDialog dialog;
    TextView status_name_text, status_status_text, status_mobile_text, status_acc_no_text, status_cust_id_text;
    String status_name, status_status, status_acc_no, status_mobile, status_cust_id;
    LinearLayout status_first_linear;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle bundle) {
        View rootView = inflator.inflate(R.layout.fragment_enquiry_accountstatus_layout, container, false);
        dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        okbtn = (Button) rootView.findViewById(R.id.accountStatusOKbtn);
        okbtn.setOnClickListener(this);
        cancelbtn = (ImageButton) rootView.findViewById(R.id.accountStatusCANCELbtn);
        cancelbtn.setOnClickListener(this);
        acc_ref_id_edit_text = (EditText) rootView.findViewById(R.id.acc_ref_id_editText);
        status_name_text = (TextView) rootView.findViewById(R.id.status_name_txt_view);
        status_status_text = (TextView) rootView.findViewById(R.id.status_status_txt_view);
        status_mobile_text = (TextView) rootView.findViewById(R.id.status_mobile_txt_view);
        status_acc_no_text = (TextView) rootView.findViewById(R.id.statuc_acc_no_text_view);
        status_cust_id_text = (TextView) rootView.findViewById(R.id.status_cust_id_text_view);
        status_first_linear = (LinearLayout) rootView.findViewById(R.id.linear_account_details_status);
        status_first_linear.setVisibility(GONE);
        return rootView;
    }

    public void onClick(View view) {
        if (view == okbtn) {

            String acc_ref = acc_ref_id_edit_text.getText().toString();

            if (!acc_ref.equals("")) {
                ConnectionDetector connDetector = new ConnectionDetector(getContext());
                if (connDetector.isnetAvailable()) {
                    acc_ref_id = acc_ref_id_edit_text.getText().toString();
                    new getAcoountStatus().execute();
                } else {
                    Toast.makeText(getContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Enter ACCOUNT REF ID or CUSTOMER REF ID ", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == cancelbtn) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        }
    }

    class getAcoountStatus extends AsyncTask<String, String, String> {
        private String api_getAccountStatus = ip + "/getAccountStatusByRefId";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitleText("Loadig");
            dialog.setContentText("Please wait..");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            try {
                hashMap.put("custRefId", acc_ref_id);
                JSONObject result = jsonParser.makeHttpRequest(api_getAccountStatus, "POST", hashMap);
                JSONObject account = result.getJSONObject("account");
                if (account.has("data")) {
                    JSONObject data = account.getJSONObject("data");
                    status_name = data.getString("NAME");
                    status_acc_no = data.getString("ACC_NO");
                    status_cust_id = data.getString("CUST_ID");
                    status_mobile = data.getString("MOBILE");
                    status_status = data.getString("ACC_STATUS");
                    msg = "ok";
                }
                if (account.has("error")) {
                    error = account.getString("error");
                    Log.e("error", error);
                    msg = "error";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (s == "error") {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText(error)
                        .show();
            }
            if (s == "ok") {
                status_first_linear.setVisibility(View.VISIBLE);
                status_name_text.setText(status_name);
                status_status_text.setText(status_status);
                status_mobile_text.setText(status_mobile);
                status_acc_no_text.setText(status_acc_no);
                status_cust_id_text.setText(status_cust_id);
            }
        }
    }
}
