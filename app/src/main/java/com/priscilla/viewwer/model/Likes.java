package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Likes {
    @SerializedName("likeId")
    @Expose
    private String likeId;

    @SerializedName("entityId")
    @Expose
    private String entityId;

    public String getLikeIdId() {
        return likeId;
    }

    public String getEntityId() {
        return entityId;
    }
}
