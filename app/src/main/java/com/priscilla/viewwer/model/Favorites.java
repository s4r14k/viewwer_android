package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favorites {
    @SerializedName("favoriteId")
    @Expose
    private String favoriteId;

    @SerializedName("entityId")
    @Expose
    private String entityId;

    public String getFavoriteId() {
        return favoriteId;
    }

    public String getEntityId() {
        return entityId;
    }
}
