package com.example.amol.fragment;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amol13704 on 8/10/2017. //commit
 */

public class ScreenFourFragment extends TabFragment implements View.OnClickListener, APIResponseListener {

    private UserModel userModel;
    private Button btnNext;

   // private EditText editTextAddressType;
    private EditText editTextAddress1;
    private EditText editTextAddress2;
    private EditText editTextCity;
    private EditText editTextPincode;
   // private EditText editTextCountry;
    private EditText editTextPhone;
    private AppCompatSpinner spinnerRegion;


    public ScreenFourFragment() {
        // Required empty public constructor
    }

    public static ScreenFourFragment newInstance() {
        ScreenFourFragment fragment = new ScreenFourFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_four, container, false);
        getUserModel();
        initializeAllViews(view);
        addClickListenerToViews();
        //setUserModelValues();
        return view;
    }

    private void getUserModel() {
        userModel = AppController.getInstance().getUserModel();
    }

    private void setUserModelValues() {
        UserAddressModel model =   userModel.getApplicantAddresses().get(0);

        if(!TextUtils.isEmpty(editTextAddress1.getText()))
        model.setAddress1(editTextAddress1.getText().toString());

        if(!TextUtils.isEmpty(editTextAddress2.getText()))
        model.setAddress2(editTextAddress2.getText().toString());

        model.setAddressID(0);

        //if(!TextUtils.isEmpty(editTextAddressType.getText()))
        //model.setAddressType(editTextAddressType.getText().toString());

        if(!TextUtils.isEmpty(editTextCity.getText()))
        model.setCity(editTextCity.getText().toString());

        //if(!TextUtils.isEmpty(editTextCountry.getText()))
        //model.setCountry(editTextCountry.getText().toString());

        model.setApplicantID(0);

        if(!TextUtils.isEmpty(editTextPhone.getText()))
        model.setPhone(editTextPhone.getText().toString());

        if(!TextUtils.isEmpty(editTextPincode.getText()))
        model.setPincode(editTextPincode.getText().toString());

    }

    private void addClickListenerToViews() {
        btnNext.setOnClickListener(this);
    }

    private void initializeAllViews(View view) {
        btnNext = view.findViewById(R.id.btn_screen_four_next);
        //editTextAddressType = view.findViewById(R.id.edt_txt_screen_four_address_type);
        editTextAddress1 = view.findViewById(R.id.edt_txt_screen_four_address1);
        editTextAddress2 = view.findViewById(R.id.edt_txt_screen_four_address2);
        editTextCity = view.findViewById(R.id.edt_txt_screen_four_city);
        editTextPincode = view.findViewById(R.id.edt_txt_screen_four_pincode);
        //editTextCountry = view.findViewById(R.id.edt_txt_screen_four_country);
        editTextPhone = view.findViewById(R.id.edt_txt_screen_four_phone);
        spinnerRegion = view.findViewById(R.id.spinner_screen_four_region);
        txtInfoHeader = view.findViewById(R.id.txt_screen_four_info_header);
        txtAddressHeader = view.findViewById(R.id.txt_screen_four_address_header);
        txtEmployerHeader = view.findViewById(R.id.txt_screen_four_employer_header);
        setOnClickListenerToTab();
        currentTab = ADDRESS_TAB;
        //UserAddressModel model = new UserAddressModel();
        //List<UserAddressModel> list = new ArrayList<UserAddressModel>();
        //list.add(model);
        //userModel.setApplicantAddresses(list);

        UserAddressModel userAddressModel = userModel.getApplicantAddresses().get(0);
        if(!TextUtils.isEmpty(userAddressModel.getAddress1()))
        editTextAddress1.setText(userAddressModel.getAddress1());

        if(!TextUtils.isEmpty(userAddressModel.getAddress2()))
        editTextAddress2.setText(userAddressModel.getAddress2());

        if(!TextUtils.isEmpty(userAddressModel.getCity()))
        editTextCity.setText(userAddressModel.getCity());

        if(!TextUtils.isEmpty(userAddressModel.getPincode()))
        editTextPincode.setText(userAddressModel.getPincode());

        if(!TextUtils.isEmpty(userAddressModel.getPhone()))
        editTextPhone.setText(userAddressModel.getPhone());

        setAdapterToSpinnerForRegion();
    }

    private void setAdapterToSpinnerForRegion() {
        List<String> listPurposeOfLoan = new ArrayList<String>();
        listPurposeOfLoan.add("Yorkshire & Lincolnshire");
        listPurposeOfLoan.add("North West England");
        listPurposeOfLoan.add("London & South East");

        userModel.getApplicantAddresses().get(0).setRegion("Yorkshire & Lincolnshire");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterPurposeOfLoan = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listPurposeOfLoan);

        // Drop down layout style - list view with radio button
        dataAdapterPurposeOfLoan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerRegion.setAdapter(dataAdapterPurposeOfLoan);
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userModel.getApplicantAddresses().get(0).setRegion(parent.getItemAtPosition(position).toString());
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
        //if(validateFields()) {

        //}
        if(view.getId() == R.id.btn_screen_four_next){
            switchToFragment = ScreenFiveFragment.newInstance();
        } else if(view.getId() == R.id.txt_screen_four_info_header){
            userModel.setQuoteExisting(true);
            switchToFragment = ScreenThreeFragment.newInstance();
        } else if(view.getId() == R.id.txt_screen_four_employer_header){
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
            Gson gson = new Gson();

            String object = gson.toJson(userModel);

            JSONObject jsonObject = new JSONObject(object);
            String url = "";
            //if(userModel.isQuoteExisting()){
                url = "http://loanappapi.azurewebsites.net/api/applicant/put";
           //} else {
             //   url = "http://loanappapi.azurewebsites.net/api/applicant/post";
           // }


            HelperStatic.jsonObjectRequest(getActivity(), JsonObjectRequest.Method.PUT, url, jsonObject, true, this, true);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        boolean isValid = false;

        /*if(TextUtils.isEmpty(editTextAddressType.getText())) {
            Toast.makeText(getContext(), "Please enter address type", Toast.LENGTH_LONG).show();
        } else*/ if(TextUtils.isEmpty(editTextAddress1.getText())){
            Toast.makeText(getContext(), "Please enter address1", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(editTextAddress2.getText())){
            Toast.makeText(getContext(), "Please enter address2", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(editTextCity.getText())){
            Toast.makeText(getContext(), "Please enter city", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(editTextPincode.getText())){
            Toast.makeText(getContext(), "Please enter pincode", Toast.LENGTH_LONG).show();
        } /*else if(TextUtils.isEmpty(editTextCountry.getText())){
            Toast.makeText(getContext(), "Please enter country", Toast.LENGTH_LONG).show();
        }*/ else if(TextUtils.isEmpty(editTextPhone.getText())){
            Toast.makeText(getContext(), "Please enter phone", Toast.LENGTH_LONG).show();
        } else {
            setUserModelValues();
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onResponse(String response) {

        changeFragment();
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
