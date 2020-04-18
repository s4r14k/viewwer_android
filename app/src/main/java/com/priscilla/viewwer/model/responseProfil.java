package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class responseProfil {

    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("canChangePassword")
    @Expose
    private Boolean canChangePassword;

    public responseProfil(String firstName, String lastName, String phone, Boolean canChangePassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.canChangePassword = canChangePassword;
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

    public Boolean getCanChangePassword() {
        return canChangePassword;
    }

    public void setCanChangePassword(Boolean canChangePassword) {
        this.canChangePassword = canChangePassword;
    }
}
