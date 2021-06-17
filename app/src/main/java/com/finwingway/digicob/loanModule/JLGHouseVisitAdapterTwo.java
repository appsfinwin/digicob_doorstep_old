package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JLGHouseVisitAdapterTwo extends Fragment {

    public static String ip = login.ip_global;
    String responseMsg, alertMsg;
    View rootView;
    Button prev, btnSubmit;
    RequestQueue requestQueue;
    JSONParser jsonParser;
    SweetAlertDialog proDialog, errorDialog, succDialog;
    ViewPager viewPagerHvisit = JLGHouseVisitCheckList.viewPagerHouseVisit;
    LinearLayout cb_layout_1, cb_layout_2, cb_layout_3, cb_layout_4, cb_layout_5, cb_layout_6;

    List<String> physicalVerfctnAry = new ArrayList<String>();
    List<String> kyc_verfctnAry = new ArrayList<String>();
    List<String> purpose_of_loanAry = new ArrayList<String>();
    List<String> ocptnIncome_vrftnAry = new ArrayList<String>();
    List<String> group_guaranteeAry = new ArrayList<String>();
    List<String> outsideBorrowngAry = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_jlg_house_visit_two, container, false);
        jsonParser = new JSONParser();
        requestQueue = Volley.newRequestQueue(getContext());
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);

        cb_layout_1 = (LinearLayout) rootView.findViewById(R.id.linr_1);
        cb_layout_2 = (LinearLayout) rootView.findViewById(R.id.linr_2);
        cb_layout_3 = (LinearLayout) rootView.findViewById(R.id.linr_3);
        cb_layout_4 = (LinearLayout) rootView.findViewById(R.id.linr_4);
        cb_layout_5 = (LinearLayout) rootView.findViewById(R.id.linr_5);
        cb_layout_6 = (LinearLayout) rootView.findViewById(R.id.linr_6);

        prev = rootView.findViewById(R.id.btn_hv_pre);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerHvisit.setCurrentItem(0, true);
            }
        });

        btnSubmit = rootView.findViewById(R.id.btn_hv_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("HouseVisitClient() ",HouseVisitClient());
                new SubmitJLGLoanTransaction().execute();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPagerHvisit.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                try {
                    if (position == 1) {
                        if (JLGHouseVisitAdapterOne.CustNameArray != null && JLGHouseVisitAdapterOne.CustNameArray.length > 0)
                            createChekbox();
                    }
                } catch (Exception e) {
                    Log.e("onPageSelected: ", String.valueOf("JLGHouseVisitAdapterTwo =- " + e));
                }
            }
        });
    }

    public void createChekbox() {
        int Array_Count = 0;
        String[] Str_Array = JLGHouseVisitAdapterOne.CustNameArray;
        Array_Count = Str_Array.length;

        cb_layout_1.removeAllViews();
        cb_layout_2.removeAllViews();
        cb_layout_3.removeAllViews();
        cb_layout_4.removeAllViews();
        cb_layout_5.removeAllViews();
        cb_layout_6.removeAllViews();

        for (int i = 0; i < Array_Count; i++) {
            TableRow row = new TableRow(getContext());
            row.setId(i);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final CheckBox checkBox = new CheckBox(rootView.getContext());
            checkBox.setId(i);
            checkBox.setText(Str_Array[i]);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int cId = checkBox.getId();
                    if (physicalVerfctnAry.contains(JLGHouseVisitAdapterOne.CustIDArray[cId]) && !isChecked) {
                        physicalVerfctnAry.remove(JLGHouseVisitAdapterOne.CustIDArray[cId]);
//                        System.out.println("Account found");
                    } else {
                        physicalVerfctnAry.add(JLGHouseVisitAdapterOne.CustIDArray[cId]);
//                        System.out.println("Account not found");
                    }
                }
            });
            row.addView(checkBox);
            cb_layout_1.addView(row);
        }

        for (int i = 0; i < Array_Count; i++) {
            TableRow row = new TableRow(getContext());
            row.setId(i);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final CheckBox checkBox = new CheckBox(getContext());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int cId = checkBox.getId();
                    if (kyc_verfctnAry.contains(JLGHouseVisitAdapterOne.CustIDArray[cId]) && !isChecked) {
                        kyc_verfctnAry.remove(JLGHouseVisitAdapterOne.CustIDArray[cId]);
                    } else {
                        kyc_verfctnAry.add(JLGHouseVisitAdapterOne.CustIDArray[cId]);
                    }
                }
            });
            checkBox.setId(i);
            checkBox.setText(Str_Array[i]);
            row.addView(checkBox);
            cb_layout_2.addView(row);
        }

        for (int i = 0; i < Array_Count; i++) {
            TableRow row = new TableRow(getContext());
            row.setId(i);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final CheckBox checkBox = new CheckBox(getContext());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int cId = checkBox.getId();
                    if (purpose_of_loanAry.contains(JLGHouseVisitAdapterOne.CustIDArray[cId]) && !isChecked) {
                        purpose_of_loanAry.remove(JLGHouseVisitAdapterOne.CustIDArray[cId]);
                    } else {
                        purpose_of_loanAry.add(JLGHouseVisitAdapterOne.CustIDArray[cId]);
                    }
                }
            });
            checkBox.setId(i);
            checkBox.setText(Str_Array[i]);
            row.addView(checkBox);
            cb_layout_3.addView(row);
        }

        for (int i = 0; i < Array_Count; i++) {
            TableRow row = new TableRow(getContext());
            row.setId(i);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final CheckBox checkBox = new CheckBox(getContext());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int cId = checkBox.getId();
                    if (ocptnIncome_vrftnAry.contains(JLGHouseVisitAdapterOne.CustIDArray[cId]) && !isChecked) {
                        ocptnIncome_vrftnAry.remove(JLGHouseVisitAdapterOne.CustIDArray[cId]);
                    } else {
                        ocptnIncome_vrftnAry.add(JLGHouseVisitAdapterOne.CustIDArray[cId]);
                    }
                }
            });
            checkBox.setId(i);
            checkBox.setText(Str_Array[i]);
            row.addView(checkBox);
            cb_layout_4.addView(row);
        }

        for (int i = 0; i < Array_Count; i++) {
            TableRow row = new TableRow(getContext());
            row.setId(i);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final CheckBox checkBox = new CheckBox(getContext());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int cId = checkBox.getId();
                    if (group_guaranteeAry.contains(JLGHouseVisitAdapterOne.CustIDArray[cId]) && !isChecked) {
                        group_guaranteeAry.remove(JLGHouseVisitAdapterOne.CustIDArray[cId]);
                    } else {
                        group_guaranteeAry.add(JLGHouseVisitAdapterOne.CustIDArray[cId]);
                    }
                }
            });
            checkBox.setId(i);
            checkBox.setText(Str_Array[i]);
            row.addView(checkBox);
            cb_layout_5.addView(row);
        }

        for (int i = 0; i < Array_Count; i++) {
            TableRow row = new TableRow(getContext());
            row.setId(i);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final CheckBox checkBox = new CheckBox(getContext());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int cId = checkBox.getId();
                    if (outsideBorrowngAry.contains(JLGHouseVisitAdapterOne.CustIDArray[cId]) && !isChecked) {
                        outsideBorrowngAry.remove(JLGHouseVisitAdapterOne.CustIDArray[cId]);
                    } else {
                        outsideBorrowngAry.add(JLGHouseVisitAdapterOne.CustIDArray[cId]);
                    }
                }
            });
            checkBox.setId(i);
            checkBox.setText(Str_Array[i]);
            row.addView(checkBox);
            cb_layout_6.addView(row);
        }

    }

    public String HouseVisitClient() {
        String StrJson = "";
        JSONObject obj;
        JSONArray jsonArray = new JSONArray();
        try {
//            if (JLGHouseVisitAdapterOne.CustIDArray != null) {
//                for (int i = 0; i < JLGHouseVisitAdapterOne.CustIDArray.length; i++) {
//                    obj = new JSONObject();
//                    obj.put("Cust_Id", JLGHouseVisitAdapterOne.CustIDArray[i]);
//                    obj.put("House_Verification", physicalVerfctnAry.get(i));
//                    obj.put("KYC_Verification", kyc_verfctnAry.get(i));
//                    obj.put("New_Loan_Purpose", purpose_of_loanAry.get(i));
//                    obj.put("Verify_Occup_Income", ocptnIncome_vrftnAry.get(i));
//                    obj.put("Group_Gurantee", group_guaranteeAry.get(i));
//                    obj.put("Outside_Borrowings", outsideBorrowngAry.get(i));
//                    jsonArray.put(obj);
//                }
//                StrJson = jsonArray.toString();
//            }

            obj = new JSONObject();
            obj.put("physical_verfctn", physicalVerfctnAry);
            obj.put("kyc_verfctn", kyc_verfctnAry);
            obj.put("purpose_of_loan", purpose_of_loanAry);
            obj.put("occuptn_Income_verfctn", ocptnIncome_vrftnAry);
            obj.put("group_guarantee", group_guaranteeAry);
            obj.put("outside_borrowings", outsideBorrowngAry);
            StrJson = jsonArray.put(obj).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return StrJson;
    }

    public String ClientList() {
        String StrJson = "";
        try {
//            List<String> jArray = new ArrayList<String>();
//            Collections.addAll(jArray, JLGHouseVisitAdapterOne.CustIDArray);
//            StrJson = jArray.toString();

            JSONArray jsonArray = new JSONArray();
            for (String js : JLGHouseVisitAdapterOne.CustIDArray) {
                jsonArray.put(js);
            }
            StrJson = String.valueOf(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StrJson;
    }

    @SuppressLint("StaticFieldLeak")
    private class SubmitJLGLoanTransaction extends AsyncTask<String, String, String> {
        private String api_url = ip + "/HouseVisitChecklist";
        String clientData = "";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();
            clientData = ClientList();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Brcode", JLGHouseVisitAdapterOne.branch_id);
            hashMap.put("Area", JLGHouseVisitAdapterOne.sArea);
            hashMap.put("CenterCode", JLGHouseVisitAdapterOne.sCenterCode);
            hashMap.put("GroupCode", JLGHouseVisitAdapterOne.sGroupCode);
            hashMap.put("Date", JLGHouseVisitAdapterOne.sDate);
            hashMap.put("CGTDay1", JLGHouseVisitAdapterOne.sCgtday1date);
            hashMap.put("CGTDay2", JLGHouseVisitAdapterOne.sCgtday2date);
            hashMap.put("HouseVisitDate", JLGHouseVisitAdapterOne.sHouse_vst_date);
            hashMap.put("GRTDate", JLGHouseVisitAdapterOne.sGrt_date);

            hashMap.put("physical_verfctn", String.valueOf(new JSONArray(physicalVerfctnAry)));
            hashMap.put("kyc_verfctn", String.valueOf(new JSONArray(kyc_verfctnAry)));
            hashMap.put("purpose_of_loan", String.valueOf(new JSONArray(purpose_of_loanAry)));
            hashMap.put("occuptn_Income_verfctn", String.valueOf(new JSONArray(ocptnIncome_vrftnAry)));
            hashMap.put("group_guarantee", String.valueOf(new JSONArray(group_guaranteeAry)));
            hashMap.put("outside_borrowings", String.valueOf(new JSONArray(outsideBorrowngAry)));
            hashMap.put("ClientList", clientData);

            Log.e("HVisitChecklist #Map", hashMap.toString());
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
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Error!!")
                        .setContentText(alertMsg)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (url.equals("exception")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
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
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!!")
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
