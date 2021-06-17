package com.finwingway.digicob;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static com.finwingway.digicob.login.agent_code;

public class Agent_Change_Password extends Fragment{
    Button okbtn,cancelbtn;
    public static String agent_id=agent_code;
    public static String  ip=login.ip_global;
    private static String api_url =ip+"/passwordChange";
    JSONParser jsonParser;
    SweetAlertDialog pDialog,dialog;
    EditText current,newP,repeatP;
    String currentpass,newpass,repeatpass,status,msg,error;
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_change_password_layout, container, false);
        okbtn = (Button) rootView.findViewById(R.id.changePasswordOKbtn);
        cancelbtn = (Button) rootView.findViewById(R.id.changePasswordCANCELbtn);
        jsonParser=new JSONParser();
        pDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        current=(EditText)rootView.findViewById(R.id.currentPasswordsTextView);
        newP=(EditText)rootView.findViewById(R.id.newPasswordTextView);
        newP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(getContext(), "before", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getContext(), "ontext", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(getContext(), "after", Toast.LENGTH_SHORT).show();
                newP.setError(null,null);
            }
        });
        repeatP=(EditText)rootView.findViewById(R.id.repeatPasswordTextView);
        repeatP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newpass=newP.getText().toString();
                if(newpass != s.toString()){
                    newP.setError("Passwords do not match!");
                    repeatP.setError("Passwords do not match!");
                }
                if(newpass.equals(s.toString())){
                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_done_black_24dp);
                    myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
                    newP.setError("Good", myIcon);
                    repeatP.setError("Good", myIcon);
                    newP.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_done_black_24dp,0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpass=current.getText().toString();
                newpass=newP.getText().toString();
                repeatpass=repeatP.getText().toString();
                if(newpass.equals(repeatpass)){
                    if(!newP.getText().toString().equals("" ) && !current.getText().toString().equals("" )&& !repeatP.getText().toString().equals("" )) {
                        new AsyncChangePassword().execute();
                    }
                }else {
                    Toast.makeText(getContext(), "Passwords do not Match!", Toast.LENGTH_SHORT).show();

                }
                //   Toast.makeText(getContext(),"WebService",Toast.LENGTH_SHORT).show();
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == okbtn){
                }
                if(view == cancelbtn){
                    getFragmentManager().popBackStack();
                }
            }
        });
        return rootView;
    }



    class AsyncChangePassword extends AsyncTask<String, String, String> {
        @Override
        public void onPreExecute(){
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMapAmount=new HashMap<>();
            hashMapAmount.put("userid",agent_id);
            hashMapAmount.put("oldPassword",currentpass);
            hashMapAmount.put("new_password",newpass);
            hashMapAmount.put("re_password",repeatpass);
            JSONObject jsonObject=jsonParser.makeHttpRequest(api_url,"POST",hashMapAmount);
            String JsonDataString=jsonObject.toString();
            Log.e("Result",JsonDataString);
            try {
                JSONObject reader=new JSONObject(JsonDataString);
                status=reader.getString("status");
                msg=reader.getString("msg");
                error="ok";
            } catch (JSONException e) {
                e.printStackTrace();
                error="error";
            }
            return msg;
        }

        @Override
        public void onPostExecute(String url){
            if(error=="error"){
                pDialog.dismiss();
                new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Something went wrong,please try again..")
                        .show();
            }else{
                pDialog.dismiss();
                if(status.equals("1")){
                    dialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setTitleText(msg);
                    dialog.show();
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            newP.setText(null);
                            current.setText(null);
                            repeatP.setText(null);
                            dialog.dismiss();
                        }
                    });
                }
                if(status.equals("0")){
                    dialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE);
                    dialog.setTitleText(msg);
                    dialog.show();
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            newP.setText(null);
                            current.setText(null);
                            repeatP.setText(null);
                            dialog.dismiss();
                        }
                    });
                }
            }

        }
    }
}
