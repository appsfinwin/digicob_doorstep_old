package com.finwingway.digicob;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import com.finwingway.digicob.support.ConnectionDetector;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.finwingway.digicob.GlobalApplicationConstants.getCustomerAccountNumber;
import static com.finwingway.digicob.GlobalApplicationConstants.getCustomerName;
import static com.finwingway.digicob.Transactions_Cash_Deposit.NumbersToWords;

/**
 * Created by AnVin on 1/6/2017.
 */

public class Transactions_Daily_Deposit extends Fragment {
    private static String agent_id = login.agent_code;
    private int mPosition;
    JSONParser jsonParser = new JSONParser();
    Button Okbtn, confirmBtn;
    ImageButton Cancelbtn;
    TextView nameText, accountStatus, mobileNumber;
    EditText accountNumer, depositAmount;
    String name, mobile, acc_status, oldBalance, depositAmouut, currentBalance, depositDate, tran_id;
    SweetAlertDialog pDialog;
    LinearLayout amount;
    public static String ip = login.ip_global;
    private static String api_url = ip + "/getAccountHolder";
    LinearLayout account_detail, amount_linear;
    String account_number = "", accountNumber,accNumber;
    TextToSpeech tts;
    AlertDialog.Builder builder;
    LayoutInflater inflater;
    ArrayAdapter arrayAdapter;
    String error, response,errorgetAcc,responsegetAcc;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View customerView = inflater.inflate(R.layout.fragment_transaction_dailydeposit_layout_customer, container, false);
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        final ListView lv;
        builder = new AlertDialog.Builder(getContext());
        inflater = this.getLayoutInflater(bundle);
        lv = (ListView) customerView.findViewById(R.id.list_view);
        Okbtn = (Button) customerView.findViewById(R.id.accNoValidationbtn);
        Cancelbtn = (ImageButton) customerView.findViewById(R.id.cashdepositCANCELbtn);
        confirmBtn = (Button) customerView.findViewById(R.id.cash_depositConfirmbtn);
        confirmBtn.setEnabled(false);
        account_detail = (LinearLayout) customerView.findViewById(R.id.linear_account_details_cash_deposit);
        amount_linear = (LinearLayout) customerView.findViewById(R.id.linear_amount_deposit);
        account_detail.setVisibility(View.GONE);
        amount_linear.setVisibility(View.GONE);
        lv.setVisibility(GONE);

        Okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getAccountHolder().execute();
            }
        });
        Cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        final LayoutInflater finalInflater = inflater;
        confirmBtn.setOnClickListener(new View.OnClickListener() {
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
                TextView alert_depo_amount_text = (TextView) dialogView.findViewById(R.id.alert_depo_amount_in_text);
                account_number = accountNumber;
                alert_acc_no.setText(account_number);
                alert_acc_name.setText(name);
                alert_acc_mobile.setText(mobile);
                alert_depo_amount_text.setText(NumbersToWords(Integer.parseInt(depositAmount.getText().toString())));
                alert_depo_amount.setText(depositAmount.getText().toString());
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        nameText = (TextView) customerView.findViewById(R.id.nameDeposittxt);
        accountStatus = (TextView) customerView.findViewById(R.id.deposit_statusTxt);
        mobileNumber = (TextView) customerView.findViewById(R.id.deposit_mobileNumberTxt);
        accountNumer = (EditText) customerView.findViewById(R.id.deposit_acNumberEditText);
        try {
            arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, getCustomerName());
            lv.setAdapter(arrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        accountNumer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lv.setVisibility(GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                try {
                    int num = Integer.parseInt(text);
                    Okbtn.setVisibility(VISIBLE);

                } catch (NumberFormatException e) {
                    Okbtn.setVisibility(GONE);
                    lv.setVisibility(View.VISIBLE);
                    arrayAdapter.getFilter().filter(s);
                }
                if (accountNumer.getText().toString().trim().equals("")) {
                    lv.setVisibility(GONE);
                    Okbtn.setVisibility(VISIBLE);
                    account_number = "";
                } else {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        depositAmount = (EditText) customerView.findViewById(R.id.depositAmountEditText);
        depositAmount.setEnabled(false);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = lv.getItemAtPosition(position).toString();
                accountNumer.setText(item);
                int ArrayPosition = indexOfString(item, getCustomerName());
                account_number = getCustomerAccountNumber()[ArrayPosition];
                lv.setVisibility(GONE);
                Okbtn.setVisibility(VISIBLE);
            }
        });

        depositAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    confirmBtn.setEnabled(false);
                } else {
                    confirmBtn.setEnabled(true);
                }
                confirmBtn.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return customerView;
    }


    public int indexOfString(String searchString, String[] domain) {
        int i;
        for (i = 0; i < domain.length; i++)
            if (searchString.equals(domain[i])) {
                return i;
            }
        return -1;
    }


    class getAccountHolder extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (account_number.equals("")) {
                accountNumber = accountNumer.getText().toString();
            } else {
                accountNumber = account_number;
            }
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("account_no", accountNumber);
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
                    name = jobj1.getString("NAME");
                    mobile = jobj1.getString("MOBILE");
                    acc_status = jobj1.getString("ACC_STATUS");
                    responsegetAcc ="ok";
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
                pDialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText(errorgetAcc)
                        .show();
            }
            else if (url.equals("exception")) {
                pDialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText("Something went wrong!")
                        .show();
            } else if (url.equals("ok")) {
                pDialog.dismiss();
                account_detail.setVisibility(VISIBLE);
                amount_linear.setVisibility(VISIBLE);
                depositAmount.setEnabled(true);
                accountStatus.setText(acc_status);
                mobileNumber.setText(mobile);
                nameText.setText(name);
            }
        }
    }

    private class DepositAmount extends AsyncTask<String, String, String> {
        private final String depositUrl = ip + "/cashDeposit";

        @Override
        public void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Depositing..");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String amount = depositAmount.getText().toString();
            HashMap<String, String> hashMapAmount = new HashMap<>();
            hashMapAmount.put("agent_id", agent_id);
            hashMapAmount.put("account_no", accountNumber);
            hashMapAmount.put("deposit_amount", amount);
            hashMapAmount.put("particular", "FromApp");
            JSONObject jsonObject = jsonParser.makeHttpRequest(depositUrl, "POST", hashMapAmount);
            String JsonDataString = jsonObject.toString();
            try {
                JSONObject reader = new JSONObject(JsonDataString);
                JSONObject receipt = reader.getJSONObject("receipt");

                if (receipt.has("error")) {
                    error = receipt.getString("error");
                    response = "error";
                }
                if (receipt.has("data")) {
                    JSONObject data = receipt.getJSONObject("data");
                    accNumber = data.getString("ACC_NO");
                    oldBalance = data.getString("OLD_BALANCE");
                    depositAmouut = data.getString("DEPOSIT_AMOUNT");
                    currentBalance = data.getString("CURRENT_BALANCE");
                    depositDate = data.getString("DEPOSIT_DATE");
                    tran_id = data.getString("TRAN_ID");
                    response = "ok";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                response = "exception";
            }
            return response;
        }

        @Override
        public void onPostExecute(String url) {
            if (url.equals("error")) {
                pDialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText(error)
                        .show();
            }
            else if (url.equals("exception")) {
                pDialog.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText("Something went wrong!")
                        .show();
            } else if (url.equals("ok")) {
                pDialog.dismiss();
                confirmBtn.setEnabled(false);
                Intent intent = new Intent(getActivity(), Receipt_Deposit_Leopard.class);
                //Intent intent = new Intent(getActivity(),Receipt_Deposit.class);
                intent.putExtra("account_number", accNumber);
                intent.putExtra("introName", name);
                intent.putExtra("mobile", mobile);
                intent.putExtra("old_balance", oldBalance);
                intent.putExtra("deposit_amount", depositAmouut);
                intent.putExtra("current_balance", currentBalance);
                intent.putExtra("deposit_date", depositDate);
                intent.putExtra("tran_id", tran_id);
                intent.putExtra("withdrawal", "");
                TextToSpeech(name, Integer.parseInt(depositAmouut));
                startActivityForResult(intent, 100);
            }
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
                        tts.speak(name + " deposited " + NumbersToWords(rupees) + " by cash", TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_CANCELED) {
            account_detail.setVisibility(GONE);
            amount_linear.setVisibility(GONE);
            accountNumer.setText(null);
            depositAmount.setText(null);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

