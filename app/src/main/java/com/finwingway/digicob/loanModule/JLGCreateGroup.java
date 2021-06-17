package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class JLGCreateGroup extends Fragment {
    ImageButton search_image_btn, search_cntr_btn;
    Button btnAdd, btnSubmit, btnViewAll;
    EditText ETsearch, EdtCtrCode, EdtCtrName, EdtGrpCode, EdtGrpName;
    String strAccNo = null, strCustId = null, strName = null, responseData, Message, StrBrcode, StrCenterCode, StrGroupCode, StrGroupName, StrMakerId, StrCustId;
    String respnsMsg, alertMsg, StrEdtSearch;
    View rootView;
    JSONParser jsonParser;
    public static String ip = login.ip_global;
    SweetAlertDialog progressDialog;

    static int srlno = 1;
    static ListviewAdapterGroup adapter;
    RequestQueue requestQueue;

    private static ArrayList<HashMap<String, String>> list;
    private static ArrayList<HashMap<String, String>> templist;
    public static final String SLNO_COLUMN = "SlNo";
    public static final String ACCNO_COLUMN = "AccNo";
    public static final String CUSTID_COLUMN = "CustID";
    public static final String NAME_COLUMN = "Name";
    public static boolean FindBtnClickd = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_create_group, container, false);
        jsonParser = new JSONParser();
        requestQueue = Volley.newRequestQueue(getContext());

        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        ETsearch = rootView.findViewById(R.id.search_edit_text);
        EdtCtrCode = rootView.findViewById(R.id.edt_cntrCode);
        EdtCtrName = rootView.findViewById(R.id.edt_cntrName);
        EdtGrpCode = rootView.findViewById(R.id.edt_grpCode);
        EdtGrpName = rootView.findViewById(R.id.edt_grpName);

        EdtCtrCode.setEnabled(false);
        EdtCtrCode.setFocusable(false);
        EdtCtrName.setEnabled(false);
        EdtCtrName.setFocusable(false);

        search_cntr_btn = rootView.findViewById(R.id.cntr_search_btn);
        search_cntr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Search_Center.class);
                startActivityForResult(intent, 1004);
            }
        });

        search_image_btn = rootView.findViewById(R.id.acc_search_btn);
        search_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindBtnClickd = true;
                Intent debit_intent = new Intent(getActivity(), Search_customer.class);
                startActivityForResult(debit_intent, 1004);
            }
        });

        list = new ArrayList<HashMap<String, String>>();
        templist = new ArrayList<HashMap<String, String>>();
        ListView listView = rootView.findViewById(R.id.listview);
        adapter = new ListviewAdapterGroup(getActivity(), list);
        listView.setAdapter(adapter);

        btnAdd = rootView.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FindBtnClickd) {
                    ETsearch.setError(null);
                    StrEdtSearch = ETsearch.getText().toString();
                    if (!StrEdtSearch.equals("")) {
                        new getAccountHolderByCustId().execute();
                    } else {
                        ETsearch.setError("Enter Customer ID");
                    }
                } else {
                    if (strCustId != null && !strCustId.isEmpty()) {
                        HashMap<String, String> hashmap = new HashMap<String, String>();
                        hashmap.put(SLNO_COLUMN, "");
                        hashmap.put(ACCNO_COLUMN, strAccNo);
                        hashmap.put(CUSTID_COLUMN, strCustId);
                        hashmap.put(NAME_COLUMN, strName);
                        templist.add(hashmap);

                        addSlNoList();
                        strAccNo = "";
                        strCustId = "";
                        strName = "";
                        ETsearch.setText("");

                        FindBtnClickd = false;
                    }
                }
            }
        });

        btnSubmit = rootView.findViewById(R.id.btn_grp_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EdtCtrCode.getText().toString().isEmpty() && !EdtGrpName.getText().toString().isEmpty()) {
//                    new createGroup().execute();
                    createGroup();
                } else {
                    if (EdtCtrCode.getText().toString().isEmpty()) {
                        EdtCtrCode.setError("Please Select Center!!");
                    }
                    if (EdtGrpName.getText().toString().isEmpty()) {
                        EdtGrpName.setError("Enter Group Name!!");
                    }
                }
            }
        });

        btnViewAll = rootView.findViewById(R.id.btn_grp_all);
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
                AllLoanGroup allLoanGroup = new AllLoanGroup();
                ft.replace(R.id.content_frame, allLoanGroup);
                ft.commit();
            }
        });

        return rootView;
    }

    public static void addSlNoList() {
        srlno = 1;
        list.clear();
        for (HashMap<String, String> map : templist) {
            map.put(SLNO_COLUMN, String.valueOf(srlno++));
//            Log.e("ROW", String.valueOf(map));
            list.add(map);
        }
        adapter.notifyDataSetChanged();
    }

    public static void removeFromList(int id) {
        templist.remove(id);
        addSlNoList();
    }

    public String makeJsonObj() {
        JSONObject obj;
        JSONArray jsonArray = new JSONArray();
        try {
            for (HashMap<String, String> map : list) {
                obj = new JSONObject();
                    obj.put("Cust_Id", map.get(CUSTID_COLUMN));
                    jsonArray.put(obj);
                    Log.e("jsonArray.put ", jsonArray.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

//    public String makeJsonObj() {
//        String[] stockArr = new String[list.size()];
//        try {
//            int index = 0;
//            for (HashMap<String, String> map : list) {
//                stockArr[index] = map.get(CUSTID_COLUMN);
//                index++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Log.e("stockArr.toString()", stockArr.toString());
//        return Arrays.toString(stockArr);
//    }


//    public String makeJsonObj() {
//        String[] stockArr = new String[list.size()];
//        try {
//            int index = 0;
//            for (HashMap<String, String> map : list) {
//                stockArr[index] = map.get(CUSTID_COLUMN);
//                index++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Log.e("stockArr.toString()", stockArr.toString());
//        return Arrays.toString(stockArr);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1004 && resultCode == RESULT_OK) {
            try {
                strAccNo = data.getStringExtra("result_acc_no");
                strCustId = data.getStringExtra("result_cus_id");
                strName = data.getStringExtra("result_acc_name");
                ETsearch.setText(strCustId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 1004 && resultCode == 1008) {
            String ctr_code = null, ctr_name = null;
            try {
                ctr_code = data.getStringExtra("result_ctr_code");
                ctr_name = data.getStringExtra("result_ctr_name");
            } catch (Exception e) {
                e.printStackTrace();
            }
            EdtCtrCode.setText(ctr_code);
            EdtCtrName.setText(ctr_name);
        }
    }
    //===================================================================================

    @SuppressLint("StaticFieldLeak")
    private class createGroup extends AsyncTask<String, String, String> {
        private String api_url = ip + "/JLGLoanGroup";//LoanGroupCreator

        @Override
        protected void onPreExecute() {
            progressDialog.setTitleText("Loading");
            progressDialog.setContentText("please wait..");
            progressDialog.show();

            StrBrcode = login.branch_id;
            StrMakerId = login.agent_code;
            StrCenterCode = EdtCtrCode.getText().toString().trim();
            StrGroupCode = EdtGrpCode.getText().toString().trim();
            StrGroupName = EdtGrpName.getText().toString().trim();
            StrCustId = makeJsonObj();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("CustId", StrCustId);
            hashMap.put("Brcode", StrBrcode);
            hashMap.put("CenterCode", StrCenterCode);
            hashMap.put("GroupCode", StrGroupCode);
            hashMap.put("GroupName", StrGroupName);
            hashMap.put("MakerId", StrMakerId);
            hashMap.put("Flag", "INSERT");
            Log.e("CustId hashMap: ", hashMap.toString());
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("JLGLoanG LoanCreateGrp", data.toString());
                if (data.has("status")) {
                    responseData = data.getString("status");
                    if (responseData.equals("1")) {
                        Message = data.getString("msg");
                    } else if (responseData.equals("0")) {
                        Message = data.getString("msg");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseData = "exception";
            }
            return responseData;
        }

        @Override
        protected void onPostExecute(String url) {
            progressDialog.dismiss();
            switch (url) {
                case "0":
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!!")
                            .setContentText(Message)
                            .show();
                    break;
                case "exception":
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!!")
                            .setContentText("Something went wrong!")
                            .show();
                    break;
                case "1":
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success!!")
                            .setContentText(Message)
                            .show();

                    list.clear();
                    templist.clear();
                    adapter.notifyDataSetChanged();
                    ETsearch.setText("");
                    EdtCtrCode.setText("");
                    EdtCtrName.setText("");
                    EdtGrpCode.setText("");
                    EdtGrpName.setText("");
                    break;
            }
        }
    }


    private void createGroup() {
        String api_url = ip + "/JLGLoanGroup";//LoanGroupCreator

        progressDialog.setTitleText("Loading");
        progressDialog.setContentText("please wait..");
        progressDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject data = new JSONObject(response);
                            Log.e("createGroup: ", data.toString());
                            if (data.has("status")) {
                                responseData = data.getString("status");
                                if (responseData.equals("1")) {
                                    Message = data.getString("msg");
                                } else if (responseData.equals("0")) {
                                    Message = data.getString("msg");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            responseData = "exception";
                        } catch (Exception e) {
                            e.printStackTrace();
                            responseData = "exception";
                        }

                        switch (responseData) {
                            case "0":
                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!!")
                                        .setContentText(Message)
                                        .show();
                                break;
                            case "exception":
                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!!")
                                        .setContentText("Something went wrong!")
                                        .show();
                                break;
                            case "1":
                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success!!")
                                        .setContentText(Message)
                                        .show();

                                list.clear();
                                templist.clear();
                                adapter.notifyDataSetChanged();
                                ETsearch.setText("");
                                EdtCtrCode.setText("");
                                EdtCtrName.setText("");
                                EdtGrpCode.setText("");
                                EdtGrpName.setText("");
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!!")
                                .setContentText("Something went wrong!")
                                .show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // the POST parameters:
                StrBrcode = login.branch_id;
                StrMakerId = login.agent_code;
                StrCenterCode = EdtCtrCode.getText().toString().trim();
                StrGroupCode = EdtGrpCode.getText().toString().trim();
                StrGroupName = EdtGrpName.getText().toString().trim();
                StrCustId = makeJsonObj();

                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("Custtable", StrCustId);
                hashMap.put("Brcode", StrBrcode);
                hashMap.put("CenterCode", StrCenterCode);
                hashMap.put("GroupCode", StrGroupCode);
                hashMap.put("GroupName", StrGroupName);
                hashMap.put("MakerId", StrMakerId);
                hashMap.put("Flag", "INSERT");
                Log.e("createGroup hashMap: ", hashMap.toString());
                return hashMap;
            }
        };
        Log.e("postRequest ", postRequest.toString());
        requestQueue.add(postRequest);
        progressDialog.dismiss();
    }

    private class getAccountHolderByCustId extends AsyncTask<String, String, String> {
        private String api_url = ip + "/getAccountHolderbyCustId";

        //private String api_url = "http://192.168.0.123:8888/GetAccountHolder";
        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Loading");
            progressDialog.setContentText("please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("custid", StrEdtSearch);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                String JsonData = data.toString();
                JSONObject reader = new JSONObject(JsonData);
                JSONObject jobj = reader.getJSONObject("account");

                if (jobj.has("error")) {
                    respnsMsg = jobj.getString("error");
                    alertMsg = "error";
                }
                if (jobj.has("data")) {
                    JSONObject jobj1 = jobj.getJSONObject("data");
                    strAccNo = jobj1.getString("ACNO");
                    strCustId = jobj1.getString("CUST_ID");
                    strName = jobj1.getString("NAME");
                    alertMsg = "ok";
                }
            } catch (Exception e) {
                e.printStackTrace();
                alertMsg = "exception";
            }

            return alertMsg;
        }

        @Override
        protected void onPostExecute(String url) {
            progressDialog.dismiss();
            if (url.equals("error")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText(respnsMsg)
                        .show();
            } else if (url.equals("exception")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText("Something went wrong!")
                        .show();
            } else if (url.equals("ok")) {
                if (strCustId != null && !strCustId.isEmpty()) {
                    HashMap<String, String> hashmap = new HashMap<String, String>();
                    hashmap.put(SLNO_COLUMN, "");
                    hashmap.put(ACCNO_COLUMN, strAccNo);
                    hashmap.put(CUSTID_COLUMN, strCustId);
                    hashmap.put(NAME_COLUMN, strName);
                    templist.add(hashmap);

                    addSlNoList();
                    strAccNo = "";
                    strCustId = "";
                    strName = "";
                    ETsearch.setText("");
                }
            }
        }
    }


}
