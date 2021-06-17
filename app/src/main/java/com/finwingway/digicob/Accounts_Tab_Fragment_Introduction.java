package com.finwingway.digicob;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import static com.finwingway.digicob.login.arrayAccountType;
import static com.finwingway.digicob.login.arrayAccountTypeId;

/**
 * Created by Anvin on 12/26/2016.
 */
@SuppressLint("ValidFragment")
public class Accounts_Tab_Fragment_Introduction extends Fragment implements Validator.ValidationListener{
    ViewPager viewPager=Accounts_Sub.viewPager;
    public Accounts_Tab_Fragment_Introduction() {
    }
    boolean validation;
    @TextRule(order = 1, minLength = 3, message = "Enter atleast 3 characters.")
    @Regex(order = 1, pattern = "[A-Z ]+", message = "Should contain only Capital Letters")
    EditText name_edit_text;
    @TextRule(order = 2, minLength = 10, message = "Enter atleast 10 characters.")
    @Regex(order = 2, pattern = "[A-Z0-9 \n]+", message = "Should contain only Capital Letters")
    EditText address_edit_text;
    @TextRule(order = 3, minLength = 10, message = "Enter atleast 10 characters.")
    @Regex(order = 3, pattern = "[A-Z0-9 \n]+", message = "Should contain only Capital Letters")
    EditText reference_edit_text;
    @TextRule(order = 4, minLength = 10, maxLength = 12, message = "Enter valid mobile Number.")
    @Regex(order = 4, pattern = "[0-9]+", message = "Enter valid mobile Number.")
    EditText mobile_edit_text;
    @TextRule(order = 5, minLength = 10, maxLength = 12, message = "Enter valid mobile Number.")
    @Regex(order = 5, pattern = "[0-9]+", message = "Enter valid mobile Number.")
    EditText contact_edit_text;
    Spinner introducer_type;
    Button next,prev;
    public static String introName="null", introtype="null",introaddress="null",introreference="null",intromobile="null",
            introcontact="null";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accountsub_introduction_layout, container, false);
        final Validator validator=new Validator(this);
        validator.setValidationListener(this);
        name_edit_text=(EditText)rootView.findViewById(R.id.intro_name_edit_text);
        navKey(name_edit_text);
        address_edit_text=(EditText)rootView.findViewById(R.id.intro_adress_edit_text);
        navKey(address_edit_text);
        reference_edit_text=(EditText)rootView.findViewById(R.id.intro_reference);
        navKey(reference_edit_text);
        mobile_edit_text=(EditText)rootView.findViewById(R.id.intro_mobile_edit_text);
        navKey(mobile_edit_text);
        contact_edit_text=(EditText)rootView.findViewById(R.id.intro_contact_numer_edit_text);
        navKey(contact_edit_text);
        introducer_type=(Spinner)rootView.findViewById(R.id.intro_type_Spinner);
        ArrayAdapter typeAdapter=new ArrayAdapter<String>(getContext(),R.layout.spinner_list_item,arrayAccountType);
        introducer_type.setAdapter(typeAdapter);
        introducer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos=parent.getSelectedItemPosition();
                introtype =arrayAccountTypeId[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        next=(Button)rootView.findViewById(R.id.intro_nextbtn);
        prev=(Button)rootView.findViewById(R.id.intro_prevbtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
                introName =name_edit_text.getText().toString();
                introaddress=address_edit_text.getText().toString();
                introreference=reference_edit_text.getText().toString();
                intromobile=mobile_edit_text.getText().toString();
                introcontact=contact_edit_text.getText().toString();
                if(validation){
                    viewPager.setCurrentItem(2);
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



    @Override
    public void onValidationSucceeded() {
        validation=true;
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        validation=false;
        if (failedView instanceof EditText) {
            Toast.makeText(getContext(), ((EditText) failedView).getHint()+" "+message, Toast.LENGTH_SHORT).show();
            ((EditText) failedView).setError(message);
        }  else {

        }
    }
    public void navKey(EditText editText){
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        return true;
                    }
                }
                return false;
            }
        });

    }
}