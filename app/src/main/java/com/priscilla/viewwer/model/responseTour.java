package com.priscilla.viewwer.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.priscilla.viewwer.activity.BaseActivity;

import java.util.List;

public class responseTour {

    @SerializedName("entityType")
    @Expose
    private String entityType;
    @SerializedName("entityId")
    @Expose
    private String entityId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("house")
    @Expose
    private Boolean house;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("area")
    @Expose
    private Double area;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("gasClass")
    @Expose
    private String gasClass;
    @SerializedName("energyClass")
    @Expose
    private String energyClass;
    @SerializedName("saleType")
    @Expose
    private String saleType;
    @SerializedName("isSold")
    @Expose
    private Boolean isSold;
    @SerializedName("moderationStatus")
    @Expose
    private String moderationStatus;
    @SerializedName("moderationMessage")
    @Expose
    private String moderationMessage;
    @SerializedName("counterRating")
    @Expose
    private Integer counterRating;
    @SerializedName("generateStatus")
    @Expose
    private String generateStatus;
    @SerializedName("isPublished")
    @Expose
    private Boolean isPublished;

    @SerializedName("isFavorite")
    @Expose
    private Boolean isFavorite;

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getUrl() {
        return BaseActivity.URL_API_SERVER + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return  BaseActivity.URL_API_SERVER + thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getHouse() {
        return house;
    }

    public void setHouse(Boolean house) {
        this.house = house;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getGasClass() {
        return gasClass;
    }

    public void setGasClass(String gasClass) {
        this.gasClass = gasClass;
    }

    public String getEnergyClass() {
        return energyClass;
    }

    public void setEnergyClass(String energyClass) {
        this.energyClass = energyClass;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public Boolean getIsSold() {
        return isSold;
    }

    public void setIsSold(Boolean isSold) {
        this.isSold = isSold;
    }

    public String getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(String moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public String getModerationMessage() {
        return moderationMessage;
    }

    public void setModerationMessage(String moderationMessage) {
        this.moderationMessage = moderationMessage;
    }

    public Integer getCounterRating() {
        return counterRating;
    }

    public void setCounterRating(Integer counterRating) {
        this.counterRating = counterRating;
    }

    public String getGenerateStatus() {
        return generateStatus;
    }

    public void setGenerateStatus(String generateStatus) {
        this.generateStatus = generateStatus;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }



}

