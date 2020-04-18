package com.priscilla.viewwer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SceneModel {

        @SerializedName("isMain")
        @Expose
        public Boolean IsMain ;
        @SerializedName("sceneId")
        @Expose
        public String SceneId ;
        @SerializedName("url")
        @Expose
        public String Url ;
        @SerializedName("file")
        @Expose
        public String ImageName ;
}
