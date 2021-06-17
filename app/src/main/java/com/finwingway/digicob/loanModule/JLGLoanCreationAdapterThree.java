package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.finwingway.digicob.Account_Search;
import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.graphics.Color.RED;
import static android.view.View.GONE;
import static com.finwingway.digicob.loanModule.JLGLoanCreationAdapterOne.CollcStaffCodeArray;
import static com.finwingway.digicob.loanModule.JLGLoanCreationAdapterOne.CollcStaffNameArray;
import static com.finwingway.digicob.loanModule.JLGLoanCreationAdapterOne.ModCodeArray;
import static com.finwingway.digicob.loanModule.JLGLoanCreationAdapterOne.ModNameArray;
import static com.finwingway.digicob.loanModule.JLGLoanCreationAdapterOne.PerCodeArray;
import static com.finwingway.digicob.loanModule.JLGLoanCreationAdapterOne.PerNameArray;
import static com.finwingway.digicob.loanModule.JLGLoanCreationAdapterTwo.adapter;
import static com.finwingway.digicob.loanModule.JLGLoanCreationGetSet.listAdapteTwo;

public class JLGLoanCreationAdapterThree extends Fragment implements
        DatePickerDialog.OnDateSetListener, Validator.ValidationListener {
    View rootView;
    JSONParser jsonParser;
    static JSONObject jobjGetAll;
    ImageButton btnSearchAcc;
    Button prev, submit, btnResDate, btnApplctnDate;
    ViewPager viewPager = JLGLoanCreation.viewPager;
    public static String ip = login.ip_global;
    TableRow Tb_row_acc;
    Spinner spinnerLoanperiodType, spinnerLoanperiod, spinnerTransMode,
            spinnerCollctnStaff, spinnerCollctnDay;
    TextView TvInterest_rate, TvEmi_type, TvPenal_interest, TvInstall_no, TvInstall_amnt;
    EditText EdtRes_no, EdtLot_no, EdtLoan_amnt, EdtLoan_date, EdtRes_date, EdtMoratorium_period, EdtNarration, EdtAcc_no;
    ArrayAdapter periodAdaptr, PeriodTypeAdaptr, TransModeAdaptr, CollcStaffAdaptr;
    SweetAlertDialog proDialog, errorDialog, succDialog;

    String responseMsg, alertMsg,
            StrLoan_date, StrRes_date, StrTransMode, StrCollctnDay, StrCollctnStaff,
            StrPeriodType = "", StrPeriod, StrInterest, StrPenalInterest,
            respLnMsg, LnMessage;
    public static String[]
            DDPeriodTypeArray, DDPeriodArray, DDIntRateArray, DDPenalRateArray,
            WWPeriodTypeArray, WWPeriodArray, WWIntRateArray, WWPenalRateArray,
            MMPeriodTypeArray, MMPeriodArray, MMIntRateArray, MMPenalRateArray;

    DatePickerDialog dpd;
    private FragmentActivity myContext;
    int DATE_DIALOG = 0;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.loan_creation_page_three, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jsonParser = new JSONParser();
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        succDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
        final FragmentManager fragManager = myContext.getFragmentManager();

        Tb_row_acc = rootView.findViewById(R.id.tb_row_acc);
        EdtLoan_amnt = rootView.findViewById(R.id.edt_loan_amnt);
//      EdtLoan_amnt.setFocusableInTouchMode(true);
        EdtLoan_amnt.setFocusable(false);

        TvInterest_rate = rootView.findViewById(R.id.tv_interest_rate);
        TvPenal_interest = rootView.findViewById(R.id.tv_penal_interest);
        TvInstall_no = rootView.findViewById(R.id.tv_install_no);
        TvInstall_amnt = rootView.findViewById(R.id.tv_install_amnt);

        EdtRes_no = rootView.findViewById(R.id.edt_res_no);
        EdtLot_no = rootView.findViewById(R.id.edt_lot_number);

        EdtMoratorium_period = rootView.findViewById(R.id.edt_moratorium_period);
        TvEmi_type = rootView.findViewById(R.id.tv_emi_type);
        EdtAcc_no = rootView.findViewById(R.id.edt_acc_no);
        EdtNarration = rootView.findViewById(R.id.edt_narration);


        spinnerTransMode = rootView.findViewById(R.id.spinner_trans_mode);
        spinnerTransMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrTransMode = ModCodeArray[position];
                TransactionMode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final List<String> CollDayList = new ArrayList<>();
        CollDayList.add("Sunday");
        CollDayList.add("Monday");
        CollDayList.add("Tuesday");
        CollDayList.add("Wednesday");
        CollDayList.add("Thursday");
        CollDayList.add("Friday");
        CollDayList.add("Saturday");
        ArrayAdapter<String> collDayAdptr = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, CollDayList);
//        collDayAdptr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCollctnDay = rootView.findViewById(R.id.spnr_collctn_day);
        spinnerCollctnDay.setAdapter(collDayAdptr);
        spinnerCollctnDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrCollctnDay = CollDayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerCollctnStaff = rootView.findViewById(R.id.spnr_collctn_staff);
        spinnerCollctnStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrCollctnStaff = CollcStaffCodeArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        prev = rootView.findViewById(R.id.btn_jlg_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);
            }
        });

        submit = rootView.findViewById(R.id.btn_jlg_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.validateFields()) {
                    new createJlg().execute();
//                    Log.e("createJlg makeJsonObj", makeJsonObj());
                } else {
                    Toast.makeText(getActivity(), "Not enough data to Register...!!\n" +
                            "Please check all Amount fields..!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSearchAcc = rootView.findViewById(R.id.btn_search_acc_lc);
        btnSearchAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent debit_intent = new Intent(getActivity(), Account_Search.class);
                startActivityForResult(debit_intent, 1044);
            }
        });

        btnApplctnDate = (Button) rootView.findViewById(R.id.btn_applctn_date);
        btnApplctnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 1;
                openDialog(fragManager);
            }
        });

        btnResDate = (Button) rootView.findViewById(R.id.btn_res_date);
        btnResDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 2;
                openDialog(fragManager);
            }
        });

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String CurrentDate = df.format(c.getTime());
        btnApplctnDate.setText(CurrentDate);
        btnResDate.setText(CurrentDate);


        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                try {
                    if (position == 2) {
                        if (PerNameArray != null && PerNameArray.length > 0) {
                            periodAdaptr = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, PerNameArray);
                            spinnerLoanperiodType.setAdapter(periodAdaptr);
                        }
                        if (ModNameArray != null && ModNameArray.length > 0) {
                            TransModeAdaptr = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, ModNameArray);
                            spinnerTransMode.setAdapter(TransModeAdaptr);
                        }
                        if (CollcStaffNameArray != null && CollcStaffNameArray.length > 0) {
                            CollcStaffAdaptr = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, CollcStaffNameArray);
                            spinnerCollctnStaff.setAdapter(CollcStaffAdaptr);
                        }

                        spinnerMode();
//                        new getLoanPeriodList().execute(JLGLoanCreationGetSet.getSchemeNo());
                    }
                    EdtLoan_amnt.setText(String.valueOf(JLGLoanCreationGetSet.getTotalAmnt()));
                    TvEmi_type.setText(String.valueOf(JLGLoanCreationGetSet.getEMIType()));
                } catch (Exception e) {
                    Log.e("onPageSelected: ", String.valueOf("JLGLoanCreationAdapterThree =- " + e));
                }
            }
        });

        spinnerLoanperiodType = rootView.findViewById(R.id.spnr_loanperiod_typ);
        spinnerLoanperiodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrPeriodType = PerCodeArray[position];
                InitSpinner(StrPeriodType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerLoanperiod = rootView.findViewById(R.id.spnr_loanperiod);
        spinnerLoanperiod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (StrPeriodType) {
                    case "DD":
                        if (DDPeriodArray != null && DDPeriodArray.length > 0 && DDIntRateArray != null && DDIntRateArray.length > 0) {
                            StrPeriod = DDPeriodArray[position];
                            StrInterest = DDIntRateArray[position];
                            StrPenalInterest = DDPenalRateArray[position];
                        }
                        break;
                    case "MM":
                        if (MMPeriodArray != null && MMPeriodArray.length > 0 && MMIntRateArray != null && MMIntRateArray.length > 0) {
                            StrPeriod = MMPeriodArray[position];
                            StrInterest = MMIntRateArray[position];
                            StrPenalInterest = MMPenalRateArray[position];
                        }
                        break;
                    case "WW":
                        if (WWPeriodArray != null && WWPeriodArray.length > 0 && WWIntRateArray != null && WWIntRateArray.length > 0) {
                            StrPeriod = WWPeriodArray[position];
                            StrInterest = WWIntRateArray[position];
                            StrPenalInterest = WWPenalRateArray[position];
                        }
                        break;
                }
                TvInterest_rate.setText(StrInterest);
                TvPenal_interest.setText(StrPenalInterest);
                TvInstall_no.setText(setInstallmentNO(StrPeriodType, StrPeriod));
                Log.e("getCalcType: ", JLGLoanCreationGetSet.getCalcType());
                double Install_amnt = CalcInstallmentAmount(StrPeriodType, StrPeriod, String.valueOf(JLGLoanCreationGetSet.getTotalAmnt()),
                        StrInterest, JLGLoanCreationGetSet.getEMIType(), JLGLoanCreationGetSet.getCalcType());
                TvInstall_amnt.setText(String.valueOf(Install_amnt));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public String makeJsonObj() {
        JSONObject obj;
        JSONArray jsonArray = new JSONArray();
        try {
            for (HashMap<String, String> map : listAdapteTwo) {
                obj = new JSONObject();
                obj.put(JLGLoanCreationGetSet.LN_SLNO, map.get(JLGLoanCreationGetSet.LN_SLNO));
                obj.put(JLGLoanCreationGetSet.LN_CUSTID, map.get(JLGLoanCreationGetSet.LN_CUSTID));
                obj.put(JLGLoanCreationGetSet.LN_CUSTNAME, map.get(JLGLoanCreationGetSet.LN_CUSTNAME));
                obj.put(JLGLoanCreationGetSet.LN_COAPPLICNT, map.get(JLGLoanCreationGetSet.LN_COAPPLICNT));
                obj.put(JLGLoanCreationGetSet.LN_MOB, map.get(JLGLoanCreationGetSet.LN_MOB));
                obj.put(JLGLoanCreationGetSet.LN_ADDRESS, map.get(JLGLoanCreationGetSet.LN_ADDRESS));
                obj.put(JLGLoanCreationGetSet.LN_CONSUMERGOODS, map.get(JLGLoanCreationGetSet.LN_CONSUMERGOODS));
                obj.put(JLGLoanCreationGetSet.LN_AMOUNT, map.get(JLGLoanCreationGetSet.LN_AMOUNT));
                obj.put(JLGLoanCreationGetSet.LN_INSURANCE_FEE, map.get(JLGLoanCreationGetSet.LN_INSURANCE_FEE));
                obj.put(JLGLoanCreationGetSet.LN_DOCUMNT_FEE, map.get(JLGLoanCreationGetSet.LN_DOCUMNT_FEE));
                obj.put(JLGLoanCreationGetSet.LN_CGST, map.get(JLGLoanCreationGetSet.LN_CGST));
                obj.put(JLGLoanCreationGetSet.LN_SGST, map.get(JLGLoanCreationGetSet.LN_SGST));
                obj.put(JLGLoanCreationGetSet.LN_CESS, map.get(JLGLoanCreationGetSet.LN_CESS));
                obj.put(JLGLoanCreationGetSet.LN_DISBRSMNT_AMNT, map.get(JLGLoanCreationGetSet.LN_DISBRSMNT_AMNT));
                jsonArray.put(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public void InitSpinner(String periodType) {
        switch (periodType) {
            case "DD":
                if (DDPeriodArray != null && DDPeriodArray.length > 0) {
                    PeriodTypeAdaptr = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, DDPeriodArray);
                    spinnerLoanperiod.setAdapter(PeriodTypeAdaptr);
                }
                break;
            case "MM":
                if (MMPeriodArray != null && MMPeriodArray.length > 0) {
                    PeriodTypeAdaptr = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, MMPeriodArray);
                    spinnerLoanperiod.setAdapter(PeriodTypeAdaptr);
                }
                break;
            case "WW":
                if (WWPeriodArray != null && WWPeriodArray.length > 0) {
                    PeriodTypeAdaptr = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, WWPeriodArray);
                    spinnerLoanperiod.setAdapter(PeriodTypeAdaptr);
                }
                break;
        }
    }

    public void spinnerMode() {
        try {
            final int sdk = Build.VERSION.SDK_INT;
            if (JLGLoanCreationGetSet.getIsSplit().equals("Y")) {
                spinnerLoanperiodType.setEnabled(false);
                spinnerLoanperiodType.setClickable(false);
                spinnerLoanperiod.setEnabled(false);
                spinnerLoanperiod.setClickable(false);

                if (sdk >= Build.VERSION_CODES.LOLLIPOP) {
                    spinnerLoanperiodType.setBackgroundTintList(rootView.getResources().getColorStateList(R.color.gray));
                    spinnerLoanperiod.setBackgroundTintList(rootView.getResources().getColorStateList(R.color.gray));
                }
            } else {
                spinnerLoanperiodType.setEnabled(true);
                spinnerLoanperiodType.setClickable(true);
                spinnerLoanperiod.setEnabled(true);
                spinnerLoanperiod.setClickable(true);

                if (sdk >= Build.VERSION_CODES.LOLLIPOP) {
                    spinnerLoanperiodType.setBackgroundTintList(rootView.getResources().getColorStateList(R.color.black));
                    spinnerLoanperiod.setBackgroundTintList(rootView.getResources().getColorStateList(R.color.black));
                }
            }
        } catch (Exception ignored) {

        }
    }

    public void TransactionMode() {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (StrTransMode.equals("T")) {//"C"
            EdtAcc_no.setFocusableInTouchMode(true);
            btnSearchAcc.setEnabled(true);
            Tb_row_acc.setVisibility(View.VISIBLE);

//            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                EdtAcc_no.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_style));
//            } else {
//                EdtAcc_no.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_style));
//            }
        } else {
            EdtAcc_no.setFocusable(false);
            EdtAcc_no.setText(null);
            btnSearchAcc.setEnabled(false);
            Tb_row_acc.setVisibility(GONE);

//            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                EdtAcc_no.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_style_disble));
//            } else {
//                EdtAcc_no.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_style_disble));
//            }
        }
    }

    public void openDialog(FragmentManager fragManager) {
        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                JLGLoanCreationAdapterThree.this,
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

    @Override
    public void onValidationSucceeded() {
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (DATE_DIALOG == 1) {
            StrLoan_date = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
            btnApplctnDate.setText(StrLoan_date);
            btnApplctnDate.setTextColor(RED);
            btnApplctnDate.setTextSize(18);
        }
        if (DATE_DIALOG == 2) {
            StrRes_date = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
            btnResDate.setText(StrRes_date);
            btnResDate.setTextColor(RED);
            btnResDate.setTextSize(18);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1044 && resultCode == RESULT_OK) {
            try {
                String result_acc_no = data.getStringExtra("result_acc_no");
                EdtAcc_no.setText(result_acc_no);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ///////////=========================================================================================

    public String formatDates(String strCurrentDate) {
        String date = null;
        Date newDate = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            newDate = format.parse(strCurrentDate);
            format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static double CalcInstallmentAmount(String PeriodType, String Period, String Amount,
                                               String Interest, String Emitype, String Loancalctype) {
        try {
            double installamt = 0;
            switch (Emitype) {
                case "N":
                    float loanamt = Float.parseFloat(Amount.equals("") ? "0" : Amount);
                    float loaninterest = Float.parseFloat(Interest.equals("") ? "0" : Interest);
                    int loanperiod = Integer.parseInt(Period.equals("") ? "0" : Period);

                    if (Loancalctype.equals("FLT")) {
                        switch (PeriodType) {
                            case "DD":
                                if (loanperiod >= 30) {
                                    float loanperiodnw = loanperiod / 30;
                                    float IntAmount = (loanamt * loaninterest * loanperiodnw) / 36500;
                                    float TotLoanAmt = loanamt + IntAmount;
                                    installamt = TotLoanAmt / loanperiodnw;
                                    installamt = Math.round((Double) installamt);//0
                                    return installamt;
                                } else {
                                    float loanperiodnw = 1;
                                    float IntAmount = (loanamt * loaninterest * loanperiodnw) / 36500;
                                    float TotLoanAmt = loanamt + IntAmount;
                                    installamt = TotLoanAmt / loanperiodnw;
                                    installamt = Math.round(installamt);
                                    return installamt;
                                }
                            case "WW": {
                                float IntAmount = (loanamt * loaninterest * loanperiod) / 5200;
                                float TotLoanAmt = loanamt + IntAmount;
                                installamt = TotLoanAmt / loanperiod;
                                installamt = Math.round(installamt);
                                return installamt;
                            }
                            default: {
                                float IntAmount = (loanamt * loaninterest * loanperiod) / 1200;
                                float TotLoanAmt = loanamt + IntAmount;
                                installamt = TotLoanAmt / loanperiod;
                                installamt = Math.round(installamt);
                                return installamt;
                            }
                        }
                    } else {
                        if (PeriodType.equals("DD")) {
                            if (loanperiod >= 30) {
                                float loanperiodnw = loanperiod / 30;
                                installamt = loanamt / loanperiodnw;
                                installamt = Math.round(installamt);
                                return installamt;
                            } else {
                                float loanperiodnw = 1;
                                installamt = loanamt / loanperiodnw;
                                installamt = Math.round(installamt);
                                return installamt;
                            }
                        } else {
                            installamt = loanamt / loanperiod;
                            installamt = Math.round(installamt);
                            return installamt;
                        }
                    }
                case "Y":
                    float P = Float.parseFloat(Amount.equals("") ? "0" : Amount);
                    float r = Float.parseFloat(Interest.equals("") ? "0" : Interest);
                    r = r / 1200;
                    int n = Integer.parseInt(Period.equals("") ? "0" : Period);
                    //if (PeriodType == "DD")
                    //{
                    //    int n = nd / 30;
                    //    installamt = ((P * r) * Math.Pow(1 + r, n)) / (Math.Pow(1 + r, n) - 1);
                    //    installamt = Math.Round(installamt, 0);
                    //    return installamt;
                    //}
                    //else
                    //{
                    installamt = ((P * r) * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);
                    installamt = Math.round(installamt);
                    return installamt;
                //}
                default:
                    return installamt;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    @SuppressLint("DefaultLocale")
    public String setInstallmentNO(String PeriodType, String Period) {
        String InstlmntNO = "";
        if (PeriodType.equals("DD")) {
            double period = Double.parseDouble(Period);
            double periodnew = period / 30;
            InstlmntNO = String.format("%.0f", periodnew);
        } else {
            InstlmntNO = Period;
        }
        return InstlmntNO;
    }

    ///////////=========================================================================================

//    @SuppressLint("StaticFieldLeak")
//    private class getLoanPeriodList extends AsyncTask<String, String, String> {
//        private String api_url = ip + "/JLGLoanPeriod";
//
//        @Override
//        public void onPreExecute() {
//            proDialog.setTitleText("Please wait..");
//            proDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            HashMap<String, String> hashMap = new HashMap<>();
//            hashMap.put("Sch_Code", params[0]);
//            try {
//                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
//                Log.e("getLoanPeriodList", jsonObject.toString());
//                JSONObject jobj = jsonObject.getJSONObject("receipt");
//                if (jobj.has("data")) {
//                    JSONObject jsonObj = jobj.getJSONObject("data");
//                    if (jsonObj.has("DD")) {
//                        JSONArray DDArray = jsonObj.getJSONArray("DD");
//                        int cnt = DDArray.length();
//                        DDPeriodTypeArray = new String[cnt];
//                        DDPeriodArray = new String[cnt];
//                        DDIntRateArray = new String[cnt];
//                        DDPenalRateArray = new String[cnt];
//                        for (int i = 0; i < cnt; i++) {
//                            JSONObject DDAryObj = DDArray.getJSONObject(i);
//                            DDPeriodTypeArray[i] = DDAryObj.getString("Ln_PeriodType");
//                            DDPeriodArray[i] = DDAryObj.getString("Ln_Period");
//                            DDIntRateArray[i] = DDAryObj.getString("Ln_IntRate");
//                            DDPenalRateArray[i] = DDAryObj.getString("Ln_PenalRate");
//                        }
//                    }
//                    if (jsonObj.has("WW")) {
//                        JSONArray WWArray = jsonObj.getJSONArray("WW");
//                        int cnt = WWArray.length();
//                        WWPeriodTypeArray = new String[cnt];
//                        WWPeriodArray = new String[cnt];
//                        WWIntRateArray = new String[cnt];
//                        WWPenalRateArray = new String[cnt];
//                        for (int i = 0; i < cnt; i++) {
//                            JSONObject WWAryObj = WWArray.getJSONObject(i);
//                            WWPeriodTypeArray[i] = WWAryObj.getString("Ln_PeriodType");
//                            WWPeriodArray[i] = WWAryObj.getString("Ln_Period");
//                            WWIntRateArray[i] = WWAryObj.getString("Ln_IntRate");
//                            WWPenalRateArray[i] = WWAryObj.getString("Ln_PenalRate");
//                        }
//                    }
//                    if (jsonObj.has("MM")) {
//                        JSONArray MMArray = jsonObj.getJSONArray("MM");
//                        int cnt = MMArray.length();
//                        MMPeriodTypeArray = new String[cnt];
//                        MMPeriodArray = new String[cnt];
//                        MMIntRateArray = new String[cnt];
//                        MMPenalRateArray = new String[cnt];
//                        for (int i = 0; i < cnt; i++) {
//                            JSONObject MMAryObj = MMArray.getJSONObject(i);
//                            MMPeriodTypeArray[i] = MMAryObj.getString("Ln_PeriodType");
//                            MMPeriodArray[i] = MMAryObj.getString("Ln_Period");
//                            MMIntRateArray[i] = MMAryObj.getString("Ln_IntRate");
//                            MMPenalRateArray[i] = MMAryObj.getString("Ln_PenalRate");
//                        }
//                    }
//                    respLnMsg = "1";
//                } else if (jobj.has("error")) {
//                    LnMessage = jobj.getJSONObject("error").getString("msg");
//                    respLnMsg = "0";
//                }
//            } catch (JSONException e) {
//                proDialog.dismiss();
//                e.printStackTrace();
//                respLnMsg = "2";
//            } catch (Exception e) {
//                proDialog.dismiss();
//                e.printStackTrace();
//                respLnMsg = "2";
//            }
//            return respLnMsg;
//        }
//
//        @Override
//        public void onPostExecute(final String result) {
//            proDialog.dismiss();
//            switch (result) {
//                case "0":
//                    Toast.makeText(getContext(), "Waring - " + LnMessage, Toast.LENGTH_SHORT).show();
////                    errorDialog.setTitleText("Error!!")
////                            .setContentText(LnMessage)
////                            .show();
//                    break;
//                case "2":
//                    Toast.makeText(getContext(), "Server Error occurred..!!", Toast.LENGTH_SHORT).show();
////                    errorDialog.setTitleText("Error!!")
////                            .setContentText("Something went wrong!")
////                            .show();
//                    break;
//                case "1":
//
//                    break;
//            }
//        }
//    }

    @SuppressLint("StaticFieldLeak")
    private class createJlg extends AsyncTask<String, String, String> {
        private String api_url = ip + "/LoanMaster";
        String groupData = "";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();

            groupData = makeJsonObj();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Schcode", JLGLoanCreationGetSet.getSchemeNo());
            hashMap.put("Brcode", JLGLoanCreationGetSet.getBrnchCode());
            hashMap.put("ApplSlno", JLGLoanCreationGetSet.getApplctn_no());
            hashMap.put("LoanType", "JGL");
            hashMap.put("Custid", login.agent_code);
            hashMap.put("Group_Id", JLGLoanCreationGetSet.getGroupCode());
            hashMap.put("ShrBrCode", JLGLoanCreationGetSet.getBrnchCode());
            hashMap.put("ShrSchCode", JLGLoanCreationGetSet.getSchemeNo());
            hashMap.put("LoanAmount", EdtLoan_amnt.getText().toString());
            hashMap.put("LoanDate", formatDates(btnApplctnDate.getText().toString()));
            hashMap.put("Period", StrPeriod);
            hashMap.put("PeriodType", StrPeriodType);
            hashMap.put("IntRate", TvInterest_rate.getText().toString());
            hashMap.put("PenalRate", TvPenal_interest.getText().toString());
            hashMap.put("InstallmentNo", TvInstall_no.getText().toString());
            hashMap.put("InstallmentAmount", TvInstall_amnt.getText().toString());
            hashMap.put("ApprID", "0");
            hashMap.put("Mainpurpose", "Trans");
            hashMap.put("Subpurpose", "Trans");
            hashMap.put("ResNo", EdtRes_no.getText().toString());
            hashMap.put("ResDate", formatDates(btnResDate.getText().toString()));
            hashMap.put("LotNo", EdtLot_no.getText().toString());
            hashMap.put("CollectionDay", StrCollctnDay);
            hashMap.put("CollectionStaff", StrCollctnStaff);
            hashMap.put("MoratoriumPeriod", EdtMoratorium_period.getText().toString());
            hashMap.put("Remarks", EdtNarration.getText().toString());
            hashMap.put("Bank_Acc_No", EdtAcc_no.getText().toString());
            hashMap.put("EmiType", TvEmi_type.getText().toString());
            hashMap.put("flag", "INSERT");
            hashMap.put("TranMode", StrTransMode);
            hashMap.put("CoApplicant", groupData);

            Log.e("LoanMaster hashMap = ", hashMap.toString());
            try {
                jobjGetAll = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
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

    ///////////=========================================================================================


}
