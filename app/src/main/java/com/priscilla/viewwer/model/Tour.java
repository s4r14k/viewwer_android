package com.priscilla.viewwer.model;


public class Tour {

    private String entityType;
    private String entityId;
    private String url;
    private String thumbnail;
    private String createdAt;
    private String type;
    private Boolean house;
    private String description;
    private String address;
    private Integer longitude;
    private Integer latitude;
    private Integer area;
    private Integer price;
    private String gasClass;
    private String energyClass;
    private String saleType;
    private Boolean isSold;
    private String moderationStatus;
    private String moderationMessage;
    private Integer counterRating;
    private String generateStatus;
    private Boolean isPublished;



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
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
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

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
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

