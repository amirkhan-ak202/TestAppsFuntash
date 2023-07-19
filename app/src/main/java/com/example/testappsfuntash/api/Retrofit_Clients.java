package com.example.testappsfuntash.api;

import com.example.testappsfuntash.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_Clients
{
    private static Retrofit retrofit;
    //private static String BASE_URL ="https://wallpaper.funtashtechnologies.com/public/api/";



    public static Retrofit getRetrofitInstance(){
        if (retrofit ==null){
            retrofit = new Retrofit.Builder().baseUrl(Constants.Base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
    public ApiInterface getApi()
    {
        return retrofit.create(ApiInterface.class);

    }
}
