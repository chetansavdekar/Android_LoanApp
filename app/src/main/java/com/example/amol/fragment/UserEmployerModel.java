package com.example.amol.fragment;

import java.io.Serializable;

/**
 * Created by amol13704 on 8/10/2017.//commit
 */

class UserEmployerModel extends BaseModel implements Serializable {

    private int employerID = 0;
    private int applicantID = 0;

    public int getEmployerID() {
        return employerID;
    }

    public void setEmployerID(int employerID) {
        this.employerID = employerID;
    }

    public int getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(int applicantID) {
        this.applicantID = applicantID;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getTenureWithEmployer() {
        return tenureWithEmployer;
    }

    public void setTenureWithEmployer(String tenureWithEmployer) {
        this.tenureWithEmployer = tenureWithEmployer;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    private String employerName = "";
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String pincode = "";
    private String country = "";
    private String phone = "";
    private String designation = "";
    private double salary = 0.0;
    private String tenureWithEmployer = "";
    private String region = "";
}
