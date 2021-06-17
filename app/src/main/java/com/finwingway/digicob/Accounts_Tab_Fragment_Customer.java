package com.finwingway.digicob;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.graphics.Color.RED;
import static com.finwingway.digicob.login.arrayId;
import static com.finwingway.digicob.login.arrayMaritalStatus;
import static com.finwingway.digicob.login.arrayMaritalStatusId;
import static com.finwingway.digicob.login.arrayOccupation;
import static com.finwingway.digicob.login.arrayOccupationId;
import static com.finwingway.digicob.login.arrayTitle;
import static com.finwingway.digicob.login.ip_global;

/**
 * Created by Anvin on 12/26/2016.
 */
@SuppressLint("ValidFragment")
public class Accounts_Tab_Fragment_Customer extends Fragment implements
        DatePickerDialog.OnDateSetListener, Validator.ValidationListener {
    public Accounts_Tab_Fragment_Customer() {
    }

    TextView date, Customer_branch;
    ViewPager viewPager = Accounts_Sub.viewPager;
    Button next;
    DatePickerDialog dpd;
    Button birthdateBtn;
    Validator validator;
    boolean validation = false;
    boolean bperdistrict = false, bdistrict = false;
    boolean bstateerror = false, perbstateerror = false;
    JSONParser jsonParser = new JSONParser();
    private String api_get_country = ip_global + "/getCountryList";
    private String api_get_state = ip_global + "/getStateList";
    private String api_get_district = ip_global + "/getDistrictList";

    @TextRule(order = 1, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 1, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText first_nameEditText;
    /*@TextRule(order = 2, minLength = 1, message = "Enter atleast 3 characters.")
    @Regex(order = 2, pattern = "[A-Z]+", message = "Should contain only Capital Letters")*/
    EditText middle_nameEditText;/*
    @TextRule(order = 3, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 3, pattern = "[A-Z]+", message = "Should contain only Capital Letters")*/
    EditText last_nameEditText;
    @TextRule(order = 4, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 4, pattern = "[A-Z ]+", message = "Should contain only Capital Letters")
    EditText mothersNameEditText;
    @TextRule(order = 5, minLength = 12, message = "Enter Correct Details.")
    @Regex(order = 5, pattern = "[0-9]+", message = "Should contain only Numbers")
    EditText adharNoEditText;
    EditText panNoEditText;
    /*@TextRule(order = 7, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 7, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText NationalityEditText;*/
    @TextRule(order = 8, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 8, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText religionEditText;
    @TextRule(order = 9, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 9, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText casteEditText;

    @TextRule(order = 10, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 10, pattern = "[A-Z ]+", message = "Should contain only Capital Letters")
    EditText guardianEditText;
    @TextRule(order = 11, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 11, pattern = "[A-Z0-9 ]+", message = "Should contain only Capital Letters")
    EditText flatnoEditText;
    @TextRule(order = 12, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 12, pattern = "[A-Z0-9 ]+", message = "Should contain only Capital Letters")
    EditText landmarkEditText;
    @TextRule(order = 13, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 13, pattern = "[A-Z0-9 ]+", message = "Should contain only Capital Letters")
    EditText locationEditText;
    @TextRule(order = 14, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 14, pattern = "[A-Z ]+", message = "Should contain only Capital Letters")
    EditText streetEditText;
    /*@TextRule(order = 15, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 15, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText countryEditText;
    @TextRule(order = 16, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 16, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText stateEditText;
    @TextRule(order = 17, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 17, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText districtEditText;*/
    @TextRule(order = 18, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 18, pattern = "[A-Z ]+", message = "Should contain only Capital Letters")
    EditText cityEditText;
    @TextRule(order = 19, minLength = 6, message = "Enter Valid Pincode.")
    @Regex(order = 19, pattern = "[0-9]+", message = "Enter Valid Pincode")
    EditText pincodeEditText;

    @TextRule(order = 20, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 20, pattern = "[A-Z ]+", message = "Should contain only Capital Letters")
    EditText perguardianEditText;
    @TextRule(order = 21, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 21, pattern = "[A-Z0-9 ]+", message = "Should contain only Capital Letters")
    EditText perflatnoEditText;
    @TextRule(order = 22, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 22, pattern = "[A-Z0-9 ]+", message = "Should contain only Capital Letters")
    EditText perlandmarkEditText;
    /*@TextRule(order = 23, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 23, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText perlocationEditText;*/
    @TextRule(order = 24, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 24, pattern = "[A-Z0-9 ]+", message = "Should contain only Capital Letters")
    EditText perstreetEditText;
    /*@TextRule(order = 25, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 25, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText percountryEditText;
    @TextRule(order = 26, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 26, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText perstateEditText;
    @TextRule(order = 27, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 27, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText perdistrictEditText;*/
    @TextRule(order = 28, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 28, pattern = "[A-Z]+", message = "Should contain only Capital Letters")
    EditText percityEditText;
    @TextRule(order = 29, minLength = 6, message = "Enter Valid Pincode.")
    @Regex(order = 29, pattern = "[0-9]+", message = "Enter Valid Pincode")
    EditText perpincodeEditText;

    @TextRule(order = 30, minLength = 10, message = "Enter Valid Mobile Number.")
    @Regex(order = 30, pattern = "[0-9]+", message = "Enter Valid Mobile Number")
    EditText mobileNumberEditText;
    @TextRule(order = 31, minLength = 10, message = "Enter Valid Mobile Number.")
    @Regex(order = 31, pattern = "[0-9]+", message = "Enter Valid Mobile Number")
    EditText contactNumberEditText;
    @TextRule(order = 32, minLength = 10, message = "Enter Valid Mobile Number.")
    @Regex(order = 32, pattern = "[0-9]+", message = "Enter Valid Mobile Number")
    EditText contactNumberHomeEditText;
    @TextRule(order = 33, minLength = 3, message = "Enter Valid Email Address")
    @Email(order = 33, message = "Enter valid Email Address")
    EditText emailidEditText;
    EditText permananetAddressFlagCheckBox;
    public static String[] arrayCountryName, arrayCountryId, arrayStateName, arrayStateId, arrayDistrictName, arrayDistrictId;
    Spinner nationality_spinner, country_spinner, state_spinner, district_spinner,
            curre_country_spinner, curre_state_spinner, curre_district_spinner;

    public static String title = "null", first_name = "null", middle_name = "null", last_name = "null", Gender = "null",
            mothersName = "null", birthdate = "null", adharNo = "null", panNo = "null", religion = "null",
            caste = "null", maritalStatus, state = "null", district = "null", country = "null", nationality = "null",
            pincode = "null", perstate = "null", perdistrict = "null", percountry = "null", perpincode = "null",
            mobileNumber = "null", contactNumberOffice = "null", contactNumberHome = "null", emailId = "null",
            permananetAddressFlag = "null", guardian_name = "null", flat_no = "null", landmark = "null", location = "null", city = "null",
            perguardian_name = "null", perflat_no = "null", perlandmark = "null", perlocation = "null", percity = "null", occupation = "null";

    public RadioGroup gender;
    public RadioButton radioButton;
    private FragmentActivity myContext;
    Spinner titleSpinner, marital_status, occupation_spinner;
    public static int gender_id;
    String msg, error;

    CheckBox permanent;
    boolean checked;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        final View rootView = inflater.inflate(R.layout.fragment_accountsub_customer_layout, container, false);
        validator = new Validator(this);
        validator.setValidationListener(this);
        final FragmentManager fragManager = myContext.getFragmentManager();
        new getCountry().execute();
        date = (TextView) rootView.findViewById(R.id.applicants_date);
        Customer_branch = (TextView) rootView.findViewById(R.id.Customer_branch);
        Customer_branch.setText(login.branch_name);
        next = (Button) rootView.findViewById(R.id.cs_nextbtn);
        first_nameEditText = (EditText) rootView.findViewById(R.id.cus_first_nameEditText);
        navKey(first_nameEditText);
        middle_nameEditText = (EditText) rootView.findViewById(R.id.cus_middle_nameEditText);
        navKey(middle_nameEditText);
        last_nameEditText = (EditText) rootView.findViewById(R.id.cus_last_nameEditText);
        navKey(last_nameEditText);
        gender = (RadioGroup) rootView.findViewById(R.id.cus_radioGroupGender);
        mothersNameEditText = (EditText) rootView.findViewById(R.id.cus_mothersnameEditText);
        navKey(mothersNameEditText);
        birthdateBtn = (Button) rootView.findViewById(R.id.cus_birthdateEditText);
        birthdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                dpd = DatePickerDialog.newInstance(
                        Accounts_Tab_Fragment_Customer.this,
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
        adharNoEditText = (EditText) rootView.findViewById(R.id.cus_adharEditText);
        navKey(adharNoEditText);
        panNoEditText = (EditText) rootView.findViewById(R.id.cus_panEditText);
        navKey(panNoEditText);
        religionEditText = (EditText) rootView.findViewById(R.id.cus_religionEditText);
        navKey(religionEditText);
        casteEditText = (EditText) rootView.findViewById(R.id.cus_casteEditText);
        navKey(casteEditText);

        guardianEditText = (EditText) rootView.findViewById(R.id.cus_comm_guardianEditText);
        navKey(guardianEditText);
        flatnoEditText = (EditText) rootView.findViewById(R.id.cus_comm_flatNameEditText);
        navKey(flatnoEditText);
        locationEditText = (EditText) rootView.findViewById(R.id.cus_comm_locationEditText);
        navKey(locationEditText);
        landmarkEditText = (EditText) rootView.findViewById(R.id.cus_comm_landmarkEditText);
        navKey(landmarkEditText);
        pincodeEditText = (EditText) rootView.findViewById(R.id.cus_comm_pincodeEditText);
        cityEditText = (EditText) rootView.findViewById(R.id.cus_comm_cityEditText);

        perguardianEditText = (EditText) rootView.findViewById(R.id.cus_per_guardianEditText);
        navKey(perguardianEditText);
        perflatnoEditText = (EditText) rootView.findViewById(R.id.cus_per_flatNameEditText);
        navKey(perflatnoEditText);
        perlandmarkEditText = (EditText) rootView.findViewById(R.id.cus_per_landmarkEditText);
        navKey(perlandmarkEditText);
        perstreetEditText = (EditText) rootView.findViewById(R.id.cus_per_locationEditText);
        navKey(perstreetEditText);
        percityEditText = (EditText) rootView.findViewById(R.id.cus_per_cityEditText);
        navKey(percityEditText);
        perpincodeEditText = (EditText) rootView.findViewById(R.id.cus_per_pincodeEditText);
        navKey(perpincodeEditText);

        mobileNumberEditText = (EditText) rootView.findViewById(R.id.cus_mobileNumberEditText);
        contactNumberEditText = (EditText) rootView.findViewById(R.id.cus_contactOfficeEditText);
        contactNumberHomeEditText = (EditText) rootView.findViewById(R.id.cus_contactHomeEditText);
        emailidEditText = (EditText) rootView.findViewById(R.id.cus_emailEditText);
        navKey(emailidEditText);
        titleSpinner = (Spinner) rootView.findViewById(R.id.cus_spinner_title);

        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),
                R.layout.list_item, arrayTitle);
        titleSpinner.setAdapter(adapter);
        titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                ////Toast.makeText(getContext(), parent.getItemAtPosition(position).toString(), //Toast.LENGTH_SHORT).show();
                int pos = parent.getSelectedItemPosition();
                Log.v("item", (String) parent.getItemAtPosition(position));
                String value = arrayId[pos];
                title = value;
                //Toast.makeText(getContext(),value,//Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        marital_status = (Spinner) rootView.findViewById(R.id.cus_maritalDropDown);
        final ArrayAdapter maritalAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.list_item, arrayMaritalStatus);
        marital_status.setAdapter(maritalAdapter);
        marital_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = parent.getSelectedItemPosition();
                String item = arrayMaritalStatusId[pos];
                //Toast.makeText(myContext, item, //Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        permanent = (CheckBox) rootView.findViewById(R.id.cus_checkBoxpermanentadress);
        final LinearLayout permamnentLayout = (LinearLayout) rootView.findViewById(R.id.linear_communicationAddress);
        //office=(CheckBox)rootView.findViewById(R.id.cus_checkBoxofficeadress);
        //final LinearLayout officeLayout=(LinearLayout)rootView.findViewById(R.id.linear_officeAddress);


        permanent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    checked = true;
                    curre_country_spinner.setEnabled(false);
                    curre_state_spinner.setEnabled(false);
                    curre_district_spinner.setEnabled(false);
                    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        permamnentLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_text_style));
                    }/*
                    else if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                        permamnentLayout.setBackground(getResources().getDrawable(R.drawable.rectangle));
                    }*/ else {
                        permamnentLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edit_text_style));
                    }
                    permananetAddressFlag = "1";
                    for (int i = 0; i < permamnentLayout.getChildCount(); i++) {
                        View child = permamnentLayout.getChildAt(i);
                        child.setEnabled(false);
                    }
                } else {
                    checked = false;
                    curre_country_spinner.setEnabled(true);
                    curre_state_spinner.setEnabled(true);
                    curre_district_spinner.setEnabled(true);
                    permananetAddressFlag = "0";
                    for (int i = 0; i < permamnentLayout.getChildCount(); i++) {
                        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            permamnentLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle));
                        } else {
                            permamnentLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangle));
                        }
                        View child = permamnentLayout.getChildAt(i);
                        child.setEnabled(true);
                    }
                }
            }
        });
        occupation_spinner = (Spinner) rootView.findViewById(R.id.cus_occupationSpinner);
        ArrayAdapter occupationAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayOccupation);
        occupation_spinner.setAdapter(occupationAdapter);
        occupation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occupation = arrayOccupationId[position];
                //Toast.makeText(myContext, occupation, //Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    for (int i = 0; i < officeLayout.getChildCount(); i++) {
                        View child = officeLayout.getChildAt(i);
                        child.setEnabled(false);
                    }
                }
                else{
                    for (int i = 0; i < officeLayout.getChildCount(); i++) {
                        View child = officeLayout.getChildAt(i);
                        child.setEnabled(true);
                    }
                }
            }
        });*/
        nationality_spinner = (Spinner) rootView.findViewById(R.id.cus_nationality_spinner);
        country_spinner = (Spinner) rootView.findViewById(R.id.cus_country_spinner);
        state_spinner = (Spinner) rootView.findViewById(R.id.cus_state_spinner);
        district_spinner = (Spinner) rootView.findViewById(R.id.cus_district_spinner);

        curre_country_spinner = (Spinner) rootView.findViewById(R.id.cus_curre_country_spinner);
        curre_state_spinner = (Spinner) rootView.findViewById(R.id.cus_curre_state_spinner);
        curre_district_spinner = (Spinner) rootView.findViewById(R.id.cus_curre_district_spinner);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //title = (String) titleSpinner.getSelectedItem();

                int selectedID = gender.getCheckedRadioButtonId();
                radioButton = (RadioButton) rootView.findViewById(selectedID);
                Gender = radioButton.getText().toString();
                if (Gender.equals("Male")) {
                    gender_id = 13;
                }
                if (Gender.equals("Female")) {
                    gender_id = 14;
                }
                if (Gender.equals("Others")) {
                    gender_id = 15;
                }
                //Toast.makeText(myContext, String.valueOf(gender_id), //Toast.LENGTH_SHORT).show();
                first_name = first_nameEditText.getText().toString();
                middle_name = middle_nameEditText.getText().toString();
                last_name = last_nameEditText.getText().toString();
                mothersName = mothersNameEditText.getText().toString();
                adharNo = adharNoEditText.getText().toString();
                panNo = panNoEditText.getText().toString();
                religion = religionEditText.getText().toString();
                caste = casteEditText.getText().toString();
                maritalStatus = (String) marital_status.getSelectedItem();

                guardian_name = guardianEditText.getText().toString();
                flat_no = flatnoEditText.getText().toString();
                landmark = guardianEditText.getText().toString();
                location = locationEditText.getText().toString();
                city = cityEditText.getText().toString();
                pincode = pincodeEditText.getText().toString();


                if (!checked) {
                    perguardian_name = perguardianEditText.getText().toString();
                    perflat_no = perflatnoEditText.getText().toString();
                    perlandmark = perlandmarkEditText.getText().toString();
                    perlocation = perstreetEditText.getText().toString();
                    percity = percityEditText.getText().toString();
                    perpincode = perpincodeEditText.getText().toString();
                }
                if (checked) {
                    perguardian_name = "null";
                    perflat_no = "null";
                    perlandmark = "null";
                    perlocation = "null";
                    percountry = "null";
                    perstate = "null";
                    perdistrict = "null";
                    percity = "null";
                    perpincode = "null";
                }


                mobileNumber = mobileNumberEditText.getText().toString();
                contactNumberHome = contactNumberHomeEditText.getText().toString();
                contactNumberOffice = contactNumberEditText.getText().toString();
                emailId = emailidEditText.getText().toString();
                validator.validate();
                if (validation) {
                    if (birthdate != "null") {
                        if (bdistrict && !bstateerror) {
                            if (!checked && bperdistrict) {
                                if (!perbstateerror) {
                                    viewPager.setCurrentItem(1, true);
                                } else {
                                    Toast.makeText(myContext, "Please select Nation/State/District", Toast.LENGTH_SHORT).show();
                                }
                            } else if (checked) {
                                viewPager.setCurrentItem(1, true);
                            } else {
                                Toast.makeText(myContext, "Please select Nation/State/District", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(myContext, "Please select Nation/State/District", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(myContext, "Please Select Birth Date", Toast.LENGTH_SHORT).show();
                    }
                }
                viewPager.setCurrentItem(1, true);
            }

        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String CurrentDate = df.format(c.getTime());
        date.setText(CurrentDate);
        return rootView;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        birthdate = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
        birthdateBtn.setText(birthdate);
        birthdateBtn.setTextColor(RED);
        birthdateBtn.setTextSize(18);
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
            Toast.makeText(myContext, ((EditText) failedView).getHint() + " " + message, Toast.LENGTH_SHORT).show();
            ((EditText) failedView).setError(message);
        } else {

        }
    }

    public void navKey(EditText editText) {
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        last_nameEditText.setNextFocusDownId(R.id.cus_mothersnameEditText);
                        return true;
                    }
                }
                return false;
            }
        });

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayAdapter countryAdapter = null;
            try {
                countryAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayCountryName);
                country_spinner.setAdapter(countryAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    country = arrayCountryId[position];
                    //Toast.makeText(myContext, country, //Toast.LENGTH_SHORT).show();
                    new getState().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            nationality_spinner.setAdapter(countryAdapter);
            nationality_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    nationality = arrayCountryId[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            curre_country_spinner.setAdapter(countryAdapter);
            curre_country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    percountry = arrayCountryId[position];
                    new getCurreState().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
            hashmap.put("COUNTRY_ID", country);
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
                msg = "error";
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("ok")) {
                bstateerror = false;
                ArrayAdapter stateAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayStateName);
                state_spinner.setAdapter(stateAdapter);
                state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        state = arrayStateId[position];
                        if (!bstateerror) {
                            new getDistrict().execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            if (s.equals("error")) {
                String[] nodata = {"---No Data---"};
                bstateerror = true;
                bdistrict = false;
                try {
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
            hashmap.put("STATE_ID", state);
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
                ArrayAdapter districtAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayDistrictName);
                district_spinner.setAdapter(districtAdapter);
                district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        district = arrayDistrictId[position];
                        Toast.makeText(myContext, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        bdistrict = true;
                        //Toast.makeText(myContext, state, //Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            if (s.equals("error")) {
                String[] nodata = {"---No Data---"};
                try {
                    //ArrayAdapter stateAdapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_list_item,nodata);
                    //district_spinner.setAdapter(stateAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class getCurreState extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("COUNTRY_ID", percountry);
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
                perbstateerror = false;
                ArrayAdapter stateAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayStateName);
                curre_state_spinner.setAdapter(stateAdapter);
                curre_state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        perstate = arrayStateId[position];
                        if (!perbstateerror) {
                            new getCurreDistrict().execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            if (s.equals("error")) {
                String[] nodata = {"---No Data---"};
                ArrayAdapter stateAdapter1 = null;
                perbstateerror = true;
                bperdistrict = false;

                try {
                    stateAdapter1 = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, nodata);
                    curre_state_spinner.setAdapter(stateAdapter1);
                    curre_district_spinner.setAdapter(stateAdapter1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class getCurreDistrict extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("STATE_ID", perstate);
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
                ArrayAdapter stateAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_list_item, arrayDistrictName);
                curre_district_spinner.setAdapter(stateAdapter);
                curre_district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        perdistrict = arrayDistrictId[position];
                        bperdistrict = true;
                        //Toast.makeText(myContext, state, //Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            if (s.equals("error")) {
                String[] nodata = {"---No Data---"};
                try {
                    //ArrayAdapter stateAdapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_list_item,nodata);
                    //curre_district_spinner.setAdapter(stateAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

