package com.example.testappsfuntash;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testappsfuntash.Model.AdsModel;
import com.example.testappsfuntash.api.ApiInterface;
import com.example.testappsfuntash.api.Retrofit_Clients;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private Timer timer;
    private ProgressBar progressBar;
    private int i=0;
    TextView textView;
    //InterstitialAd mInterstitialAd;
    ApiInterface apiInterface;
    List<AdsModel.Banner> bannerModelList;
    List<AdsModel.Interstitial> interstialModelList;
    List<AdsModel.Ad> dataModelList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        apiInterface = Retrofit_Clients.getRetrofitInstance().create(ApiInterface.class);
        progressBar= findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        textView= findViewById(R.id.textView);
        textView.setText("Loading");
        sharedPreferences=this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        final long period = 100;

        Call<AdsModel> listads = apiInterface.getAdsData();
        listads.enqueue(new Callback<AdsModel>() {
            @Override
            public void onResponse(Call<AdsModel> call, Response<AdsModel> response) {
                if (response.isSuccessful()){
                    AdsModel adsModel = response.body();
                    bannerModelList = adsModel.getData().getBanners();
                    interstialModelList = adsModel.getData().getInterstitals();
                    dataModelList = adsModel.getData().getAds();

                    String app_id = dataModelList.get(0).getAppId();
                    String banner_id = bannerModelList.get(0).getBannerAdUnit();
                    String interstitial_id = interstialModelList.get(0).getInterstitialAdUnit();

                    //Toast.makeText(SplashActivity.this,banner_id+" "+app_id +" "+interstitial_id, Toast.LENGTH_SHORT).show();

                    editor.putString("app_id", app_id);
                    editor.putString("banner_id", banner_id);
                    editor.putString("interstitial_id", interstitial_id);
                    editor.apply();

                }
            }

            @Override
            public void onFailure(Call<AdsModel> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Failed Ads", Toast.LENGTH_SHORT).show();

            }
        });







        timer=new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //this repeats every 100 ms
                if (i<100){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("Loading...");
                        }
                    });
                    progressBar.setProgress(i);
                    i++;
                }else{
                    //closing the timer
                    timer.cancel();
                    Intent intent =new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    // close this activity
                    finish();
                }
            }
        }, 0, period);





    }
}
