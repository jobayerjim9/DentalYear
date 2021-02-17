package com.dy.dentalyear.model.api;

import com.google.gson.annotations.SerializedName;

public class SponsorsResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("acf")
    private ACF acf;

    public int getId() {
        return id;
    }

    public ACF getAcf() {
        return acf;
    }

    public class ACF {
        @SerializedName("sponsor_name")
        private String sponsor_name;
        @SerializedName("sponsor_message")
        private String sponsor_message;
        @SerializedName("sponsor_link")
        private String sponsor_link;
        @SerializedName("sponsor_logo")
        private String sponsor_logo;

        public String getSponsor_name() {
            return sponsor_name;
        }

        public String getSponsor_message() {
            return sponsor_message;
        }

        public String getSponsor_link() {
            return sponsor_link;
        }

        public String getSponsor_logo() {
            return sponsor_logo;
        }
    }
}
