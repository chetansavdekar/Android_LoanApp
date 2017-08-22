package com.example.amol.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.amol.loanquote.APIResponseListener;
import com.example.amol.loanquote.AppController;
import com.example.amol.loanquote.R;
import com.example.amol.util.HelperStatic;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amol13704 on 8/10/2017.
 */

public class ScreenFiveFragment extends Fragment implements View.OnClickListener, APIResponseListener {


    private UserModel userModel;
    private Button btnSubmit;

    private EditText editTextEmployerName;
    private EditText editTextEmployerAddress1;
    private EditText editTextEmployerAddress2;
    private EditText editTextEmployerCity;
    private EditText editTextEmployerPincode;
    //private EditText editTextEmployerCountry;
    private EditText editTextEmployerPhone;
    private EditText editTextEmployerDesignation;
    private EditText editTextEmployerSalary;

    private AppCompatSpinner spinnerTenureWithEmployer;
    private AppCompatSpinner spinnerRegion;

    public ScreenFiveFragment() {
        // Required empty public constructor
    }

    public static ScreenFiveFragment newInstance() {
        ScreenFiveFragment fragment = new ScreenFiveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_five, container, false);
        getUserModel();
        initializeAllViews(view);
        addClickListenerToViews();
        return view;
    }

    private void getUserModel() {
        userModel = AppController.getInstance().getUserModel();
    }

    private void setUserModelValues() {

        UserEmployerModel userEmployerModel = userModel.getApplicantEmployers().get(0);

        if (!TextUtils.isEmpty(editTextEmployerPincode.getText()))
        userEmployerModel.setPincode(editTextEmployerPincode.getText().toString());

        if (!TextUtils.isEmpty(editTextEmployerPhone.getText()))
        userEmployerModel.setPhone(editTextEmployerPhone.getText().toString());

        userEmployerModel.setApplicantID(0);

        if (!TextUtils.isEmpty(editTextEmployerCity.getText()))
        userEmployerModel.setCity(editTextEmployerCity.getText().toString());

        if (!TextUtils.isEmpty(editTextEmployerAddress1.getText()))
        userEmployerModel.setAddress1(editTextEmployerAddress1.getText().toString());

        if (!TextUtils.isEmpty(editTextEmployerAddress2.getText()))
        userEmployerModel.setAddress2(editTextEmployerAddress2.getText().toString());

        if (!TextUtils.isEmpty(editTextEmployerDesignation.getText()))
        userEmployerModel.setDesignation(editTextEmployerDesignation.getText().toString());


        userEmployerModel.setEmployerID(0);

        if (!TextUtils.isEmpty(editTextEmployerName.getText()))
        userEmployerModel.setEmployerName(editTextEmployerName.getText().toString());

        if (!TextUtils.isEmpty(editTextEmployerSalary.getText())) {
            double salary = Double.parseDouble(editTextEmployerSalary.getText().toString());
            userEmployerModel.setSalary(salary);
        }

        //if (!TextUtils.isEmpty(editTextEmployerCountry.getText()))
        //userEmployerModel.setCountry(editTextEmployerCountry.getText().toString());
    }

    private void addClickListenerToViews() {
        btnSubmit.setOnClickListener(this);
    }

    private void initializeAllViews(View view) {
        btnSubmit = view.findViewById(R.id.btn_screen_five_submit);
        editTextEmployerName = view.findViewById(R.id.edt_txt_screen_five_employer_name);
        editTextEmployerAddress1 = view.findViewById(R.id.edt_txt_screen_five_employer_address1);
        editTextEmployerAddress2 = view.findViewById(R.id.edt_txt_screen_five_employer_address2);
        editTextEmployerCity = view.findViewById(R.id.edt_txt_screen_five_employer_city);
        editTextEmployerPincode = view.findViewById(R.id.edt_txt_screen_five_employer_pincode);
       // editTextEmployerCountry = view.findViewById(R.id.edt_txt_screen_five_employer_country);
        editTextEmployerPhone = view.findViewById(R.id.edt_txt_screen_five_employer_phone);
        editTextEmployerDesignation = view.findViewById(R.id.edt_txt_screen_five_employer_designation);
        editTextEmployerSalary = view.findViewById(R.id.edt_txt_screen_five_employer_salary);
        spinnerRegion = view.findViewById(R.id.spinner_screen_five_region);
        spinnerTenureWithEmployer = view.findViewById(R.id.spinner_screen_five_tenure);

        UserEmployerModel model = new UserEmployerModel();
        List<UserEmployerModel> list = new ArrayList<UserEmployerModel>();
        list.add(model);
        userModel.setApplicantEmployers(list);
        setAdapterToSpinnerForRegion();
        setAdapterToSpinnerForTenure();
    }

    private void setAdapterToSpinnerForRegion() {
        List<String> listRegion = new ArrayList<String>();

        listRegion.add("Yorkshire & Lincolnshire");
        listRegion.add("North West England");
        listRegion.add("London & South East");

        userModel.getApplicantEmployers().get(0).setRegion("Yorkshire & Lincolnshire");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterTenure = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listRegion);

        // Drop down layout style - list view with radio button
        dataAdapterTenure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerRegion.setAdapter(dataAdapterTenure);
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userModel.getApplicantEmployers().get(0).setRegion(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setAdapterToSpinnerForTenure() {
        List<String> listTenure = new ArrayList<String>();

        listTenure.add("> 1 year");
        listTenure.add("> 2 year");
        listTenure.add("> 3 year");
        listTenure.add("> 4 year");
        listTenure.add("<= 5 year");

        userModel.getApplicantEmployers().get(0).setTenureWithEmployer("> 1 year");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterTenure = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listTenure);

        // Drop down layout style - list view with radio button
        dataAdapterTenure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerTenureWithEmployer.setAdapter(dataAdapterTenure);
        spinnerTenureWithEmployer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userModel.getApplicantEmployers().get(0)
                        .setTenureWithEmployer(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        //call final API
        //if(validateFields()) {
        setUserModelValues();
            try {
                Gson gson = new Gson();
                String object = gson.toJson(userModel);
                Log.d("JSON", " " + object);

                HelperStatic.jsonObjectRequest(getActivity(), 1,
                        "http://loanappapi.azurewebsites.net/api/applicant/post", new JSONObject(object), true, this, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        //}
    }

    private boolean validateFields() {
        boolean isValid = false;

        if (TextUtils.isEmpty(editTextEmployerName.getText())) {
            Toast.makeText(getContext(), "Please enter Employer Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextEmployerAddress1.getText())) {
            Toast.makeText(getContext(), "Please enter address1", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextEmployerAddress2.getText())) {
            Toast.makeText(getContext(), "Please enter address2", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextEmployerCity.getText())) {
            Toast.makeText(getContext(), "Please enter city", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextEmployerPincode.getText())) {
            Toast.makeText(getContext(), "Please enter pincode", Toast.LENGTH_LONG).show();
        }/* else if (TextUtils.isEmpty(editTextEmployerCountry.getText())) {
            Toast.makeText(getContext(), "Please enter country", Toast.LENGTH_LONG).show();
        }*/ else if (TextUtils.isEmpty(editTextEmployerSalary.getText())) {
            Toast.makeText(getContext(), "Please enter salary", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextEmployerDesignation.getText())) {
            Toast.makeText(getContext(), "Please enter your Designation", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextEmployerPhone.getText())) {
            Toast.makeText(getContext(), "Please enter phone", Toast.LENGTH_LONG).show();
        } else {
            setUserModelValues();
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onResponse(String response) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setMessage("Data saved succesfully!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

}
