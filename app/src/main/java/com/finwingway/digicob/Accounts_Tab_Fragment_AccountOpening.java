package com.finwingway.digicob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.finwingway.digicob.support.ConnectionDetector;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static android.graphics.Color.RED;
import static android.view.View.GONE;
import static com.finwingway.digicob.Accounts_Tab_Fragment_KYC.*;
import static com.finwingway.digicob.login.arrayAccountCategory;
import static com.finwingway.digicob.login.arrayAccountCategoryId;
import static com.finwingway.digicob.login.arrayAccountType;
import static com.finwingway.digicob.login.arrayAccountTypeId;
import static com.finwingway.digicob.login.arrayAccountrelation;
import static com.finwingway.digicob.login.arrayAccountrelationId;
import static com.finwingway.digicob.login.arrayConstitution;
import static com.finwingway.digicob.login.arrayConstitutionId;
import static com.finwingway.digicob.login.arrayModeOfOpeartion;
import static com.finwingway.digicob.login.arrayModeOfOpeartionId;

/**
 * Created by Anvin on 12/26/2016.
 */
@SuppressLint("ValidFragment")
public class Accounts_Tab_Fragment_AccountOpening extends Fragment implements DatePickerDialog.OnDateSetListener {
    public Accounts_Tab_Fragment_AccountOpening() {
    }

    ViewPager viewPager = Accounts_Sub.viewPager;
    Button minor_birthdateBtn, CreateAccount_Btn;
    FragmentActivity myContext;
    public static String ip = login.ip_global;
    DatePickerDialog dpd;
    SweetAlertDialog alert;
    JSONParser jsonParser = new JSONParser();
    Spinner constitution_spinner, productCode_spinner, modeOfOPeration_spinner, category_spinner, relationship_spinner;
    String refId;
    EditText nominee_name_edit_text, nominee_address_edit_text, guardian_name_edit_text, guardian_address_edit_text,
            agent_id_edit_text, initial_deposit_edit_text;
    public String product_code, constitution, mode_of_operation, category, nominee_name = "null",
            nominee_address = "null", relationship_with_acc_holder, is_minor = "null", minor_date_of_birth = "null",
            guardian_name = "null", guardian_address = "null", guardian_relationship = "null", agent_id, initial_deposit, msg = "null";

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accountsub_account_opening_layout, container, false);
        final FragmentManager fragManager = myContext.getFragmentManager();

        constitution_spinner = (Spinner) rootView.findViewById(R.id.ac_constitutionSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_list_item, arrayConstitution);
        adapter.setDropDownViewResource(R.layout.spinner_list_item);
        constitution_spinner.setAdapter(adapter);
        constitution_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                constitution = arrayConstitutionId[position];
                //Toast.makeText(myContext, constitution, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        productCode_spinner = (Spinner) rootView.findViewById(R.id.ac_productCodeSpinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_list_item, arrayAccountType);
        adapter.setDropDownViewResource(R.layout.spinner_list_item);
        productCode_spinner.setAdapter(adapter1);
        productCode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product_code = arrayAccountTypeId[position];
                //Toast.makeText(myContext, product_code, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        modeOfOPeration_spinner = (Spinner) rootView.findViewById(R.id.ac_modeofoperationSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_list_item, arrayModeOfOpeartion);
        adapter.setDropDownViewResource(R.layout.spinner_list_item);
        modeOfOPeration_spinner.setAdapter(adapter2);
        modeOfOPeration_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode_of_operation = arrayModeOfOpeartionId[position];
                //Toast.makeText(myContext, mode_of_operation, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category_spinner = (Spinner) rootView.findViewById(R.id.ac_categorySpinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_list_item, arrayAccountCategory);
        adapter.setDropDownViewResource(R.layout.spinner_list_item);
        category_spinner.setAdapter(adapter3);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = arrayAccountCategoryId[position];
                //Toast.makeText(myContext, category, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        relationship_spinner = (Spinner) rootView.findViewById(R.id.ac_relationShipSpinner);
        ArrayAdapter relationshipAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayAccountrelation);
        relationship_spinner.setAdapter(relationshipAdapter);
        relationship_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relationship_with_acc_holder = arrayAccountrelationId[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button prev = (Button) rootView.findViewById(R.id.open_prevbtn);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

            }
        });
        final LinearLayout minor = (LinearLayout) rootView.findViewById(R.id.minor_address_linear);
        minor.setVisibility(GONE);
        final RadioGroup minorRadioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup_minor);
        minorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                View radioButton = minorRadioGroup.findViewById(checkedId);
                int index = minorRadioGroup.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 0: // first button
                        is_minor = "YES";
                        minor.setVisibility(View.VISIBLE);
                        break;
                    case 1: // secondbutton
                        is_minor = "NO";
                        minor.setVisibility(GONE);
                        break;
                }
            }
        });
        minor_birthdateBtn = (Button) rootView.findViewById(R.id.birthdatebtn_minor);
        minor_birthdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                dpd = DatePickerDialog.newInstance(
                        Accounts_Tab_Fragment_AccountOpening.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setTitle("SELECT FROM DATE");
                dpd.setThemeDark(true);
                dpd.vibrate(true);
                dpd.setVersion(DatePickerDialog.Version.VERSION_1);
                dpd.show(fragManager, "Datepickerdialog");
            }
        });


        initial_deposit_edit_text = (EditText) rootView.findViewById(R.id.initial_deposit_edit_text_account_opening);

        nominee_name_edit_text = (EditText) rootView.findViewById(R.id.nominee_name_editText);

        nominee_address_edit_text = (EditText) rootView.findViewById(R.id.nominee_address_editText);


        CreateAccount_Btn = (Button) rootView.findViewById(R.id.open_craeteAccountbtn);
        CreateAccount_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connDetector = new ConnectionDetector(getContext());
                if (connDetector.isnetAvailable()) {
                    initial_deposit = initial_deposit_edit_text.getText().toString();
                    nominee_name = nominee_name_edit_text.getText().toString();
                    nominee_address = nominee_address_edit_text.getText().toString();
                    new create_account().execute();
                } else {
                    Toast.makeText(getContext(), "You Are NOT CONNECTED TO INTERNET!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String birthdate = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
        minor_date_of_birth = birthdate;
        minor_birthdateBtn.setText(birthdate);
        minor_birthdateBtn.setTextColor(RED);
        minor_birthdateBtn.setTextSize(18);
    }


    /*-------------------------------------------CreateAccountAsyncTask--------------------------------------------------*/

    class create_account extends AsyncTask<String, String, String> {
        private String api_url_uploadImage = ip + "/newCustomer";

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
            hashMapAmount.put("TITLE", Accounts_Tab_Fragment_Customer.title);
            hashMapAmount.put("FIRSTNAME", Accounts_Tab_Fragment_Customer.first_name);
            hashMapAmount.put("MIDDLENAME", Accounts_Tab_Fragment_Customer.middle_name);
            hashMapAmount.put("LASTNAME", Accounts_Tab_Fragment_Customer.last_name);
            hashMapAmount.put("GENDER", Accounts_Tab_Fragment_Customer.Gender);
            hashMapAmount.put("MOTHERS_NAME", Accounts_Tab_Fragment_Customer.mothersName);
            hashMapAmount.put("DOB", Accounts_Tab_Fragment_Customer.birthdate);
            hashMapAmount.put("AADHAR", Accounts_Tab_Fragment_Customer.adharNo);
            hashMapAmount.put("PANNO", Accounts_Tab_Fragment_Customer.panNo);
            hashMapAmount.put("OCCUPATION", Accounts_Tab_Fragment_Customer.occupation);
            hashMapAmount.put("NATIONALITY", Accounts_Tab_Fragment_Customer.nationality);
            hashMapAmount.put("RELIGION", Accounts_Tab_Fragment_Customer.religion);
            hashMapAmount.put("CASTE", Accounts_Tab_Fragment_Customer.caste);
            hashMapAmount.put("MARITAL_STATUS", Accounts_Tab_Fragment_Customer.maritalStatus);
            hashMapAmount.put("EMAIL_ID", Accounts_Tab_Fragment_Customer.emailId);
            hashMapAmount.put("MOBILE_NO", Accounts_Tab_Fragment_Customer.mobileNumber);
            hashMapAmount.put("CONTACT_NO", Accounts_Tab_Fragment_Customer.contactNumberHome);
            hashMapAmount.put("CORRES_CAREOF", Accounts_Tab_Fragment_Customer.perguardian_name);
            hashMapAmount.put("CORRES_BUILDING", Accounts_Tab_Fragment_Customer.perflat_no);
            hashMapAmount.put("CORRES_LANDMARK", Accounts_Tab_Fragment_Customer.perlandmark);
            hashMapAmount.put("CORRES_STREET", Accounts_Tab_Fragment_Customer.perlocation);
            hashMapAmount.put("CORRES_COUNTRY", Accounts_Tab_Fragment_Customer.percountry);
            hashMapAmount.put("CORRES_STATE", Accounts_Tab_Fragment_Customer.perstate);
            hashMapAmount.put("CORRES_DISTRICT", Accounts_Tab_Fragment_Customer.perdistrict);
            hashMapAmount.put("CORRES_CITY", Accounts_Tab_Fragment_Customer.percity);
            hashMapAmount.put("CORRES_PIN", Accounts_Tab_Fragment_Customer.perpincode);
            hashMapAmount.put("PERM_SAME_AS_PERM", Accounts_Tab_Fragment_Customer.permananetAddressFlag);
            hashMapAmount.put("PERM_CAREOF", Accounts_Tab_Fragment_Customer.guardian_name);
            hashMapAmount.put("PERM_BUILDING", Accounts_Tab_Fragment_Customer.flat_no);
            hashMapAmount.put("PERM_LANDMARK", Accounts_Tab_Fragment_Customer.landmark);
            hashMapAmount.put("PERM_STREET", Accounts_Tab_Fragment_Customer.location);
            hashMapAmount.put("PERM_COUNTRY", Accounts_Tab_Fragment_Customer.country);
            hashMapAmount.put("PERM_STATE", Accounts_Tab_Fragment_Customer.state);
            hashMapAmount.put("PERM_DISTRICT", Accounts_Tab_Fragment_Customer.district);
            hashMapAmount.put("PERM_CITY", Accounts_Tab_Fragment_Customer.city);
            hashMapAmount.put("PERM_PIN", Accounts_Tab_Fragment_Customer.pincode);
            hashMapAmount.put("IS_ID_PROOF_SUBMITTED", Accounts_Tab_Fragment_KYC.is_id_proof_submitted);
            hashMapAmount.put("IDENTITY_PROOF_DATA", isSubmitted(imageAdapter.getId_image_string()));
            hashMapAmount.put("IS_DOB_SUBMITTED", "0");
            hashMapAmount.put("IS_ADDR_PROOF_SUBMITTED", Accounts_Tab_Fragment_KYC.is_address_proof_submitted);
            hashMapAmount.put("ADDRESS_PROOF_DATA", isSubmitted(imageAdapter.getAddress_image_string()));
            hashMapAmount.put("PHOTOGRAPH_SUBMITTED", Accounts_Tab_Fragment_KYC.is_photograph_submitted);
            hashMapAmount.put("PHOTOGRAPH_DATA", isSubmitted(imageAdapter.getPhotograph_image_string()));
            hashMapAmount.put("SIGNATURE_SUBMITTED", Accounts_Tab_Fragment_KYC.is_signature_submitted);
            hashMapAmount.put("SIGNATURE_DATA", isSubmitted(imageAdapter.getSignature_image_string()));
            hashMapAmount.put("BIOMETRIC_SUBMITTED", is_biometric_submitted);
            hashMapAmount.put("BIOMETRIC_DATA", isSubmitted(imageAdapter.getBiometric_image_string()));
            hashMapAmount.put("BRANCH_CODE", login.branch_id);
            hashMapAmount.put("PRODUCT_CODE", product_code);
            hashMapAmount.put("CONSTITUTION", constitution);
            hashMapAmount.put("MODE_OF_OPERATION", mode_of_operation);
            hashMapAmount.put("CATEGORY", category);
            hashMapAmount.put("EMP_ID", login.agent_code);
            hashMapAmount.put("ACC_OP_BAL", initial_deposit);
            hashMapAmount.put("AGENT", login.agent_code);
            hashMapAmount.put("NOM_NAME", nominee_name);
            hashMapAmount.put("NOM_ADDR", nominee_address);
            hashMapAmount.put("NOM_REL", relationship_with_acc_holder);
            hashMapAmount.put("INTRO_NAME", Accounts_Tab_Fragment_Introduction.introName);
            hashMapAmount.put("INTRO_TYPE", Accounts_Tab_Fragment_Introduction.introtype);
            hashMapAmount.put("INTRO_ADDR", Accounts_Tab_Fragment_Introduction.introaddress);
            hashMapAmount.put("INTRO_REFDETAIL", Accounts_Tab_Fragment_Introduction.introreference);
            hashMapAmount.put("INTRO_MOBILE_NO", Accounts_Tab_Fragment_Introduction.intromobile);
            hashMapAmount.put("INTRO_CONTACT_NO", Accounts_Tab_Fragment_Introduction.introcontact);
            hashMapAmount.put("BRANCHNAME", login.branch_name);
            hashMapAmount.put("CREATED_BY", "null");
            hashMapAmount.put("MACID", "null");
            try {
                Log.e("Here HashMap Amount", hashMapAmount.toString());
                JSONObject jsonObject = jsonParser.makeHttpRequest(api_url_uploadImage, "POST", hashMapAmount);
                String JsonDataString = jsonObject.toString();
                Log.e("string", JsonDataString);
                JSONObject reader = new JSONObject(JsonDataString);
                if (reader.has("data")) {
                    JSONObject data = reader.getJSONObject("data");
                    refId = data.getString("REF_ID");
                    Log.e("refID", refId);

                    return "OK";
                } else {
                    msg = reader.getString("msg");
                    return "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(String result) {
            alert.dismiss();
            if (result == "OK") {
                Toast.makeText(getContext(), refId, Toast.LENGTH_SHORT).show();

                Toast.makeText(getActivity(), msg,
                        Toast.LENGTH_LONG).show();
                imageAdapter.setPhotograph_image_string("null");
                imageAdapter.setBiometric_image_string("null");
                imageAdapter.setId_image_string("null");
                imageAdapter.setAddress_image_string("null");
                imageAdapter.setSignature_image_string("null");
                imageAdapter.setFingerBitmap(null);
                is_photograph_submitted = "0";
                is_id_proof_submitted = "0";
                is_address_proof_submitted = "0";
                is_signature_submitted = "0";
                is_biometric_submitted = "0";

            }
            if (result == "error") {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String isSubmitted(String data) {
        return "data:image/jpeg;base64," + data;
    }

}