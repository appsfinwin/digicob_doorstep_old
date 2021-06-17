package com.finwingway.digicob;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
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

import org.json.JSONObject;

import java.util.HashMap;

import com.finwingway.digicob.support.ConnectionDetector;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;
import static com.finwingway.digicob.login.agent_code;

/**
 * Created by AnVin on 12/30/2016.
 */

public class Transactions_Transfer extends Fragment {
    Button CreditOkbtn, DebitOkbtn, Confirm, Cancel;
    LinearLayout otpLinear;
    TextView debitMobile, creditStatus, debitName, debitStatus, debitBalance, creditName, creditMob;
    JSONParser jsonParser;
    SweetAlertDialog dialog;
    String name, mobile, acc_status, cre_name, cre_mobile, cre_acc_status;
    String balance, CreditName, CreditMobile, CreditAccNo, TransferAmount;
    String receiptTransferDate, receiptDebitAccno, receiptCreditAccno, receiptName, receiptMobile, receiptOldBalance,
            receiptWithdrawalAmount, receiptCurrentBalance;
    public String otpNumber;
    String msg, error;
    String otp_id;
    final String agentId = agent_code;
    EditText transferAmount, otp, creditAccno, debitAccno;
    Context ctx;
    Button amount, authOTP;//, debit_search_btn, credit_search_btn;
    ImageButton cancel_btn, debit_search_btn, credit_search_btn;
    public static String ip = login.ip_global;
    LinearLayout linear, debit_linear, credit_linear, amount_transfer_linear;
    String debitaccountNumber = "null", creditAccountNumber = "null", tran_id;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle bundle) {
        View rootView = inflator.inflate(R.layout.fragment_transaction_transfer_layout, container, false);
        DebitOkbtn = (Button) rootView.findViewById(R.id.debitAuthbtn);
        CreditOkbtn = (Button) rootView.findViewById(R.id.creditAuthbtn);
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        linear = (LinearLayout) rootView.findViewById(R.id.LinearTransfer);
        debit_linear = (LinearLayout) rootView.findViewById(R.id.debit_linear_layout);
        credit_linear = (LinearLayout) rootView.findViewById(R.id.credit_linear_layout);
        amount_transfer_linear = (LinearLayout) rootView.findViewById(R.id.amount_transfer_linear);
        debit_linear.setVisibility(GONE);
        credit_linear.setVisibility(GONE);
        amount_transfer_linear.setVisibility(GONE);
        ctx = getActivity();
        jsonParser = new JSONParser();
        otpLinear = (LinearLayout) rootView.findViewById(R.id.otpLinearLayout);
        otpLinear.setVisibility(GONE);
        debitAccno = (EditText) rootView.findViewById(R.id.debitAccountNumberTextView);
        debitName = (TextView) rootView.findViewById(R.id.debitName);
        debitStatus = (TextView) rootView.findViewById(R.id.debitStatus);
        debitMobile = (TextView) rootView.findViewById(R.id.debitMobile);
        debitBalance = (TextView) rootView.findViewById(R.id.debitBalance);
        creditAccno = (EditText) rootView.findViewById(R.id.creditAccountNumberTextView);
        creditName = (TextView) rootView.findViewById(R.id.creditName);
        creditStatus = (TextView) rootView.findViewById(R.id.CreditStatusText);
        creditMob = (TextView) rootView.findViewById(R.id.creditMobile);
        transferAmount = (EditText) rootView.findViewById(R.id.transferAmountEditText);
        otp = (EditText) rootView.findViewById(R.id.otpEditTxt);
        debit_search_btn = rootView.findViewById(R.id.debit_search_btn);
        credit_search_btn = rootView.findViewById(R.id.credit_search_btn);
        cancel_btn = (ImageButton) rootView.findViewById(R.id.TransferCancelBtn);
        authOTP = (Button) rootView.findViewById(R.id.authOTPbtn);
        authOTP.setVisibility(GONE);
        otp.setVisibility(GONE);
        creditAccno.setEnabled(false);
        CreditOkbtn.setEnabled(false);
        credit_search_btn.setEnabled(false);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        debit_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent debit_intent = new Intent(getActivity(), Account_Search.class);
                startActivityForResult(debit_intent, 1001);
            }
        });
        credit_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent debit_intent = new Intent(getActivity(), Account_Search.class);
                startActivityForResult(debit_intent, 1002);
            }
        });

        amount = (Button) rootView.findViewById(R.id.transferAmountOKbtn);
        amount.setEnabled(false);

        transferAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    amount.setEnabled(false);
                } else {
                    amount.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (debitaccountNumber.equals("")) {
                    Toast.makeText(ctx, "Please select Debit Acc no", Toast.LENGTH_SHORT).show();
                } else if (creditAccountNumber.equals("")) {
                    Toast.makeText(ctx, "Please select Credit Acc no", Toast.LENGTH_SHORT).show();
                } else {
                    new AuthOTP().execute();
                }

            }
        });

        authOTP = (Button) rootView.findViewById(R.id.authOTPbtn);
        authOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionDetector connDetector = new ConnectionDetector(getContext());
                if (connDetector.isnetAvailable()) {
                    TransferAmount = transferAmount.getText().toString();
                    otpNumber = otp.getText().toString();
                    new Transfer().execute(TransferAmount, otpNumber);
                } else {
                    Toast.makeText(getContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DebitOkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                debitaccountNumber = debitAccno.getText().toString();
                new getAccountHolder().execute(debitaccountNumber);
            }
        });

        CreditOkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creditAccountNumber = creditAccno.getText().toString();
                new getAccountHolder2().execute(creditAccountNumber);
            }
        });

        return rootView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            try {
                debitName.setText("");
                debitStatus.setText("");
                debitMobile.setText("");
                debitBalance.setText("");
                debitaccountNumber = "";
//                debit_linear.setVisibility(GONE);
                String result_acc_no = data.getStringExtra("result_debit_acc_no");
                debitAccno.setText(result_acc_no);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 1002) {
            try {
                creditName.setText("");
                creditStatus.setText("");
                creditMob.setText("");
                creditAccountNumber = "";
//                credit_linear.setVisibility(GONE);
                String result_acc_no = data.getStringExtra("result_acc_no");
                creditAccno.setText(result_acc_no);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class getAccountHolder extends AsyncTask<String, String, String> {
        final String api_url = ip + "/getAccountHolder";

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
            hashMap.put("account_no", debitaccountNumber);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                String JsonData = data.toString();
                JSONObject reader = new JSONObject(JsonData);
                JSONObject jobj = reader.getJSONObject("account");
                if (jobj.has("data")) {
                    JSONObject jobj1 = jobj.getJSONObject("data");
                    name = jobj1.getString("NAME");
                    acc_status = jobj1.getString("ACC_STATUS");
                    mobile = jobj1.getString("MOBILE");
                    balance = jobj1.getString("CURRENT_BALANCE");
                    msg = "ok";
                }
                if (jobj.has("error")) {
                    error = jobj.getString("error");
                    msg = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String url) {
            dialog.dismiss();
            if (url == "ok") {
                credit_search_btn.setEnabled(true);
                creditAccno.setEnabled(true);
                CreditOkbtn.setEnabled(true);
                debit_linear.setVisibility(View.VISIBLE);
                debitName.setText(name);
                debitStatus.setText(acc_status);
                debitMobile.setText(mobile);
                debitBalance.setText(balance);
            }
            if (url == "error") {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText(error)
                        .show();
            }
        }
    }

    private class getAccountHolder2 extends AsyncTask<String, String, String> {
        final String api_url = ip + "/getAccountHolder";

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
            hashMap.put("account_no", creditAccountNumber);
            JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
            String JsonData = data.toString();
            try {
                JSONObject reader = new JSONObject(JsonData);
                JSONObject jobj = reader.getJSONObject("account");
                if (jobj.has("data")) {
                    JSONObject jobj1 = jobj.getJSONObject("data");
                    cre_name = jobj1.getString("NAME");
                    cre_acc_status = jobj1.getString("ACC_STATUS");
                    cre_mobile = jobj1.getString("MOBILE");
                    msg = "ok";
                }
                if (jobj.has("error")) {
                    error = jobj.getString("error");
                    msg = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return msg;
        }

        @Override
        protected void onPostExecute(String url) {
            dialog.dismiss();
            if (url == "ok") {
                credit_linear.setVisibility(View.VISIBLE);
                amount_transfer_linear.setVisibility(View.VISIBLE);
                creditName.setText(cre_name);
                creditStatus.setText(cre_acc_status);
                creditMob.setText(cre_mobile);
            }
            if (url == "error") {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText(error)
                        .show();
            }
        }
    }

    class AuthOTP extends AsyncTask<String, String, String> {
        final String otpurl = ip + "/OTPGenerate";

        @Override
        public void onPreExecute() {
            dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            dialog.setTitleText("Loading");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", debitaccountNumber);
            hashMap.put("particular", "Transfer");
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
                return "NoInternet";
            }
            return null;
        }

        @Override
        public void onPostExecute(String result) {
            dialog.dismiss();
            if (result == "NoInternet") {
                Toast.makeText(ctx, "NETWORK ERROR", Toast.LENGTH_LONG).show();
            } else {

                authOTP.setEnabled(true);
                otpLinear.setVisibility(View.VISIBLE);
                otp.setVisibility(View.VISIBLE);
                authOTP.setVisibility(View.VISIBLE);

            }
        }
    }


    class Transfer extends AsyncTask<String, String, String> {

        final String transferurl = ip + "/cashTransfer";

        @Override
        public void onPreExecute() {
            dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            dialog.setTitleText("Transferring..");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMapn = new HashMap<>();
            Log.e("otp", otpNumber);
            Log.e("otp-id", otp_id);
            Log.e("otp-id", TransferAmount);
            hashMapn.put("account_no", debitaccountNumber);
            hashMapn.put("beni_account_no", creditAccountNumber);
            hashMapn.put("process_amount", TransferAmount);
            hashMapn.put("otp_val", otpNumber);
            hashMapn.put("otp_id", otp_id);
            hashMapn.put("agent_id", agentId);
            hashMapn.put("transferType", "own");

            try {
                JSONObject serverResponse = jsonParser.makeHttpRequest(transferurl, "POST", hashMapn);
                JSONObject jobj1 = serverResponse.getJSONObject("receipt");
                if (jobj1.has("data")) {
                    JSONObject jobj2 = jobj1.getJSONObject("data");
                    Log.e("Receipt_Deposit", jobj2.toString());
                    receiptTransferDate = jobj2.getString("TRANSFER_DATE");
                    receiptDebitAccno = jobj2.getString("ACC_NO");
                    receiptCreditAccno = jobj2.getString("BEN_ACC_NO");
                    receiptName = jobj2.getString("NAME");
                    receiptMobile = jobj2.getString("MOBILE");
                    tran_id = jobj2.getString("TRAN_ID");
                    receiptOldBalance = jobj2.getString("OLD_BALANCE");
                    receiptWithdrawalAmount = jobj2.getString("WITHDRAWAL_AMOUNT");
                    receiptCurrentBalance = jobj2.getString("CURRENT_BALANCE");
                    return jobj2.toString();
                }
                if (jobj1.has("error")) {
                    return "InvalidOtp";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(String errorCode) {
            if (errorCode == "InvalidOtp") {
                dialog.dismiss();
                authOTP.setEnabled(true);
                Toast.makeText(ctx, "Invalid OTP!", Toast.LENGTH_LONG).show();
            } else {
                dialog.dismiss();
                //Log.e("Printed",errorCode);
                //Toast.makeText(ctx, "errorCode ", Toast.LENGTH_SHORT).show();
                authOTP.setEnabled(false);
                otp.setText("");
                otpLinear.setVisibility(View.GONE);
                otp.setVisibility(View.GONE);
                authOTP.setVisibility(View.GONE);

                Intent intent = new Intent(ctx, Receipt_Transfer.class);
                intent.putExtra("transactionDate", receiptTransferDate);
                intent.putExtra("account_number", receiptDebitAccno);
                intent.putExtra("debit_account_number", receiptCreditAccno);
                intent.putExtra("introName", receiptName);
                intent.putExtra("mobile", receiptMobile);
                intent.putExtra("old_balance", receiptOldBalance);
                intent.putExtra("withdrawalAmount", receiptWithdrawalAmount);
                intent.putExtra("current_balance", receiptCurrentBalance);
                intent.putExtra("tran_id", tran_id);
                startActivity(intent);


                // Create new fragment and transaction
                Fragment newFragment = new Transactions_Transfer();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();

            }

        }


    }
}