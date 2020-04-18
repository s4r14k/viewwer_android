package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class responseProfilEdit {
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }
}
