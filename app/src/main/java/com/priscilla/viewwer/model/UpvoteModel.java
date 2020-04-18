package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpvoteModel
{
    public String getLikeId() {
        return LikeId;
    }

    public void setLikeId(String likeId) {
        LikeId = likeId;
    }

    @SerializedName("entityType")
    @Expose
    public String EntityType;


    @SerializedName("likeId")
    @Expose
    public String LikeId;

    @SerializedName("entityId")
    @Expose

    public String EntityId;

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public Boolean isLiked;

    public UpvoteModel() {


    }

    public UpvoteModel(String entityType, String entityId, String action) {
        EntityType = entityType;
        EntityId = entityId;
        Action = action;

    }

    public String getEntityType() {
        return EntityType;
    }

    public void setEntityType(String entityType) {
        EntityType = entityType;
    }

    public String getEntityId() {
        return EntityId;
    }

    public void setEntityId(String entityId) {
        EntityId = entityId;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    @SerializedName("action")
    @Expose

    public String Action;

}
