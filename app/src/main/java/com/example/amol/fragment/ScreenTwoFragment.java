package com.example.amol.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.amol.loanquote.APIResponseListener;
import com.example.amol.loanquote.AppController;
import com.example.amol.loanquote.FragmentUtils;
import com.example.amol.loanquote.R;

/**
 * Created by amol13704 on 8/10/2017.
 */

public class ScreenTwoFragment extends Fragment implements View.OnClickListener, APIResponseListener, SeekBar.OnSeekBarChangeListener {

    private SeekBar loanAmountSeekBar;
    private int loanAmountSeekBarStartLimit;
    private int loanAmountSeekBarEndLimit;

    private SeekBar tenureSeekBar;
    private int tenureSeekBarStartLimit;
    private int tenureSeekBarEndLimit;


    private TextView txtLoanAmount;
    private TextView txtLoanAmountSeekBarStartLimit;
    private TextView txtLoanAmountSeekBarEndLimit;

    private TextView txtTenure;
    private TextView txtTenureSeekBarStartLimit;
    private TextView txtTenureSeekBarEndLimit;

    private TextView txtEMI;
    private TextView txtInterestRate;

    private double dblEMI;
    private double dblInterestRate;

    private Button btnContinue;

    private UserModel userModel;
    private boolean showDialog;


    public ScreenTwoFragment() {
        // Required empty public constructor
    }

    public static ScreenTwoFragment newInstance() {
        ScreenTwoFragment fragment = new ScreenTwoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDialog = getArguments().getBoolean("showDialog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_screen_two, container, false);

        initializeAllViews(view);
        addClickListenerToViews();
        if (showDialog)
            getUserModel();
        setUserModelValues();
        return view;
    }

    private void getUserModel() {
        userModel = AppController.getInstance().getUserModel();
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setMessage("Your Quote is " + userModel.getQuoteID())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showDialog = false;
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void setUserModelValues() {
        txtLoanAmount.setText("Loan Amount: " + loanAmountSeekBar.getProgress() + " pound");
        txtLoanAmountSeekBarStartLimit.setText("100");
        txtLoanAmountSeekBarEndLimit.setText("5000");
        txtTenure.setText("Tenure: " + tenureSeekBar.getProgress() + " years");
        txtTenureSeekBarStartLimit.setText("1 year");
        txtTenureSeekBarEndLimit.setText("10 year");
        txtEMI.setText("96.96");
        txtInterestRate.setText("3.2%");
    }

    private void addClickListenerToViews() {
        btnContinue.setOnClickListener(this);
        loanAmountSeekBar.setOnSeekBarChangeListener(this);
        tenureSeekBar.setOnSeekBarChangeListener(this);
    }

    private void initializeAllViews(View view) {
        btnContinue = view.findViewById(R.id.btn_screen_two_continue);
        loanAmountSeekBar = view.findViewById(R.id.seekbar_screen_two_loan_amount);
        tenureSeekBar = view.findViewById(R.id.seekbar_screen_two_tenure);
        txtLoanAmount = view.findViewById(R.id.txt_screen_two_loan_amount);
        txtLoanAmountSeekBarStartLimit = view.findViewById(R.id.txt_start_limit_loan_amount);
        txtLoanAmountSeekBarEndLimit = view.findViewById(R.id.txt_end_limit_loan_amount);
        txtTenure = view.findViewById(R.id.txt_screen_two_tenure);
        txtTenureSeekBarStartLimit = view.findViewById(R.id.txt_start_limit_tenure);
        txtTenureSeekBarEndLimit = view.findViewById(R.id.txt_end_limit_tenure);
        txtEMI = view.findViewById(R.id.txt_screen_two_EMI);
        txtInterestRate = view.findViewById(R.id.txt_screen_two_interest_rate);
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
        FragmentUtils.getInstance()
                .addFragment(ScreenThreeFragment.newInstance(),
                        getActivity().getSupportFragmentManager(), Boolean.TRUE);
    }

    @Override
    public void onResponse(String response) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar.getId() == R.id.seekbar_screen_two_loan_amount)
            txtLoanAmount.setText("Loan Amount: " + i + " pound");

        if (seekBar.getId() == R.id.seekbar_screen_two_tenure)
            txtTenure.setText("Tenure: " + i + " years");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
