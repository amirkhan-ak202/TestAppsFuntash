package com.example.testappsfuntash.api;

import com.example.testappsfuntash.Adapter.MyAdapter;
import com.example.testappsfuntash.Model.AdsModel;
import com.example.testappsfuntash.Model.CategoryModel;
import com.example.testappsfuntash.Model.Gif_Model;
import com.example.testappsfuntash.Model.SongModel;
import com.example.testappsfuntash.Model.Text_Model;
import com.example.testappsfuntash.Model.VideoModel;
import com.example.testappsfuntash.Model.picModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface
{
    @FormUrlEncoded
    @POST("get-wallpaper")
    Call<picModel> getImages(
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("get-wallpaper")
    Call<VideoModel> getVideos(@Field("type") String type);

    @FormUrlEncoded
    @POST("get-wallpaper")
    Call<SongModel> getSong(@Field("type") String type);

    @FormUrlEncoded
    @POST("get-wallpaper")
    Call<Text_Model> getText(@Field("type") String type);

    @FormUrlEncoded
    @POST("get-wallpaper")
    Call<Gif_Model> getGif(@Field("type") String type);


//for category endpoint

    @GET("get-category")
    Call<CategoryModel> getCategory();

    @GET("ads")
    Call<AdsModel> getAdsData();
}
