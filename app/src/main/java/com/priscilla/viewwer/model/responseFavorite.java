package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class responseFavorite {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("favoriteId")
    @Expose
    private String FavoriteId;

    public responseFavorite(String status, String favoriteId) {
        this.status = status;
        FavoriteId = favoriteId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFavoriteId() {
        return FavoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        FavoriteId = favoriteId;
    }
}
