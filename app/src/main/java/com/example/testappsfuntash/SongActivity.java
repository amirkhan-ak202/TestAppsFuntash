package com.example.testappsfuntash;

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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testappsfuntash.Adapter.PicAdapter;
import com.example.testappsfuntash.Adapter.SongAdapter;
import com.example.testappsfuntash.Model.SongModel;
import com.example.testappsfuntash.Model.VideoModel;
import com.example.testappsfuntash.Model.picModel;
import com.example.testappsfuntash.api.ApiInterface;
import com.example.testappsfuntash.api.Retrofit_Clients;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongActivity extends AppCompatActivity {
    RecyclerView recyclersong;
    SongAdapter songadapter;
    ApiInterface apiInterface;
    List<SongModel.Datum> songdata;
    private InterstitialAd mInterstitialAd;
    LinearLayout pictureshowContianer;
    SharedPreferences sharedPreferences;
    String banner_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        recyclersong = findViewById(R.id.recyclerviewsong);
        MobileAds.initialize(this);
        pictureshowContianer = findViewById(R.id.songContainer);
        sharedPreferences =getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        banner_id = sharedPreferences.getString("banner_id", "");
        Toast.makeText(this, banner_id, Toast.LENGTH_SHORT).show();
        //loads ads
        AdsSongdata();


      //  songadapter = new SongAdapter(songlists,this);
        recyclersong.setLayoutManager(new GridLayoutManager(this,2));
        apiInterface = Retrofit_Clients.getRetrofitInstance().create(ApiInterface.class);
        Call<SongModel> songlists = apiInterface.getSong("song");

        songlists.enqueue(new Callback<SongModel>() {
            @Override
            public void onResponse(Call<SongModel> call, Response<SongModel> response) {
                if (response.isSuccessful()){
                    SongModel songsmodel= response.body();
                    songdata = songsmodel.getData();
                    if (songdata !=null){
                        songadapter = new SongAdapter(songdata,SongActivity.this);
                        recyclersong.setAdapter(songadapter);
                        songadapter.setSongItemClickListener(new SongAdapter.SongItemClickListener() {
                            @Override
                            public void onClick(View v, int position) {
                                Intent intent = new Intent(SongActivity.this,SongShowActivity.class);
                                intent.putExtra("song_url",Constants.images_path+songdata.get(position).getFile());
                                intent.putExtra("song",songdata.get(position).getTitle());
                                //next song are play at songshowactivty next button clicked
                                startActivity(intent);



                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<SongModel> call, Throwable t) {

            }
        });





    }
    public void AdsSongdata()
    {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(banner_id);
        pictureshowContianer.addView(adView);
        adView.loadAd(adRequest);
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(SongActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();

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
                            mInterstitialAd.show(SongActivity.this);
                        else
                            Toast.makeText(SongActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                },1000);
            }
        });
    }
}