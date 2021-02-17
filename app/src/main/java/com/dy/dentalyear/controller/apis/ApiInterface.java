package com.dy.dentalyear.controller.apis;

import com.dy.dentalyear.model.api.PromptResponse;
import com.dy.dentalyear.model.api.SponsorsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.dy.dentalyear.model.constant.ApiConstants.PROMPT_API;
import static com.dy.dentalyear.model.constant.ApiConstants.SPONSOR_API;

public interface ApiInterface {
    @GET(PROMPT_API)
    Call<ArrayList<PromptResponse>> getPromptByDate(@Query("filter[meta_key]") String name, @Query("filter[meta_value]") String date);

    @GET(SPONSOR_API)
    Call<ArrayList<SponsorsResponse>> getAllSponsors();


}
