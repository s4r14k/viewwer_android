    package com.priscilla.viewwer.model;

    import android.os.Parcel;
    import android.os.Parcelable;
    import android.widget.ImageView;

    import com.google.gson.annotations.Expose;
    import com.google.gson.annotations.SerializedName;

    public class HotSpot implements Parcelable
    {
        @SerializedName("image")
        @Expose
        private String image;

        @SerializedName("hotspotLeadTo")
        @Expose
        private int hotspotLeadTo;

        @SerializedName("hotspot")
        @Expose
        private ImageView hotspot;

        @SerializedName("sceneId")
        public String SceneId ;
        @SerializedName("atv")
        public double ATV ;
        @SerializedName("ath")
        public double ATH ;
        @SerializedName("action")
        public ClickAction Action ;


        public HotSpot(){

        }

        public class ClickAction
        {
            @SerializedName("loadScene")
            public LoadScene LoadScene;

            public class LoadScene {
                @SerializedName("sceneId")
                public String SceneId ;
            }
        }



        protected HotSpot(Parcel in) {
            image = in.readString();
            hotspotLeadTo = in.readInt();
            SceneId = in.readString();
            ATV = in.readDouble();
            ATH = in.readDouble();
        }

        public static final Creator<HotSpot> CREATOR = new Creator<HotSpot>() {
            @Override
            public HotSpot createFromParcel(Parcel in) {
                return new HotSpot(in);
            }

            @Override
            public HotSpot[] newArray(int size) {
                return new HotSpot[size];
            }
        };

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

        public ClickAction getAction() {
            return Action;
        }

        public void setAction(ClickAction action) {
            Action = action;
        }
        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }


        public int getHotspotLeadTo() {
            return hotspotLeadTo;
        }

        public void setHotspotLeadTo(int hotspotLeadTo) {
            this.hotspotLeadTo = hotspotLeadTo;
        }

        public ImageView getHotspot() {
            return hotspot;
        }

        public void setHotspot(ImageView hotspot) {
            this.hotspot = hotspot;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(image);
            parcel.writeInt(hotspotLeadTo);
            parcel.writeString(SceneId);
            parcel.writeDouble(ATV);
            parcel.writeDouble(ATH);
        }


    }
