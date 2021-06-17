package com.finwingway.digicob.loanModule.JLGwithSplit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.graphics.Color.RED;

@SuppressLint("ValidFragment")
public class ClosingAdapterThree extends Fragment implements
        DatePickerDialog.OnDateSetListener, Validator.ValidationListener {
    public static String ip = login.ip_global;
    SweetAlertDialog proDialog, errorDialog, succDialog;
    Button prev, btnSubmit, btnInstrmtDate;
    TextView tvParticulers, tvInstrmt_nu, tvAmnt;
    View rootView;
    RequestQueue requestQueue;
    JSONParser jsonParser;
    ViewPager viewPagerClsng = MainClosingLoan.viewPagerClsng;
    private FragmentActivity myContext;
    DatePickerDialog dpd;
    String StrLoan_date,
            responseMsg, alertMsg;
    Spinner spinerInstrmtType;
    public static String StrInstrmtType;
    static String TrType;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @SuppressLint("ValidFragment")
    public ClosingAdapterThree(String strClsgType) {
        TrType = strClsgType;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_jlg_clsng_three, container, false);
        jsonParser = new JSONParser();
        requestQueue = Volley.newRequestQueue(getContext());
        final FragmentManager fragManager = myContext.getFragmentManager();
        tvParticulers = rootView.findViewById(R.id.txt_particulers_clsng);
        tvInstrmt_nu = rootView.findViewById(R.id.tv_instrmt_nu_clsng);
        tvAmnt = rootView.findViewById(R.id.tv_amnt_clsng);

//        linearLaySplit = rootView.findViewById(R.id.linear_lay_split);
//        if (TrType.equals("SPLIT")) {
//            linearLaySplit.setVisibility(View.VISIBLE);
//        } else {
//            linearLaySplit.setVisibility(View.GONE);
//        }

        viewPagerClsng.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.e("viewPagerTrans position", String.valueOf(position));
                if (position == 2) {
                    ArrayAdapter brachAdapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, ClosingAdapterOne.arrayInstrmntName);
                    spinerInstrmtType.setAdapter(brachAdapter2);
                    tvAmnt.setText(ClosingAdapterGetSet.getTotalRemitncAmnt());
                }
            }
        });
        spinerInstrmtType = rootView.findViewById(R.id.spinner_instrmt_type_clsng);
        ArrayAdapter brachAdapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, ClosingAdapterOne.arrayInstrmntName);
        spinerInstrmtType.setAdapter(brachAdapter2);
        spinerInstrmtType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrInstrmtType = ClosingAdapterOne.arrayInstrmntCode[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        prev = rootView.findViewById(R.id.btn_pre_three_clsng);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerClsng.setCurrentItem(1, true);
            }
        });
        btnInstrmtDate = rootView.findViewById(R.id.btn_instrmt_date_clsng);
        btnInstrmtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(fragManager);
            }
        });

        btnSubmit = rootView.findViewById(R.id.btn_trns_submit_clsng);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SubmitJLGLoanClosing().execute();
            }
        });

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String CurrentDate = df.format(c.getTime());
        btnInstrmtDate.setText(CurrentDate);
        return rootView;
    }

    @Override
    public void onValidationSucceeded() {
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        StrLoan_date = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
        btnInstrmtDate.setText(StrLoan_date);
        btnInstrmtDate.setTextColor(RED);
        btnInstrmtDate.setTextSize(18);
    }

    public void openDialog(FragmentManager fragManager) {
        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                ClosingAdapterThree.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setTitle("SELECT FROM DATE");
        dpd.setThemeDark(true);
        dpd.vibrate(true);
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.show(fragManager, "Datepickerdialog");
    }


    @SuppressLint("StaticFieldLeak")
    private class SubmitJLGLoanClosing extends AsyncTask<String, String, String> {
        private String api_url = ip + "/JLGLoanTransaction";
        String groupData = "";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();

            groupData = RemittanceData();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Ln_Type", TrType);
            hashMap.put("Brcode", login.branch_id);
            hashMap.put("Tran_no", "");
            hashMap.put("Schcode", ClosingAdapterGetSet.getSchCode());
            hashMap.put("Accno", ClosingAdapterGetSet.getAccountNo());
            hashMap.put("Trdate", ClosingAdapterGetSet.getDate());
            hashMap.put("Transacteddate", ClosingAdapterGetSet.getDate());
            hashMap.put("Effectivedate", ClosingAdapterGetSet.getEffctDate());
            hashMap.put("TranType", ClosingAdapterGetSet.getTransType());
            hashMap.put("SubTranType", ClosingAdapterGetSet.getSubTransType());
            hashMap.put("Amount", tvAmnt.getText().toString());
            hashMap.put("TransferAccNo", ClosingAdapterGetSet.getTransAccountNo());
            hashMap.put("Narration", tvParticulers.getText().toString());
            hashMap.put("InstrumentNo", tvInstrmt_nu.getText().toString());
            hashMap.put("InstrumentDate", btnInstrmtDate.getText().toString());
            hashMap.put("InstrumentType", StrInstrmtType);
            hashMap.put("MakerId", login.agent_code);
            hashMap.put("Makingtime", "");
            hashMap.put("CheckerId", "");
            hashMap.put("Checkingtime", "");
            hashMap.put("flag", "INSERT");
            hashMap.put("IsClosing", "Y");
            hashMap.put("AccountDetails", groupData);
            hashMap.put("Charge", "");

            Log.e("SbmtJLGClosing #mp", hashMap.toString());
            try {
                JSONObject jobjGetAll = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("createJlg data ", jobjGetAll.toString());
                if (jobjGetAll.has("status")) {
                    responseMsg = jobjGetAll.getString("status");
                    if (responseMsg.equals("1")) {
                        alertMsg = jobjGetAll.getString("msg");
                    } else if (responseMsg.equals("0")) {
                        alertMsg = jobjGetAll.getString("msg");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseMsg = "exception";
            }
            return responseMsg;
        }

        @Override
        protected void onPostExecute(String url) {
            proDialog.dismiss();
            if (url.equals("0")) {
                errorDialog.setTitleText("Error!!")
                        .setContentText(alertMsg)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (url.equals("exception")) {
                errorDialog.setTitleText("Error!!")
                        .setContentText("Something went wrong!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
            if (url.equals("1")) {
                succDialog.setTitleText("Success!!")
                        .setContentText(alertMsg)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    public String RemittanceData() {
        String StrJson = "";
        JSONObject obj;
        JSONArray jsonArray = new JSONArray();
        try {
            if (TrType.equals("SPLIT")) {
                if (ClosingLoanRecyclerSplit.dataSet != null) {
                    for (int i = 0; i < ClosingLoanRecyclerSplit.dataSet.size(); i++) {
                        obj = new JSONObject();
                        obj.put("CustId", ClosingLoanRecyclerSplit.dataSet.get(i).getSplitCustId());
                        obj.put("CustName", ClosingLoanRecyclerSplit.dataSet.get(i).getSplitCustName());
                        obj.put("AccountNo", ClosingLoanRecyclerSplit.dataSet.get(i).getSplitAccountNo());
                        obj.put("PrincipalBlnc", ClosingLoanRecyclerSplit.dataSet.get(i).getSplitPrincipalBlnc());
                        obj.put("Interest", ClosingLoanRecyclerSplit.dataSet.get(i).getSplitInterest());
                        obj.put("PenalInterest", ClosingLoanRecyclerSplit.dataSet.get(i).getSplitPenalInterest());
                        obj.put("TotalInterest", ClosingLoanRecyclerSplit.dataSet.get(i).getSplitTotalInterest());
                        obj.put("Remittance", ClosingLoanRecyclerSplit.dataSet.get(i).getSplitRemittance());
                        obj.put("Closing", ClosingLoanRecyclerSplit.dataSet.get(i).getSplitSelected());
                        jsonArray.put(obj);
                    }
                    StrJson = jsonArray.toString();
                }
            } else {
                if (ClosingLoanRecycler.dataSet != null) {
                    for (int i = 0; i < ClosingLoanRecycler.dataSet.size(); i++) {
                        obj = new JSONObject();
                        obj.put("CustId", ClosingLoanRecycler.dataSet.get(i).getCustId());
                        obj.put("CustName", ClosingLoanRecycler.dataSet.get(i).getCustName());
                        obj.put("Principal", ClosingLoanRecycler.dataSet.get(i).getPrincipalBlnc());
                        obj.put("Interest", ClosingLoanRecycler.dataSet.get(i).getInterest());
                        obj.put("PenalInterest", ClosingLoanRecycler.dataSet.get(i).getPenalInterest());
                        obj.put("TotalInterest", ClosingLoanRecycler.dataSet.get(i).getTotalInterest());
                        obj.put("Remittance", ClosingLoanRecycler.dataSet.get(i).getRemittance());
                        obj.put("Closing", ClosingLoanRecycler.dataSet.get(i).getSelected());
                        jsonArray.put(obj);
                    }
                    StrJson = jsonArray.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return StrJson;
    }


}

