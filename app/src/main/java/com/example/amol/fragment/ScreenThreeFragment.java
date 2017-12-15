package com.example.amol.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.amol.loanquote.APIResponseListener;
import com.example.amol.loanquote.AppController;
import com.example.amol.loanquote.FragmentUtils;
import com.example.amol.loanquote.R;
import com.example.amol.util.HelperStatic;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amol13704 on 8/10/2017.
 */

public class ScreenThreeFragment extends TabFragment implements View.OnClickListener, APIResponseListener {

    private UserModel userModel;
    private Button btnNext;
    private AppCompatSpinner spinnerNoOfDependents;
    private AppCompatSpinner spinnerPurposeOfLoan;
    private EditText editTextDOB;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextMobileNumber;
    private EditText editTextEmail;
    private AppCompatSpinner spinnerGender;
    private AppCompatSpinner spinnerMarritalStatus;
    private EditText editTextNationalInsuranceNumber;


    public ScreenThreeFragment() {
        // Required empty public constructor
    }

    public static ScreenThreeFragment newInstance() {
        ScreenThreeFragment fragment = new ScreenThreeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_three, container, false);
        getUserModel();
        initializeAllViews(view);
        if (userModel.isQuoteExisting()) {
            fetchApplicantData();
        }
        addClickListenerToViews();
        return view;
    }

    private void fetchApplicantData() {

        //http://loanappapi.azurewebsites.net/api/applicant/get?quoteId=2
        Map<String, String> map = new HashMap<>();

        HelperStatic.jsonObjectRequest(getActivity(), 0, "http://loanappapi.azurewebsites.net/api/applicant/get?quoteId=" + userModel.getQuoteID(),
                new JSONObject(map), true, new APIResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        handleApplicantData(response);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }, false);
    }

    private void handleApplicantData(String response) {

        try {
            JSONArray jsonArray = new JSONArray(response);

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            Gson gson = new Gson();
            UserModel userModelFromServer = gson.fromJson(jsonObject.toString(), UserModel.class);
            if (!TextUtils.isEmpty(userModelFromServer.getDob()))
                editTextDOB.setText(userModelFromServer.getDob());

            if (!TextUtils.isEmpty(userModelFromServer.getMobile()))
                editTextMobileNumber.setText(userModelFromServer.getMobile());

            if (!TextUtils.isEmpty(userModelFromServer.getFirstName()))
                editTextFirstName.setText(userModelFromServer.getFirstName());

            if (!TextUtils.isEmpty(userModelFromServer.getLastName()))
                editTextLastName.setText(userModelFromServer.getLastName());

            if (!TextUtils.isEmpty(userModelFromServer.getEmail()))
                editTextEmail.setText(userModelFromServer.getEmail());

            if (!TextUtils.isEmpty(userModelFromServer.getNationalInsuranceNumber()))
                editTextNationalInsuranceNumber.setText(userModelFromServer.getNationalInsuranceNumber());

            userModelFromServer.setQuoteExisting(userModel.isQuoteExisting());
            userModel = userModelFromServer;
            AppController.getInstance().setUserModel(userModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void getUserModel() {
        userModel = AppController.getInstance().getUserModel();
    }

    private void setUserModelValues() {

        if (!TextUtils.isEmpty(editTextDOB.getText().toString()))
            userModel.setDob(editTextDOB.getText().toString());

        if (!TextUtils.isEmpty(editTextMobileNumber.getText()))
            userModel.setMobile(editTextMobileNumber.getText().toString());

        if (!TextUtils.isEmpty(editTextFirstName.getText()))
            userModel.setFirstName(editTextFirstName.getText().toString());

        if (!TextUtils.isEmpty(editTextLastName.getText()))
            userModel.setLastName(editTextLastName.getText().toString());


        //userModel.setApplicantID(0);
        // int noOfdep = Integer.parseInt(spinnerNoOfDependents.getSelectedItem().toString());
        if (!TextUtils.isEmpty(editTextEmail.getText()))
            userModel.setEmail(editTextEmail.getText().toString());

        // if(!TextUtils.isEmpty(editTextMarritalStatus.getText()))
        //userModel.setMaritialStatus(editTextMarritalStatus.getText().toString());

        if (!TextUtils.isEmpty(editTextNationalInsuranceNumber.getText()))
            userModel.setNationalInsuranceNumber(editTextNationalInsuranceNumber.getText().toString());

        //if(!TextUtils.isEmpty(editTextCustomerCategory.getText()))
        //userModel.setCustomerCategory(editTextCustomerCategory.getText().toString());
    }

    private void addClickListenerToViews() {
        btnNext.setOnClickListener(this);
    }

    private void initializeAllViews(View view) {
        btnNext = view.findViewById(R.id.btn_screen_three_next);
        editTextMobileNumber = view.findViewById(R.id.edt_txt_screen_three_mobile);
        editTextFirstName = view.findViewById(R.id.edt_txt_screen_three_first_name);
        editTextDOB = view.findViewById(R.id.edt_txt_screen_three_dob);
        editTextLastName = view.findViewById(R.id.edt_txt_screen_three_last_name);
        spinnerNoOfDependents = view.findViewById(R.id.spinner_screen_three_no_of_dependents);
        editTextEmail = view.findViewById(R.id.edt_txt_screen_three_email);
        spinnerGender = view.findViewById(R.id.spinner_screen_three_gender);
        spinnerMarritalStatus = view.findViewById(R.id.spinner_screen_three_marital_status);
        editTextNationalInsuranceNumber = view.findViewById(R.id.edt_txt_screen_three_national_insurance_number);
        //editTextCustomerCategory = view.findViewById(R.id.edt_txt_screen_three_customer_category);
        spinnerPurposeOfLoan = view.findViewById(R.id.spinner_screen_three_purpose_of_loan);
        txtInfoHeader = view.findViewById(R.id.txt_screen_three_info_header);
        txtAddressHeader = view.findViewById(R.id.txt_screen_three_adreess_header);
        txtEmployerHeader = view.findViewById(R.id.txt_screen_three_employer_header);
        setOnClickListenerToTab();
        currentTab = INFO_TAB;
        editTextDOB.setFocusable(Boolean.FALSE);

        editTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if(b){
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
                        editTextDOB.setText(date);
                        userModel.setDob(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                //}
            }
        });
        setAdapterToSpinnerForDependents();
        setAdapterToSpinnerForGender();
        setAdapterToSpinnerForPurposeOfLoan();
        setAdapterToSpinnerForMaritalStatus();
    }

    private void setAdapterToSpinnerForMaritalStatus() {
        List<String> listMaritalStatus = new ArrayList<String>();
        listMaritalStatus.add("Married");
        listMaritalStatus.add("Unmarried");


        userModel.setMaritialStatus("Married");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterMaritalStatus = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listMaritalStatus);

        // Drop down layout style - list view with radio button
        dataAdapterMaritalStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerMarritalStatus.setAdapter(dataAdapterMaritalStatus);
        spinnerMarritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userModel.setMaritialStatus(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setAdapterToSpinnerForPurposeOfLoan() {
        List<String> listPurposeOfLoan = new ArrayList<String>();
        listPurposeOfLoan.add("Buying a car/Vehical");
        listPurposeOfLoan.add("Home Furnishing");
        listPurposeOfLoan.add("Home Improvements");
        listPurposeOfLoan.add("Holiday");
        listPurposeOfLoan.add("Debt consolidation");
        listPurposeOfLoan.add("Electrical Goods");

        userModel.setPurposeOfLoan("Buying a car/Vehical");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterPurposeOfLoan =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listPurposeOfLoan);

        // Drop down layout style - list view with radio button
        dataAdapterPurposeOfLoan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerPurposeOfLoan.setAdapter(dataAdapterPurposeOfLoan);
        spinnerPurposeOfLoan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userModel.setPurposeOfLoan(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setAdapterToSpinnerForGender() {
        List<String> listGender = new ArrayList<String>();
        listGender.add("Male");
        listGender.add("Female");
        userModel.setGender("M");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterGender
                = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listGender);

        // Drop down layout style - list view with radio button
        dataAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerGender.setAdapter(dataAdapterGender);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userModel.setGender(parent.getItemAtPosition(position).toString().charAt(0) + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void setAdapterToSpinnerForDependents() {


        List<String> listNoOfDependents = new ArrayList<String>();

        listNoOfDependents.add("" + 1);
        listNoOfDependents.add("" + 2);
        listNoOfDependents.add("" + 3);
        listNoOfDependents.add("" + 4);
        userModel.setDependents(1);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterDependents
                = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listNoOfDependents);

        // Drop down layout style - list view with radio button
        dataAdapterDependents.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerNoOfDependents.setAdapter(dataAdapterDependents);
        spinnerNoOfDependents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Integer item = Integer.parseInt(parent.getItemAtPosition(position).toString());
                userModel.setDependents(item);
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
    public void onDetach() {
        super.onDetach();
    }

    private Fragment switchToFragment;

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btn_screen_three_next){
            switchToFragment = ScreenFourFragment.newInstance();
        } else if(view.getId() == R.id.txt_screen_three_adreess_header){
            switchToFragment = ScreenFourFragment.newInstance();
        } else if(view.getId() == R.id.txt_screen_three_employer_header){
            switchToFragment = ScreenFiveFragment.newInstance();
        }

        try {
            setUserModelValues();


        /*Map<String, String> map = new HashMap<>();
        map.put("loanID", "" + loanType);
        map.put("loanAmount", userModel.getLoanAmount());
        map.put("city", userModel.getLocation());
        map.put("salary", userModel.getMonthlyIncome());
        map.put("employerName", userModel.getOrganisation());*/
        //hello //commit
            Gson gson = new Gson();

            String object = gson.toJson(userModel);

            JSONObject jsonObject = new JSONObject(object);


            /////// remove Applicant ID from applicant address and employer as told by chetan
            JSONObject addressJsonObject = jsonObject.getJSONArray("applicantAddresses")
                    .getJSONObject(0);
            if(addressJsonObject.has("applicantID")) {
                addressJsonObject.remove("applicantID");
            }

            JSONObject employerJsonObject = jsonObject.getJSONArray("applicantEmployers")
                    .getJSONObject(0);
            if(employerJsonObject.has("applicantID")) {
                employerJsonObject.remove("applicantID");
            }



            String url = "";
            int method;
            int applicantID = userModel.getApplicantID();
            if (applicantID > 0) {
                url = "http://loanappapi.azurewebsites.net/api/applicant/put";
                method = JsonObjectRequest.Method.PUT;
            } else {
                if(jsonObject.has("applicantID")){
                    jsonObject.remove("applicantID");
                }
                url = "http://loanappapi.azurewebsites.net/api/applicant/post";
                method = JsonObjectRequest.Method.POST;
            }

            HelperStatic.jsonObjectRequest(getActivity(), method, url, jsonObject, true, this, true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        boolean isValid = false;
        if (TextUtils.isEmpty(editTextFirstName.getText())) {
            Toast.makeText(getContext(), "Please enter First Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextLastName.getText())) {
            Toast.makeText(getContext(), "Please enter Last Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextMobileNumber.getText())) {
            Toast.makeText(getContext(), "Please enter Mobile Number", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextEmail.getText())) {
            Toast.makeText(getContext(), "Please enter email", Toast.LENGTH_LONG).show();
        } /*else if(TextUtils.isEmpty(editTextDOB.getText())){
            Toast.makeText(getContext(), "Please enter date of birth", Toast.LENGTH_LONG).show();
        }*/ /*else if(TextUtils.isEmpty(editTextMarritalStatus.getText())){
            Toast.makeText(getContext(), "Please enter your marrital status", Toast.LENGTH_LONG).show();
        }*/ else if (TextUtils.isEmpty(editTextNationalInsuranceNumber.getText())) {
            Toast.makeText(getContext(), "Please enter your National Insurance Number", Toast.LENGTH_LONG).show();
        } /*else if(TextUtils.isEmpty(editTextCustomerCategory.getText())){
            Toast.makeText(getContext(), "Please enter customer catrgory", Toast.LENGTH_LONG).show();
        }*/ else {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            int applicantId = object.getInt("applicantID");
            AppController.getInstance().getUserModel().setApplicantID(applicantId);
            changeFragment();
            //switchTab(ScreenFourFragment.newInstance());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changeFragment(){
        FragmentUtils.getInstance()
                .addFragment(switchToFragment,
                        getActivity().getSupportFragmentManager(), Boolean.FALSE);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Nilesh", " error = " + error.toString());
    }

    @Override
    public void switchTab(View view) {
       onClick(view);
    }
}
