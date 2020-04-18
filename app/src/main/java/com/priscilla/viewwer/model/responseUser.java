package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class responseUser {

    @SerializedName("status")
    @Expose
    private String status;

    // Getters et setters

    public responseUser(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

//    public void setResultat(ArrayList<User> result) {
//        this.data = result;
//    }

    @Override
    public String toString() {
        return "resultat : " +status;
    }
}
