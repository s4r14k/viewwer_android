package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TourBuildRequest
{
		
    @SerializedName("preset")
    @Expose
    public String Preset ;
    @SerializedName("scenes")
    @Expose
    public List<SceneModel> Scenes ;
    @SerializedName("thumbnails")
    @Expose
    public List<SceneModel> Thumbnails ;

    public TourBuildRequest() {
    }


}

