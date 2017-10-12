package com.example.amol.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.amol.loanquote.APIResponseListener;
import com.example.amol.loanquote.AppController;
import com.example.amol.loanquote.FragmentUtils;
import com.example.amol.loanquote.R;
import com.example.amol.util.HelperStatic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ScreenOneFragment extends Fragment implements View.OnClickListener, APIResponseListener{

    private int loanType = 1;

    private Button btnGetQuote;
    private EditText editTextExistingQuote;
    private EditText editTextFullName;//
    private EditText editTextContactNumber;//
    private EditText editTextLocation;
    private EditText editTextOrganizationBuilding;
    private EditText editTextPound;
    private EditText editTextLoan;
    private ImageView imgExistingQuoteArrow;
    private LinearLayout llPersonalLoan;
    private LinearLayout llhomeLoan;
    private ImageView imgPersonalLoan;
    private ImageView imghomeLoan;
    private UserModel userModel;

    public ScreenOneFragment() {
        // Required empty public constructor
    }

    public static ScreenOneFragment newInstance() {
        ScreenOneFragment fragment = new ScreenOneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_one, container, false);
        initializeAllViews(view);
        addClickListenerToViews();
        return view;
    }

    private void addClickListenerToViews() {
        btnGetQuote.setOnClickListener(this);
        llhomeLoan.setOnClickListener(this);
        llPersonalLoan.setOnClickListener(this);
        imgExistingQuoteArrow.setOnClickListener(this);
    }

    private void initializeAllViews(View view) {
        btnGetQuote = view.findViewById(R.id.btn_screen_one_get_quote);
        editTextContactNumber = view.findViewById(R.id.edt_txt_screen_one_contact_number);
        editTextExistingQuote = view.findViewById(R.id.edt_txt_screen_one_exisiting_quote_id);
        editTextFullName = view.findViewById(R.id.edt_txt_screen_one_full_name);
        editTextLoan = view.findViewById(R.id.edt_txt_screen_one_loan);
        editTextLocation = view.findViewById(R.id.edt_txt_screen_one_location);
        editTextPound = view.findViewById(R.id.edt_txt_screen_one_pound);
        editTextOrganizationBuilding = view.findViewById(R.id.edt_txt_screen_one_organization_building);
        llhomeLoan = view.findViewById(R.id.ll_screen_one_header_home_loan);
        llPersonalLoan = view.findViewById(R.id.ll_screen_one_header_personal_loan);
        imgPersonalLoan = view.findViewById(R.id.img_screen_one_header_personal_loan);
        imghomeLoan = view.findViewById(R.id.img_screen_one_header_home_loan);
        imgExistingQuoteArrow = view.findViewById(R.id.img_screen_one_exisiting_quote_id_arrow);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_screen_one_get_quote:
                handleBtnGetQuoteClick();
                break;

            case R.id.ll_screen_one_header_home_loan:
                handlehomeLoanClick();
                break;

            case R.id.ll_screen_one_header_personal_loan:
                handlePersonalLoanClick();
                break;

            case R.id.img_screen_one_exisiting_quote_id_arrow:
                handleExisitingQouteClick();
                break;
        }
    }

    private void handleExisitingQouteClick() {
        //navigate to the next screen with the quote
        //Get : http://loanappapi.azurewebsites.net/api/quote/get?quoteid=2
        //08-04 20:20:14.765 16732-16732/com.example.amol.loanquote E/Nilesh: onResponse: {"quoteID":86,"loanID":2,"loanAmount":350000,"interestRate":3.2,"monthlyEMI":6766.67,"employerName":"HDFC","salary":3500,"city":"London"}
        Map<String, String> map = new HashMap<>();
        String quoteId = editTextExistingQuote.getText().toString();
        HelperStatic.jsonObjectRequest(getActivity(), 0, "http://loanappapi.azurewebsites.net/api/quote/get?quoteid=" + quoteId,
                new JSONObject(map), true, new APIResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        handleExisitingQuoteResponse(response);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }, false);
    }

    private void handleExisitingQuoteResponse(String response) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("showDialog" , false);
            ScreenTwoFragment screenTwoFragment = ScreenTwoFragment.newInstance();
            screenTwoFragment.setArguments(bundle);
           JSONArray respArray = new JSONArray(response);
            if (respArray != null && respArray.length() > 0) {
                JSONObject resp = respArray.getJSONObject(0);
                UserModel userModel = getUserModel();
                if (userModel == null)
                    userModel = new UserModel();
                userModel.setQuoteID(resp.getInt("quoteID"));
                //userModel.setApplicantID(resp.getInt("applicantID"));
                userModel.setLoanType(resp.getInt("loanID"));
                userModel.setInterestRate(resp.getDouble("interestRate"));
                userModel.setMonthlyEMI(resp.getDouble("monthlyEMI"));
                userModel.setLoanAmount("" + resp.getLong("loanAmount"));
                userModel.setMonthlyIncome("" + resp.getLong("salary"));
                userModel.setOrganisation("" + resp.getString("employerName"));
                userModel.setLocation("" + resp.getString("city"));
                userModel.setQuoteExisting(true);
                editTextLoan.setText("" + resp.getLong("loanAmount"));
                editTextOrganizationBuilding.setText("" + resp.getString("employerName"));
                editTextPound.setText("" + resp.getLong("salary"));
                editTextLocation.setText(resp.getString("city"));
                userModel.getApplicantEmployers().add(new UserEmployerModel());
                userModel.getApplicantAddresses().add(new UserAddressModel());
                AppController.getInstance().setUserModel(userModel);
                FragmentUtils.getInstance()
                        .addFragment(screenTwoFragment,
                                getActivity().getSupportFragmentManager(), Boolean.TRUE);
            } else {
                showAlertMessage("Incorrect quote");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handlePersonalLoanClick() {
        loanType =  1;
        imgPersonalLoan.setBackgroundResource(R.drawable.vector_drawable_hexagon_loan_selected);
        imghomeLoan.setBackgroundResource(R.drawable.vector_drawable_hexagon_loan_deselected);
        //Toast.makeText(getContext(), "PersonalLoan clicked loanType = " + loanType, Toast.LENGTH_LONG).show();
    }

    private void handlehomeLoanClick() {
        loanType = 2;
        imghomeLoan.setBackgroundResource(R.drawable.vector_drawable_hexagon_loan_selected);
        imgPersonalLoan.setBackgroundResource(R.drawable.vector_drawable_hexagon_loan_deselected);
        //Toast.makeText(getContext(), "home loan clicked loanType = " + loanType, Toast.LENGTH_LONG).show();
    }

    private void handleBtnGetQuoteClick() {
       // Toast.makeText(getContext(), "btn get qoute clicked", Toast.LENGTH_LONG).show();
        //if(validateAllTheFields()) {
            createUserModel();
            callAPI();
        //}
    }

    private boolean validateAllTheFields() {
        //edtTextExistingQuote;

        boolean isValid = false;
        if(TextUtils.isEmpty(editTextFullName.getText())) {
            Toast.makeText(getContext(), "Please enter Full Name", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(editTextContactNumber.getText())){
            Toast.makeText(getContext(), "Please enter Contact Number", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(editTextLocation.getText())){
            Toast.makeText(getContext(), "Please enter location", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(editTextOrganizationBuilding.getText())){
            Toast.makeText(getContext(), "Please enter organization", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(editTextPound.getText())){
            Toast.makeText(getContext(), "Please enter monthly income", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(editTextLoan.getText())){
            Toast.makeText(getContext(), "Please enter loan amount", Toast.LENGTH_LONG).show();
        } else {
            createUserModel();
            isValid = true;
        }
        return isValid;
    }

    private void createUserModel() {
        UserModel userModel = new UserModel();
        if(!TextUtils.isEmpty(editTextFullName.getText()))
            userModel.setFullName(editTextFullName.getText().toString());

        if(!TextUtils.isEmpty(editTextContactNumber.getText()))
            userModel.setMobile(editTextContactNumber.getText().toString());

        if(!TextUtils.isEmpty(editTextLocation.getText()))
            userModel.setLocation(editTextLocation.getText().toString());

        if(!TextUtils.isEmpty(editTextOrganizationBuilding.getText()))
            userModel.setOrganisation(editTextOrganizationBuilding.getText().toString());

        if(!TextUtils.isEmpty(editTextPound.getText()))
            userModel.setMonthlyIncome(editTextPound.getText().toString());

        if(!TextUtils.isEmpty(editTextLoan.getText()))
            userModel.setLoanAmount(editTextLoan.getText().toString());

        setUserModel(userModel);
    }

    private void callAPI() {

        //Post: https://loanappapi.azurewebsites.net/api/quote/post

       // onResponse: {"quoteID":116,"loanID":1,"loanAmount":500000,
            //    "interestRate":3.2,"monthlyEMI":9666.67,"employerName":"mastek","salary":50000,"city":"London"}

       // [{"quoteID":86,"loanID":2,"loanAmount":350000,"interestRate":3.2,"monthlyEMI":6766.67,"employerName":"HDFC","salary":3500,"city":"London"}]
        UserModel userModel = getUserModel();
        Map<String, String> map = new HashMap<>();
        map.put("loanID", "" + loanType);
        map.put("loanAmount", userModel.getLoanAmount());
        map.put("city", userModel.getLocation());
        map.put("salary", userModel.getMonthlyIncome());
        map.put("employerName", userModel.getOrganisation());
        HelperStatic.jsonObjectRequest(getActivity(), 1, "https://loanappapi.azurewebsites.net/api/quote/post", new JSONObject(map), true, this, true);
    }

    @Override
    public void onResponse(String response) {
        //Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("showDialog" , true);
            ScreenTwoFragment screenTwoFragment = ScreenTwoFragment.newInstance();
            screenTwoFragment.setArguments(bundle);
                JSONObject resp = new JSONObject(response);
            if (resp != null && resp.length() > 0) {
                UserModel userModel = getUserModel();
                userModel.setQuoteID(resp.getInt("quoteID"));
                userModel.setLoanType(resp.getInt("loanID"));
                userModel.setInterestRate(resp.getDouble("interestRate"));
                userModel.setMonthlyEMI(resp.getDouble("monthlyEMI"));
                userModel.setQuoteExisting(false);
                userModel.getApplicantEmployers().add(new UserEmployerModel());
                userModel.getApplicantAddresses().add(new UserAddressModel());
                AppController.getInstance().setUserModel(userModel);
                FragmentUtils.getInstance()
                        .addFragment(screenTwoFragment,
                                getActivity().getSupportFragmentManager(), Boolean.TRUE);

            } else {
                showAlertMessage("Sorry no response");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAlertMessage(String s) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setMessage(s)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
