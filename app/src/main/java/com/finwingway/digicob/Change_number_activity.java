package com.finwingway.digicob;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.HashMap;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;
import static com.finwingway.digicob.login.ip_global;

public class Change_number_activity extends AppCompatActivity {
    private Button otp_validate_btn,verify_textView;
    private EditText mobile_number_edittext;
    private LinearLayout otp_linear;
    private JSONParser jsonParser=new JSONParser();
    SweetAlertDialog pDialog;
    String otp_id,msg,error="error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number_activity);
        pDialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
        otp_linear=(LinearLayout)findViewById(R.id.cmn_otp_linear);
        otp_linear.setVisibility(GONE);
        verify_textView=(Button) findViewById(R.id.verify_mobile_no_textView);
        otp_validate_btn=(Button)findViewById(R.id.cmn_validate_otp);
        mobile_number_edittext=(EditText)findViewById(R.id.change_mobile_number_editText);
        verify_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthOTP().execute();
            }
        });
        otp_validate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new validate_otp().execute();
            }
        });
    }
    class AuthOTP extends AsyncTask<String, String, String> {
        final String otpurl = ip_global+"/OTPGenerate";
        String mobile_number=mobile_number_edittext.getText().toString();

        @Override
        public void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("mobile_number", mobile_number);
            hashMap.put("particular", "Withdrawal");
            try {
                JSONObject serverResponce = jsonParser.makeHttpRequest(otpurl, "POST", hashMap);
                String jsonData = serverResponce.toString();
                Log.e("data=>", jsonData);
                JSONObject jsonDataObj = new JSONObject(jsonData);
                JSONObject jobj1 = jsonDataObj.getJSONObject("otp");
                otp_id = jobj1.getString("otp_id");
                Log.e("OTP-ID", otp_id);
            } catch (Exception e) {
                e.printStackTrace();
                error="error";
            }
            return  error;
        }

        @Override
        public void onPostExecute(String result) {
            pDialog.dismiss();
            if(result.equals("error")){
                otp_linear.setVisibility(View.VISIBLE);
            }else {
                otp_linear.setVisibility(View.VISIBLE);
            }
        }
    }
    private class validate_otp extends AsyncTask<String, String, String> {
        final String otpurl = ip_global+"/OTPValidate";
        String mobile_number=mobile_number_edittext.getText().toString();

        @Override
        public void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Validating");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("mobile_number", mobile_number);
            hashMap.put("particular", "Withdrawal");
            try {
                JSONObject serverResponce = jsonParser.makeHttpRequest(otpurl, "POST", hashMap);
                String jsonData = serverResponce.toString();
                Log.e("data=>", jsonData);
                JSONObject jsonDataObj = new JSONObject(jsonData);
                JSONObject jobj1 = jsonDataObj.getJSONObject("otp");
                otp_id = jobj1.getString("otp_id");
                Log.e("OTP-ID", otp_id);
            } catch (Exception e) {
                e.printStackTrace();
                error="error";
            }
            return  error;
        }

        @Override
        public void onPostExecute(String result) {
            pDialog.dismiss();
            if(result.equals("error")){
                new SweetAlertDialog(Change_number_activity.this,SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("validation Success")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent=new Intent();
                                intent.putExtra("mobile_number","9447506214");
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        }).show();
            }else {
            }
        }
    }

}
