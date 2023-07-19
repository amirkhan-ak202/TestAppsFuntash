package com.example.testappsfuntash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testappsfuntash.Adapter.GifAdapter;
import com.example.testappsfuntash.Adapter.PicAdapter;
import com.example.testappsfuntash.Model.Gif_Model;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GifActivity extends AppCompatActivity {
    RecyclerView mrecyclerView;
    GifAdapter madapter;
    ApiInterface apiInterface;
    List<Gif_Model.Datum> gifsdata;
    private InterstitialAd mInterstitialAd;
    SharedPreferences sharedPreferences;
    String banner_id;
    private LinearLayout linearLayout_gift;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        mrecyclerView = findViewById(R.id.recyclerive_gif);
        linearLayout_gift = findViewById(R.id.gift_container);
        apiInterface = Retrofit_Clients.getRetrofitInstance().create(ApiInterface.class);
        sharedPreferences =getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        MobileAds.initialize(this);
        banner_id = sharedPreferences.getString("banner_id", "");
        Toast.makeText(this, banner_id, Toast.LENGTH_SHORT).show();
        AdsGiftsdata();



        mrecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        Call<Gif_Model> mlists = apiInterface.getGif("gif");

        mlists.enqueue(new Callback<Gif_Model>() {
            @Override
            public void onResponse(Call<Gif_Model> call, Response<Gif_Model> response) {
                if (response.isSuccessful()){
                    Gif_Model gif_model = response.body();
                    gifsdata = gif_model.getData();
                    madapter = new GifAdapter(gifsdata, GifActivity.this);
                    mrecyclerView.setAdapter(madapter);
                    madapter.setGfclicklistener(new GifAdapter.GifClickListener() {
                        @Override
                        public void onClick(View v, int position) {
                            Intent mintent = new Intent(GifActivity.this,GifShow_Activity.class);
                            mintent.putExtra("url",Constants.images_path+gifsdata.get(position).getFile());
                            startActivity(mintent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Gif_Model> call, Throwable t) {
                Toast.makeText(GifActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void AdsGiftsdata(){
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(banner_id);
        linearLayout_gift.addView(adView);
        adView.loadAd(adRequest);
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(GifActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();

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
                            mInterstitialAd.show(GifActivity.this);
                        else
                            Toast.makeText(GifActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                },1000);
            }
        });
    }
}