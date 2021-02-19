package com.dy.dentalyear.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideoResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("acf")
    private ACF acf;

    private boolean selected;


    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public ACF getAcf() {
        return acf;
    }

    public boolean isSelected() {
        return selected;
    }

    public class ACF {
        @SerializedName("video_title")
        private String video_title;
        @SerializedName("video_link")
        private String video_link;
        @SerializedName("download_video_link")
        private String download_video_link;
        @SerializedName("category")
        private ArrayList<Category> category;
        @SerializedName("thumb_image")
        private String thumb_image;
        @SerializedName("purchase_id")
        private String purchase_id;
        @SerializedName("duration")
        private String duration;

        public String getVideo_title() {
            return video_title;
        }

        public String getVideo_link() {
            return video_link;
        }

        public String getDownload_video_link() {
            return download_video_link;
        }

        public ArrayList<Category> getCategory() {
            return category;
        }

        public String getThumb_image() {
            return thumb_image;
        }

        public String getPurchase_id() {
            return purchase_id;
        }

        public String getDuration() {
            return duration;
        }
    }

    public class Category {
        @SerializedName("ID")
        private int id;
        @SerializedName("post_title")
        private String post_title;

        public int getId() {
            return id;
        }

        public String getPost_title() {
            return post_title;
        }
    }
}

