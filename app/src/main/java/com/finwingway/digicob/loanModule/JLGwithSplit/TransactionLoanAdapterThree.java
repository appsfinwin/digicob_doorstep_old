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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.loanModule.JLGSplitSupportClasses.ListviewAdapterBankCharge;
import com.finwingway.digicob.login;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.graphics.Color.RED;

@SuppressLint("ValidFragment")
public class TransactionLoanAdapterThree extends Fragment implements
        DatePickerDialog.OnDateSetListener, Validator.ValidationListener {
    public static String ip = login.ip_global;
    private static ArrayList<HashMap<String, String>> chargeslist;
    static ListviewAdapterBankCharge chrgAdapter;
    Button prev, btnInstrmtDate, btnSubmit, btnAddChrg;
    TextView tvInstrmt_nu, tvAmnt;
    EditText EdtParticulers, EdtChrgAmount;
    View rootView;
    SweetAlertDialog proDialog, errorDialog, succDialog;
    RequestQueue requestQueue;
    JSONParser jsonParser;
    ViewPager viewPagerTrans = MainTransactionLoan.viewPagerTrans;
    private FragmentActivity myContext;
    DatePickerDialog dpd;
    String StrLoan_date,
            responseMsg, alertMsg,
            StrAmount, StrAccNo;
    Spinner spinerInstrmtType, spinerChargesType, spinerAccNo;
    public static String StrInstrmtType, StrInstrmtTypeCode,
            StrChargesType, StrChargesTypeCode;
    LinearLayout linearLaySplit;
    static String TrType;
    ArrayAdapter bnkAccNoAdapter;
    String[] arrayBnkAccNo;
    public static final String ACCNO_COLUMN = "AccNo";
    public static final String BNK_CHRGS_COLUMN = "Charges";
    public static final String BNK_CHRGS_ID_COLUMN = "Charges_id";
    public static final String AMOUNT_COLUMN = "Amount";

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @SuppressLint("ValidFragment")
    public TransactionLoanAdapterThree(String strTrnsType) {
        TrType = strTrnsType;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.loan_jlg_transaction_three, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
        jsonParser = new JSONParser();
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        succDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
        final FragmentManager fragManager = myContext.getFragmentManager();
        EdtParticulers = rootView.findViewById(R.id.edt_particulers);
        EdtChrgAmount = rootView.findViewById(R.id.edt_chrg_amount_splt);
        tvInstrmt_nu = rootView.findViewById(R.id.tv_instrmt_nu);
        tvAmnt = rootView.findViewById(R.id.tv_amnt);

        chargeslist = new ArrayList<HashMap<String, String>>();

        linearLaySplit = rootView.findViewById(R.id.linear_lay_split);
        if (TrType.equals("SPLIT")) {
            linearLaySplit.setVisibility(View.VISIBLE);
        } else {
            linearLaySplit.setVisibility(View.GONE);
        }

        spinerAccNo = rootView.findViewById(R.id.spinner_acc_no_splt);
        spinerChargesType = rootView.findViewById(R.id.spinner_charges_splt);

        viewPagerTrans.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.e("viewPagerTrans position", String.valueOf(position));
                if (position == 2) {
                    ArrayAdapter brachAdapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item,
                            TransactionLoanAdapterOne.arrayInstrmntName);
                    spinerInstrmtType.setAdapter(brachAdapter2);

                    if (TrType.equals("SPLIT")) {
                        if (TransactionLoanAdapterOne.StrSubTransTypeCode.equals("Dr")) {
                            arrayBnkAccNo = TransactionLoanAdapterOne.StrAccountNumberAryDr;
                            bnkAccNoAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayBnkAccNo);
                        } else {
                            arrayBnkAccNo = TransactionLoanAdapterOne.StrAccountNumberAry;
                            bnkAccNoAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayBnkAccNo);
                        }
                        spinerAccNo.setAdapter(bnkAccNoAdapter);
                    }

                    ArrayAdapter chargeAdapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item,
                            TransactionLoanAdapterOne.arrayChargesName);
                    spinerChargesType.setAdapter(chargeAdapter2);

                    tvAmnt.setText(String.valueOf(TransactionLoanGetSet.getTotalRemitncAmnt()));
                }
            }
        });

        spinerAccNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrAccNo = arrayBnkAccNo[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinerChargesType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrChargesType = TransactionLoanAdapterOne.arrayChargesName[position];
                StrChargesTypeCode = TransactionLoanAdapterOne.arrayChargesCode[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinerInstrmtType = rootView.findViewById(R.id.spinner_instrmt_type);
        ArrayAdapter brachAdapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, TransactionLoanAdapterOne.arrayInstrmntName);
        spinerInstrmtType.setAdapter(brachAdapter2);
        spinerInstrmtType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrInstrmtType = TransactionLoanAdapterOne.arrayInstrmntName[position];
                StrInstrmtTypeCode = TransactionLoanAdapterOne.arrayInstrmntCode[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        prev = rootView.findViewById(R.id.btn_pre_three);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerTrans.setCurrentItem(1, true);
            }
        });
        btnInstrmtDate = rootView.findViewById(R.id.btn_instrmt_date);
        btnInstrmtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(fragManager);
            }
        });

        ListView listView = rootView.findViewById(R.id.listview_charges);
        chrgAdapter = new ListviewAdapterBankCharge(getActivity(), chargeslist);
        listView.setAdapter(chrgAdapter);
        btnAddChrg = rootView.findViewById(R.id.btn_trns_add_chrg);
        btnAddChrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrAmount = EdtChrgAmount.getText().toString();
                if (!TextUtils.isEmpty(StrAmount) && !StrAccNo.isEmpty()) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    hashmap.put(ACCNO_COLUMN, StrAccNo);
                    hashmap.put(BNK_CHRGS_COLUMN, StrChargesType);
                    hashmap.put(BNK_CHRGS_ID_COLUMN, StrChargesTypeCode);
                    hashmap.put(AMOUNT_COLUMN, StrAmount);
                    chargeslist.add(hashmap);
                    chrgAdapter.notifyDataSetChanged();
                    EdtChrgAmount.setText("");
                } else {
                    Toast.makeText(getContext(), "Please enter Group Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSubmit = rootView.findViewById(R.id.btn_trns_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SubmitJLGLoanTransaction().execute();
            }
        });

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String CurrentDate = df.format(c.getTime());
        btnInstrmtDate.setText(CurrentDate);
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
                TransactionLoanAdapterThree.this,
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

    public String RemittanceData() {
        String StrJson = "";
        JSONObject obj;
        JSONArray jsonArray = new JSONArray();
        try {
            if (TrType.equals("SPLIT")) {
                if (TransactionLoanRecyclerSplit.dataSet != null) {
                    for (int i = 0; i < TransactionLoanRecyclerSplit.dataSet.size(); i++) {
                        obj = new JSONObject();
                        obj.put("CustId", TransactionLoanRecyclerSplit.dataSet.get(i).getSplitCustId());
                        obj.put("CustName", TransactionLoanRecyclerSplit.dataSet.get(i).getSplitCustName());
                        obj.put("AccountNo", TransactionLoanRecyclerSplit.dataSet.get(i).getSplitAccountNo());
                        obj.put("PrincipalBlnc", TransactionLoanRecyclerSplit.dataSet.get(i).getSplitPrincipalBlnc());
                        obj.put("PrincipalDue", TransactionLoanRecyclerSplit.dataSet.get(i).getSplitPrincipalDue());
                        obj.put("Interest", TransactionLoanRecyclerSplit.dataSet.get(i).getSplitInterest());
                        obj.put("PenalInterest", TransactionLoanRecyclerSplit.dataSet.get(i).getSplitPenalInterest());
                        obj.put("TotalInterest", TransactionLoanRecyclerSplit.dataSet.get(i).getSplitTotalInterest());
                        obj.put("Remittance", TransactionLoanRecyclerSplit.dataSet.get(i).getSplitRemittance());
                        obj.put("Closing", TransactionLoanRecyclerSplit.dataSet.get(i).getSplitSelected());
                        jsonArray.put(obj);
                    }
                    StrJson = jsonArray.toString();
                }
            } else {
                if (TransactionLoanRecycler.dataSet != null) {
                    for (int i = 0; i < TransactionLoanRecycler.dataSet.size(); i++) {
                        obj = new JSONObject();
                        obj.put("CustId", TransactionLoanRecycler.dataSet.get(i).getCustId());
                        obj.put("CustName", TransactionLoanRecycler.dataSet.get(i).getCustName());
                        obj.put("Principal", TransactionLoanRecycler.dataSet.get(i).getPrincipal());
                        obj.put("Interest", TransactionLoanRecycler.dataSet.get(i).getInterest());
                        obj.put("PenalInterest", TransactionLoanRecycler.dataSet.get(i).getPenalInterest());
                        obj.put("TotalInterest", TransactionLoanRecycler.dataSet.get(i).getTotalInterest());
                        obj.put("Remittance", TransactionLoanRecycler.dataSet.get(i).getRemittance());
                        obj.put("Closing", TransactionLoanRecycler.dataSet.get(i).getSelected());
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

    public String ChargesData() {
        String StrJson = "";
        JSONObject obj;
        JSONArray jsonArray = new JSONArray();
        try {
            if (TrType.equals("SPLIT")) {
                if (chargeslist != null) {
                    for (HashMap<String, String> map : chargeslist) {
                        obj = new JSONObject();
                        obj.put("AccNo", map.get(ACCNO_COLUMN));
//                        obj.put("BankChargeCode", map.get(BNK_CHRGS_COLUMN));
                        obj.put("BankChargeCode", map.get(BNK_CHRGS_ID_COLUMN));
                        obj.put("Amount", map.get(AMOUNT_COLUMN));
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

    public static void removeFromList(int id) {
        chargeslist.remove(id);
        chrgAdapter.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    private class SubmitJLGLoanTransaction extends AsyncTask<String, String, String> {
        private String api_url = ip + "/JLGLoanTransaction";
        String groupData = "", chargesData = "";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();

            groupData = RemittanceData();
            chargesData = ChargesData();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Ln_Type", "");
            hashMap.put("Brcode", login.branch_id);
            hashMap.put("Tran_no", "");
            hashMap.put("Schcode", TransactionLoanGetSet.getSchCode());
            hashMap.put("Accno", TransactionLoanGetSet.getAccountNo());
            hashMap.put("Trdate", TransactionLoanGetSet.getDate());
            hashMap.put("Transacteddate", TransactionLoanGetSet.getDate());
            hashMap.put("Effectivedate", TransactionLoanGetSet.getEffctDate());
            hashMap.put("TranType", TransactionLoanGetSet.getTransType());
            hashMap.put("SubTranType", TransactionLoanGetSet.getSubTransType());
            hashMap.put("Amount", tvAmnt.getText().toString());
            hashMap.put("TransferAccNo", TransactionLoanGetSet.getTransAccountNo());
            hashMap.put("Narration", EdtParticulers.getText().toString());
            hashMap.put("InstrumentNo", tvInstrmt_nu.getText().toString());
            hashMap.put("InstrumentDate", btnInstrmtDate.getText().toString());
            hashMap.put("InstrumentType", StrInstrmtTypeCode);
            hashMap.put("MakerId", login.agent_code);
            hashMap.put("Makingtime", "");
            hashMap.put("CheckerId", "");
            hashMap.put("Checkingtime", "");
            hashMap.put("flag", "INSERT");
            hashMap.put("IsClosing", "Y");
            hashMap.put("table", "");
            hashMap.put("AccountDetails", groupData);
            hashMap.put("Charge", chargesData);

            Log.e("JLGLoanTrans hashMap = ", hashMap.toString());
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


}
