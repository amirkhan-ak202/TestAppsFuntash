package com.example.testappsfuntash;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testappsfuntash.Adapter.MyAdapter;
import com.example.testappsfuntash.Adapter.PicAdapter;
import com.example.testappsfuntash.Model.picModel;
import com.example.testappsfuntash.api.ApiInterface;
import com.example.testappsfuntash.api.Retrofit_Clients;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PictureActivity extends AppCompatActivity {
    RecyclerView mrecyclerView;
    PicAdapter madapter;
    ApiInterface apiInterface;
    List<picModel.Datum> imagesdata;
    private InterstitialAd mInterstitialAd;
    LinearLayout pictureContianer;
    SharedPreferences sharedPreferences;
    String banner_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        pictureContianer = findViewById(R.id.pictureContainer);
        mrecyclerView = findViewById(R.id.recyclerive_pic);
        sharedPreferences =getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        banner_id = sharedPreferences.getString("banner_id", "");
        Toast.makeText(this, banner_id, Toast.LENGTH_SHORT).show();
        //loads ad
       AdsLoadData();


        apiInterface = Retrofit_Clients.getRetrofitInstance().create(ApiInterface.class);


        mrecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        Call<picModel> mlists = apiInterface.getImages("image");

        mlists.enqueue(new Callback<picModel>() {
                           @Override
                           public void onResponse(Call<picModel> call, Response<picModel> response) {
                               if (response.isSuccessful()) {
                                   picModel picModel = response.body();
                                   Log.e(TAG, "onResponse: "+picModel.toString() );
                                   imagesdata = picModel.getData();
                                   if (imagesdata != null) {
                                       madapter = new PicAdapter(imagesdata, PictureActivity.this);
                                       mrecyclerView.setAdapter(madapter);
                                       madapter.setPicclickListner(new PicAdapter.picclickListner() {
                                           @Override
                                           public void onClick(View v, int position) {
                                               //startActivity(new Intent(PictureActivity.this, ShowImageActivity.class));
                                               Intent mintent = new Intent(PictureActivity.this,ShowImageActivity.class);
                                               mintent.putExtra("url",Constants.images_path+imagesdata.get(position).getFile());
                                               startActivity(mintent);

                                           }
                                       });
                                   }
                               } else {
                                   Toast.makeText(PictureActivity.this, "Error", Toast.LENGTH_SHORT).show();


                               }
                           }

                           @Override
                           public void onFailure(Call<picModel> call, Throwable t) {
                               Toast.makeText(PictureActivity.this, "Error showing", Toast.LENGTH_SHORT).show();

                           }
                       });





    }
    public void AdsLoadData(){
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(banner_id);
        pictureContianer.addView(adView);
        adView.loadAd(adRequest);
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(PictureActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        mInterstitialAd = null;

                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mInterstitialAd!= null)
                            mInterstitialAd.show(PictureActivity.this);
                        else
                            Toast.makeText(PictureActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                },1000);
            }
        });
    }
}