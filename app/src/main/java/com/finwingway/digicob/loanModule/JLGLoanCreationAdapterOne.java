package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class JLGLoanCreationAdapterOne extends Fragment {
    View rootView;
    JSONParser jsonParser;
    Button next;
    ViewPager viewPager = JLGLoanCreation.viewPager;
    public static String ip = login.ip_global;
    static String StrBrnchId = login.branch_id;
    String responseMsg, Message,
            respLnMsg, LnMessage;
    SweetAlertDialog proDialog, errorDialog;
    TextView edtScheme_no, edtBrnch_code, txtRemark, txtApplctn_no;
    Spinner spinerType, spinnerScheme, spinnerBrnch, spinnerSector, spinnerSub_sector;
    //            TypCodeArray, TypType_CodArry, TypNameArray = null,

    public static String[]
            SchCodeArray, SchNameArray = null, SchIsSplitArray = null, SchEMITypeArray = null, SchIntCalcArray = null,
            SecCodeArray, SecType_CodArry, SecNameArray = null,
            SubCodeArray, SubType_CodArry, SubNameArray = null,
            ModCodeArray, ModNameArray = null, PerCodeArray, PerNameArray = null,
            CollcStaffCodeArray, CollcStaffNameArray;

    public static String StrSpinnerCode = "null", StrSpinnerScheme = "null", StrSpinnerBrnch = "null";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_creation_page_one, container, false);
        jsonParser = new JSONParser();
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);

        edtScheme_no = rootView.findViewById(R.id.edt_scheme_no);
        edtBrnch_code = rootView.findViewById(R.id.edt_brnch_code);
        edtBrnch_code.setText(StrBrnchId);
        txtRemark = rootView.findViewById(R.id.txt_remark);
        txtApplctn_no = rootView.findViewById(R.id.txt_applctn_no);

        next = rootView.findViewById(R.id.btn_loan_nxt);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                viewPager.setCurrentItem(1, true);
            }
        });

        spinnerScheme = rootView.findViewById(R.id.spinner_scheme);
        spinnerScheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrSpinnerScheme = SchNameArray[position];
                StrSpinnerCode = SchCodeArray[position];

                JLGLoanCreationGetSet.setIsSplit(SchIsSplitArray[position]);
                JLGLoanCreationGetSet.setEMIType(SchEMITypeArray[position]);
                JLGLoanCreationGetSet.setCalcType(SchIntCalcArray[position]);
                edtScheme_no.setText(StrSpinnerCode);
                new getLoanPeriodList().execute(StrSpinnerCode);
//                new getSchemeDetails().execute(StrSpinnerCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerBrnch = rootView.findViewById(R.id.spinner_brnch);
        spinnerBrnch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrSpinnerBrnch = login.arrayBrachId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerSector = rootView.findViewById(R.id.spinner_sector);
        spinnerSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                StrSpinnerSector = SecNameArray[position];
                JLGLoanCreationGetSet.setSector(SecCodeArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerSub_sector = rootView.findViewById(R.id.spinner_sub_sector);
        spinnerSub_sector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                StrSpinerSubSect = SubNameArray[position];
                JLGLoanCreationGetSet.setSubSector(SubCodeArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        new getDropdownList().execute();
        new getProduct().execute();
        return rootView;
    }

    public void setData() {
        JLGLoanCreationGetSet.setSchemeNo(edtScheme_no.getText().toString());
        JLGLoanCreationGetSet.setBrnchCode(edtBrnch_code.getText().toString());
        JLGLoanCreationGetSet.setRemark(txtRemark.getText().toString());
        JLGLoanCreationGetSet.setApplctn_no(txtApplctn_no.getText().toString());
    }


    ///////////=========================================================================================
    @SuppressLint("StaticFieldLeak")
    private class getDropdownList extends AsyncTask<String, String, String> {
        private String api_url = ip + "/RefCodeMaster";

        @Override
        public void onPreExecute() {
            proDialog.setTitleText("Please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("", "");
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "GET", hashMap);
                Log.e("getDropdownList", jsonObject.toString());
                responseMsg = jsonObject.getString("status");
                if (responseMsg.equals("1")) {
                    //-----------------Sector
                    JSONArray sectorArray = new JSONArray(jsonObject.getString("Sector"));
                    Log.e("Sector=>", sectorArray.toString());
                    SecCodeArray = new String[sectorArray.length()];
                    SecType_CodArry = new String[sectorArray.length()];
                    SecNameArray = new String[sectorArray.length()];
                    for (int i = 0; i < sectorArray.length(); i++) {
                        JSONObject secArrayObject = sectorArray.getJSONObject(i);
                        SecCodeArray[i] = secArrayObject.getString("Code");
                        SecType_CodArry[i] = secArrayObject.getString("Type_Code");
                        SecNameArray[i] = secArrayObject.getString("Name");
                    }

                    //-----------------Subsector
                    JSONArray subSecArray = new JSONArray(jsonObject.getString("Subsector"));
                    Log.e("Subsector=>", subSecArray.toString());
                    SubCodeArray = new String[subSecArray.length()];
                    SubType_CodArry = new String[subSecArray.length()];
                    SubNameArray = new String[subSecArray.length()];
                    for (int i = 0; i < subSecArray.length(); i++) {
                        JSONObject SubSecArrayObject = subSecArray.getJSONObject(i);
                        SubCodeArray[i] = SubSecArrayObject.getString("Code");
                        SubType_CodArry[i] = SubSecArrayObject.getString("Type_Code");
                        SubNameArray[i] = SubSecArrayObject.getString("Name");
                    }

                    //-----------------Scheme
                    JSONArray SchemeArray = new JSONArray(jsonObject.getString("Scheme"));
                    Log.e("Scheme=>", SchemeArray.toString());
                    SchCodeArray = new String[SchemeArray.length()];
                    SchNameArray = new String[SchemeArray.length()];
                    SchIsSplitArray = new String[SchemeArray.length()];
                    SchEMITypeArray = new String[SchemeArray.length()];
                    SchIntCalcArray = new String[SchemeArray.length()];
                    for (int i = 0; i < SchemeArray.length(); i++) {
                        JSONObject SchAryObject = SchemeArray.getJSONObject(i);
                        SchCodeArray[i] = SchAryObject.getString("Sch_Code");
                        SchNameArray[i] = SchAryObject.getString("Sch_Name");
                        SchIsSplitArray[i] = SchAryObject.getString("Lnp_IsSplitAcc");
                        SchEMITypeArray[i] = SchAryObject.getString("Lnp_EMIType");
                        SchIntCalcArray[i] = SchAryObject.getString("Lnp_IntCalcType");
                    }

                    //-----------------Period
                    JSONArray PeriodArray = new JSONArray(jsonObject.getString("Period"));
                    Log.e("Period=>", PeriodArray.toString());
                    PerCodeArray = new String[PeriodArray.length()];
                    PerNameArray = new String[PeriodArray.length()];
                    for (int i = 0; i < PeriodArray.length(); i++) {
                        JSONObject PeriodAryObj = PeriodArray.getJSONObject(i);
                        PerCodeArray[i] = PeriodAryObj.getString("Code");
                        PerNameArray[i] = PeriodAryObj.getString("Name");
                    }

                    //-----------------Mode
                    JSONArray ModeArray = new JSONArray(jsonObject.getString("Mode"));
                    Log.e("Period=>", ModeArray.toString());
                    ModCodeArray = new String[ModeArray.length()];
                    ModNameArray = new String[ModeArray.length()];
                    for (int i = 0; i < ModeArray.length(); i++) {
                        JSONObject PeriodAryObj = ModeArray.getJSONObject(i);
                        ModCodeArray[i] = PeriodAryObj.getString("Code");
                        ModNameArray[i] = PeriodAryObj.getString("Name");
                    }

                    //-----------------Collection Staff
                    JSONArray CollcStaffArray = new JSONArray(jsonObject.getString("CollectionStaff"));
                    Log.e("CollcStaff=>", CollcStaffArray.toString());
                    CollcStaffCodeArray = new String[CollcStaffArray.length()];
                    CollcStaffNameArray = new String[CollcStaffArray.length()];
                    for (int i = 0; i < CollcStaffArray.length(); i++) {
                        JSONObject PeriodAryObj = CollcStaffArray.getJSONObject(i);
                        CollcStaffCodeArray[i] = PeriodAryObj.getString("Cust_ID");
                        CollcStaffNameArray[i] = PeriodAryObj.getString("Name");
                    }

                } else if (responseMsg.equals("0")) {
                    Message = jsonObject.getString("msg");
                }
            } catch (JSONException e) {
                responseMsg = "2";
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                responseMsg = "2";
            }
            return responseMsg;
        }

        @Override
        public void onPostExecute(final String result) {
            switch (result) {
                case "0":
                    proDialog.dismiss();
                    errorDialog.setTitleText("Error!!")
                            .setContentText(Message)
                            .show();
                    break;
                case "2":
                    proDialog.dismiss();
                    errorDialog.setTitleText("Error!!")
                            .setContentText("Something went wrong!")
                            .show();
                    break;
                case "1":

                    if (SchNameArray != null && SchNameArray.length > 0) {
                        ArrayAdapter SchemeAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, SchNameArray);
                        spinnerScheme.setAdapter(SchemeAdapter);
                    }
                    if (login.arrayBrachName != null && login.arrayBrachName.length > 0) {
                        ArrayAdapter brachAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, login.arrayBrachName);
                        spinnerBrnch.setAdapter(brachAdapter);
                        try {
                            spinnerBrnch.setSelection(brachAdapter.getPosition(login.branch_name));
                        } catch (Exception ignore) {
                        }
                    }
                    if (SecNameArray != null && SecNameArray.length > 0) {
                        ArrayAdapter SectorAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, SecNameArray);
                        spinnerSector.setAdapter(SectorAdapter);
                    }
                    if (SubNameArray != null && SubNameArray.length > 0) {
                        ArrayAdapter SubSectorAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, SubNameArray);
                        spinnerSub_sector.setAdapter(SubSectorAdapter);
                    }

                    proDialog.dismiss();
                    break;
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getProduct extends AsyncTask<String, String, String> {
        private String api_url = ip + "/JLGSelectProduct";

        @Override
        protected void onPreExecute() {
            proDialog.dismiss();
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... arg) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Br_Code", login.branch_id);
            try {
                JSONObject jobjGetAllmemb = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("JLGSelectProduct =", jobjGetAllmemb.toString());
                if (jobjGetAllmemb.has("status")) {
                    responseMsg = jobjGetAllmemb.getString("status");
                    if (responseMsg.equals("1")) {
                        JSONArray jsonarray = new JSONArray(jobjGetAllmemb.getString("data"));
                        int cnt = jsonarray.length();
                        JLGLoanCreationGetSet.StrProductIdArry = new String[cnt];
                        JLGLoanCreationGetSet.StrProductNameArry = new String[cnt];
                        for (int i = 0; i < cnt; i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            JLGLoanCreationGetSet.StrProductIdArry[i] = jsonobject.getString("ProductId");
                            JLGLoanCreationGetSet.StrProductNameArry[i] = jsonobject.getString("ProductName");
                        }
                    } else if (responseMsg.equals("0")) {
                        Message = jobjGetAllmemb.getString("msg");
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
//            adapter.notifyDataSetChanged();
            proDialog.dismiss();
            if (url.equals("0")) {
//                errorDialog.setTitleText("Error!!")
//                        .setContentText(Message)
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismiss();
//                            }
//                        })
//                        .show();
                Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();
            } else if (url.equals("exception")) {
//                errorDialog.setTitleText("Error!!")
//                        .setContentText("Something went wrong!")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismiss();
//                            }
//                        })
//                        .show();
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ///////////========

    @SuppressLint("StaticFieldLeak")
    private class getLoanPeriodList extends AsyncTask<String, String, String> {
        private String api_url = ip + "/JLGLoanPeriod";

        @Override
        public void onPreExecute() {
            proDialog.setTitleText("Please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Sch_Code", params[0]);
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("getLoanPeriodList", jsonObject.toString());
                JSONObject jobj = jsonObject.getJSONObject("receipt");
                if (jobj.has("data")) {
                    JSONObject jsonObj = jobj.getJSONObject("data");
                    if (jsonObj.has("DD")) {
                        JSONArray DDArray = jsonObj.getJSONArray("DD");
                        int cnt = DDArray.length();
                        JLGLoanCreationAdapterThree.DDPeriodTypeArray = new String[cnt];
                        JLGLoanCreationAdapterThree.DDPeriodArray = new String[cnt];
                        JLGLoanCreationAdapterThree.DDIntRateArray = new String[cnt];
                        JLGLoanCreationAdapterThree.DDPenalRateArray = new String[cnt];
                        for (int i = 0; i < cnt; i++) {
                            JSONObject DDAryObj = DDArray.getJSONObject(i);
                            JLGLoanCreationAdapterThree.DDPeriodTypeArray[i] = DDAryObj.getString("Ln_PeriodType");
                            JLGLoanCreationAdapterThree.DDPeriodArray[i] = DDAryObj.getString("Ln_Period");
                            JLGLoanCreationAdapterThree.DDIntRateArray[i] = DDAryObj.getString("Ln_IntRate");
                            JLGLoanCreationAdapterThree.DDPenalRateArray[i] = DDAryObj.getString("Ln_PenalRate");
                        }
                    }
                    if (jsonObj.has("WW")) {
                        JSONArray WWArray = jsonObj.getJSONArray("WW");
                        int cnt = WWArray.length();
                        JLGLoanCreationAdapterThree.WWPeriodTypeArray = new String[cnt];
                        JLGLoanCreationAdapterThree.WWPeriodArray = new String[cnt];
                        JLGLoanCreationAdapterThree.WWIntRateArray = new String[cnt];
                        JLGLoanCreationAdapterThree.WWPenalRateArray = new String[cnt];
                        for (int i = 0; i < cnt; i++) {
                            JSONObject WWAryObj = WWArray.getJSONObject(i);
                            JLGLoanCreationAdapterThree.WWPeriodTypeArray[i] = WWAryObj.getString("Ln_PeriodType");
                            JLGLoanCreationAdapterThree.WWPeriodArray[i] = WWAryObj.getString("Ln_Period");
                            JLGLoanCreationAdapterThree.WWIntRateArray[i] = WWAryObj.getString("Ln_IntRate");
                            JLGLoanCreationAdapterThree.WWPenalRateArray[i] = WWAryObj.getString("Ln_PenalRate");
                        }
                    }
                    if (jsonObj.has("MM")) {
                        JSONArray MMArray = jsonObj.getJSONArray("MM");
                        int cnt = MMArray.length();
                        JLGLoanCreationAdapterThree.MMPeriodTypeArray = new String[cnt];
                        JLGLoanCreationAdapterThree.MMPeriodArray = new String[cnt];
                        JLGLoanCreationAdapterThree.MMIntRateArray = new String[cnt];
                        JLGLoanCreationAdapterThree.MMPenalRateArray = new String[cnt];
                        for (int i = 0; i < cnt; i++) {
                            JSONObject MMAryObj = MMArray.getJSONObject(i);
                            JLGLoanCreationAdapterThree.MMPeriodTypeArray[i] = MMAryObj.getString("Ln_PeriodType");
                            JLGLoanCreationAdapterThree.MMPeriodArray[i] = MMAryObj.getString("Ln_Period");
                            JLGLoanCreationAdapterThree.MMIntRateArray[i] = MMAryObj.getString("Ln_IntRate");
                            JLGLoanCreationAdapterThree.MMPenalRateArray[i] = MMAryObj.getString("Ln_PenalRate");
                        }
                    }
                    respLnMsg = "1";
                } else if (jobj.has("error")) {
                    LnMessage = jobj.getJSONObject("error").getString("msg");
                    respLnMsg = "0";
                }
            } catch (JSONException e) {
                proDialog.dismiss();
                e.printStackTrace();
                respLnMsg = "2";
            } catch (Exception e) {
                proDialog.dismiss();
                e.printStackTrace();
                respLnMsg = "2";
            }
            return respLnMsg;
        }

        @Override
        public void onPostExecute(final String result) {
            proDialog.dismiss();
            switch (result) {
                case "0":
                    Toast.makeText(getContext(), "Waring - " + LnMessage, Toast.LENGTH_SHORT).show();
//                    errorDialog.setTitleText("Error!!")
//                            .setContentText(LnMessage)
//                            .show();
                    break;
                case "2":
                    Toast.makeText(getContext(), "Server Error occurred..!!", Toast.LENGTH_SHORT).show();
//                    errorDialog.setTitleText("Error!!")
//                            .setContentText("Something went wrong!")
//                            .show();
                    break;
                case "1":

                    break;
            }
        }
    }
}

