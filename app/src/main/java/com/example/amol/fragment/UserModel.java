package com.example.amol.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amol13704 on 8/8/2017.
 */

public class UserModel extends BaseModel implements Serializable {

    private String fullName = "";
    private String firstName = "";

    private String lastName = "";
    private String mobile = "";
    private String location = "";//city
    private String organisation = "";//employer name
    private String monthlyIncome = "";
    private String loanAmount = "";
    private String title = "";
    private String dob = "";
    private String gender = "";
    private String maritialStatus = "";
    private String email = "";
    private String customerCategory = "";
    private String nationalInsuranceNumber = "";

    private String purposeOfLoan = "";
    private int dependents = 0;
    private int applicantID = 0;
    private int quoteID = 0;
    private int loanType = 0;

    private double interestRate = 0.0;
    private double monthlyEMI = 0.0;

    private boolean isQuoteExisting = false;

    private List<UserAddressModel> applicantAddresses
            = new ArrayList<UserAddressModel>();
    private List<UserEmployerModel> applicantEmployers
            = new ArrayList<UserEmployerModel>();


    public boolean isQuoteExisting() {
        return isQuoteExisting;
    }

    public void setQuoteExisting(boolean quoteExisting) {
        isQuoteExisting = quoteExisting;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPurposeOfLoan() {
        return purposeOfLoan;
    }

    public void setPurposeOfLoan(String purposeOfLoan) {
        this.purposeOfLoan = purposeOfLoan;
    }

    public int getLoanType() {
        return loanType;
    }

    public void setLoanType(int loanType) {
        this.loanType = loanType;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getMonthlyEMI() {
        return monthlyEMI;
    }

    public void setMonthlyEMI(double monthlyEMI) {
        this.monthlyEMI = monthlyEMI;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDependents() {
        return dependents;
    }

    public void setDependents(int dependents) {
        this.dependents = dependents;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public List<UserAddressModel> getApplicantAddresses() {
        return applicantAddresses;
    }

    public void setApplicantAddresses(List<UserAddressModel> applicantAddresses) {
        this.applicantAddresses = applicantAddresses;
    }

    public List<UserEmployerModel> getApplicantEmployers() {
        return applicantEmployers;
    }

    public void setApplicantEmployers(List<UserEmployerModel> applicantEmployers) {
        this.applicantEmployers = applicantEmployers;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritialStatus() {
        return maritialStatus;
    }

    public void setMaritialStatus(String maritialStatus) {
        this.maritialStatus = maritialStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    public String getNationalInsuranceNumber() {
        return nationalInsuranceNumber;
    }

    public void setNationalInsuranceNumber(String nationalInsuranceNumber) {
        this.nationalInsuranceNumber = nationalInsuranceNumber;
    }

    public int getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(int applicantID) {
        this.applicantID = applicantID;
    }

    public int getQuoteID() {
        return quoteID;
    }

    public void setQuoteID(int quoteID) {
        this.quoteID = quoteID;
    }
}
