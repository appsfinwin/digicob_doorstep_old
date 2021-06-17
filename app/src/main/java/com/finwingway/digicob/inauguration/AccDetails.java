package com.finwingway.digicob.inauguration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.finwingway.digicob.R;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class AccDetails extends Fragment implements View.OnClickListener {
    Button okbtn;
    ImageButton cancelbtn;
    EditText acc_no;
    String account_no;
    ImageButton search;
//    JSONParser jsonParser = new JSONParser();
//    String ip = login.ip_global;
    SweetAlertDialog progressDialog;
    String msg, error;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.aaaaa_inaugu_acc_detls, container, false);
        okbtn = (Button) rootView.findViewById(R.id.inau_miniStatementOKbtn);
        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        okbtn.setOnClickListener(this);
        cancelbtn = (ImageButton) rootView.findViewById(R.id.inau_miniStatementCANCELbtn);
        cancelbtn.setOnClickListener(this);
        acc_no = (EditText) rootView.findViewById(R.id.inau_accountNumberMiniStatementEditText);
        search = (ImageButton) rootView.findViewById(R.id.inau_mini_statement_acc_search_btn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent debit_intent = new Intent(getActivity(), Account_Search.class);
//                startActivityForResult(debit_intent, 1006);
            }
        });
        return rootView;
    }

    public void onClick(View view) {
        if (view == okbtn) {
//            account_no = acc_no.getText().toString();
////            new getAccountHolder().execute();

            Intent intent = new Intent(getActivity(), Acc_Statement_List.class);
//                intent.putExtra("acc_no", account_no);
                startActivity(intent);

        }
        if (view == cancelbtn) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
        }
    }


//    private class getAccountHolder extends AsyncTask<String, String, String> {
//        private String api_url = ip + "/getAccountHolder";
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setTitleText("Loading");
//            progressDialog.setContentText("please wait..");
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            HashMap<String, String> hashMap = new HashMap<>();
//            hashMap.put("account_no", account_no);
//            try {
//                JSONObject data = jsonParser.makeHttpRequest(api_url, "POST", hashMap);
//                String JsonData = data.toString();
//                JSONObject reader = new JSONObject(JsonData);
//                JSONObject jobj = reader.getJSONObject("account");
//                if (jobj.has("data")) {
//                    JSONObject jobj1 = jobj.getJSONObject("data");
//                    String name = jobj1.getString("NAME");
//                    msg = "ok";
//                }
//                if (jobj.has("error")) {
//                    error = jobj.getString("error");
//                    msg = "error";
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                msg = "exception";
//                error = "Something went wrong!";
//                return msg = "exception";
//            }
//
//            return msg;
//        }
//
//        @Override
//        protected void onPostExecute(String url) {
//            progressDialog.dismiss();
//            if (url.equals("error") || url.equals("exception")) {
//                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("ERROR")
//                        .setContentText(error)
//                        .show();
//            }
//            if (url.equals("ok")) {
//                account_no = acc_no.getText().toString();
//                Intent intent = new Intent(getActivity(), Enquiry_Mini_Statement_List.class);
////                intent.putExtra("acc_no", account_no);
////                startActivity(intent);
//            }
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1006) {
            try {
                String result_acc_no = data.getStringExtra("result_acc_no");
                acc_no.setText(result_acc_no);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
