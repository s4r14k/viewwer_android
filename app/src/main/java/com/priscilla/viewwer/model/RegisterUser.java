package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterUser {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private  String lastName;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("isPro")
    @Expose
    private Boolean IsPro;

    @SerializedName("companyName")
    @Expose
    private String companyName;

    @SerializedName("activity")
    @Expose
    private String activity;

    @SerializedName("partenerCode")
    @Expose
    private String partenerCode;

    public RegisterUser(String email, String password, String firstName, String lastName, String phone, Boolean isPro, String companyName, String activity, String partenerCode) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        IsPro = isPro;
        this.companyName = companyName;
        this.activity = activity;
        this.partenerCode=partenerCode;
    }

    public RegisterUser(String status, String email, String password, String firstName, String lastName, String phone, String partenerCode) {
        this.status = status;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.partenerCode = partenerCode;
    }

    public RegisterUser(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getPro() {
        return IsPro;
    }

    public void setPro(Boolean pro) {
        IsPro = pro;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPartenerCode() {
        return partenerCode;
    }

    public void setPartenerCode(String partenerCode) {
        this.partenerCode = partenerCode;
    }
}
