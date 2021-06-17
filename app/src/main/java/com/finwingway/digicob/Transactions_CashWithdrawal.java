package com.finwingway.digicob;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import com.finwingway.digicob.support.ConnectionDetector;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.finwingway.digicob.Transactions_Cash_Deposit.NumbersToWords;
import static com.finwingway.digicob.login.agent_code;

/**
 * Created by AnVin on 12/30/2016.
 */

public class Transactions_CashWithdrawal extends Fragment implements View.OnClickListener {
    Button Okbtn, confirmbtn, checkAccountHolde;
    ImageButton Cancelbtn, search_image_btn;
    SweetAlertDialog pDialog;
    EditText accnoEditText, withdrawalAmountEditText, otpEditText;
    JSONParser jsonParser;
    String name, mobile, acc_status, currentBalance, withdrawalDate, withdrawalAmount, tran_id, oldBalance, otpNumber, otp_id, acc_mobile;
    public static String ip = login.ip_global;
    TextView nameTextView, status, balance, acc_mobile_textView;
    private static String agent_id = agent_code;
    LinearLayout amount, linear_acc_detail;
    LinearLayout linear_otp;
    String acc_name, acc_scheme, acc_type, error, account_name, account_number, account_account_phone;
    String[] acc_type_id, CustomernameArray, CustomerAccountNumberArray, CustomerMobileNumberArray;
    TextToSpeech tts;
    LayoutInflater layoutInflater;
    String WithdrawalAmount, otpmsg;
    String errMsg = "";

    @Override
    public View onCreateView(LayoutInflater inflator, final ViewGroup container, Bundle bundle) {
        final View rootView = inflator.inflate(R.layout.fragment_transaction_cashwithdrawal_layout, container, false);
        jsonParser = new JSONParser();
        layoutInflater = this.getLayoutInflater(bundle);
        acc_type_id = getResources().getStringArray(R.array.cashdeposit_acc_type_id);
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        confirmbtn = (Button) rootView.findViewById(R.id.with_CONFIRMbtn);
        confirmbtn.setEnabled(false);
        Okbtn = (Button) rootView.findViewById(R.id.withdrawalAmountConfirm);
        Okbtn.setEnabled(false);
        Cancelbtn = (ImageButton) rootView.findViewById(R.id.with_CANCELbtn);
        accnoEditText = (EditText) rootView.findViewById(R.id.accNoEditTextWithdrawal);
        nameTextView = (TextView) rootView.findViewById(R.id.withdrawalaccName);
        status = (TextView) rootView.findViewById(R.id.withdrawalAccStatus);
        balance = (TextView) rootView.findViewById(R.id.withdrawalBalance);
        otpEditText = (EditText) rootView.findViewById(R.id.otpnumberEditText);
        acc_mobile_textView = (TextView) rootView.findViewById(R.id.withdrawal_mobileNumberTxt);
        Cancelbtn.setOnClickListener(this);
        linear_otp = (LinearLayout) rootView.findViewById(R.id.with_linear_otp);
        amount = (LinearLayout) rootView.findViewById(R.id.linear_withdrawal_amount);
        linear_acc_detail = (LinearLayout) rootView.findViewById(R.id.linear_account_details_withdrawal);
        confirmbtn.setEnabled(false);
        linear_acc_detail.setVisibility(GONE);
        amount.setVisibility(GONE);
        linear_otp.setVisibility(GONE);
        confirmbtn.setVisibility(GONE);

        search_image_btn = (ImageButton) rootView.findViewById(R.id.with_search_image_btn);
        otpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().trim().length() == 0) {
                    confirmbtn.setEnabled(false);
                } else {
                    confirmbtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        withdrawalAmountEditText = (EditText) rootView.findViewById(R.id.withdrawalAmount);
        withdrawalAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    Okbtn.setEnabled(false);
                } else {
                    Okbtn.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        checkAccountHolde = (Button) rootView.findViewById(R.id.getAcoountbtnWithdrawal);
        checkAccountHolde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withdrawalAmountEditText.setEnabled(true);
                new getAccountHolder().execute();
            }
        });

        Okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!currentBalance.equals("0") &&
                            Float.parseFloat(currentBalance) > Integer.parseInt(withdrawalAmountEditText.getText().toString())) {
                        new AuthOTP().execute();
                        WithdrawalAmount = withdrawalAmountEditText.getText().toString();
                        withdrawalAmountEditText.setEnabled(false);
                    } else {
                        Toast.makeText(getContext(), "Insufficient Balance!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Enter Valid Amount!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        final LayoutInflater finalInflater = layoutInflater;
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                View dialogView = finalInflater.inflate(R.layout.alert_deposit_layout, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setNegativeButton("CANCEL", null);
                dialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ConnectionDetector connDetector = new ConnectionDetector(getContext());
                        if (connDetector.isnetAvailable()) {
                            otpEditText = (EditText) rootView.findViewById(R.id.otpnumberEditText);
                            otpNumber = otpEditText.getText().toString();
                            new CashWithdrawal().execute(otpNumber);
                        } else {
                            Toast.makeText(getContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
                        }


//                        if (connectionStatus) {
//                            otpEditText = (EditText) rootView.findViewById(R.id.otpnumberEditText);
//                            otpNumber = otpEditText.getText().toString();
//                            new CashWithdrawal().execute(otpNumber);
//                        } else {
//                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
//                                    .setTitleText("BT Device Not Connected")
//                                    .setContentText("are you sure you want to continue?")
//                                    .setCancelText("NO")
//                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                            sweetAlertDialog.dismiss();
//                                        }
//                                    })
//                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                            otpEditText = (EditText) rootView.findViewById(R.id.otpnumberEditText);
//                                            otpNumber = otpEditText.getText().toString();
//                                            new CashWithdrawal().execute(otpNumber);
//                                            sweetAlertDialog.dismiss();
//                                        }
//                                    }).show();
//                        }
                    }
                });
                TextView alert_acc_no = (TextView) dialogView.findViewById(R.id.alert_depo_acc_no);
                TextView alert_acc_name = (TextView) dialogView.findViewById(R.id.alert_depo_acc_name);
                TextView alert_acc_mobile = (TextView) dialogView.findViewById(R.id.alert_depo_mobile);
                TextView alert_depo_amount = (TextView) dialogView.findViewById(R.id.alert_depo_amount);
                TextView alert_caption_amount = (TextView) dialogView.findViewById(R.id.alert_caption_amount);
                alert_caption_amount.setText("Withdrawal Amount        ");
                TextView alert_depo_amount_text = (TextView) dialogView.findViewById(R.id.alert_depo_amount_in_text);
                alert_acc_no.setText(accnoEditText.getText().toString());
                alert_acc_name.setText(name);
                alert_acc_mobile.setText(acc_mobile);
                alert_depo_amount_text.setText(NumbersToWords(Integer.parseInt(withdrawalAmountEditText.getText().toString())));
                alert_depo_amount.setText(withdrawalAmountEditText.getText().toString());
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
        search_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent debit_intent = new Intent(getActivity(), Account_Search.class);
                startActivityForResult(debit_intent, 1005);
            }
        });

        return rootView;

    }


    public void onClick(View view) {
        if (view == Cancelbtn) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        }
    }


    class getAccountHolder extends AsyncTask<String, String, String> {
        String api_url = ip + "/getAccountHolder";

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String accountNumber = accnoEditText.getText().toString();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", accountNumber);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                String JsonData = data.toString();
                JSONObject reader = new JSONObject(JsonData);
                JSONObject jobj = reader.getJSONObject("account");
                JSONObject jobj1 = jobj.getJSONObject("data");
                name = jobj1.getString("NAME");
                acc_status = jobj1.getString("ACC_STATUS");
                acc_mobile = jobj1.getString("MOBILE");
                currentBalance = jobj1.getString("CURRENT_BALANCE");
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String url) {
            if (url == "error") {
                pDialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText("Invalid Account Number")
                        .show();
            } else {
                pDialog.dismiss();
                amount.setVisibility(View.VISIBLE);
                linear_acc_detail.setVisibility(View.VISIBLE);
                status.setText(acc_status);
                nameTextView.setText(name);
                balance.setText(currentBalance);
                acc_mobile_textView.setText(acc_mobile);
            }
        }
    }


    class AuthOTP extends AsyncTask<String, String, String> {
        final String otpurl = ip + "/OTPGenerate";
        String accountNumber = accnoEditText.getText().toString();

        @Override
        public void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String msg = "";
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("amount", WithdrawalAmount);
            hashMap.put("agent_id", agent_code);
            hashMap.put("account_no", accountNumber);
            hashMap.put("particular", "Withdrawal");
            try {
                JSONObject serverResponce = jsonParser.makeHttpRequest(otpurl, "POST", hashMap);
                String jsonData = serverResponce.toString();
                Log.e("data=>", jsonData);
                JSONObject jsonDataObj = new JSONObject(jsonData);
                JSONObject jobj1 = jsonDataObj.getJSONObject("otp");
                if (jobj1.has("data")) {
                    otp_id = jobj1.getString("otp_id");
                    msg = "ok";
                    Log.e("OTP-ID", otp_id);
                }
                if (jobj1.has("error")) {
                    otpmsg = jobj1.getString("error");
                    msg = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "NoInternet";
            }
            return msg;
        }

        @Override
        public void onPostExecute(String result) {
            pDialog.dismiss();
            if (result.equals("ok")) {
                linear_otp.setVisibility(View.VISIBLE);
                confirmbtn.setVisibility(VISIBLE);
            }
            if (result.equals("error")) {
                Toast.makeText(getActivity(), otpmsg, Toast.LENGTH_LONG).show();
            }
            if (result.equals("NoInternet")) {
                Toast.makeText(getActivity(), "NETWORK ERROR", Toast.LENGTH_LONG).show();
            } else {
            }
        }
    }

    class getAccountNumbers extends AsyncTask<String, String, String> {
        private String api_getCustomers = ip + "/getCustUnderAgent";

        @Override
        public void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("introName", acc_name);
            hashMap.put("acc_type", acc_type);
            //hashMap.put("acc_cat",acc_scheme);
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
                    CustomerAccountNumberArray = new String[jsonArray.length()];
                    CustomerMobileNumberArray = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arrayObject = jsonArray.getJSONObject(i);
                        account_name = arrayObject.getString("CUST_NAME");
                        CustomernameArray[i] = account_name;
                        Log.e("introName", account_name);
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
        public void onPostExecute(String result) {
            if (result == "ok") {
            }
            if (result == "error") {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        }
    }


    class CashWithdrawal extends AsyncTask<String, String, String> {
        private final String depositUrl = ip + "/cashWithdrawal";
        String accountNumber = accnoEditText.getText().toString();
        String otp = otpEditText.getText().toString();
        String amount = withdrawalAmountEditText.getText().toString();

        @Override
        public void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Processing..");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMapAmount = new HashMap<>();
            hashMapAmount.put("agent_id", agent_id);
            hashMapAmount.put("account_no", accountNumber);
            hashMapAmount.put("withdrawal_amount", amount);
            hashMapAmount.put("auth_mode", amount);
            hashMapAmount.put("otp_val", otp);
            hashMapAmount.put("otp_id", otp_id);
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(depositUrl, "POST", hashMapAmount);
                String JsonDataString = jsonObject.toString();
                JSONObject reader = new JSONObject(JsonDataString);
                JSONObject receipt = reader.getJSONObject("receipt");
                if (receipt.has("data")) {
                    JSONObject data = receipt.getJSONObject("data");
                    mobile = data.getString("MOBILE");
                    oldBalance = data.getString("OLD_BALANCE");
                    tran_id = data.getString("TRAN_ID");
                    withdrawalAmount = data.getString("WITHDRAWAL_AMOUNT");
                    currentBalance = data.getString("CURRENT_BALANCE");
                    withdrawalDate = data.getString("WITHDRAWAL_DATE");
                    return "ok";
                }
                if (receipt.has("error")) {
                    errMsg = receipt.getString("error");
                    return "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Jsonerror";
            }
            return null;
        }

        @Override
        public void onPostExecute(String url) {
            if (url == "Jsonerror") {
                pDialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText("Invalid Amount")
                        .show();
            }
            if (url == "error") {
                pDialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText(errMsg.equals("") ? "Something went wrong" : errMsg)
                        .show();
            } else {
                pDialog.dismiss();
                Intent intent = new Intent(getActivity(), Receipt_Deposit_Leopard.class);
                intent.putExtra("withdrawal", "WITH");
                intent.putExtra("account_number", accountNumber);
                intent.putExtra("introName", name);
                intent.putExtra("mobile", mobile);
                intent.putExtra("old_balance", oldBalance);
                intent.putExtra("withdrawalAmount", withdrawalAmount);
                intent.putExtra("current_balance", currentBalance);
                intent.putExtra("withdrawalDate", withdrawalDate);
                intent.putExtra("tran_id", tran_id);
                TextToSpeech(name, Integer.parseInt(withdrawalAmount));
                startActivityForResult(intent, 100);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1005 && resultCode == RESULT_OK) {
            try {
                String result_acc_no = data.getStringExtra("result_acc_no");
                accnoEditText.setText(result_acc_no);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 100 && resultCode == RESULT_CANCELED) {
            linear_otp.setVisibility(GONE);
            amount.setVisibility(View.GONE);
            linear_acc_detail.setVisibility(View.GONE);
            accnoEditText.setText(null);
            otpEditText.setText(null);
            withdrawalAmountEditText.setText(null);
        }
    }

    public void TextToSpeech(final String name, final int rupees) {
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(new Locale("en", "IN"));
                    tts.setSpeechRate(.8f);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    } else {
                        tts.speak(NumbersToWords(rupees) + " debited from " + name + "'s account", TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });
    }
}