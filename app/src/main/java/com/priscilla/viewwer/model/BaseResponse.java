package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("taskId")
    @Expose
    public String TaskId ;

    @SerializedName("uploadUrl")
    @Expose
    public String Url ;

    @SerializedName ("url")
    @Expose
    public String ResultUrl ;
    @SerializedName ("uploadApiKey")
    @Expose
    public String ApiKey ;

    public void setStatus(String result) {
        this.status = result;
    }

    public BaseResponse() {
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @SerializedName("errorDescription")
    @Expose
    private String errorDescription;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }



    // Getters et setters

    public BaseResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

//    public void setResultat(ArrayList<User> result) {
//        this.data = result;
//    }

    @Override
    public String toString() {
        return "resultat : " +status;
    }
}
