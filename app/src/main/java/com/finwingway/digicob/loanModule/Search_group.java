package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.VISIBLE;

public class Search_group extends Activity {

    Spinner branch_spinner, center_spinner;
    String Message, responseMsg, StrSearch, branch_id, StrGroupCode, StrGroupName, StrBranch, StrCenter,
            StrBrnchId, StrCntrCode;
    String[] BranchArray, CenterCodeArray, GroupCodeArray, GroupNameArray,
            BranchNameArray, BranchCodeArray, CntrCodeArray,
            CntrNameArray, MakerIdArray, MakeTimeArray;

    EditText EdtSearch_cusid;
    TextView tvWrngMsg;
    Button search_btn, search_cancel;
    public static String ip = login.ip_global;
    ListView listView;
    JSONParser jsonParser;
    static JSONObject jobjCntrDele, jobjGetAll;
    Dialog progressDialog;
    SweetAlertDialog errorDialog, proDialog;
    private static ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);
        jsonParser = new JSONParser();
        tvWrngMsg = findViewById(R.id.tv_wrng_msg_grp);
        EdtSearch_cusid = (EditText) findViewById(R.id.edt_search_grpname);
        search_btn = (Button) findViewById(R.id.btn_search_grp);
        search_cancel = (Button) findViewById(R.id.btn_cancel_grp);
        listView = (ListView) findViewById(R.id.search_details_grp);
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialogue_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        proDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);

        //progressBar=(ProgressBar)progressDialog.findViewById(R.id.progressBar);
        //acc_type_id = getResources().getStringArray(R.array.cashdeposit_acc_type_id);

        branch_spinner = (Spinner) findViewById(R.id.spinner_branch_grp);
        ArrayAdapter brachAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_list_item, login.arrayBrachName);
        branch_spinner.setAdapter(brachAdapter);
        try {
            branch_spinner.setSelection(brachAdapter.getPosition(login.branch_name));
        } catch (Exception ignore) {
        }
        branch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch_id = login.arrayBrachId[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        center_spinner = (Spinner) findViewById(R.id.spinner_cntr_slct);
        center_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrCntrCode = CntrCodeArray[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        new getAllCenter().execute();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                StrSearch = EdtSearch_cusid.getText().toString();
                listView.setAdapter(null);
                new getGroupList().execute();
            }
        });
        search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private class getAllCenter extends AsyncTask<String, String, String> {
        private String api_url = ip + "/GetAllLoanCentreByBranch";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();
            StrBrnchId = login.branch_id;
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Brcode", StrBrnchId);
            Log.e("GtAlLonCntrBr Brcode", String.valueOf(hashMap));
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
                e.printStackTrace();
                responseMsg = "exception";
            }
            return responseMsg;
        }

        @Override
        protected void onPostExecute(String url) {
            proDialog.dismiss();
            if (url.equals("1")) {
                ArrayAdapter centerAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_list_item, CntrNameArray);
                center_spinner.setAdapter(centerAdapter);
            }
            if (url.equals("0")) {
                Toast.makeText(Search_group.this, Message, Toast.LENGTH_SHORT).show();
            } else if (url.equals("exception")) {
                Toast.makeText(Search_group.this, "Server error occurred!!..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class getGroupList extends AsyncTask<String, String, String> {
        private String api_url = ip + "/SelectGroup";

        @Override
        public void onPreExecute() {
            progressDialog.setTitle("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("BrCode", login.branch_id);
            hashMap.put("CenterCode", StrCntrCode);
            Log.e("SelectGroup: ", String.valueOf(hashMap));

            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("JLGLoanGrp jsonObj", String.valueOf(jsonObject));
                responseMsg = jsonObject.getString("status");
                if (responseMsg.equals("1")) {
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
                } else if (responseMsg.equals("0")) {
                    Message = jsonObject.getString("msg");
                }
            } catch (JSONException e) {
                responseMsg = "0";
                Message = "Internal Server Error Occurred!";
                e.printStackTrace();
            }
            return responseMsg;
        }

        @Override
        public void onPostExecute(final String result) {
            progressDialog.dismiss();
            if (result.equals("1")) {
                final SearchAdapterGroup searchAdapter
                        = new SearchAdapterGroup(getBaseContext(), GroupNameArray, GroupCodeArray, BranchArray, CenterCodeArray);
                listView.setAdapter(searchAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StrGroupCode = searchAdapter.getItem(position).toString();
                        StrGroupName = searchAdapter.getGrpName(position).toString();
                        StrBranch = searchAdapter.getBranchGrp(position).toString();
                        StrCenter = searchAdapter.getCenterGrp(position).toString();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("result_grp_code", StrGroupCode);
                        resultIntent.putExtra("result_grp_name", StrGroupName);
                        resultIntent.putExtra("result_brnch_name", StrBranch);
                        resultIntent.putExtra("result_Center", StrCenter);
                        setResult(1009, resultIntent);
                        finish();
                    }
                });
                tvWrngMsg.setVisibility(View.INVISIBLE);
                listView.setVisibility(VISIBLE);
            } else if (result.equals("0")) {
                listView.setVisibility(View.INVISIBLE);
                tvWrngMsg.setVisibility(VISIBLE);
                tvWrngMsg.setText(Message);
            }
        }
    }


//    @SuppressLint("StaticFieldLeak")
//    private class getGroupList extends AsyncTask<String, String, String> {
//        private String api_url = ip + "/JLGLoanGroup";
//
//        @Override
//        public void onPreExecute() {
//            progressDialog.setTitle("Please wait..");
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            HashMap<String, String> hashMap = new HashMap<>();
//            hashMap.put("Brcode", branch_id);
//            hashMap.put("CenterCode", "");
//            hashMap.put("GroupCode", "");
//            hashMap.put("GroupName", "");
//            hashMap.put("MakerId", login.agent_code);
//            hashMap.put("Flag", "ByBranch");
//            hashMap.put("Custtable", "{}");
//
//            Log.e("JLGLoanGroup: ", String.valueOf(hashMap));
//
//            try {
//                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
//                Log.e("JLGLoanGrp jsonObj", String.valueOf(jsonObject));
//                responseMsg = jsonObject.getString("status");
//                if (responseMsg.equals("1")) {
//                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
//                    Log.e("JLGLoanG Search_group", jsonArray.toString());
//                    BranchArray = new String[jsonArray.length()];
//                    CenterCodeArray = new String[jsonArray.length()];
//                    GroupCodeArray = new String[jsonArray.length()];
//                    GroupNameArray = new String[jsonArray.length()];
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject arrayObject = jsonArray.getJSONObject(i);
//                        BranchArray[i] = arrayObject.getString("Branch");
//                        CenterCodeArray[i] = arrayObject.getString("Center Code");
//                        GroupCodeArray[i] = arrayObject.getString("Group Code");
//                        GroupNameArray[i] = arrayObject.getString("Group Name");
//                    }
//                } else if (responseMsg.equals("0")) {
//                    Message = jsonObject.getString("msg");
//                }
//            } catch (JSONException e) {
//                responseMsg = "0";
//                Message = "Internal Server Error Occurred!";
//                e.printStackTrace();
//            }
//            return responseMsg;
//        }
//
//        @Override
//        public void onPostExecute(final String result) {
//            progressDialog.dismiss();
//            if (result.equals("1")) {
//                final SearchAdapterGroup searchAdapter
//                        = new SearchAdapterGroup(getBaseContext(), GroupNameArray, GroupCodeArray, BranchArray, CenterCodeArray);
//                listView.setAdapter(searchAdapter);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        StrGroupCode = searchAdapter.getItem(position).toString();
//                        StrGroupName = searchAdapter.getGrpName(position).toString();
//                        StrBranch = searchAdapter.getBranchGrp(position).toString();
//                        StrCenter = searchAdapter.getCenterGrp(position).toString();
//                        Intent resultIntent = new Intent();
//                        resultIntent.putExtra("result_grp_code", StrGroupCode);
//                        resultIntent.putExtra("result_grp_name", StrGroupName);
//                        resultIntent.putExtra("result_brnch_name", StrBranch);
//                        resultIntent.putExtra("result_Center", StrCenter);
//                        setResult(1009, resultIntent);
//                        finish();
//                    }
//                });
//                tvWrngMsg.setVisibility(View.INVISIBLE);
//                listView.setVisibility(VISIBLE);
//            } else if (result.equals("0")) {
//                listView.setVisibility(View.INVISIBLE);
//                tvWrngMsg.setVisibility(VISIBLE);
//                tvWrngMsg.setText(Message);
//            }
//        }
//    }


//
//    private void getGroupList() {
//        String api_url = ip + "/JLGLoanGroup";//LoanGroupCreator
//
//        progressDialog.setTitleText("Loading");
//        progressDialog.setContentText("please wait..");
//        progressDialog.show();
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        try {
//                            JSONObject data = new JSONObject(response);
//                            Log.e("createGroup: ", data.toString());
//                            if (data.has("status")) {
//                                responseData = data.getString("status");
//                                if (responseData.equals("1")) {
//                                    Message = data.getString("msg");
//                                } else if (responseData.equals("0")) {
//                                    Message = data.getString("msg");
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            responseData = "exception";
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            responseData = "exception";
//                        }
//
//                        switch (responseData) {
//                            case "0":
//                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("Error!!")
//                                        .setContentText(Message)
//                                        .show();
//                                break;
//                            case "exception":
//                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("Error!!")
//                                        .setContentText("Something went wrong!")
//                                        .show();
//                                break;
//                            case "1":
//                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
//                                        .setTitleText("Success!!")
//                                        .setContentText(Message)
//                                        .show();
//
//                                list.clear();
//                                templist.clear();
//                                adapter.notifyDataSetChanged();
//                                ETsearch.setText("");
//                                EdtCtrCode.setText("");
//                                EdtCtrName.setText("");
//                                EdtGrpCode.setText("");
//                                EdtGrpName.setText("");
//                                break;
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//                        Log.d("ERROR", "error => " + error.toString());
//                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText("Error!!")
//                                .setContentText("Something went wrong!")
//                                .show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                // the POST parameters:
//                StrBrcode = login.branch_id;
//                StrMakerId = login.agent_code;
//                StrCenterCode = EdtCtrCode.getText().toString().trim();
//                StrGroupCode = EdtGrpCode.getText().toString().trim();
//                StrGroupName = EdtGrpName.getText().toString().trim();
//                StrCustId = makeJsonObj();
//
//                Map<String, String> hashMap = new HashMap<>();
//                hashMap.put("Custtable", StrCustId);
//                hashMap.put("Brcode", StrBrcode);
//                hashMap.put("CenterCode", StrCenterCode);
//                hashMap.put("GroupCode", StrGroupCode);
//                hashMap.put("GroupName", StrGroupName);
//                hashMap.put("MakerId", StrMakerId);
//                hashMap.put("Flag", "INSERT");
//                Log.e("CustId hashMap: ", hashMap.toString());
//                return hashMap;
//            }
//        };
//        Log.e("postRequest ", postRequest.toString());
//        requestQueue.add(postRequest);
//        progressDialog.dismiss();
//    }


}
