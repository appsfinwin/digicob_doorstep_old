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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.finwingway.digicob.JSONParser;
import com.finwingway.digicob.R;
import com.finwingway.digicob.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.VISIBLE;

public class Search_group_created extends Activity {

    Spinner branch_spinner;
    String Message, responseMsg, StrSearch, branch_id, StrGroupCode, StrGroupName, StrAccNo;
    String[] AccNoArray, GroupCodeArray, GroupNameArray;
    EditText EdtSearch_cusid;
    TextView tvWrngMsg;
    Button search_btn, search_cancel;
    public static String ip = login.ip_global;
    ListView listView;
    JSONParser jsonParser;
    Dialog progressDialog;
    ProgressBar progressBar;

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

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrSearch = EdtSearch_cusid.getText().toString();
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
    private class getGroupList extends AsyncTask<String, String, String> {
        private String api_url = ip + "/JLGFetchAccNo";

        @Override
        public void onPreExecute() {
            progressDialog.setTitle("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Br_Code", branch_id);
            hashMap.put("Searchkey", "");
            hashMap.put("Searchtype", "AccNo");
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
                responseMsg = jsonObject.getString("status");
                if (responseMsg.equals("1")) {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    Log.e("JLGFetchAccNo", jsonArray.toString());
                    AccNoArray = new String[jsonArray.length()];
                    GroupCodeArray = new String[jsonArray.length()];
                    GroupNameArray = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arrayObject = jsonArray.getJSONObject(i);
                        AccNoArray[i] = arrayObject.getString("Ln_GlobalAccNo");
                        GroupCodeArray[i] = arrayObject.getString("JLG_GroupCode");
                        GroupNameArray[i] = arrayObject.getString("JLG_GroupName");
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
                final SearchAdapterGroupJLG searchAdapter
                        = new SearchAdapterGroupJLG(getBaseContext(), GroupNameArray, GroupCodeArray, AccNoArray);
                listView.setAdapter(searchAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StrGroupCode = searchAdapter.getGrpCode(position).toString();
                        StrGroupName = searchAdapter.getGrpName(position).toString();
                        StrAccNo = searchAdapter.getItem(position).toString();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("result_grp_code", StrGroupCode);
                        resultIntent.putExtra("result_grp_name", StrGroupName);
                        resultIntent.putExtra("result_Accno", StrAccNo);
                        setResult(RESULT_OK, resultIntent);
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

