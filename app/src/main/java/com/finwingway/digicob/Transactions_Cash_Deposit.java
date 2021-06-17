package com.finwingway.digicob;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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

/**
 * Created by AnVin on 1/6/2017.
 */

public class Transactions_Cash_Deposit extends Fragment {

    private static String agent_id = login.agent_code;
    JSONParser jsonParser;
    SweetAlertDialog dialog;
    public static String ip = login.ip_global;
    ImageButton Cancelbtn, search_image_btn;
    EditText acc_no_edit_text, cash_depo_amount_editText;
    Button deposit_submit_btn, cash_depositsearch_amount_Confirmbtn;
    String[] acc_type_id;
    String error, msg, accNumber;
    LinearLayout linear_search_deposit;
    String searched_acc_no, searched_name, searched_status, searched_phone, search_depo_amount, oldBalance,
            depositAmouut, currentBalance, depositDate, accountNumber, tran_id;
    TextView searched_name_textView, searched_status_textView, searched_phone_textView;
    ProgressDialog progressDialog;
    TextToSpeech tts;
    Context context;
    LayoutInflater inflate;
    String errorgetAcc, responsegetAcc;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_cashdeposit_layout_customer, container, false);
        jsonParser = new JSONParser();
        context = getContext();
        inflater = this.getLayoutInflater(bundle);
        progressDialog = new ProgressDialog(getContext(), DialogInterface.BUTTON_POSITIVE);
        linear_search_deposit = (LinearLayout) rootView.findViewById(R.id.linear_search_deposit);
        linear_search_deposit.setVisibility(GONE);
        cash_depositsearch_amount_Confirmbtn = (Button) rootView.findViewById(R.id.cash_depositsearch_amount_Confirmbtn);
        cash_depositsearch_amount_Confirmbtn.setEnabled(false);
        acc_type_id = getResources().getStringArray(R.array.cashdeposit_acc_type_id);
        acc_no_edit_text = (EditText) rootView.findViewById(R.id.cash_depo_acc_no_edit_text);
        searched_name_textView = (TextView) rootView.findViewById(R.id.cash_deposit_nametxt);
        searched_status_textView = (TextView) rootView.findViewById(R.id.cash_deposit_acc_statusTxt);
        searched_phone_textView = (TextView) rootView.findViewById(R.id.cash_deposit_mobileNumberTxt);
        cash_depo_amount_editText = (EditText) rootView.findViewById(R.id.cash_depositAmountEditText);
        deposit_submit_btn = (Button) rootView.findViewById(R.id.cash_depo_acc_no_submit_btn);
        dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        Cancelbtn = (ImageButton) rootView.findViewById(R.id.dailydepositCANCELbtn);
        Cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        deposit_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searched_acc_no = acc_no_edit_text.getText().toString();
                new getAccountHolder().execute();
            }
        });

        search_image_btn = (ImageButton) rootView.findViewById(R.id.cash_depo_acc_search_btn);
        search_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent debit_intent = new Intent(getActivity(), Account_Search.class);
                startActivityForResult(debit_intent, 1004);
            }
        });

        final LayoutInflater finalInflater = inflater;
        cash_depositsearch_amount_Confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositAmouut = cash_depo_amount_editText.getText().toString();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                View dialogView = finalInflater.inflate(R.layout.alert_deposit_layout, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setNegativeButton("CANCEL", null);
                dialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectionDetector connDetector = new ConnectionDetector(getContext());
                        if (connDetector.isnetAvailable()) {
                            new DepositAmount().execute();
                        } else {
                            Toast.makeText(getContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
                        }
//                        if (connectionStatus) {
//                            ConnectionDetector connDetector = new ConnectionDetector(getContext());
//                            if (connDetector.isnetAvailable()) {
//                                new DepositAmount().execute();
//                            } else {
//                                Toast.makeText(getContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
//                            }
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
//                                            new DepositAmount().execute();
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
                TextView alert_depo_amount_text = (TextView) dialogView.findViewById(R.id.alert_depo_amount_in_text);
                alert_acc_no.setText(acc_no_edit_text.getText().toString());
                alert_acc_name.setText(searched_name);
                alert_acc_mobile.setText(searched_phone);
                alert_depo_amount_text.setText(NumbersToWords(Integer.parseInt(cash_depo_amount_editText.getText().toString())));
                alert_depo_amount.setText(cash_depo_amount_editText.getText().toString());
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        cash_depo_amount_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    cash_depositsearch_amount_Confirmbtn.setEnabled(false);
                } else {
                    cash_depositsearch_amount_Confirmbtn.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    private class getAccountHolder extends AsyncTask<String, String, String> {
        private String api_url = ip + "/getAccountHolder";

        //private String api_url = "http://192.168.0.123:8888/GetAccountHolder";
        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", searched_acc_no);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                String JsonData = data.toString();
                JSONObject reader = new JSONObject(JsonData);
                JSONObject jobj = reader.getJSONObject("account");

                if (jobj.has("error")) {
                    errorgetAcc = jobj.getString("error");
                    responsegetAcc = "error";
                }
                if (jobj.has("data")) {
                    JSONObject jobj1 = jobj.getJSONObject("data");
                    searched_name = jobj1.getString("NAME");
                    searched_phone = jobj1.getString("MOBILE");
                    searched_status = jobj1.getString("ACC_STATUS");
                    responsegetAcc = "ok";
                }
            } catch (Exception e) {
                e.printStackTrace();
                responsegetAcc = "exception";
            }

            return responsegetAcc;
        }

        @Override
        protected void onPostExecute(String url) {

            if (url.equals("error")) {
                progressDialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText(errorgetAcc)
                        .show();
            } else if (url.equals("exception")) {
                progressDialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText("Something went wrong!")
                        .show();
            } else if (url.equals("ok")) {
                progressDialog.dismiss();
                searched_status_textView.setText(searched_status);
                searched_phone_textView.setText(searched_phone);
                searched_name_textView.setText(searched_name);
                linear_search_deposit.setVisibility(VISIBLE);
                accountNumber = acc_no_edit_text.getText().toString();

            }
        }
    }

    class DepositAmount extends AsyncTask<String, String, String> {
        private final String depositUrl = ip + "/cashDeposit";

        @Override
        public void onPreExecute() {
            Log.e("DEPOSIT AMOUNT", depositAmouut);
            dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            dialog.setTitleText("Depositing..");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMapAmount = new HashMap<>();
            hashMapAmount.put("agent_id", agent_id);
            hashMapAmount.put("account_no", searched_acc_no);
            hashMapAmount.put("deposit_amount", depositAmouut);
            hashMapAmount.put("particular", "FromApp");
            JSONObject jsonObject = jsonParser.makeHttpRequest(depositUrl, "POST", hashMapAmount);
            String JsonDataString = jsonObject.toString();
            try {
                JSONObject reader = new JSONObject(JsonDataString);
                JSONObject receipt = reader.getJSONObject("receipt");
                if (receipt.has("error")) {
                    error = receipt.getString("error");
                    msg = "error";
                }
                if (receipt.has("data")) {
                    JSONObject data = receipt.getJSONObject("data");
                    accNumber = data.getString("ACC_NO");
                    oldBalance = data.getString("OLD_BALANCE");
                    depositAmouut = data.getString("DEPOSIT_AMOUNT");
                    currentBalance = data.getString("CURRENT_BALANCE");
                    depositDate = data.getString("DEPOSIT_DATE");
                    tran_id = data.getString("TRAN_ID");
                    msg = "ok";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                msg = "exception";
            }
            return msg;
        }

        @Override
        public void onPostExecute(String url) {
            if (url == "error") {
                dialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText(error)
                        .show();
            }
            if (url == "exception") {
                dialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText("Something went wrong!")
                        .show();
            }
            if (url == "ok") {
                dialog.dismiss();
                cash_depositsearch_amount_Confirmbtn.setEnabled(false);
                Intent intent = new Intent(getActivity(), Receipt_Deposit_Leopard.class);
                //Intent intent = new Intent(getActivity(),Receipt_Deposit.class);
                intent.putExtra("account_number", accNumber);
                intent.putExtra("introName", searched_name);
                intent.putExtra("mobile", searched_phone);
                intent.putExtra("old_balance", oldBalance);
                intent.putExtra("deposit_amount", depositAmouut);
                intent.putExtra("current_balance", currentBalance);
                intent.putExtra("deposit_date", depositDate);
                intent.putExtra("tran_id", tran_id);
                intent.putExtra("withdrawal", "");
                startActivityForResult(intent, 100);
                TextToSpeech(searched_name, Integer.parseInt(depositAmouut));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1004 && resultCode == RESULT_OK) {
            try {
                linear_search_deposit.setVisibility(GONE);
                cash_depo_amount_editText.setText(null);
                acc_no_edit_text.setText(null);

                String result_acc_no = data.getStringExtra("result_acc_no");
                acc_no_edit_text.setText(result_acc_no);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 100 && resultCode == RESULT_CANCELED) {
            linear_search_deposit.setVisibility(GONE);
            cash_depo_amount_editText.setText(null);
            acc_no_edit_text.setText(null);
        }
    }

    public void TextToSpeech(final String name, final int rupees) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(new Locale("en", "IN"));
                    tts.setSpeechRate(.8f);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    } else {
                        tts.speak(name + " deposited " + NumbersToWords(rupees) + " by cash", TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });
    }

    public static String NumbersToWords(int Number) {
        if (Number > 0) {
            int inputNo = Number;

            if (inputNo == 0)
                return "Zero";

            int[] numbers = new int[4];
            int first = 0;
            int u, h, t;
            StringBuilder sb = new StringBuilder();

            if (inputNo < 0) {
                sb.append("Minus ");
                inputNo = -inputNo;
            }

            String[] words0 = {"", "One ", "Two ", "Three ", "Four ",
                    "Five ", "Six ", "Seven ", "Eight ", "Nine "};
            String[] words1 = {"Ten ", "Eleven ", "Twelve ", "Thirteen ", "Fourteen ",
                    "Fifteen ", "Sixteen ", "Seventeen ", "Eighteen ", "Nineteen "};
            String[] words2 = {"Twenty ", "Thirty ", "Forty ", "Fifty ", "Sixty ",
                    "Seventy ", "Eighty ", "Ninety "};
            String[] words3 = {"Thousand ", "Lakh ", "Crore "};

            numbers[0] = inputNo % 1000; // units
            numbers[1] = inputNo / 1000;
            numbers[2] = inputNo / 100000;
            numbers[1] = numbers[1] - 100 * numbers[2]; // thousands
            numbers[3] = inputNo / 10000000; // crores
            numbers[2] = numbers[2] - 100 * numbers[3]; // lakhs

            for (int i = 3; i > 0; i--) {
                if (numbers[i] != 0) {
                    first = i;
                    break;
                }
            }
            for (int i = first; i >= 0; i--) {
                if (numbers[i] == 0) continue;
                u = numbers[i] % 10; // ones
                t = numbers[i] / 10;
                h = numbers[i] / 100; // hundreds
                t = t - 10 * h; // tens
                if (h > 0) sb.append(words0[h] + "Hundred ");
                if (u > 0 || t > 0) {
                    if (h > 0 || (i == 0 && first > 0)) {
                        sb.append("and ");
                    }
                    if (t == 0) {
                        sb.append(words0[u]);
                    } else if (t == 1)
                        sb.append(words1[u]);
                    else
                        sb.append(words2[t - 2] + words0[u]);
                }
                if (i != 0) sb.append(words3[i - 1]);
            }
            return sb.toString().trim() + "  Rupees Only";
        } else {
            return "";
        }
    }

}
