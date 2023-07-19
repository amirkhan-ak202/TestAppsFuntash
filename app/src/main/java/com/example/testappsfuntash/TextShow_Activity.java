package com.example.testappsfuntash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testappsfuntash.api.ApiInterface;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class TextShow_Activity extends AppCompatActivity {
String imagepath,title_text;
TextView text_title;
ImageView imageView;
private InterstitialAd mInterstitialAd;
LinearLayout textshowContainer;
SharedPreferences sharedPreferences;
String banner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_show);
        Intent intent = getIntent();
        imagepath = intent.getStringExtra("url");
        title_text = intent.getStringExtra("title");
        text_title = findViewById(R.id.text1);
        imageView = findViewById(R.id.img2);
        MobileAds.initialize(this);
        textshowContainer = findViewById(R.id.textshowcontainer);
        sharedPreferences =getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        banner_id = sharedPreferences.getString("banner_id", "");
        Toast.makeText(this, banner_id, Toast.LENGTH_SHORT).show();

        AdsTextShowdata();


        Toast.makeText(this, imagepath+"showing", Toast.LENGTH_SHORT).show();
        Glide.with(this).load(imagepath).into(imageView);
        text_title.setText(text_title.getText());






    }
    public void AdsTextShowdata(){
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(banner_id);
        textshowContainer.addView(adView);
        adView.loadAd(adRequest);
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(TextShow_Activity.this, "Failed to load", Toast.LENGTH_SHORT).show();

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
                            mInterstitialAd.show(TextShow_Activity.this);
                        else
                            Toast.makeText(TextShow_Activity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                },1000);
            }
        });
    }
}