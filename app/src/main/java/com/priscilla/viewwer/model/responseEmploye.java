package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class responseEmploye {

    @SerializedName("data")
    @Expose
    private ArrayList<Employe> data;

    // Getters et setters

    public responseEmploye(ArrayList<Employe> result) {
        this.data = result;
    }

    public ArrayList<Employe> getResultat() {
        return data;
    }

    public void setResultat(ArrayList<Employe> result) {
        this.data = result;
    }

    @Override
    public String toString() {
        return "resultat : " +data;
    }
}
