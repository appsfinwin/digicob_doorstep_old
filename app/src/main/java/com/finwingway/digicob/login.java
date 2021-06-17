package com.finwingway.digicob;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.finwingway.digicob.leopard.Act_Main;
import com.finwingway.digicob.leopard.bluetooth.BluetoothComm;
import com.finwingway.digicob.sil.BluetoothChat;
import com.finwingway.digicob.support.ConnectionDetector;
import com.finwingway.digicob.support.UI_validation;
import com.leopard.api.FPS;
import com.leopard.api.FpsConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;
import static com.finwingway.digicob.GlobalApplicationConstants.getCustomerName;
import static com.finwingway.digicob.GlobalApplicationConstants.setCustomerAccountNumber;
import static com.finwingway.digicob.GlobalApplicationConstants.setCustomerName;
import static com.finwingway.digicob.MainActivity.connectionStatus;
import static com.finwingway.digicob.leopard.Act_Main.mGP;
import static com.finwingway.digicob.support.UI_validation.COLLECTION;

public class login extends FragmentActivity {
    JSONParser jsonParser = new JSONParser();
    TextView Status;
    TextView inputName, inputPassword;
    String JsonData, error;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    //public static String ip_global = "http://doorstep.gway.digicob.in/";
    //public static String ip_global = "http://doorstep.gramavikas.digicob.in/";
    public static String ip_global = "http://doorstep.tunl.digicob.in";

    //========================
//    public static String ip_global = "http://192.168.0.222:147/";
//    public static String ip_global = "http://192.168.0.221:147/";
//    public static String ip_global = "http://www.finwintechnologies.com:8888/";
    //========================

    private String api_get_masters = ip_global + "/getMastersAll";
    SweetAlertDialog pDialog, dialogWarning;
    public static String agent_code, agent_name, branch_name, branch_id;
    JSONObject jobj2;

    String accountName, account_Number;
    String[] CustomernameArray = {"null"}, CustomerAccountNumberArray = {"null"};
    public static String[] arrayTitle = {"null"}, arrayId = {"null"}, arrayGender = {"null"}, arrayGenderId = {"null"},
            arrayMaritalStatus = {"null"}, arrayMaritalStatusId = {"null"}, arrayOccupation = {"null"}, arrayOccupationId = {"null"},
            arrayAccountType = {"null"}, arrayAccountTypeId = {"null"}, arrayModeOfOpeartion = {"null"},
            arrayModeOfOpeartionId = {"null"}, arrayConstitution = {"null"}, arrayConstitutionId = {"null"}, arrayAccountCategory = {"null"},
            arrayAccountCategoryId = {"null"}, arrayAccountrelation = {"null"}, arrayAccountrelationId = {"null"},
            arrayBrachName = {"null"}, arrayBrachId = {"null"};
    BroadcastReceiver receiver;


    public static boolean connectionStatusLogin;
    public static Intent serviceIntent;
    public static final int DEVICE_NOTCONNECTED = -100;
    private int iRetVal;
    OutputStream outputStream;
    InputStream inputstream;
    FPS fps;
    private SweetAlertDialog dialog;
    Button button, login_finger;
    private boolean fingerPrintcaptured;
    public static boolean authorizedForbiometric = false;
    ImageButton exit_app_image_btn, bluetooth_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        UI_validation.setUiType(COLLECTION);
//        UI_validation.setUiType("11");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        pDialog = new SweetAlertDialog(login.this, SweetAlertDialog.PROGRESS_TYPE);
        Status = (TextView) findViewById(R.id.loginStatustxt);
        Status.setVisibility(GONE);
        inputName = (TextView) findViewById(R.id.username_edit_text);
        inputPassword = (TextView) findViewById(R.id.password_edit_text);
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogWarning = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        button = (Button) findViewById(R.id.login_btn);
        login_finger = (Button) findViewById(R.id.login_biometric_btn);
        bluetooth_btn = (ImageButton) findViewById(R.id.bluetooth_img_btn);
        exit_app_image_btn = (ImageButton) findViewById(R.id.app_exit_img_btn);


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(Bluetooth_Service.COPA_MESSAGE);
                //Toast.makeText(context, s+"Mainactivity", Toast.LENGTH_SHORT).show();
                if (s.equals("disconnected")) {
//                    connectionStatusLogin = false;
                    //mGP.closeConn();
                    //Toast.makeText(context, "if condition disconnected", Toast.LENGTH_SHORT).show();
                }
                if (s.equals("connected")) {
//                    connectionStatusLogin = true;
                    connectionStatus = true;
                    //Toast.makeText(context, "if condition connected", Toast.LENGTH_SHORT).show();
                }
            }
        };





      /*  Button ip=(Button)findViewById(R.id.set_ip);
        //new CreateNewProduct().execute();
        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText=(EditText)findViewById(R.id.ip_global);
                ip_global=editText.getText().toString();
            }
        });*/

        bluetooth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(login.this, Act_Main.class));
////                                    sweetAlertDialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), BluetoothChat.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionDetector connDetector = new ConnectionDetector(getApplicationContext());
                boolean ConStatus = connDetector.isnetAvailable();

//                boolean ConStatus = CheckNetwork();
                if (ConStatus) {
                    new UserLogin().execute();
                } else {
                    Toast.makeText(login.this, "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionStatusLogin) {
                    try {
                        outputStream = BluetoothComm.mosOut;
                        inputstream = BluetoothComm.misIn;
                        fps = new FPS(Act_Main.setupInstance, outputStream, inputstream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    new VerifyTempleAsync().execute();
                } else {
                    new SweetAlertDialog(login.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("BT Device Not Connected")
                            .setContentText("please connect the device")
                            .setCancelText("NO")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(login.this, Act_Main.class));
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                }
            }
        });

        exit_app_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(login.this)
                        .setIcon(R.drawable.logout_rounded)
                        .setTitle("Exit")
                        .setMessage("Are you sure you want to Exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if (mGP != null) {
                                        mGP.closeConn();
                                    }
                                    BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
                                    stopService(serviceIntent);
                                    connectionStatusLogin = false;
                                    connectionStatus = false;
                                    mAdapter.disable();
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    finish();
                                }
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        /*
        This activity is created and opened when SplashScreen finishes its animations.
        To ensure a smooth transition between activities, the activity creation animation
        is removed.
        RelativeLayout with EditTexts and Button is animated with a default fade in.
         */
       /* overridePendingTransition(0,0);
        View relativeLayout=findViewById(R.id.login_container);
        Animation animation=AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        relativeLayout.startAnimation(animation);*/
        askPermissions();
    }


    public void askPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.BLUETOOTH_ADMIN)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    class UserLogin extends AsyncTask<String, String, String> {
        int invalidCredentials = 0;
        private String api_url = ip_global + "/userLogin";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            int success;
            String name = inputName.getText().toString();
            String password = inputPassword.getText().toString();
            try {
                // Building Parameters
                HashMap<String, String> h1 = new HashMap<>();
                h1.put("username", name);
                h1.put("password", password);
                JSONObject json = jsonParser.makeHttpRequest(api_url, "POST", h1);
//                Log.e("Server Response", json.toString());
                JsonData = json.toString();
                JSONObject reader = new JSONObject(JsonData);
                JSONObject jobj = reader.getJSONObject("user");
                if (jobj.has("data")) {
                    Log.e("Create Response 2", jobj.toString());
                    jobj2 = jobj.getJSONObject("data");
                    agent_code = jobj2.getString("USER_ID");
                    agent_name = jobj2.getString("USER_NAME");
                    branch_name = jobj2.getString("BRANCH_NAME");
                    branch_id = jobj2.getString("BRANCH_ID");
                    invalidCredentials = 1;
                    //String bimage=jobj2.getString("DISPLAY_PIC");
                    return "OK";
                }
                if (jobj.has("error")) {
                    error = jobj.getString("error");
                    return "invalid";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "error";

            }
            // check for success tag
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String val) {
            // dismiss the dialog once done
            if (val == "invalid") {
                pDialog.dismiss();
                new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText(error)
                        .show();
            }
            if (val == "error") {
                pDialog.dismiss();
                new SweetAlertDialog(login.this, SweetAlertDialog.BUTTON_NEGATIVE)
                        .setTitleText("ERROR")
                        .setContentText("Error in connection.")
                        .show();
            }
            if (val == null) {
                pDialog.dismiss();
                new SweetAlertDialog(login.this, SweetAlertDialog.BUTTON_NEGATIVE)
                        .setTitleText("Oops!")
                        .setContentText("Sorry, something went Wrong.")
                        .show();
            }
            if (val == "OK") {
                SharedPreferences createSharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = createSharedPref.edit();
                editor.putString("agent_id", agent_code);
                editor.putString("branch_id", branch_id);
                editor.commit();

                new getAccountNumbers().execute();

//===========================================================================================================
////                new getAccountNumbers().execute();
////                if (mChatService != null) {
////                    if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
////                    }
////                }
//                if (!connectionStatusLogin) {
//                    pDialog.dismiss();
//                    if (mChatService == null) {
//                        dialogWarning
//                                .setTitleText("BT Device Not Connected")
//                                .setContentText("Do you want to connect with bluetooth device")
//                                .setConfirmText("YES")
//                                .setCancelText("NO")
//                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
////                                    pDialog.dismiss();
//                                        new getAccountNumbers().execute();
//                                        dialogWarning.dismiss();
//                                    }
//                                })
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        Intent intent = new Intent(getApplicationContext(), BluetoothChat.class);
//                                        startActivity(intent);
//                                        dialogWarning.dismiss();
//                                    }
//                                }).show();
//
//                    }
//                    if (mChatService != null) {
//                        new getAccountNumbers().execute();
//                    }
//                }

//===========================================================================================================
//                new getAccountNumbers().execute();

//                if (!fingerPrintcaptured) {
//                    new getAccountNumbers().execute();
////                } else if (connectionStatusLogin) {
//                } else
//
//                if (!connectionStatusLogin) {
//                    pDialog.dismiss();
//                    authorizedForbiometric = true;
//                    exit_app_image_btn.setEnabled(false);
//                    exit_app_image_btn.setVisibility(GONE);
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentTransaction ft = fragmentManager.beginTransaction();
//                    Agent_Change_Biometric agent_change_biometric = new Agent_Change_Biometric();
//                    ft.replace(R.id.login_container_frame_layout, agent_change_biometric, null);
//                    ft.commit();
//                }else {
//                    pDialog.dismiss();
//                    new getAccountNumbers().execute();
//                }

//                if (!connectionStatusLogin && !fingerPrintcaptured) {
//                    pDialog.dismiss();
//                    new SweetAlertDialog(login.this, SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("BT Device Not Connected")
//                            .setContentText("please connect the device")
//                            .setCancelText("NO")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    startActivity(new Intent(login.this, Act_Main.class));
//                                    sweetAlertDialog.dismiss();
//                                }
//                            }).show();
//
//                }
            }
        }
    }

    class getAccountNumbers extends AsyncTask<String, String, String> {
        private String api_getCustomers = ip_global + "/getCustUnderAgent";
        String msg = "Something went wrong.";

        @Override
        public void onPreExecute() {
            button.setEnabled(false);
//            login_finger.setEnabled(false);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("agent_id", agent_code);
            hashMap.put("branch_id", branch_id);
            hashMap.put("name", "null");
            hashMap.put("acc_type", "null");
            hashMap.put("acc_cat", "null");

            Log.e("getCustUnderAgent: ", String.valueOf(hashMap));

            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_getCustomers, "POST", hashMap);
                String json_result = jsonObject.toString();
                Log.e("getCustomerunderAgent=", json_result);
                JSONObject data = new JSONObject(json_result);
                JSONObject customer_list = data.getJSONObject("customer_list");
                JSONArray jsonArray = customer_list.getJSONArray("data");
                Log.e("JSONArray=>", jsonArray.toString());
                CustomernameArray = new String[jsonArray.length()];
                CustomerAccountNumberArray = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject arrayObject = jsonArray.getJSONObject(i);
                    accountName = arrayObject.getString("CUST_NAME");
                    CustomernameArray[i] = accountName;
                    account_Number = arrayObject.getString("ACC_NO");
                    CustomerAccountNumberArray[i] = account_Number;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                msg = "Network issue occurred";
            }

            return msg;
        }

        @Override
        public void onPostExecute(String result) {
            setCustomerName(CustomernameArray);
            setCustomerAccountNumber(CustomerAccountNumberArray);
            for (int i = 0; i < getCustomerName().length; i++) {
                String[] name = getCustomerName();
                Log.e("Name : ", name[i]);
            }

            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            new getMasters().execute();

            //Log.e("Account Names",getCustomerName().toString());
            //Log.e("Account Numbers",getCustomerAccountNumber().toString());
        }
    }

    class getMasters extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("null", "null");
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_get_masters, "POST", hashmap);
                JSONObject masters = data.getJSONObject("NTITLE");
                JSONArray jsonArray = masters.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonArray.toString());
                int length = jsonArray.length();
                arrayId = new String[length];
                arrayTitle = new String[length];
                for (int i = 0; i < length; i++) {
                    JSONObject arrayObject = jsonArray.getJSONObject(i);
                    arrayTitle[i] = arrayObject.getString("NAME");
                    arrayId[i] = arrayObject.getString("VALUE");
                }
                JSONObject masters1 = data.getJSONObject("GNDR");
                JSONArray jsonGender = masters1.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonGender.toString());
                int genderlength = jsonGender.length();
                arrayGender = new String[genderlength];
                arrayGenderId = new String[genderlength];
                for (int i = 0; i < genderlength; i++) {
                    JSONObject arrayObject = jsonGender.getJSONObject(i);
                    arrayGender[i] = arrayObject.getString("NAME");
                    arrayGenderId[i] = arrayObject.getString("VALUE");
                }
                JSONObject masters2 = data.getJSONObject("MARSTATUS");
                JSONArray jsonMaritalStatusArray = masters2.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonMaritalStatusArray.toString());
                int marital_length = jsonMaritalStatusArray.length();
                arrayMaritalStatus = new String[marital_length];
                arrayMaritalStatusId = new String[marital_length];
                for (int i = 0; i < marital_length; i++) {
                    JSONObject arrayObject = jsonMaritalStatusArray.getJSONObject(i);
                    arrayMaritalStatus[i] = arrayObject.getString("NAME");
                    arrayMaritalStatusId[i] = arrayObject.getString("VALUE");
                }
                JSONObject masters3 = data.getJSONObject("OCCU");
                JSONArray jsonOccupationArray = masters3.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonOccupationArray.toString());
                int occu_length = jsonOccupationArray.length();
                arrayOccupation = new String[occu_length];
                arrayOccupationId = new String[occu_length];
                for (int i = 0; i < occu_length; i++) {
                    JSONObject arrayObject = jsonOccupationArray.getJSONObject(i);
                    arrayOccupation[i] = arrayObject.getString("NAME");
                    arrayOccupationId[i] = arrayObject.getString("VALUE");
                }
                JSONObject masters4 = data.getJSONObject("ACC_TYPE");
                JSONArray jsonacc_type_Array = masters4.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonacc_type_Array.toString());
                int acc_type_length = jsonacc_type_Array.length();
                arrayAccountType = new String[acc_type_length];
                arrayAccountTypeId = new String[acc_type_length];
                for (int i = 0; i < acc_type_length; i++) {
                    JSONObject arrayObject = jsonacc_type_Array.getJSONObject(i);
                    arrayAccountType[i] = arrayObject.getString("NAME");
                    arrayAccountTypeId[i] = arrayObject.getString("VALUE");
                }
                JSONObject masters5 = data.getJSONObject("MODE_OPER");
                JSONArray jsonmode_of_oper_Array = masters5.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonmode_of_oper_Array.toString());
                int mode_of_operation_length = jsonmode_of_oper_Array.length();
                arrayModeOfOpeartion = new String[mode_of_operation_length];
                arrayModeOfOpeartionId = new String[mode_of_operation_length];
                for (int i = 0; i < mode_of_operation_length; i++) {
                    JSONObject arrayObject = jsonmode_of_oper_Array.getJSONObject(i);
                    arrayModeOfOpeartion[i] = arrayObject.getString("NAME");
                    arrayModeOfOpeartionId[i] = arrayObject.getString("VALUE");
                }
                JSONObject masters6 = data.getJSONObject("CONSTITUTION");
                JSONArray jsonConstitutionArray = masters6.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonConstitutionArray.toString());
                int constitution_length = jsonConstitutionArray.length();
                arrayConstitution = new String[constitution_length];
                arrayConstitutionId = new String[constitution_length];
                for (int i = 0; i < constitution_length; i++) {
                    JSONObject arrayObject = jsonConstitutionArray.getJSONObject(i);
                    arrayConstitution[i] = arrayObject.getString("NAME");
                    arrayConstitutionId[i] = arrayObject.getString("VALUE");
                }
                JSONObject masters7 = data.getJSONObject("ACC_CTG");
                JSONArray jsonacc_cat_Array = masters7.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonacc_cat_Array.toString());
                int acc_cat_length = jsonacc_cat_Array.length();
                arrayAccountCategory = new String[acc_cat_length];
                arrayAccountCategoryId = new String[acc_cat_length];
                for (int i = 0; i < acc_cat_length; i++) {
                    JSONObject arrayObject = jsonacc_cat_Array.getJSONObject(i);
                    arrayAccountCategory[i] = arrayObject.getString("NAME");
                    arrayAccountCategoryId[i] = arrayObject.getString("VALUE");
                }
                JSONObject masters8 = data.getJSONObject("ACC_RELAT");
                JSONArray jsonaacc_relation_Array = masters8.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonaacc_relation_Array.toString());
                int acc_rela_length = jsonaacc_relation_Array.length();
                arrayAccountrelation = new String[acc_rela_length];
                arrayAccountrelationId = new String[acc_rela_length];
                for (int i = 0; i < acc_rela_length; i++) {
                    JSONObject arrayObject = jsonaacc_relation_Array.getJSONObject(i);
                    arrayAccountrelation[i] = arrayObject.getString("NAME");
                    arrayAccountrelationId[i] = arrayObject.getString("VALUE");
                }

                JSONObject masters9 = data.getJSONObject("BRANCH_DETAILS");
                JSONArray branch_Array = masters9.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", branch_Array.toString());
                int bra_arr_length = branch_Array.length();
                arrayBrachName = new String[bra_arr_length];
                arrayBrachId = new String[bra_arr_length];
                for (int i = 0; i < bra_arr_length; i++) {
                    JSONObject arrayObject = branch_Array.getJSONObject(i);
                    arrayBrachName[i] = arrayObject.getString("NAME");
                    arrayBrachId[i] = arrayObject.getString("VALUE");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "accountHolderError";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("id", agent_code);
            // intent.putExtra("image",bimage);
            startActivity(intent);
            dialog.dismiss();
            pDialog.dismiss();
        }
    }

    public boolean CheckNetwork() {
        boolean connection = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connection = true;
        } else {
            connection = false;
        }
        return connection;
    }


    public class VerifyTempleAsync extends AsyncTask<Integer, Integer, Integer> {
        /* displays the progress dialog until background task is completed */
        @Override
        protected void onPreExecute() {
            dialog.setTitleText("Processing")
                    .setContentText("Place your finger on FPS ...").show();
            //progressDialog(context, "Place your finger on FPS ...");
            super.onPreExecute();
        }

        /* Task of VerifyTempleAsync performing in the background */
        @Override
        protected Integer doInBackground(Integer... params) {

            try {
                File file = new File(getFilesDir() + File.separator + "94!!4&$75!@#$$06%@1=4");
                int size = (int) file.length();
                byte[] bytes = new byte[size];
                try {
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                iRetVal = fps.iFpsVerifyMinutiae(bytes, new FpsConfig(1, (byte) 0x0f));
            } catch (Exception e) {
                iRetVal = DEVICE_NOTCONNECTED;
                return iRetVal;
            }
            return iRetVal;
        }

        /*
         * This function sends message to handler to display the status messages
         * of Diagnose in the dialog box
         */
        @Override
        protected void onPostExecute(Integer result) {
            dialog.dismiss();
            if (iRetVal == DEVICE_NOTCONNECTED) {
                dialogWarning.setTitleText("Error")
                        .setContentText("Device not connected").show();
            } else if (iRetVal == FPS.SUCCESS) {
                new getAccountNumbers().execute();
            } else if (iRetVal == FPS.FPS_INACTIVE_PERIPHERAL) {
                dialogWarning.setTitleText("Error")
                        .setContentText("Peripheral is inactive").show();
            } else if (iRetVal == FPS.TIME_OUT) {
                dialogWarning.setTitleText("Error")
                        .setContentText("Capture finger time out").show();
            } else if (iRetVal == FPS.FAILURE) {
                dialogWarning.setTitleText("Verification Failed")
                        .setContentText("if your finger is wet, please clean and try again.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                dialogWarning.dismiss();
                            }
                        }).show();
            } else if (iRetVal == FPS.PARAMETER_ERROR) {
                dialogWarning.setTitleText("Error")
                        .setContentText("Parameter error").show();
            } else if (iRetVal == FPS.FPS_INVALID_DEVICE_ID) {
                dialogWarning.setTitleText("Error")
                        .setContentText("Connected  device is not license authenticated.").show();
            } else if (iRetVal == FPS.FPS_ILLEGAL_LIBRARY) {
                dialogWarning.setTitleText("Error")
                        .setContentText("Library not valid").show();
            }
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(Bluetooth_Service.COPA_RESULT));

        super.onStart();
    }


    @Override
    protected void onResume() {
        button.setEnabled(true);
//        login_finger.setEnabled(true);
        serviceIntent = new Intent(this, Bluetooth_Service.class);
        startService(serviceIntent);

        try {
            outputStream = BluetoothComm.mosOut;
            inputstream = BluetoothComm.misIn;
            fps = new FPS(Act_Main.setupInstance, outputStream, inputstream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        agent_code = sharedPref.getString("agent_id", null);
        branch_id = sharedPref.getString("branch_id", null);

        File file = getFilesDir();
        Log.e("onResume: file.toStrin", file.toString());
        try {
            if (file.isDirectory() && file.list().length == 0) {
                fingerPrintcaptured = false;
            } else {
                fingerPrintcaptured = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "No Fingerprint Captured!", Toast.LENGTH_SHORT).show();
        }

//        if (agent_code == null && branch_id == null && !fingerPrintcaptured) {
//            login_finger.setVisibility(GONE);
//
//        }
//        if (agent_code != null && branch_id != null && fingerPrintcaptured) {
//            login_finger.setVisibility(View.VISIBLE);
//        }
//        if (!fingerPrintcaptured) {
//            login_finger.setVisibility(View.GONE);
//        }
        super.onResume();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
