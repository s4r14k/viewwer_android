package com.priscilla.viewwer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TourSettings implements Serializable {
    @SerializedName("previewTaskId")
    public String TourTaskId ;
    @SerializedName("type")
    public String PropertyType ;
    @SerializedName("house")
    public Boolean IsHouse ;
    @SerializedName("description")
    public String TourName ;
    @SerializedName("address")
    public String Address ;
    @SerializedName("latitude")
    public double Latitude ;
    @SerializedName("longitude")
    public double Longitude ;
    @SerializedName("area")
    public double Area ;
    @SerializedName("gasClass")
    public String GasClass ;
    @SerializedName("energyClass")
    public String EnergyClass ;
    @SerializedName("saleType")
    public String SaleType ;
    @SerializedName("price")
    public double Price ;
    @SerializedName("isSold")
    public Boolean IsSold ;
}
