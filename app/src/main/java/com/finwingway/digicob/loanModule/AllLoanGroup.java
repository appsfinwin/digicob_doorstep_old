package com.finwingway.digicob.loanModule;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class AllLoanGroup extends Fragment {

    public static String ip = login.ip_global;
    String Message, responseMsg, branch_id, StrSearch = "";
    View rootView;
    JSONParser jsonParser;
    Dialog progressDialog;
    Button SearchBtn;
    EditText edtSearchAll;
    ListView listView;
    static ListviewAdapterGroupAll adapter;
    private static ArrayList<HashMap<String, String>> list;
    static final String ALL_BRNAME = "BrName";
    static final String ALL_CNTRCOD = "Center Code";
    static final String ALL_GRPNAME = "Group Name";
    static final String ALL_GRPCOD = "Group Code";
    static final String ALL_MAKERID = "Maker Id";
    static final String ALL_MAKINGTIM = "Making Time";

    static String FLAG = "ByBranch";//ByName
    RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_all_group, container, false);
        branch_id = login.branch_id;
        jsonParser = new JSONParser();
        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.progress_dialogue_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        edtSearchAll = rootView.findViewById(R.id.edt_search_grpall_name);
        requestQueue = Volley.newRequestQueue(getContext());

        list = new ArrayList<HashMap<String, String>>();
        listView = rootView.findViewById(R.id.listview_all_grp);
        adapter = new ListviewAdapterGroupAll(getActivity(), list);
        listView.setAdapter(adapter);

//        new getGroupList().execute();
        getGroupList();

        SearchBtn = rootView.findViewById(R.id.btn_grpall_search);
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrSearch = edtSearchAll.getText().toString();
                if (StrSearch.equals("")) {
                    FLAG = "ByBranch";
                } else {
                    FLAG = "ByName";
                }
//                new getGroupList().execute();
                getGroupList();
            }
        });

        return rootView;
    }

    private void getGroupList() {
        String api_url = ip + "/JLGLoanGroup";
        progressDialog.setTitle("Please wait..");
        progressDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            responseMsg = jsonObject.getString("status");
                            if (responseMsg.equals("1")) {
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                                Log.e("getGroupList=>", jsonArray.toString());
                                list.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> hashmap = new HashMap<String, String>();
                                    hashmap.put(ALL_BRNAME, jsonobject.getString("Branch"));
                                    hashmap.put(ALL_CNTRCOD, jsonobject.getString("Center Code"));
                                    hashmap.put(ALL_GRPCOD, jsonobject.getString("Group Code"));
                                    hashmap.put(ALL_GRPNAME, jsonobject.getString("Group Name"));
                                    hashmap.put(ALL_MAKERID, jsonobject.getString("Maker Id"));
                                    hashmap.put(ALL_MAKINGTIM, jsonobject.getString("Making Time"));
                                    list.add(hashmap);
                                }
                            } else if (responseMsg.equals("0")) {
                                Message = jsonObject.getString("msg");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            responseMsg = "exception";
                        } catch (Exception e) {
                            e.printStackTrace();
                            responseMsg = "exception";
                        }
                        adapter.notifyDataSetChanged();
                        StrSearch = "";
                        progressDialog.dismiss();
                        if (responseMsg.equals("0")) {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!!")
                                    .setContentText(Message)
                                    .show();
                        } else if (responseMsg.equals("exception")) {
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Something went wrong!")
                                    .show();
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
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("Brcode", branch_id);
                hashMap.put("CenterCode", "");
                hashMap.put("GroupCode", "");
                hashMap.put("GroupName", StrSearch);
                hashMap.put("MakerId", login.agent_code);
                hashMap.put("Flag", FLAG);
                hashMap.put("Custtable","[]");
                Log.e("AllLoanGroup #=>", hashMap.toString());
                return hashMap;
            }
        };
        Log.e("postRequest ", postRequest.toString());
        requestQueue.add(postRequest);
        progressDialog.dismiss();
    }


}