package com.priscilla.viewwer.model;

import android.drm.DrmStore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChangeSettingsRequest implements Serializable
{
    @Expose
    @SerializedName("preset")
    public String Preset ;
    @Expose
    @SerializedName("settings")
    public Settings Settings ;

    public class Settings implements Serializable
    {
        @SerializedName("sceneTypes")
        public ArrayList<SceneTypeRequest> SceneTypes ;
        @SerializedName("hotspots")
        public ArrayList<Hotspot> Hotspots ;

        public class SceneTypeRequest implements Serializable
        {

            @SerializedName("sceneId")
            public String SceneId ;
            @SerializedName("type")
            public String Type ;

            public SceneTypeRequest() {

            }

            public String getSceneId() {
                return SceneId;
            }

            public void setSceneId(String sceneId) {
                SceneId = sceneId;
            }

            public String getType() {
                return Type;
            }

            public void setType(String type) {
                Type = type;
            }
        }

        public class Hotspot implements Serializable
        {
            @SerializedName("sceneId")
            public String SceneId ;
            @SerializedName("atv")
            public double ATV ;
            @SerializedName("ath")
            public double ATH ;
            @SerializedName("action")
            public Actions Action ;

            public void setAction(Actions action) {
                Action = action;
            }

            public class Actions implements Serializable
            {
                @SerializedName("loadScene")
                public LoadScene LoadScene;

                public class LoadScene implements Serializable
                {
                    public String getSceneIds() {
                        return SceneIds;
                    }

                    public void setSceneIds(String sceneIds) {
                        SceneIds = sceneIds;
                    }

                    @SerializedName("sceneId")
                    public String SceneIds ;

                    public LoadScene(){

                    }
                }

                public Actions(){

                }


                public Actions.LoadScene getLoadScene() {
                    return LoadScene;
                }

                public void setLoadScene(Actions.LoadScene loadScene) {
                    LoadScene = loadScene;
                }

            }

            public String getSceneId() {
                return SceneId;
            }

            public void setSceneId(String sceneId) {
                SceneId = sceneId;
            }

            public double getATV() {
                return ATV;
            }

            public void setATV(double ATV) {
                this.ATV = ATV;
            }

            public double getATH() {
                return ATH;
            }

            public void setATH(double ATH) {
                this.ATH = ATH;
            }

            public Hotspot(){

            }
        }



    }
}










