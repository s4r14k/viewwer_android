package com.priscilla.viewwer.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("isConnected")
    public Boolean isConnected;

    @SerializedName("user")
    public User user;

    @SerializedName("status")
    public String status;


    // ===========================================================
    // Constructors
    // ===========================================================

    public User(){
    }

    public User(String email, String password, String status) {
        this.email = email;
        this.password = password;
        this.status = status;
    }



    // ===========================================================
    // Getter & Setter
    // ===========================================================


    public String getName() {
        return email;
    }

    public void setName(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }



    // ===========================================================
    // Methods from SuperClass
    // ===========================================================

    // ===========================================================
    // Methods for Interfaces
    // ===========================================================

    // ===========================================================
    // Public Methods
    // ===========================================================

    // ===========================================================
    // Private Methods
    // ===========================================================

    // ===========================================================
    // Inner Classes/Interfaces
    // ===========================================================

}
