package com.finwingway.digicob.loanModule;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.graphics.Color.RED;

public class JLGHouseVisitAdapterOne extends Fragment implements
        DatePickerDialog.OnDateSetListener, Validator.ValidationListener {

    public static String ip = login.ip_global;
    View rootView;
    RequestQueue requestQueue;
    JSONParser jsonParser;
    JSONObject jobjGetAll;
    SweetAlertDialog proDialog, errorDialog;
    ViewPager viewPagerHvisit = JLGHouseVisitCheckList.viewPagerHouseVisit;
    ArrayAdapter<String> adapterClient;
    ListView listViewClient;
    EditText edtArea;
    Button next,
            btnDate, btnCgtday1date, btnCgtday2date,
            btnHouse_vst_date, btnGrt_date;
    Spinner spnrbranch, spnrCenter, spnrGroup;
    static String[] CustIDArray, CustNameArray;
    String[] BranchNameArray, BranchCodeArray, CntrCodeArray,
            CntrNameArray, MakerIdArray, MakeTimeArray,
            BranchArray, CenterCodeArray, GroupCodeArray, GroupNameArray;
    String StrDate, StrCgtday1date, StrCgtday2date, StrHouseVstDate, StrGrtDate,
            Message, responseMsg, CMessage, CresponseMsg, GMessage, GresponseMsg;
    static String branch_id, sCenterCode, sGroupCode,
            sArea, sDate, sCgtday1date, sCgtday2date,
            sHouse_vst_date, sGrt_date;

    DatePickerDialog dpd;
    int DATE_DIALOG = 0;
    private FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.loan_jlg_house_visit_one, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentManager fragManager = myContext.getFragmentManager();
        jsonParser = new JSONParser();
        requestQueue = Volley.newRequestQueue(getContext());
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        listViewClient = (ListView) rootView.findViewById(R.id.list_client);

        edtArea = rootView.findViewById(R.id.edt_hv_area);
        next = rootView.findViewById(R.id.btn_hv_nxt);
        btnDate = rootView.findViewById(R.id.btn_hv_date);
        btnCgtday1date = rootView.findViewById(R.id.btn_hv_cgtday1date);
        btnCgtday2date = rootView.findViewById(R.id.btn_hv_cgtday2date);
        btnHouse_vst_date = rootView.findViewById(R.id.btn_hv_house_vst_date);
        btnGrt_date = rootView.findViewById(R.id.btn_hv_grt_date);

        spnrbranch = (Spinner) rootView.findViewById(R.id.spinner_hv_brnch);
        ArrayAdapter brachAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, login.arrayBrachName);
        spnrbranch.setAdapter(brachAdapter);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String compareValue = "Branch2";
        if (compareValue != null) {
            int spinnerPosition = brachAdapter.getPosition(compareValue);
            spnrbranch.setSelection(spinnerPosition);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        spnrbranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch_id = login.arrayBrachId[position];
                new getAllCenter().execute(branch_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnrCenter = (Spinner) rootView.findViewById(R.id.spinner_hv_center);
        spnrCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sCenterCode = CntrCodeArray[position];
                new getGroupList().execute(branch_id, sCenterCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnrGroup = (Spinner) rootView.findViewById(R.id.spinner_hv_group);
        spnrGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sGroupCode = GroupCodeArray[position];
                new getCoApplicantDetails().execute(sGroupCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                viewPagerHvisit.setCurrentItem(1, true);
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 1;
                openDialog(fragManager);
            }
        });

        btnCgtday1date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 2;
                openDialog(fragManager);
            }
        });

        btnCgtday2date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 3;
                openDialog(fragManager);
            }
        });

        btnHouse_vst_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 4;
                openDialog(fragManager);
            }
        });

        btnGrt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATE_DIALOG = 5;
                openDialog(fragManager);
            }
        });


        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String CurrentDate = df.format(c.getTime());
        btnDate.setText(CurrentDate);
        btnCgtday1date.setText(CurrentDate);
        btnCgtday2date.setText(CurrentDate);
        btnHouse_vst_date.setText(CurrentDate);
        btnGrt_date.setText(CurrentDate);
    }

    public void setData() {
        sArea = edtArea.getText().toString();
        sDate = btnDate.getText().toString();
        sCgtday1date = btnCgtday1date.getText().toString();
        sCgtday2date = btnCgtday2date.getText().toString();
        sHouse_vst_date = btnHouse_vst_date.getText().toString();
        sGrt_date = btnGrt_date.getText().toString();
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
            StrDate = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
            btnDate.setText(StrDate);
            btnDate.setTextColor(RED);
            btnDate.setTextSize(18);
        }
        if (DATE_DIALOG == 2) {
            StrCgtday1date = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
            btnCgtday1date.setText(StrCgtday1date);
            btnCgtday1date.setTextColor(RED);
            btnCgtday1date.setTextSize(18);
        }
        if (DATE_DIALOG == 3) {
            StrCgtday2date = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
            btnCgtday2date.setText(StrCgtday2date);
            btnCgtday2date.setTextColor(RED);
            btnCgtday2date.setTextSize(18);
        }
        if (DATE_DIALOG == 4) {
            StrHouseVstDate = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
            btnHouse_vst_date.setText(StrHouseVstDate);
            btnHouse_vst_date.setTextColor(RED);
            btnHouse_vst_date.setTextSize(18);
        }
        if (DATE_DIALOG == 5) {
            StrGrtDate = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
            btnGrt_date.setText(StrGrtDate);
            btnGrt_date.setTextColor(RED);
            btnGrt_date.setTextSize(18);
        }
    }

    public void openDialog(FragmentManager fragManager) {
        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                JLGHouseVisitAdapterOne.this,
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
    private class getAllCenter extends AsyncTask<String, String, String> {
        private String api_url = ip + "/GetAllLoanCentreByBranch";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Brcode", strings[0]);
            Log.e("GtAlLonCntrBr HV", String.valueOf(hashMap));
            try {
                jobjGetAll = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("getAllCenter data ", jobjGetAll.toString());
                if (jobjGetAll.has("status")) {
                    responseMsg = jobjGetAll.getString("status");
                    if (responseMsg.equals("1")) {
                        JSONArray jsonarray = new JSONArray(jobjGetAll.getString("data"));
                        int cnt = jsonarray.length();
                        BranchNameArray = new String[cnt];
                        BranchCodeArray = new String[cnt];
                        CntrCodeArray = new String[cnt];
                        CntrNameArray = new String[cnt];
                        MakerIdArray = new String[cnt];
                        MakeTimeArray = new String[cnt];
                        for (int i = 0; i < cnt; i++) {
                            JSONObject arrayObject = jsonarray.getJSONObject(i);
                            BranchNameArray[i] = arrayObject.getString("BranchName");
                            BranchCodeArray[i] = arrayObject.getString("BranchCode");
                            CntrCodeArray[i] = arrayObject.getString("Center Code");
                            CntrNameArray[i] = arrayObject.getString("Center Name");
                            MakerIdArray[i] = arrayObject.getString("Maker Id");
                            MakeTimeArray[i] = arrayObject.getString("Making Time");
                        }
                    } else if (responseMsg.equals("0")) {
                        Message = jobjGetAll.getString("msg");
                    }
                }
            } catch (Exception e) {
                proDialog.dismiss();
                e.printStackTrace();
                responseMsg = "exception";
            }
            return responseMsg;
        }

        @Override
        protected void onPostExecute(String url) {
            proDialog.dismiss();
            if (url.equals("1")) {
                ArrayAdapter centerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, CntrNameArray);
                spnrCenter.setAdapter(centerAdapter);
            } else if (url.equals("0")) {
                Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();
            } else if (url.equals("exception")) {
                Toast.makeText(getActivity(), "Server error occurred!!..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getGroupList extends AsyncTask<String, String, String> {
        private String api_url = ip + "/SelectGroup";

        @Override
        public void onPreExecute() {
            proDialog.setTitle("Please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("BrCode", params[0]);
            hashMap.put("CenterCode", params[1]);
            Log.e("SelectGroup:HV ", String.valueOf(hashMap));

            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("JLGLoanGrp jsonObj", String.valueOf(jsonObject));
                GresponseMsg = jsonObject.getString("status");
                if (GresponseMsg.equals("1")) {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    Log.e("JLGLoanG Search_group", jsonArray.toString());
                    BranchArray = new String[jsonArray.length()];
                    CenterCodeArray = new String[jsonArray.length()];
                    GroupCodeArray = new String[jsonArray.length()];
                    GroupNameArray = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arrayObject = jsonArray.getJSONObject(i);
                        BranchArray[i] = arrayObject.getString("Br_Code");
                        CenterCodeArray[i] = arrayObject.getString("JLG_CenterCode");
                        GroupCodeArray[i] = arrayObject.getString("JLG_GroupCode");
                        GroupNameArray[i] = arrayObject.getString("JLG_GroupName");
                    }
                } else if (GresponseMsg.equals("0")) {
                    GMessage = jsonObject.getString("msg");
                }
            } catch (JSONException e) {
                GresponseMsg = "0";
                GMessage = "Internal Server Error Occurred!";
                e.printStackTrace();
            }
            return GresponseMsg;
        }

        @Override
        public void onPostExecute(final String result) {
            proDialog.dismiss();
            if (result.equals("1")) {
                ArrayAdapter brachAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, GroupNameArray);
                spnrGroup.setAdapter(brachAdapter);
            } else if (result.equals("0")) {
                Toast.makeText(getActivity(), GMessage, Toast.LENGTH_SHORT).show();
            } else if (result.equals("exception")) {
                Toast.makeText(getActivity(), "Server error occurred!!..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getCoApplicantDetails extends AsyncTask<String, String, String> {
        private String api_url = ip + "/CoApplicantDetails";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("GroupCode", strings[0]);
            Log.e("getCoApplcntDtls HV", String.valueOf(hashMap));
            try {
                JSONObject jobjGetAll = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("getCoApplcntDtls data ", jobjGetAll.toString());
                if (jobjGetAll.has("status")) {
                    CresponseMsg = jobjGetAll.getString("status");
                    if (CresponseMsg.equals("1")) {
                        JSONArray jsonarray = new JSONArray(jobjGetAll.getString("data"));
                        int cnt = jsonarray.length();
//                        SlNoArray = new String[cnt];
                        CustIDArray = new String[cnt];
                        CustNameArray = new String[cnt];
                        for (int i = 0; i < cnt; i++) {
                            JSONObject arrayObject = jsonarray.getJSONObject(i);
//                            SlNoArray[i] = arrayObject.getString("SlNo");
                            CustIDArray[i] = arrayObject.getString("CustID");
                            CustNameArray[i] = arrayObject.getString("CustName");
                        }
                    } else if (CresponseMsg.equals("0")) {
                        CMessage = jobjGetAll.getString("msg");
                    }
                }
            } catch (Exception e) {
                proDialog.dismiss();
                e.printStackTrace();
                CresponseMsg = "exception";
            }
            return CresponseMsg;
        }

        @Override
        protected void onPostExecute(String url) {
            proDialog.dismiss();
            if (url.equals("1")) {
                adapterClient = new ArrayAdapter<String>(getContext(), R.layout.list_item, CustNameArray);
                listViewClient.setAdapter(adapterClient);
            } else if (url.equals("0")) {
                Toast.makeText(getActivity(), CMessage, Toast.LENGTH_SHORT).show();
            } else if (url.equals("exception")) {
                Toast.makeText(getActivity(), "Server error occurred!!..", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
