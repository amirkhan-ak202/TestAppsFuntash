package com.example.testappsfuntash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testappsfuntash.Adapter.MyAdapter;
import com.example.testappsfuntash.Adapter.TextAdapter;
import com.example.testappsfuntash.Model.Model_Adapter;
import com.example.testappsfuntash.Model.Text_Model;
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

public class TextActivity extends AppCompatActivity {
    RecyclerView recyclerText;
    TextAdapter textAdapter;
    ImageView img_back;
    ApiInterface apiInterface;
    List<Text_Model.Datum> modeldata;
    private InterstitialAd mInterstitialAd;
    LinearLayout textContainer;
    SharedPreferences sharedPreferences;
    String banner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        recyclerText = findViewById(R.id.recyclertext);
        img_back = findViewById(R.id.back);
        textContainer =findViewById(R.id.textContainer);
        recyclerText.setLayoutManager(new LinearLayoutManager(this));
        apiInterface = Retrofit_Clients.getRetrofitInstance().create(ApiInterface.class);
        sharedPreferences =getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        banner_id = sharedPreferences.getString("banner_id", "");
        Toast.makeText(this, banner_id, Toast.LENGTH_SHORT).show();
        MobileAds.initialize(this);

        AdsTextdata();



      Call<Text_Model> modellist = apiInterface.getText("text");
      modellist.enqueue(new Callback<Text_Model>() {
          @Override
          public void onResponse(Call<Text_Model> call, Response<Text_Model> response) {
              if (response.isSuccessful()){
                 Text_Model text_model = response.body();
                 modeldata = text_model.getData();
                 recyclerText.setLayoutManager(new LinearLayoutManager(TextActivity.this));
                 textAdapter= new TextAdapter(modeldata,TextActivity.this);
                 recyclerText.setAdapter(textAdapter);
                 textAdapter.setClicklistner(new TextAdapter.TextClicklistener() {
                     @Override
                     public void onClick(View v, int position) {
                         Intent in = new Intent(TextActivity.this,TextShow_Activity.class);
                         in.putExtra("url",Constants.images_path+modeldata.get(position).getFile());
                         in.putExtra("title",modeldata.get(position).getTitle());

                         startActivity(in);


                     }
                 });


              }
          }

          @Override
          public void onFailure(Call<Text_Model> call, Throwable t) {
              Toast.makeText(TextActivity.this, "Error", Toast.LENGTH_SHORT).show();


          }
      });







       // List<Text_Model> lists = new ArrayList<>();

       // lists.add(new Text_Model("https://img.freepik.com/free-photo/miami-night-scene_649448-5353.jpg","Simple Text","06/05/2023"));
       // textAdapter = new TextAdapter(lists,this);
        //recyclerText.setAdapter(textAdapter);









        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TextActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void AdsTextdata(){
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(banner_id);
        textContainer.addView(adView);
        adView.loadAd(adRequest);
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(TextActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();

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
                            mInterstitialAd.show(TextActivity.this);
                        else
                            Toast.makeText(TextActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                },1000);
            }
        });
    }
}