package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.VISIBLE;

public class JLGLoanCreationAdapterTwo extends Fragment {

    View rootView;
    JSONParser jsonParser;
    static JSONObject jobjGetAll;
    ViewPager viewPager = JLGLoanCreation.viewPager;
    public static String ip = login.ip_global;
    SweetAlertDialog proDialog, errorDialog, succDialog;
    private static ArrayList<HashMap<String, String>> list;
    @SuppressLint("StaticFieldLeak")
    static ListviewAdapterJLGcreation adapter;
    ArrayAdapter<String> collDayAdptr;

    ListView listView;
    Spinner spnrOption;
    ImageButton btnSelect_grp;
    Button prev, next, BtnSbmt_grp, BtnCalc;
    EditText edtSelect_grp;
    TextView tvWrngMsg;
    String responseMsg, Message,
            StrOption = "", StrSelect_grp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        rootView = inflater.inflate(R.layout.loan_creation_page_two, container, false);
        jsonParser = new JSONParser();
        proDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        succDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);

        list = new ArrayList<HashMap<String, String>>();
        listView = rootView.findViewById(R.id.list_group_memb);

        tvWrngMsg = rootView.findViewById(R.id.tv_wrng_msg_grp);
        edtSelect_grp = rootView.findViewById(R.id.edt_select_grp);

        btnSelect_grp = rootView.findViewById(R.id.btn_select_grp_jlg);
        btnSelect_grp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent debit_intent = new Intent(getActivity(), Search_group.class);
                startActivityForResult(debit_intent, 1004);
            }
        });

        BtnSbmt_grp = rootView.findViewById(R.id.btn_sbmt_grp);
        BtnSbmt_grp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrSelect_grp = edtSelect_grp.getText().toString();
                if (!TextUtils.isEmpty(StrSelect_grp)) {
                    new getGroupMembers().execute(StrSelect_grp);
                } else {
                    Toast.makeText(getContext(), "Please enter Group Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        prev = rootView.findViewById(R.id.btn_jlg_prev_two);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, true);
            }
        });

        next = rootView.findViewById(R.id.btn_loan_nxt_two);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.validateFields()) {
                    adapter.calcu();
                } else {
                    Toast.makeText(getActivity(), "Not enough data to Register...!!\n" +
                            "Please enter Valid Data.", Toast.LENGTH_SHORT).show();
                    return;
                }
                JLGLoanCreationGetSet.setGroupData(makeJsonArray());
                JLGLoanCreationGetSet.setTotalAmnt(addAmountInList());
//                JLGLoanCreationGetSet.setGroupCode(edtSelect_grp.getText().toString());
                adapter.setListofData();
                viewPager.setCurrentItem(2, true);
            }
        });

        BtnCalc = rootView.findViewById(R.id.btn_list_sum_m);
        BtnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.validateFields()) {
                    adapter.calcu();
                } else {
                    Toast.makeText(getActivity(), "Not enough data to Register...!!\n" +
                            "Please enter Valid Data.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spnrOption = rootView.findViewById(R.id.spnr_option);
        collDayAdptr = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, JLGLoanCreationGetSet.OptionList);
        spnrOption.setAdapter(collDayAdptr);
        spnrOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrOption = JLGLoanCreationGetSet.OptionList.get(position);
                Log.e("StrOption: ", StrOption);
                adapter = new ListviewAdapterJLGcreation(getActivity(), list, StrOption);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1009) {
            try {
//                String resultGrpName = data.getStringExtra("result_grp_name");
                String resultGrpCode = data.getStringExtra("result_grp_code");
                edtSelect_grp.setText(resultGrpCode);
                new getGroupMembers().execute(resultGrpCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String makeJsonArray() {
        JSONObject obj;
        JSONArray jsonArray = new JSONArray();
        try {
            for (HashMap<String, String> map : list) {
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
//                obj.put("Action", map.get(LN_ACTION));
                jsonArray.put(obj);
//                Log.e("makeJsonArray Adap2", String.valueOf(map.get(LN_AMOUNT)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public static Double addAmountInList() {
        double total_amnt = 0;
        try {
            for (HashMap<String, String> map : list) {
                total_amnt = total_amnt + Double.parseDouble(map.get(JLGLoanCreationGetSet.LN_AMOUNT));
            }
        } catch (Exception ignored) {
        }
        return total_amnt;
    }

    public static void removeFromListJLGcrtn(int id) {
        list.remove(id);
        adapter.notifyDataSetChanged();
    }

    ///////////=========================================================================================
    @SuppressLint("StaticFieldLeak")
    private class getGroupMembers extends AsyncTask<String, String, String> {
        private String api_url = ip + "/JLGGroupSelect";

        @Override
        protected void onPreExecute() {
            proDialog.setTitleText("Loading");
            proDialog.setContentText("please wait..");
            proDialog.show();
            tvWrngMsg.setVisibility(View.INVISIBLE);
            listView.setVisibility(VISIBLE);
        }

        @Override
        protected String doInBackground(String... arg) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("groupcode", arg[0]);
            Log.e("arg[0] ", arg[0]);
            try {
                JSONObject jobjGetAllmemb = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                Log.e("JLGGroupSelect =", jobjGetAllmemb.toString());
                if (jobjGetAllmemb.has("status")) {
                    responseMsg = jobjGetAllmemb.getString("status");
                    if (responseMsg.equals("1")) {
                        JSONArray jsonarray = new JSONArray(jobjGetAllmemb.getString("data"));
                        list.clear();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            HashMap<String, String> hashmap = new HashMap<String, String>();
                            hashmap.put(JLGLoanCreationGetSet.LN_SLNO, jsonobject.getString("Co_app_Slno"));
                            hashmap.put(JLGLoanCreationGetSet.LN_CUSTID, jsonobject.getString("Cust_Id"));
                            hashmap.put(JLGLoanCreationGetSet.LN_CUSTNAME, jsonobject.getString("Cust_Name"));
                            hashmap.put(JLGLoanCreationGetSet.LN_COAPPLICNT, "");//jsonobject.getString("Co_app_Slno")
                            hashmap.put(JLGLoanCreationGetSet.LN_MOB, "");
                            hashmap.put(JLGLoanCreationGetSet.LN_ADDRESS, "");
                            hashmap.put(JLGLoanCreationGetSet.LN_CONSUMERGOODS, "");
                            hashmap.put(JLGLoanCreationGetSet.LN_AMOUNT, "");
                            hashmap.put(JLGLoanCreationGetSet.LN_INSURANCE_FEE, "");
                            hashmap.put(JLGLoanCreationGetSet.LN_DOCUMNT_FEE, "");
                            hashmap.put(JLGLoanCreationGetSet.LN_CGST, "");
                            hashmap.put(JLGLoanCreationGetSet.LN_SGST, "");
                            hashmap.put(JLGLoanCreationGetSet.LN_CESS, "");
                            hashmap.put(JLGLoanCreationGetSet.LN_DISBRSMNT_AMNT, "");
                            list.add(hashmap);
                        }
                        JLGLoanCreationGetSet.setGroupCode(edtSelect_grp.getText().toString());
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
            adapter.notifyDataSetChanged();
            proDialog.dismiss();
            if (url.equals("0")) {
                listView.setVisibility(View.INVISIBLE);
                tvWrngMsg.setVisibility(VISIBLE);
                tvWrngMsg.setText(Message);
            } else if (url.equals("exception")) {
                tvWrngMsg.setVisibility(View.INVISIBLE);
                listView.setVisibility(VISIBLE);
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