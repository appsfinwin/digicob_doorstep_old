package com.finwingway.digicob;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.finwingway.digicob.leopard.Act_Main;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;

import com.finwingway.digicob.support.ConnectionDetector;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static android.graphics.Color.RED;
import static android.view.View.GONE;
import static com.finwingway.digicob.MainActivity.connectionStatus;

/**
 * Created by AnVin on 12/30/2016.
 */

public class Accounts_KYCUpdate extends Fragment {
    EditText cus_id_edit_text, otp_edit_text, pan_no_edit_text;
    Button submit_btn, dob_btn, update_kyc_btn;
    ImageButton search_cus_id_image_btn, photograph_image_btn, signature_image_btn,
            biometric_image_btn, idproof_image_btn, addressproof_image_btn;
    String searched_cus_id = "null";
    String ip = login.ip_global;
    private String name, gender, dob, pan, mobile;
    private boolean proof_photo, proof_signature, proof_biometric, proof_id, proof_address;
    JSONParser jsonParser = new JSONParser();
    SweetAlertDialog sweetAlertDialog;
    //Spinner gender_spinner;
    TextView acc_name_text_view, change_mobile_no_textView, mobile_number_textView;
    private String error = "error", msg = "Something went wrong";
    LinearLayout details_linear;
    File file;
    String stringUri, stringAddressUri;
    String encoded, encoded2, encoded3;
    Bitmap compressedImageBitmap;
    Uri idProof, addressProof;
    public static String is_id_proof_submitted = "0", is_address_proof_submitted = "0", is_photograph_submitted = "0",
            is_signature_submitted = "0", is_biometric_submitted = "0";
    SweetAlertDialog alert;
    static String dob_btn_txt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.fragment_accounts_kyc_update, container, false);
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        search_cus_id_image_btn = (ImageButton) rootView.findViewById(R.id.kycu_acc_search_btn);
        details_linear = (LinearLayout) rootView.findViewById(R.id.kycu_details_linear);
        details_linear.setVisibility(GONE);
        alert = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        search_cus_id_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Account_Search.class);
                startActivityForResult(intent, 1007);
            }
        });
        cus_id_edit_text = (EditText) rootView.findViewById(R.id.kycu_acc_no_edit_text);
        submit_btn = (Button) rootView.findViewById(R.id.kycu_acc_no_submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connDetector = new ConnectionDetector(getContext());
                if (connDetector.isnetAvailable()) {
                    new getKYCDetails().execute();
                } else {
                    Toast.makeText(getContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        acc_name_text_view = (TextView) rootView.findViewById(R.id.kycu_account_name_text_view);
        change_mobile_no_textView = (TextView) rootView.findViewById(R.id.kycu_change_mobile_textView);
        mobile_number_textView = (TextView) rootView.findViewById(R.id.kycu_mobile_number_textView);

        //gender_spinner=(Spinner)rootView.findViewById(R.id.kycu_gender_spinner);

        dob_btn = (Button) rootView.findViewById(R.id.kycu_date_btn);
        update_kyc_btn = (Button) rootView.findViewById(R.id.kycu_update_btn);
        update_kyc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connDetector = new ConnectionDetector(getContext());
                if (connDetector.isnetAvailable()) {
                    new update_kyc().execute();
                } else {
                    Toast.makeText(getContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pan_no_edit_text = (EditText) rootView.findViewById(R.id.kycu_pan_no_edit_text);
        otp_edit_text = (EditText) rootView.findViewById(R.id.kycu_otp_edit_text);

        photograph_image_btn = (ImageButton) rootView.findViewById(R.id.kycu_applicantsPhoto_imageButton);
        signature_image_btn = (ImageButton) rootView.findViewById(R.id.kycu_imagebtn_sign);
        biometric_image_btn = (ImageButton) rootView.findViewById(R.id.kycu_biometric_imagebtn);
        idproof_image_btn = (ImageButton) rootView.findViewById(R.id.kycu_idproof_imagebtn);
        addressproof_image_btn = (ImageButton) rootView.findViewById(R.id.kycu_address_imagebtn);

        dob_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
//                dob_btn=dob_btn_txt;
            }
        });

        photograph_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent id = new Intent(getActivity(), Accounts_Tab_Fragment_PhotographActivity.class);
                startActivityForResult(id, 0);
            }
        });
        photograph_image_btn.setOnClickListener(new View.OnClickListener() {
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
        idproof_image_btn.setOnClickListener(new View.OnClickListener() {
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

        addressproof_image_btn.setOnClickListener(new View.OnClickListener() {
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

        signature_image_btn.setOnClickListener(new View.OnClickListener() {
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
        biometric_image_btn.setOnClickListener(new View.OnClickListener() {
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

        change_mobile_no_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Change_number_activity.class);
                startActivityForResult(intent, 2000);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1007 && resultCode == RESULT_OK) {
            searched_cus_id = data.getStringExtra("result_cus_id");
            cus_id_edit_text.setText(searched_cus_id);
        }
        if (requestCode == 2000 && resultCode == RESULT_OK) {
            mobile = data.getStringExtra("mobile_number");
            mobile_number_textView.setText(mobile);
        }

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
                photograph_image_btn.setImageBitmap(compressedImageBitmap);
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
                idproof_image_btn.setImageBitmap(compressedImageBitmap);
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
                addressproof_image_btn.setImageBitmap(compressedImageBitmap);
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
                signature_image_btn.setImageBitmap(compressedImageBitmap);
            } catch (Exception e) {

            }
        }
        if (requestCode == 104 && resultCode == RESULT_OK) {
            if (imageAdapter.getFingerBItmap() != null) {
                try {
                    byte[] array = data.getByteArrayExtra("biometric_byte_array");
                    is_biometric_submitted = "1";
                    biometric_image_btn.setImageResource(R.drawable.biometric_updated);
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
                    biometric_image_btn.setImageResource(R.drawable.biometric_updated);
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
                    biometric_image_btn.setImageResource(R.drawable.biometric_updated);
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

        super.onActivityResult(requestCode, resultCode, data);
    }


    private class getKYCDetails extends AsyncTask<String, String, String> {

        private String api_url_kyc_details = ip + "/getCustomerDetailsByCustID";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sweetAlertDialog.setTitleText("Loading..")
                    .setContentText("fetching details please wait")
                    .show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            if (searched_cus_id.equals("null")) {
                searched_cus_id = cus_id_edit_text.getText().toString();
                Log.e("CUSTID EDITTEXT", searched_cus_id);
            }
            hashMap.put("CUST_ID", searched_cus_id);
            try {
                JSONObject jsonResult = jsonParser.makeHttpRequest(api_url_kyc_details, "POST", hashMap);
                JSONObject customer_list = jsonResult.getJSONObject("customer_list");
                if (customer_list.has("data")) {
                    JSONObject data = customer_list.getJSONObject("data");
                    name = data.getString("firstname");
                    gender = data.getString("gender");
                    dob = data.getString("dob");
                    pan = data.getString("PanNo");
                    mobile = data.getString("mobileNo");
                    proof_photo = data.getBoolean("photographSubmitted");
                    proof_signature = data.getBoolean("signatureSubmitted");
                    proof_biometric = data.getBoolean("biometricSubmitted");
                    proof_id = data.getBoolean("identityProofSubmitted");
                    proof_address = data.getBoolean("addressProofSubmitted");
                    error = "ok";
                }
                if (customer_list.has("error")) {
                    msg = customer_list.getString("error");
                    error = "error";
                }

            } catch (JSONException e) {
                sweetAlertDialog.dismiss();
                e.printStackTrace();
            }
            return error;
        }

        @Override
        protected void onPostExecute(String result) {
            sweetAlertDialog.dismiss();
            if (result.equals("ok")) {
                details_linear.setVisibility(View.VISIBLE);
                acc_name_text_view.setText(name);
                acc_name_text_view.setTextColor(RED);
                dob_btn.setText(dob);
                dob_btn.setTextColor(RED);
                mobile_number_textView.setText(mobile);
                pan_no_edit_text.setText(pan);
                pan_no_edit_text.setTextColor(RED);

                if (proof_photo) {
                    photograph_image_btn.setImageResource(R.drawable.proof_id_not_updated);
                } else {
                    photograph_image_btn.setImageResource(R.drawable.proof_id_updated);
                }

                if (proof_signature) {
                    signature_image_btn.setImageResource(R.drawable.signature_not_updated);
                } else {
                    signature_image_btn.setImageResource(R.drawable.signature_updated);
                }

                if (proof_biometric) {
                    biometric_image_btn.setImageResource(R.drawable.biometric_not_updated);
                } else {
                    biometric_image_btn.setImageResource(R.drawable.biometric_updated);
                }

                if (proof_address) {
                    addressproof_image_btn.setImageResource(R.drawable.proof_address_updated);
                } else {
                    addressproof_image_btn.setImageResource(R.drawable.proof_address_not_updated);
                }
                if (proof_id) {
                    idproof_image_btn.setImageResource(R.drawable.proof_id_updated);
                } else {
                    idproof_image_btn.setImageResource(R.drawable.proof_id_not_updated);
                }
            }
            if (result.equals("error")) {
                searched_cus_id = "null";
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(msg)
                        .show();
            }
            super.onPostExecute(result);
        }
    }


    private class update_kyc extends AsyncTask<String, String, String> {
        private String api_url_uploadImage = ip + "/UpdateCustomerKYC";

        @Override
        public void onPreExecute() {
            alert = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            alert.setTitleText("Updating");
            alert.setContentText("Please wait..");
            alert.show();
            //encodedtextView.setText(encoded);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMapAmount = new HashMap<>();
            hashMapAmount.put("CUST_ID", searched_cus_id);
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
            hashMapAmount.put("BIOMETRIC_DATA", isSubmitted(imageAdapter.getBiometric_data_string()));
            try {
                Log.e("Here HashMap Amount", hashMapAmount.toString());
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url_uploadImage, "POST", hashMapAmount);
                Log.e("Result After Update: ", jsonObject.toString());
                JSONObject account = jsonObject.getJSONObject("account");
                if (account.has("msg")) {
                    msg = account.getString("msg");
                    error = "ok";
                }
                if (account.has("error")) {
                    msg = account.getString("error");
                    error = "error";
                }
            } catch (Exception e) {
                error = "error";
                e.printStackTrace();
            }

            return error;
        }

        @Override
        public void onPostExecute(String result) {
            alert.dismiss();
            if (result.equals("ok")) {
                imageAdapter.setPhotograph_image_string("null");
                imageAdapter.setBiometric_image_string("null");
                imageAdapter.setId_image_string("null");
                imageAdapter.setAddress_image_string("null");
                imageAdapter.setSignature_image_string("null");
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("KYC Updated of " + searched_cus_id)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                fm.popBackStack();
                                sweetAlertDialog.dismiss();
                            }
                        }).show();
            }
            if (result.equals("error")) {
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

    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        @SuppressLint("SetTextI18n")
        public void populateSetDate(int year, int month, int day) {
//            dob.setText(month+"/"+day+"/"+year);
            dob_btn_txt = (month + "-" + day + "-" + year).toString();

        }

    }
}
