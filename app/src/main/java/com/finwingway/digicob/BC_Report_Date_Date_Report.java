package com.finwingway.digicob;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Anvin on 12/20/2016.
 */
@SuppressLint("ValidFragment")
public class BC_Report_Date_Date_Report extends Fragment implements DatePickerDialog.OnDateSetListener {
    Button editText, editText2;
    private FragmentActivity myContext;
    DatePickerDialog dpd, dpd1;
    static String fromDate = null, toDate = null;
    String jtotaldepo, jtotalwith, jtotalcusCreated, jtotalaccCreated, jtotalcheque, jtotalcashinhand;
    public static String agent_id = login.agent_code;
    public static String ip = login.ip_global;
    private static String api_url = ip + "/BCReport";
    JSONParser jsonParser;
    SweetAlertDialog pDialog;
    String type = "DTD";
    TextView depositTextView, withdrawalTextView, customersTextView, accountsTextView, chequeTextView, cashTextView;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bc_report_date_date_layout, container, false);
        final FragmentManager fragManager = myContext.getFragmentManager();
        fromDate = null;
        toDate = null;
        editText = (Button) rootView.findViewById(R.id.datePicker);
        editText2 = (Button) rootView.findViewById(R.id.datePicker2);
        jsonParser = new JSONParser();
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        depositTextView = (TextView) rootView.findViewById(R.id.bcTotalDep);
        withdrawalTextView = (TextView) rootView.findViewById(R.id.bcWithdrawals);
        customersTextView = (TextView) rootView.findViewById(R.id.bcCusomersCreated);
        accountsTextView = (TextView) rootView.findViewById(R.id.bcAccountsCreated);
        chequeTextView = (TextView) rootView.findViewById(R.id.bcCheques);
        cashTextView = (TextView) rootView.findViewById(R.id.bcCashInHand);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                dpd = DatePickerDialog.newInstance(
                        BC_Report_Date_Date_Report.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setTitle("SELECT FROM DATE");
                dpd.setThemeDark(true);
                dpd.vibrate(true);
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.show(fragManager, "Datepickerdialog");
            }
        });

        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now1 = Calendar.getInstance();
                dpd1 = DatePickerDialog.newInstance(
                        BC_Report_Date_Date_Report.this,
                        now1.get(Calendar.YEAR),
                        now1.get(Calendar.MONTH),
                        now1.get(Calendar.DAY_OF_MONTH)
                );
                dpd1.setTitle("SELECT TO DATE");
                dpd1.setThemeDark(true);
                dpd1.vibrate(true);
                dpd1.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd1.show(fragManager, "Datepickerdialog");
            }
        });
        Button okBtn = (Button) rootView.findViewById(R.id.bcDatetoDatebtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromDate == null || toDate == null) {
                    Toast.makeText(myContext, "Please select FROM DATE and TO DATE", Toast.LENGTH_SHORT).show();
                } else {
                    {
                        //Toast.makeText(myContext, fromDate+"  "+toDate,Toast.LENGTH_SHORT).show();
                        new getBCReport().execute();

                    }
                }
            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + (++monthOfYear) + "-" + dayOfMonth;
        if (view == dpd) {
            fromDate = date;
            editText.setText(date);
            editText.setTextColor(Color.RED);
        }
        if (view == dpd1) {
            toDate = date;
            editText2.setText(date);
            editText2.setTextColor(Color.RED);
        }
    }

    class getBCReport extends AsyncTask<String, String, String> {
        @Override
        public void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading..");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMapAmount = new HashMap<>();
            hashMapAmount.put("agent_id", agent_id);
            hashMapAmount.put("date_from", fromDate);
            hashMapAmount.put("date_to", toDate);
            hashMapAmount.put("introtype", type);
            JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "POST", hashMapAmount);
            String JsonDataString = jsonObject.toString();
            Log.e("BC_Report", JsonDataString);
            try {
                JSONObject reader = new JSONObject(JsonDataString);
                JSONObject receipt = reader.getJSONObject("bc_report");
                JSONObject data = receipt.getJSONObject("data");
                jtotaldepo = data.getString("TOTAL_DEPOSITS");
                jtotalwith = data.getString("TOTAL_WITHDRAWAL");
                jtotalcusCreated = data.getString("NO_OF_CUST_CREATED");
                jtotalaccCreated = data.getString("NO_OF_ACC_CREATED");
                jtotalcheque = data.getString("NO_OF_CHEQUES_COLLECTED");
                jtotalcashinhand = data.getString("CASH_IN_HAND");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(String url) {
            depositTextView.setText(jtotaldepo);
            withdrawalTextView.setText(jtotalwith);
            customersTextView.setText(jtotalcusCreated);
            accountsTextView.setText(jtotalaccCreated);
            chequeTextView.setText(jtotalcheque);
            cashTextView.setText(jtotalcashinhand);
            pDialog.dismiss();


        }
    }

}

