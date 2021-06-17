package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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

import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JLGCreateCenter extends Fragment {
    public static String ip = login.ip_global;
    static JSONParser jsonParser;
    static JSONObject jobjCntrDele, jobjGetAll;
    private static ArrayList<HashMap<String, String>> list;
    private static ArrayList<HashMap<String, String>> templist;
    @SuppressLint("StaticFieldLeak")
    static ListviewAdapterCenter adapter;
    public static final String BRNAME = "BrName";
    public static final String BRID = "BrCode";
    public static final String CNTRCOD = "Center Code";
    public static final String CNTRNME = "Center Name";
    public static final String MAKERID = "Maker Id";
    public static final String MAKINGTIM = "Making Time";
    public static final String BUTTONNAME = "UPDATE";
    static final String _UPDATE = "UPDATE";
    static final String _SUBMIT = "SUBMIT";

    View rootView;
    @SuppressLint("StaticFieldLeak")
    static Button btnSubmit;
    @SuppressLint("StaticFieldLeak")
    static EditText EdtCtrCode, EdtCtrName;
    static SweetAlertDialog proDialog, wrngDialog, errorDialog, succDialog;
    static String StrCtrCode, StrCtrName, StrBrnchId, StrAgentCode, StrUpdtBrId, StrUpdtCtrName, StrUpdtCtrCode, StrDeltBrId, StrDeltCtrName, StrDeltCtrCode;
    static String responseMsg = "exception", Message;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_create_center, container, false);
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        wrngDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        succDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
        jsonParser = new JSONParser();
        jobjGetAll = new JSONObject();
        jobjCntrDele = new JSONObject();
        EdtCtrCode = rootView.findViewById(R.id.edt_ctr_cntrCode);
        EdtCtrName = rootView.findViewById(R.id.edt_ctr_cntrName);

        list = new ArrayList<HashMap<String, String>>();
        ListView listView = rootView.findViewById(R.id.listview_center);
        adapter = new ListviewAdapterCenter(getActivity(), list);
        listView.setAdapter(adapter);

        btnSubmit = rootView.findViewById(R.id.btn_cntr_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EdtCtrName.getText().toString().isEmpty()) {
                    if (btnSubmit.getText().toString().equals(_UPDATE)) {
                        new updateCenter().execute();
                    } else {
                        new createCenter().execute();
                    }
                } else {
                    EdtCtrName.setError("Please Enter Center Name!!");
                }
                if (!EdtCtrCode.isEnabled()) {
                    EdtCtrCode.setEnabled(true);
                }
            }
        });
        new getAllCenter().execute();
        return rootView;
    }

    @SuppressWarnings("unchecked")
    public static void removeFromListCntr(final String ctrCode, final String ctrName, final String brhCode) {
        wrngDialog.setTitleText("Delete Centre!!")
                .setContentText("Do you really want to delete!!")
                .setConfirmText("YES")
                .setCancelText("NO")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        StrDeltBrId = brhCode;
                        StrDeltCtrCode = ctrCode;
                        StrDeltCtrName = ctrName;
                        sweetAlertDialog.dismiss();
                        new deleteCenter().execute();
                       /*list.remove(id);
                        adapter.notifyDataSetChanged();*/
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }

    public static void updateListCntr(String ctrCode, String ctrName, String brid) {
        EdtCtrCode.setText(ctrCode);
        EdtCtrCode.setEnabled(false);
        EdtCtrName.setText(ctrName);
        btnSubmit.setText(_UPDATE);

        StrUpdtCtrCode = ctrCode;
        StrUpdtBrId = brid;
    }

    @SuppressWarnings("unchecked")
    public static void ConfirmUpdateCntr(final String ctrCode, final String ctrName, final String brid, final String UpdtbtnText) {
        if (UpdtbtnText.equals(_UPDATE)) {
            if (templist != null && !templist.isEmpty()) {
                templist.clear();
            }
            templist = (ArrayList<HashMap<String, String>>) list.clone();
            wrngDialog.setTitleText("Update!!")
                    .setContentText("Do you really want to update!!")
                    .setConfirmText("YES")
                    .setCancelText("NO")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            updateListCntr(ctrCode, ctrName, brid);
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            CancelUpdate();
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
        } else {
            EdtCtrCode.setText("");
            EdtCtrName.setText("");
            CancelUpdate();
        }
    }

    public static void CancelUpdate() {
        btnSubmit.setText(_SUBMIT);
        list.clear();
        for (HashMap<String, String> map : templist) {
            map.put(BUTTONNAME, BUTTONNAME);
            list.add(map);
        }
        adapter.notifyDataSetChanged();
//        wrngDialog.dismiss();
    }

    public static void refreshUpdate() {
        btnSubmit.setText(_SUBMIT);
        EdtCtrCode.setText("");
        EdtCtrName.setText("");

        if (list != null && !list.isEmpty()) {
            list.clear();
        }
        if (templist != null && !templist.isEmpty()) {
            templist.clear();
        }
//        wrngDialog.dismiss();
        new getAllCenter().execute();
    }

    ///////////=========================================================================================
    @SuppressLint("StaticFieldLeak")
    private static class deleteCenter extends AsyncTask<String, String, String> {
        private String api_url = ip + "/LoanCentreDelete";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Brcode", StrDeltBrId);
            hashMap.put("CentreCode", StrDeltCtrCode);
            hashMap.put("MakerId", login.agent_code);
            hashMap.put("CentreName", StrDeltCtrName);
            Log.e("doInBackground: ", hashMap.toString());
            try {
                jobjCntrDele = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("updateCenter data ", jobjCntrDele.toString());
                if (jobjCntrDele.has("status")) {
                    responseMsg = jobjCntrDele.getString("status");
                    if (responseMsg.equals("1")) {
                        Message = jobjCntrDele.getString("msg");
                    } else if (responseMsg.equals("0")) {
                        Message = jobjCntrDele.getString("msg");
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
            refreshUpdate();
            if (url.equals("0")) {
                errorDialog.setTitleText("Can't Delete!!")
                        .setContentText(Message)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (url.equals("exception")) {
                errorDialog.setTitleText("Can't Delete!!")
                        .setContentText("Something went wrong!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else if (url.equals("1")) {
                succDialog.setTitleText("Successfully Deleted!!")
                        .setContentText(Message)
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
    @SuppressLint("StaticFieldLeak")
    private class updateCenter extends AsyncTask<String, String, String> {
        private String api_url = ip + "/LoanCentreUpdater";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();

            StrAgentCode = login.agent_code;
            StrUpdtCtrName = EdtCtrName.getText().toString();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Brcode", StrUpdtBrId);
            hashMap.put("CentreCode", StrUpdtCtrCode);
            hashMap.put("MakerId", StrAgentCode);
            hashMap.put("CentreName", StrUpdtCtrName);
            Log.e("doInBackground: ", hashMap.toString());
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("updateCenter data ", data.toString());
                if (data.has("status")) {
                    responseMsg = data.getString("status");
                    if (responseMsg.equals("1")) {
                        Message = data.getString("msg");
                    } else if (responseMsg.equals("0")) {
                        Message = data.getString("msg");
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
            refreshUpdate();
            proDialog.dismiss();
            if (url.equals("0")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Can't Update!!")
                        .setContentText(Message)
                        .show();
            } else if (url.equals("exception")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Can't Update!!")
                        .setContentText("Something went wrong!")
                        .show();
            } else if (url.equals("1")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Successfully Updated!!")
                        .setContentText(Message)
                        .show();
            }
        }
    }

    ///////////=========================================================================================
    @SuppressLint("StaticFieldLeak")
    private class createCenter extends AsyncTask<String, String, String> {
        private String api_url = ip + "/LoanCentreCreator";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();

            StrCtrCode = EdtCtrCode.getText().toString();
            StrCtrName = EdtCtrName.getText().toString();
            StrBrnchId = login.branch_id;
            StrAgentCode = login.agent_code;
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Brcode", StrBrnchId);
            hashMap.put("CentreCode", StrCtrCode);
            hashMap.put("MakerId", StrAgentCode);
            hashMap.put("CentreName", StrCtrName);
            Log.e("createCenter: ", hashMap.toString());
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("createCenter data ", data.toString());
                if (data.has("status")) {
                    responseMsg = data.getString("status");
                    if (responseMsg.equals("1")) {
                        Message = data.getString("msg");
                    } else if (responseMsg.equals("0")) {
                        Message = data.getString("msg");
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
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText(Message)
                        .show();
            } else if (url.equals("exception")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!")
                        .setContentText("Something went wrong!")
                        .show();
            } else if (url.equals("1")) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!!")
                        .setContentText(Message)
                        .show();
                new getAllCenter().execute();
            }
            EdtCtrCode.setText("");
            EdtCtrName.setText("");
        }
    }

    ///////////=========================================================================================
    @SuppressLint("StaticFieldLeak")
    private static class getAllCenter extends AsyncTask<String, String, String> {
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
                        list.clear();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            HashMap<String, String> hashmap = new HashMap<String, String>();
                            hashmap.put(BRNAME, jsonobject.getString("BranchName"));
                            hashmap.put(BRID, jsonobject.getString("BranchCode"));
                            hashmap.put(CNTRCOD, jsonobject.getString("Center Code"));
                            hashmap.put(CNTRNME, jsonobject.getString("Center Name"));
                            hashmap.put(MAKERID, jsonobject.getString("Maker Id"));
                            hashmap.put(MAKINGTIM, jsonobject.getString("Making Time"));
                            hashmap.put(BUTTONNAME, BUTTONNAME);
                            list.add(hashmap);
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
            adapter.notifyDataSetChanged();
            proDialog.dismiss();
            if (url.equals("0")) {
                errorDialog.setTitleText("Error!!")
                        .setContentText(Message)
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
        }
    }

}
