package com.dy.dentalyear.controller.apis;

import com.dy.dentalyear.model.api.PromptResponse;
import com.dy.dentalyear.model.api.SponsorsResponse;
import com.dy.dentalyear.model.api.VideoCategoryResponse;
import com.dy.dentalyear.model.api.VideoResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.dy.dentalyear.model.constant.ApiConstants.ALL_VIDEO_API;
import static com.dy.dentalyear.model.constant.ApiConstants.PROMPT_API;
import static com.dy.dentalyear.model.constant.ApiConstants.SPONSOR_API;
import static com.dy.dentalyear.model.constant.ApiConstants.VIDEO_CATEGORY_API;

public interface ApiInterface {
    @GET(PROMPT_API)
    Call<ArrayList<PromptResponse>> getPromptByDate(@Query("filter[meta_key]") String name, @Query("filter[meta_value]") String date);

    @GET(SPONSOR_API)
    Call<ArrayList<SponsorsResponse>> getAllSponsors(@Query("per_page") int per_page);

    @GET(VIDEO_CATEGORY_API)
    Call<ArrayList<VideoCategoryResponse>> getAllVideoCategory();

    @GET(ALL_VIDEO_API)
    Call<ArrayList<VideoResponse>> getAllVideo();

}
