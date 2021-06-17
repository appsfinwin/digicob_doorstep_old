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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.VISIBLE;


public class Search_Center extends Activity {
    public static String ip = login.ip_global;
    JSONParser jsonParser;
    Button search_btn, search_cancel;
    ListView listView;
    Dialog progressDialog;
    RadioGroup radioGroup;
    RadioButton rbCenterCode, rbCenterName, radioButton;
    String Message, StrBrnchId, responseMsg, StrCenterName, StrCenterCode, StrBranchName, StrMakerId;
    String strSrchName = "", StrSrchCode = "";
    EditText edtSearch;
    String[] CenterNameArray, CenterCodeArray, BranchNameArray, MakerIdArray;
    TextView tvWrngMsg, txtBrnchname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_search);
        jsonParser = new JSONParser();
        listView = findViewById(R.id.search_details_list_view);
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialogue_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txtBrnchname = findViewById(R.id.txt_branchname);
        txtBrnchname.setText(login.branch_name);
        tvWrngMsg = findViewById(R.id.tv_wrng_msg_ctr);
        edtSearch = findViewById(R.id.edt_search_center);
        radioGroup = findViewById(R.id.radio_grp);
//        rbCenterCode = findViewById(R.id.rb_cntercode);
//        rbCenterName = findViewById(R.id.rb_cntername);

        search_btn = findViewById(R.id.search_sbmt_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSrchName = "";
                StrSrchCode = "";
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                if (radioButton.getText().equals("Center Name")) {
                    strSrchName = edtSearch.getText().toString().trim().length() == 0 ? StrCenterCode = "" : edtSearch.getText().toString();
                    StrSrchCode = "";
                } else if (radioButton.getText().equals("Center Code")) {
                    StrSrchCode = edtSearch.getText().toString().trim().length() == 0 ? StrCenterCode = "" : edtSearch.getText().toString();
                    strSrchName = "";
                }
                new getCenter().execute();
            }
        });
        search_cancel = findViewById(R.id.search_cncl_btn);
        search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    ///////////=========================================================================================
    @SuppressLint("StaticFieldLeak")
    private class getCenter extends AsyncTask<String, String, String> {
        private String api_url = ip + "/LoanCentreSearch";

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Please wait..");
            progressDialog.show();
            StrBrnchId = login.branch_id;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Brcode", StrBrnchId);
            hashMap.put("CentreCode", StrSrchCode);//StrSrchCode);
            hashMap.put("CentreName", strSrchName);//strSrchName);
            Log.e("hashMap -- : ", hashMap.toString());

            JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
            try {
                responseMsg = jsonObject.getString("status");
                if (responseMsg.equals("1")) {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    Log.e("JSONArray=>", jsonArray.toString());
                    CenterNameArray = new String[jsonArray.length()];
                    CenterCodeArray = new String[jsonArray.length()];
                    BranchNameArray = new String[jsonArray.length()];
                    MakerIdArray = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arrayObject = jsonArray.getJSONObject(i);
                        CenterNameArray[i] = arrayObject.getString("Center Name");
                        CenterCodeArray[i] = arrayObject.getString("Center Code");
                        BranchNameArray[i] = arrayObject.getString("BranchName");
                        MakerIdArray[i] = arrayObject.getString("Maker Id");
                    }
                } else if (responseMsg.equals("0")) {
                    Message = jsonObject.getString("msg");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseMsg;
        }

        @Override
        public void onPostExecute(final String result) {
            progressDialog.dismiss();
            if (result.equals("1")) {
                final SearchAdapterCenter searchAdapter
                        = new SearchAdapterCenter(getBaseContext(), CenterNameArray, CenterCodeArray, BranchNameArray, MakerIdArray);
                listView.setAdapter(searchAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StrCenterCode = searchAdapter.getItem(position).toString();
                        StrCenterName = searchAdapter.getCenterName(position).toString();
                        StrBranchName = searchAdapter.getBranchName(position).toString();
                        StrMakerId = searchAdapter.getMakerID(position).toString();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("result_ctr_code", StrCenterCode);
                        resultIntent.putExtra("result_ctr_name", StrCenterName);
                        resultIntent.putExtra("result_brnch_name", StrBranchName);
                        resultIntent.putExtra("result_maker_id", StrMakerId);
                        setResult(1008, resultIntent);
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
}
