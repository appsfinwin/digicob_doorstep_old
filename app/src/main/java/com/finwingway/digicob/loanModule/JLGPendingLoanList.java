package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.finwingway.digicob.login.branch_id;
import static com.finwingway.digicob.login.ip_global;

public class JLGPendingLoanList extends Fragment {
    View rootView;
    static RequestQueue requestQueue;
    static JSONParser jsonParser;
    static JSONObject jObject;
    static ListViewAdapterJLGPendingLoan adapter;
    private static ArrayList<HashMap<String, String>> list;
    static ListView listView;
    static SweetAlertDialog proDialog, wrngDialog, errorDialog, succDialog;
    //    static SweetAlertDialog proDialogSTA, wrngDialogSTA, errorDialogSTA, succDialogSTA;
    static String responseMsg, Message;
    static String responseMsgSTA, MessageSTA;
    EditText edtSearch;

    static String PND_ACC_NO = "AccountNo";
    static String PND_LOANTYP = "Ln_Type";
    static String PND_BRNAME = "Branch_Name";
    static String PND_PERIODTYP = "Ln_PeriodType";
    static String PND_PURPOSE = "Ln_Mainpurpose";
    static String PND_SUBPURPOSE = "Ln_Subpurpose";
    static String PND_GROUPNAME = "JLG_GroupName";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_jlg_pending_loan_list, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        wrngDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        succDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
        jsonParser = new JSONParser();
        jObject = new JSONObject();

        list = new ArrayList<HashMap<String, String>>();
        listView = rootView.findViewById(R.id.listview_pending_jlg);
        adapter = new ListViewAdapterJLGPendingLoan(getActivity(), list);
        listView.setAdapter(adapter);

        getPendingLoanList();

        edtSearch = rootView.findViewById(R.id.edt_search_pndgloan);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text [" + s + "]");
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button btnSearch = rootView.findViewById(R.id.btn_pndgloan_refrsh);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                getPendingLoanList();
            }
        });


        return rootView;
    }

    private static void getPendingLoanList() {
        String api_url = ip_global + "/JLGPending";
        proDialog.setTitleText("Please wait..");
        proDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            responseMsg = jsonObject.getString("status");
                            if (responseMsg.equals("1")) {
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                                Log.e("JLGPending=>", jsonArray.toString());
                                list.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> hashmap = new HashMap<String, String>();
                                    hashmap.put(PND_ACC_NO, jsonobject.getString("Acc No"));
                                    hashmap.put(PND_LOANTYP, jsonobject.getString("Loan Type"));
                                    hashmap.put(PND_BRNAME, jsonobject.getString("Branch"));
                                    hashmap.put(PND_PERIODTYP, jsonobject.getString("Period Type"));
                                    hashmap.put(PND_PURPOSE, jsonobject.getString("Purpose"));
                                    hashmap.put(PND_SUBPURPOSE, jsonobject.getString("SubPurpose"));
                                    hashmap.put(PND_GROUPNAME, jsonobject.getString("Group Name"));
                                    list.add(hashmap);
                                }
                                adapter.notifyDataSetChanged();

                            } else if (responseMsg.equals("0") || responseMsg.equals("2")) {
                                Message = jsonObject.getString("msg");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            responseMsg = "exception";
                        } catch (Exception e) {
                            e.printStackTrace();
                            responseMsg = "exception";
                        }
                        proDialog.dismiss();
                        switch (responseMsg) {
                            case "0":
                                errorDialog.setTitleText("Error!!")
                                        .setContentText(Message)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                errorDialog.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            case "2":
                                wrngDialog.setTitleText("No data!!")
                                        .setContentText(Message)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                wrngDialog.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                            case "exception":
                                errorDialog.setTitleText("Unexpected server error!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                errorDialog.dismiss();
                                            }
                                        })
                                        .show();
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        proDialog.dismiss();
//                        errorDialog.setTitleText("Error!!")
//                                .setContentText("Something went wrong!")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        errorDialog.dismiss();
//                                    }
//                                })
//                                .show();

                        wrngDialog.setTitleText("No Pending Data!!")
                                .setContentText("Don't have any pending loan!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        wrngDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // the POST parameters:
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("BranchCode", branch_id);
                Log.e("JLGPendingLoan=>", hashMap.toString());
                return hashMap;
            }
        };
        requestQueue.add(postRequest);
    }

    public static void removePendingList(final String AccNo) {
        wrngDialog.setTitleText("Delete!!")
                .setContentText("Do you really want to delete!!")
                .setConfirmText("YES")
                .setCancelText("NO")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        new deletePendingLoan().execute(AccNo);
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();

    }

    ///////////=========================================================================================
    @SuppressLint("StaticFieldLeak")
    private static class deletePendingLoan extends AsyncTask<String, String, String> {
        private String api_url = ip_global + "/JLGLoanRemove";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Acc_No", strings[0]);
            hashMap.put("BranchCode", branch_id);
            try {
                jObject = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("jObject data ", jObject.toString());
                responseMsgSTA = jObject.getString("status");
                if (responseMsgSTA.equals("1")) {
                    MessageSTA = jObject.getString("msg");
                } else if (responseMsgSTA.equals("0")) {
                    MessageSTA = jObject.getString("msg");
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseMsgSTA = "exception";
            }
            return responseMsgSTA;
        }

        @Override
        protected void onPostExecute(String url) {
            proDialog.dismiss();
            switch (url) {
                case "1":
                    succDialog.setTitleText(MessageSTA)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    succDialog.dismiss();
                                    getPendingLoanList();
                                }
                            }).show();
                    break;
                case "0":
                    errorDialog.setTitleText("Error!!")
                            .setContentText(MessageSTA)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    errorDialog.dismiss();
                                }
                            }).show();
                    break;
                case "exception":
                    errorDialog.setTitleText("Something went wrong!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    errorDialog.dismiss();
                                }
                            }).show();
                    break;
            }
        }
    }

}
