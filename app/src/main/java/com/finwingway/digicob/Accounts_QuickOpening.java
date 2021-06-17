package com.finwingway.digicob;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.finwingway.digicob.leopard.Act_Main;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static com.finwingway.digicob.MainActivity.connectionStatus;
import static com.finwingway.digicob.login.ip_global;

/**
 * Created by AnVin on 12/28/2016.
 */

public class Accounts_QuickOpening extends Fragment implements Validator.ValidationListener {
    TextView branch_text_view, date_text_view;
    //    @TextRule(order = 1, minLength = 3, message = "Enter atleast 3 characters.")
//    @Regex(order = 1, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText fname_edit_text;
    //    @TextRule(order = 2, minLength = 0, message = "Enter atleast 1 characters.")
//    @Regex(order = 2, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText mname_edit_text;
    //    @TextRule(order = 3, minLength = 0, message = "Enter atleast 3 characters.")
//    @Regex(order = 3, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText lname_edit_text;

    @TextRule(order = 5, message = "Enter atleast 3 characters.")
    @Regex(order = 5, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText guardianEditText;
    @TextRule(order = 6, minLength = 3, message = "Enter atleast 3 characters.")
    EditText flatnoEditText;
    @TextRule(order = 7, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 7, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText landmarkEditText;
    @TextRule(order = 8, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 8, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText locationEditText;
    @TextRule(order = 9, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 9, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText cityEditText;
    @TextRule(order = 10, minLength = 6, message = "Enter valid pincode.")
    @Regex(order = 10, pattern = "[0-9]+", message = "Should contain only Numbers")
    EditText pincodeEditText;
    @TextRule(order = 11, minLength = 10, message = "Pleaese Enter a  Valid Mobile Number")
    @Regex(order = 11, pattern = "[0-9]+", message = "Should contain only Numbers")
    EditText phone_number_edit_text;
    @Email(order = 12, message = "Please check email you entered", messageResId = 0)
    EditText qemail_edit_text;

    Spinner gender_spinner, acc_type_spinner, country_spinner, state_spinner, district_spinner, title_spinner;
    ImageView photograph_image_view, signature_image_view, biometric_image_view, id_proof_image_view,
            address_proof_image_view;
    String qtitle = "null", qfname = "null", qmname = "null", qlname = "null", qgender = "null", qaddress = "null", qphone = "null", qacc_type = "null",
            qguardian = "null", qflat = "null", qlandmark = "null", qlocation = "null", qcontry = "null", qstate = "null", qdistrict = "null",
            qcity = "null", qpincode = "null", qemail = "null";
    Button quick_save_btn;
    File file;
    String stringUri, stringAddressUri, strRtnCountry;
    String encoded, encoded2, encoded3;
    Bitmap compressedImageBitmap;
    Uri idProof, addressProof;
    public static String is_id_proof_submitted = "0", is_address_proof_submitted = "0", is_photograph_submitted = "0",
            is_signature_submitted = "0", is_biometric_submitted = "0";
    String ip = login.ip_global;
    SweetAlertDialog alert;
    JSONParser jsonParser = new JSONParser();
    private String api_get_country = ip_global + "/getCountryList";
    private String api_get_state = ip_global + "/getStateList";
    private String api_get_district = ip_global + "/getDistrictList";
    public static String[] arrayCountryName, arrayCountryId, arrayStateName, arrayStateId, arrayDistrictName, arrayDistrictId;
    String msg, error, refId;
    boolean validation = false;
    boolean state = false;
    boolean district = false;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View rootview = layoutInflater.inflate(R.layout.fragment_accounts_quick_opening, viewGroup, false);
        new getCountry().execute();
        final Validator validator = new Validator(this);
        validator.setValidationListener(this);
        branch_text_view = (TextView) rootview.findViewById(R.id.quick_branch_text_view);
        date_text_view = (TextView) rootview.findViewById(R.id.quick_date_text_view);
        fname_edit_text = (EditText) rootview.findViewById(R.id.quick_fname_edit_text);
        mname_edit_text = (EditText) rootview.findViewById(R.id.quick_mname_edit_text);
        lname_edit_text = (EditText) rootview.findViewById(R.id.quick_lname_edit_text);
        guardianEditText = (EditText) rootview.findViewById(R.id.quick_cus_comm_guardianEditText);
        flatnoEditText = (EditText) rootview.findViewById(R.id.quick_cus_comm_flatNameEditText);
        locationEditText = (EditText) rootview.findViewById(R.id.quick_cus_comm_landmarkEditText);
        landmarkEditText = (EditText) rootview.findViewById(R.id.quick_cus_comm_locationEditText);
        pincodeEditText = (EditText) rootview.findViewById(R.id.quick_cus_comm_pincodeEditText);
        cityEditText = (EditText) rootview.findViewById(R.id.quick_cus_comm_cityEditText);
        phone_number_edit_text = (EditText) rootview.findViewById(R.id.quick_phone_edit_text);
        qemail_edit_text = (EditText) rootview.findViewById(R.id.quick_e_mail_edit_text);
        title_spinner = (Spinner) rootview.findViewById(R.id.quick_spinner_title);
        gender_spinner = (Spinner) rootview.findViewById(R.id.quick_gender_spinner);
        acc_type_spinner = (Spinner) rootview.findViewById(R.id.quick_acc_type_spinner);
        country_spinner = (Spinner) rootview.findViewById(R.id.quick_cus_country_spinner);
        state_spinner = (Spinner) rootview.findViewById(R.id.quick_cus_state_spinner);
        district_spinner = (Spinner) rootview.findViewById(R.id.quick_cus_district_spinner);
        photograph_image_view = (ImageView) rootview.findViewById(R.id.quick_photograph_image_view);
        signature_image_view = (ImageView) rootview.findViewById(R.id.quick_sign_image_view);
        biometric_image_view = (ImageView) rootview.findViewById(R.id.quick_biometric_image_view);
        id_proof_image_view = (ImageView) rootview.findViewById(R.id.quick_id_image_view);
        address_proof_image_view = (ImageView) rootview.findViewById(R.id.quick_address_image_view);
        quick_save_btn = (Button) rootview.findViewById(R.id.quick_save_button);
        branch_text_view.setText(login.branch_name);

        ArrayAdapter titleAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, login.arrayTitle);
        title_spinner.setAdapter(titleAdapter);
        title_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qtitle = login.arrayId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter genderAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, login.arrayGender);
        gender_spinner.setAdapter(genderAdapter);
        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qgender = login.arrayGenderId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter acc_type_Adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, login.arrayAccountType);
        acc_type_spinner.setAdapter(acc_type_Adapter);
        acc_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qacc_type = login.arrayAccountTypeId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                qacc_type = "null";
            }
        });

        ArrayAdapter countryAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, login.arrayGender);
        country_spinner.setAdapter(countryAdapter);
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qcontry = login.arrayGenderId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                qcontry = "null";
            }
        });

        ArrayAdapter stateAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, login.arrayGender);
        state_spinner.setAdapter(stateAdapter);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qstate = login.arrayGenderId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter districtAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, login.arrayGender);
        district_spinner.setAdapter(districtAdapter);
        district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qdistrict = login.arrayGenderId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        photograph_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_photograph_submitted.equals("1")) {
                    Intent intent = new Intent(getActivity(), Change_kyc_image.class);
                    intent.putExtra("data", "photograph");
                    startActivityForResult(intent, 1001);
                } else {
                    getPhotograph();
                }
            }
        });
        id_proof_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_id_proof_submitted.equals("1")) {
                    Intent intent = new Intent(getActivity(), Change_kyc_image.class);
                    intent.putExtra("data", "id_proof");
                    startActivityForResult(intent, 1002);
                } else {
                    getIdProof();
                }
            }
        });

        address_proof_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (is_address_proof_submitted.equals("1")) {
                    Intent intent = new Intent(getActivity(), Change_kyc_image.class);
                    intent.putExtra("data", "address_proof");
                    startActivityForResult(intent, 1003);
                } else {
                    getAddressProof();
                }
            }
        });

        signature_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_signature_submitted.equals("1")) {
                    Intent intent = new Intent(getActivity(), Change_kyc_image.class);
                    intent.putExtra("data", "signature");
                    startActivityForResult(intent, 1004);
                } else {
                    getSignature();
                }
            }
        });
        biometric_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_biometric_submitted.equals("1")) {
                    Intent intent = new Intent(getActivity(), Change_kyc_image.class);
                    intent.putExtra("data", "biometric");
                    startActivityForResult(intent, 1005);
                } else {
                    getBiometric();
                }
            }
        });

        quick_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validator.validate();
                qfname = fname_edit_text.getText().toString();
                qmname = mname_edit_text.getText().toString();
                qlname = lname_edit_text.getText().toString();

                if (TextUtils.isEmpty(qfname)) {
                    fname_edit_text.setError("Enter First Name");
                    return;
                }
                if (TextUtils.isEmpty(qlname)) {
                    lname_edit_text.setError("Enter Last Name");
                    return;
                }

                if (TextUtils.isEmpty(qmname)) {
                    qmname = "";
                }


                qguardian = guardianEditText.getText().toString();
                qflat = flatnoEditText.getText().toString();
                qlandmark = landmarkEditText.getText().toString();
                qlocation = locationEditText.getText().toString();
                qcity = locationEditText.getText().toString();
                qpincode = locationEditText.getText().toString();
                qphone = phone_number_edit_text.getText().toString();
                qemail = qemail_edit_text.getText().toString();
                new create_account().execute();
                if (validation) {
                    if (state || district) {
                        new create_account().execute();
                    } else {
                        Toast.makeText(getContext(), qcontry + qdistrict + qstate + "Please select Country/State/District",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            try {
                stringUri = data.getStringExtra("cropUriPhotograph");
                idProof = Uri.parse(stringUri);
                file = new File(idProof.getPath());
                compressedImageBitmap = Compressor.getDefault(getContext()).compressToBitmap(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] array = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(array, Base64.DEFAULT);
                imageAdapter.setPhotograph_image_string(encoded);
                Log.e("encoded", Base64.encodeToString(array, Base64.DEFAULT));
                is_photograph_submitted = "1";
                photograph_image_view.setImageBitmap(compressedImageBitmap);
            } catch (Exception e) {

            }
        }

        if (requestCode == 200) {
            try {
                stringUri = data.getStringExtra("cropUri");
                idProof = Uri.parse(stringUri);
                file = new File(idProof.getPath());
                compressedImageBitmap = Compressor.getDefault(getContext()).compressToBitmap(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] array = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(array, Base64.DEFAULT);
                imageAdapter.setId_image_string(encoded);
                Log.e("encoded", Base64.encodeToString(array, Base64.DEFAULT));
                is_id_proof_submitted = "1";
                id_proof_image_view.setImageBitmap(compressedImageBitmap);
            } catch (Exception e) {

            }
        }
        if (requestCode == 201) {
            try {
                stringAddressUri = data.getStringExtra("cropUriAddress");
                Toast.makeText(getActivity(), stringAddressUri, Toast.LENGTH_LONG).show();
                addressProof = Uri.parse(stringAddressUri);
                file = new File(addressProof.getPath());
                compressedImageBitmap = Compressor.getDefault(getContext()).compressToBitmap(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] array = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(array, Base64.DEFAULT);
                imageAdapter.setAddress_image_string(encoded);
                is_address_proof_submitted = "1";
                address_proof_image_view.setImageBitmap(compressedImageBitmap);
            } catch (Exception e) {

            }
        }
        if (requestCode == 1) {
            try {
                byte[] byteArray = data.getByteArrayExtra("BMP");
                File f = File.createTempFile("Signature", ".jpg", getContext().getCacheDir());
                f.createNewFile();
                FileOutputStream stream = new FileOutputStream(f.getPath());
                stream.write(byteArray);

                compressedImageBitmap = new Compressor.Builder(getContext())
                        .setMaxWidth(1024)
                        .setMaxHeight(600)
                        .setQuality(70)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .build()
                        .compressToBitmap(f);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] array = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(array, Base64.DEFAULT);
                imageAdapter.setSignature_image_string(encoded);
                is_signature_submitted = "1";
                signature_image_view.setImageBitmap(compressedImageBitmap);
            } catch (Exception e) {

            }
        }
        if (resultCode == RESULT_OK && requestCode == 104) {
            if (imageAdapter.getFingerBItmap() != null) {
                try {
                    byte[] array = data.getByteArrayExtra("biometric_byte_array");
                    is_biometric_submitted = "1";
                    biometric_image_view.setImageResource(R.drawable.biometric_updated);
                    encoded = Base64.encodeToString(array, Base64.DEFAULT);
                    imageAdapter.setBiometric_image_string(encoded);
                    if (imageAdapter.getBiometric_image_string() != null) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    byte[] array2 = data.getByteArrayExtra("biometric_byte_array2");
                    is_biometric_submitted = "1";
                    biometric_image_view.setImageResource(R.drawable.biometric_updated);
                    encoded2 = Base64.encodeToString(array2, Base64.DEFAULT);
                    imageAdapter.setBiometric_image_string2(encoded2);
                    if (imageAdapter.getBiometric_image_string() != null) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    byte[] array3 = data.getByteArrayExtra("biometric_byte_array3");
                    is_biometric_submitted = "1";
                    biometric_image_view.setImageResource(R.drawable.biometric_updated);
                    encoded3 = Base64.encodeToString(array3, Base64.DEFAULT);
                    imageAdapter.setBiometric_image_string3(encoded3);
                    if (imageAdapter.getBiometric_image_string() != null) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                //Toast.makeText(getContext(), "Please Capture Your Finger Print", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "NO Fingerprint Captured", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == 1001) {
            getPhotograph();
        }
        if (resultCode == RESULT_OK && requestCode == 1002) {
            getIdProof();
        }
        if (resultCode == RESULT_OK && requestCode == 1003) {
            getAddressProof();
        }
        if (resultCode == RESULT_OK && requestCode == 1004) {
            getSignature();
        }
        if (resultCode == RESULT_OK && requestCode == 1005) {
            getBiometric();
        }
    }

    @Override
    public void onValidationSucceeded() {
        validation = true;
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        validation = false;
        if (failedView instanceof EditText) {
            Toast.makeText(getContext(), ((EditText) failedView).getHint() + " " + message, Toast.LENGTH_SHORT).show();
            ((EditText) failedView).setError(message);
        } else {

        }
    }


    private class getCountry extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("null", "null");
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_get_country, "POST", hashmap);
                JSONObject masters = data.getJSONObject("country_list");
                JSONArray jsonArray = masters.getJSONArray("data");
                Log.e("JSON_ARRAY_MASTERS=>", jsonArray.toString());
                int length = jsonArray.length();
                arrayCountryName = new String[length];
                arrayCountryId = new String[length];
                for (int i = 0; i < length; i++) {
                    JSONObject arrayObject = jsonArray.getJSONObject(i);
                    arrayCountryName[i] = arrayObject.getString("NAME");
                    arrayCountryId[i] = arrayObject.getString("ID");
                }
                strRtnCountry = "ok";
            } catch (Exception e) {
                strRtnCountry = "error";
                e.printStackTrace();
            }
            return strRtnCountry;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayAdapter countryAdapter = null;
            if (s.equals("ok")) {
                try {
                    countryAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayCountryName);
                    country_spinner.setAdapter(countryAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            qcontry = arrayCountryId[position];
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new getState().execute();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            if (s.equals("error")) {
                String[] nodata = {"---No Data---"};
                try {
                    countryAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, nodata);
                    country_spinner.setAdapter(countryAdapter);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class getState extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("COUNTRY_ID", qcontry);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_get_state, "POST", hashmap);
                JSONObject masters = data.getJSONObject("state_list");
                if (masters.has("data")) {
                    JSONArray jsonArray = masters.getJSONArray("data");
                    Log.e("JSON_ARRAY_MASTERS=>", jsonArray.toString());
                    int length = jsonArray.length();
                    arrayStateName = new String[length];
                    arrayStateId = new String[length];
                    for (int i = 0; i < length; i++) {
                        JSONObject arrayObject = jsonArray.getJSONObject(i);
                        arrayStateName[i] = arrayObject.getString("NAME");
                        arrayStateId[i] = arrayObject.getString("ID");
                        msg = "ok";
                    }
                }
                if (masters.has("msg")) {
                    error = masters.getString("msg");
                    msg = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("ok")) {
                state = true;
                ArrayAdapter stateAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayStateName);
                state_spinner.setAdapter(stateAdapter);
                state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        qstate = arrayStateId[position];
                        new getDistrict().execute();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            if (s.equals("error")) {
                String[] nodata = {"---No Data---"};
                try {
                    state = false;
                    ArrayAdapter stateAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, nodata);
                    state_spinner.setAdapter(stateAdapter);
                    district_spinner.setAdapter(stateAdapter);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class getDistrict extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("STATE_ID", qstate);
            try {
                JSONObject data = jsonParser.makeHttpRequest(api_get_district, "POST", hashmap);
                JSONObject masters = data.getJSONObject("district_list");
                if (masters.has("data")) {
                    JSONArray jsonArray = masters.getJSONArray("data");
                    Log.e("JSON_ARRAY_MASTERS=>", jsonArray.toString());
                    int length = jsonArray.length();
                    arrayDistrictName = new String[length];
                    arrayDistrictId = new String[length];
                    for (int i = 0; i < length; i++) {
                        JSONObject arrayObject = jsonArray.getJSONObject(i);
                        arrayDistrictName[i] = arrayObject.getString("NAME");
                        arrayDistrictId[i] = arrayObject.getString("ID");
                        msg = "ok";
                    }
                }
                if (masters.has("msg")) {
                    error = masters.getString("msg");
                    msg = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("ok")) {
                district = true;
                ArrayAdapter stateAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayDistrictName);
                district_spinner.setAdapter(stateAdapter);
                district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        qdistrict = arrayDistrictId[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            if (s.equals("error")) {
                String[] nodata = {"---No Data---"};
                try {
                    district = false;
                    ArrayAdapter stateAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, nodata);
                    district_spinner.setAdapter(stateAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class create_account extends AsyncTask<String, String, String> {
        private String api_url_uploadImage = ip + "/Quickopening";

        @Override
        public void onPreExecute() {
            alert = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            alert.setTitleText("Processing");
            alert.setContentText("Please wait..");
            alert.show();
            //encodedtextView.setText(encoded);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMapAmount = new HashMap<>();
            hashMapAmount.put("TITLE", qtitle);
            hashMapAmount.put("FIRSTNAME", qfname);
            hashMapAmount.put("MIDDLENAME", qmname);
            hashMapAmount.put("LASTNAME", qlname);
            hashMapAmount.put("GENDER", qgender);
            hashMapAmount.put("EMAIL_ID", qemail);
            hashMapAmount.put("MOBILE_NO", qphone);
            hashMapAmount.put("CORRES_CAREOF", qguardian);
            hashMapAmount.put("CORRES_BUILDING", qflat);
            hashMapAmount.put("CORRES_LANDMARK", qlandmark);
            hashMapAmount.put("CORRES_STREET", qlocation);
            hashMapAmount.put("CORRES_COUNTRY", qcontry);
            hashMapAmount.put("CORRES_STATE", qstate);
            hashMapAmount.put("CORRES_DISTRICT", qdistrict);
            hashMapAmount.put("CORRES_CITY", qcity);
            hashMapAmount.put("CORRES_PIN", qpincode);
            hashMapAmount.put("IS_ID_PROOF_SUBMITTED", is_id_proof_submitted);
            hashMapAmount.put("IDENTITY_PROOF_DATA", isSubmitted(imageAdapter.getId_image_string()));
            hashMapAmount.put("IS_DOB_SUBMITTED", "0");
            hashMapAmount.put("IS_ADDR_PROOF_SUBMITTED", is_address_proof_submitted);
            hashMapAmount.put("ADDRESS_PROOF_DATA", isSubmitted(imageAdapter.getAddress_image_string()));
            hashMapAmount.put("PHOTOGRAPH_SUBMITTED", is_photograph_submitted);
            hashMapAmount.put("PHOTOGRAPH_DATA", isSubmitted(imageAdapter.getPhotograph_image_string()));
            hashMapAmount.put("SIGNATURE_SUBMITTED", is_signature_submitted);
            hashMapAmount.put("SIGNATURE_DATA", isSubmitted(imageAdapter.getSignature_image_string()));
            hashMapAmount.put("BIOMETRIC_SUBMITTED", is_biometric_submitted);
            hashMapAmount.put("BIOMETRIC_DATA", isSubmitted(imageAdapter.getBiometric_image_string()));
            hashMapAmount.put("BRANCH_CODE", login.branch_id);
            hashMapAmount.put("PRODUCT_CODE", qacc_type);
            hashMapAmount.put("EMP_ID", login.agent_code);
            hashMapAmount.put("AGENT", login.agent_code);
            hashMapAmount.put("BRANCHNAME", login.branch_name);
            hashMapAmount.put("CREATED_BY", "null");
            hashMapAmount.put("MACID", "null");
            try {
                Log.e("REQUEST", hashMapAmount.toString());
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url_uploadImage, "POST", hashMapAmount);
                String JsonDataString = jsonObject.toString();
                Log.e("string", JsonDataString);
                JSONObject reader1 = new JSONObject(JsonDataString);
                if (reader1.has("status")) {
                    if (reader1.getString("status").equals("0")) {
                        msg = reader1.getString("msg");
                        return msg;
                    }
                }

                JSONObject reader = reader1.getJSONObject("account_info");
                if (reader.has("data")) {
                    JSONObject data = reader.getJSONObject("data");
                    refId = data.getString("REF_ID");
                    Log.e("refID", refId);
                    msg = "OK";
                    return msg;
                }

                if (reader.has("error")) {
                    msg = reader.getString("error");
                } else {
                    msg = reader.getString("msg");
                    return msg;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return msg;
        }

        @Override
        public void onPostExecute(String result) {
            alert.dismiss();
            if (result.equals("OK")) {
                Toast.makeText(getContext(), refId, Toast.LENGTH_SHORT).show();
                imageAdapter.setPhotograph_image_string("null");
                imageAdapter.setBiometric_image_string("null");
                imageAdapter.setId_image_string("null");
                imageAdapter.setAddress_image_string("null");
                imageAdapter.setSignature_image_string("null");
                Bundle bundle = new Bundle();
                bundle.putString("name", qfname);
                bundle.putString("phone", qphone);
                bundle.putString("ac_type", qacc_type);
                bundle.putString("ref_id", refId);
                // set Fragmentclass Arguments

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
                Receipt_Ref_No accounts_kycUpdate = new Receipt_Ref_No();
                accounts_kycUpdate.setArguments(bundle);
                ft.replace(R.id.content_frame, accounts_kycUpdate);
                ft.commit();

            } else {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String isSubmitted(String data) {
        return "data:image/jpeg;base64," + data;
    }

    private void getPhotograph() {
        Intent id = new Intent(getActivity(), Accounts_Tab_Fragment_PhotographActivity.class);
        startActivityForResult(id, 0);
    }

    private void getSignature() {
        Intent intent = new Intent(getActivity(), Signature_activity.class);
        startActivityForResult(intent, 1);
    }

    private void getIdProof() {
        Intent id = new Intent(getActivity(), Accounts_Tab_Fragment_IDActivity.class);
        startActivityForResult(id, 200);
    }

    private void getAddressProof() {
        Intent id = new Intent(getActivity(), Accounts_Tab_Fragment_AddressActivity.class);
        startActivityForResult(id, 201);
    }

    private void getBiometric() {
        if (connectionStatus) {
            Intent intent = new Intent(getContext(), Biometric_Leopard.class);
            startActivityForResult(intent, 104);
        } else {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("BT Device Not Connected")
                    .setContentText("please connect the device")
                    .setCancelText("NO")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(new Intent(getContext(), Act_Main.class));
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
        }
    }
}
