package com.priscilla.viewwer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.priscilla.viewwer.activity.BaseActivity;
import com.priscilla.viewwer.api.ApiCallBack;

import org.w3c.dom.Entity;


public class responseSearch implements Parcelable {

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




    @SerializedName("isFavorite")
    @Expose
    private Boolean isFavorite;

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

    private Boolean isLiked;

    private String LikedId;

    public responseSearch(Boolean isLiked, String likedId) {
        this.isLiked = isLiked;
        LikedId = likedId;
    }


    public String getTest() {
        return Test;
    }

    public void setTest(String test) {
        Test = test;
    }

    private String Test;

    @SerializedName("program")
    @Expose
    public Program Program;


    public static class Program
    {
        @SerializedName("url")
        @Expose
        private String url;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("isPublished")
        @Expose
        private boolean isPublished;

        public Program() {

        }

        public Program(String url, String name, boolean isPublished) {
            this.url = url;
            this.name = name;
            this.isPublished = isPublished;
        }

        public String getUrl() {
            return BaseActivity.URL_API_SERVER + url;
        }

        public String getName() {
            return name;
        }

        public boolean isPublished() {
            return isPublished;
        }
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

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
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

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public String getLikedId() {
        return LikedId;
    }

    public void setLikedId(String likedId) {
        LikedId = likedId;
    }

    public responseSearch()
    {

    }


    protected responseSearch(Parcel in) {
        entityType = in.readString();
        entityId = in.readString();
        url = in.readString();
        thumbnail = in.readString();
        createdAt = in.readString();
        type = in.readString();
        byte houseVal = in.readByte();
        house = houseVal == 0x02 ? null : houseVal != 0x00;
        description = in.readString();
        address = in.readString();
        longitude = in.readByte() == 0x00 ? null : in.readDouble();
        latitude = in.readByte() == 0x00 ? null : in.readDouble();
        area = in.readByte() == 0x00 ? null : in.readDouble();
        price = in.readByte() == 0x00 ? null : in.readDouble();
        gasClass = in.readString();
        energyClass = in.readString();
        saleType = in.readString();
        byte isSoldVal = in.readByte();
        isSold = isSoldVal == 0x02 ? null : isSoldVal != 0x00;

        byte isFavoriteVal = in.readByte();
        isFavorite = isFavoriteVal == 0x02 ? null : isFavoriteVal != 0x00;

        moderationStatus = in.readString();
        moderationMessage = in.readString();
        counterRating = in.readByte() == 0x00 ? null : in.readInt();
        generateStatus = in.readString();
        byte isPublishedVal = in.readByte();
        isPublished = isPublishedVal == 0x02 ? null : isPublishedVal != 0x00;
        byte isLikedVal = in.readByte();
        isLiked = isLikedVal == 0x02 ? null : isLikedVal != 0x00;
        LikedId = in.readString();
        Test = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entityType);
        dest.writeString(entityId);
        dest.writeString(url);
        dest.writeString(thumbnail);
        dest.writeString(createdAt);
        dest.writeString(type);
        if (house == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (house ? 0x01 : 0x00));
        }
        dest.writeString(description);
        dest.writeString(address);
        if (longitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(longitude);
        }
        if (latitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(latitude);
        }
        if (area == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(area);
        }
        if (price == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(price);
        }
        dest.writeString(gasClass);
        dest.writeString(energyClass);
        dest.writeString(saleType);
        if (isSold == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isSold ? 0x01 : 0x00));
        }
        if (isFavorite == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isSold ? 0x01 : 0x00));
        }
        dest.writeString(moderationStatus);
        dest.writeString(moderationMessage);
        if (counterRating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(counterRating);
        }
        dest.writeString(generateStatus);
        if (isPublished == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isPublished ? 0x01 : 0x00));
        }
        if (isLiked == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isLiked ? 0x01 : 0x00));
        }
        dest.writeString(LikedId);
        dest.writeString(Test);
    }

    public boolean ManageLike(String entityId) {

        BaseActivity.ListeLike(entityId, new ApiCallBack() {
            public void onResponse(UpvoteModel resultLike) {

                if(!resultLike.isLiked){
                    setLiked(true);
                    Log.d("Response like tsara", "=====");

                    //  list.setLiked(isLiked);
                }else{
                    setLiked(true);
                }
            }
        });
        return getLiked();
    }
    //       BaseActivity.ListeLike(list.getEntityId(), new ApiCallBack() {
//        public void onResponse(UpvoteModel resultLike) {
//            if (resultLike.isLiked) {
//                // do something
//                list.setLikedId(resultLike.LikeId);
//                list.setLiked(true);
//
//            } else {
//                // do something
//                list.setLiked(false);
//                list.setLikedId(resultLike.LikeId);
//            }
//        }
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<responseSearch> CREATOR = new Parcelable.Creator<responseSearch>() {
        @Override
        public responseSearch createFromParcel(Parcel in) {
            return new responseSearch(in);
        }

        @Override
        public responseSearch[] newArray(int size) {
            return new responseSearch[size];
        }
    };
}