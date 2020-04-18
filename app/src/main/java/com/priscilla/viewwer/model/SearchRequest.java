package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SearchRequest {

    @SerializedName("placeType")
    @Expose
    private String PlaceType;

    @SerializedName("saleType")
    @Expose
    private String SaleType;

    @SerializedName("page")
    @Expose
    private int Page;

    @SerializedName("budgetFrom")
    @Expose
    private double BudgetFrom;

    @SerializedName("budgetTo")
    @Expose
    private double BudgetTo;

    @SerializedName("roomsFrom")
    @Expose
    private double RoomsFrom;

    @SerializedName("roomsTo")
    @Expose
    private double RoomsTo;

    @SerializedName("areaFrom")
    @Expose
    private double AreaFrom;

    @SerializedName("areaTo")
    @Expose
    private double AreaTo;

    @SerializedName("location")
    @Expose
    private Location Location;



    public SearchRequest.Location getLocation() {
        return Location;
    }

    public void setLocation(SearchRequest.Location location) {
        Location = location;
    }


    public String getPlaceType() {
        return PlaceType;
    }

    public void setPlaceType(String PlaceType) {
        this.PlaceType = PlaceType;
    }


    public String getSaleType() {
        return SaleType;
    }

    public void setSaleType(String SaleType) {
        this.SaleType = SaleType;
    }


    public int getPage() {
        return  Page;
    }

    public void setPage(int Page) {
        this.Page = Page;
    }

    public double getBudgetFrom() {
        return BudgetFrom;
    }

    public void setBudgetFrom(double BudgetFrom) {
        this.BudgetFrom = BudgetFrom;
    }

    public double getBudgetTo() {
        return BudgetTo;
    }

    public void setBudgetTo(double BudgetTo) {
        this.BudgetTo = BudgetTo;
    }


    public double getRoomsFrom() {
        return RoomsFrom;
    }

    public void setRoomsFrom(double RoomsFrom) {
        this.RoomsFrom = RoomsFrom;
    }


    public double getRoomsTo() {
        return RoomsTo;
    }

    public void setRoomsTo(double RoomsTo) {
        this.RoomsTo = RoomsTo;
    }

    public double getAreaFrom() {
        return AreaFrom;
    }

    public void setAreaFrom(double AreaFrom) {
        this.AreaFrom = AreaFrom;
    }


    public double getAreaTo() {
        return AreaTo;
    }

    public void setAreaTo(double AreaTo) {
        this.AreaTo = AreaTo;
    }



    public static class Location
    {
        @SerializedName("user")
        @Expose
        private boolean User;

        @SerializedName("lat")
        @Expose
        private double Latitude;

        @SerializedName("lng")
        @Expose
        private double Longitude;

        @SerializedName("placeId")
        @Expose
        private String PlaceId;


        public Location() {

        }

        public Location(boolean user, double latitude, double longitude, String placeId) {
            User = user;
            Latitude = latitude;
            Longitude = longitude;
            PlaceId = placeId;
        }

//        public boolean getUser() {
//            return User;
//        }
//
//        public void setUser(boolean User) {
//            this.User = User;
//        }
//
//        public double getLatitude() {
//            return Latitude;
//        }
//
//        public void setLatitude(double Latitude) {
//            this.Latitude = 48.85661400000001;
//        }
//        public double getLongitude() {
//            return Longitude;
//        }
//
//        public void setLongitude(double Longitude) {
//            this.Longitude = 2.3522219;
//        }
//
//        public String getPlaceId() {
//            return PlaceId;
//        }
//
//        public void setPlaceId(String PlaceId) {
//            this.PlaceId = PlaceId;
//        }
    }


}
