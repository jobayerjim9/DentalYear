package com.dy.dentalyear.model.api;

import com.google.gson.annotations.SerializedName;

public class VideoCategoryResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("category_title")
    private String category_title;

    public int getId() {
        return id;
    }

    public String getCategory_title() {
        return category_title;
    }
}
