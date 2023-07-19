package com.example.testappsfuntash;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testappsfuntash.Adapter.MyAdapter;
import com.example.testappsfuntash.Model.CategoryModel;
import com.example.testappsfuntash.Model.Model_Adapter;
import com.example.testappsfuntash.Model.picModel;
import com.example.testappsfuntash.api.ApiInterface;
import com.example.testappsfuntash.api.Retrofit_Clients;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView navView;
    Toolbar toolbar;
    RecyclerView recyclerView;
    MyAdapter adapter;
    LinearLayout share_apps, privacy_policy, rateus;
    private ApiInterface apiInterface;
    List<CategoryModel.Datum> categoryModellist;
    private AdView adView;
    //bannerAdView.setAdUnitId("your_interstitial_ad_unit_id");
    private InterstitialAd mInterstitialAd;
    SharedPreferences sharedPreferences;
    String banner_id;
    LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.my_drawer_layout);
        navView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        share_apps = findViewById(R.id.share_app);
        privacy_policy = findViewById(R.id.privacy_policy);
        rateus = findViewById(R.id.rate_us);
        linearLayout = findViewById(R.id.addcontainer);
        apiInterface = Retrofit_Clients.getRetrofitInstance().create(ApiInterface.class);
        sharedPreferences =getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        //adView = findViewById(R.id.adView);
//        interstitialAd = new InterstitialAd(this);
//        //interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
//        AdRequest adRequest = new AdRequest.Builder().build();
//        bannerAdView.loadAd(adRequest);
        MobileAds.initialize(this);

        banner_id = sharedPreferences.getString("banner_id", "");
        Toast.makeText(this, banner_id, Toast.LENGTH_SHORT).show();

        AdsData();

        //loadAd();


        //setAds();


        onclicklistenermethod();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);
        setupDrawer();


        Call<CategoryModel> list = apiInterface.getCategory();
        list.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.isSuccessful()) {
                    CategoryModel categoryModel = response.body();
                    categoryModellist = categoryModel.getData();

                    adapter = new MyAdapter(categoryModellist, MainActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(adapter);
                    adapter.setClicklistner(new MyAdapter.itemclickListner() {
                        @Override
                        public void onClick(View v, int position) {

                            if (categoryModellist.get(position).getTitle().toLowerCase().equals("image")) {

                                startActivity(new Intent(MainActivity.this, PictureActivity.class));
                            } else if (categoryModellist.get(position).getTitle().toLowerCase().equals("video")) {

                                startActivity(new Intent(MainActivity.this, VideosActivity.class));
                            } else if (categoryModellist.get(position).getTitle().toLowerCase().equals("text")) {
                                startActivity(new Intent(MainActivity.this, TextActivity.class));

                            } else if (categoryModellist.get(position).getTitle().toLowerCase().equals("song")) {
                                startActivity(new Intent(MainActivity.this, SongActivity.class));
                            } else if (categoryModellist.get(position).getTitle().toLowerCase().equals("gif")) {

                                startActivity(new Intent(MainActivity.this, GifActivity.class));

                            }

                        }
                    });

                }

            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });


        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.funtash.creations.testapps"));
                startActivity(rateIntent);
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawers();
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.freeprivacypolicy.com/blog/privacy-policy-url/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void setupDrawer() {
        // Show the burger button on the ActionBar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar,
                R.string.nav_open,
                R.string.nav_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);

    }

    public void onclicklistenermethod() {
        share_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id=com.funtash.creations.testapps";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                mDrawerLayout.closeDrawers();
            }
        });
    }
//    public void loadAd(){
//        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
//            @Override
//            public void onAdClicked() {
//                // Called when a click is recorded for an ad.
//                Log.d(TAG, "Ad was clicked.");
//            }
//
//            @Override
//            public void onAdDismissedFullScreenContent() {
//                // Called when ad is dismissed.
//                // Set the ad reference to null so you don't show the ad a second time.
//                Log.d(TAG, "Ad dismissed fullscreen content.");
//                mInterstitialAd = null;
//            }
//
//            @Override
//            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                // Called when ad fails to show.
//                Log.e(TAG, "Ad failed to show fullscreen content.");
//                mInterstitialAd = null;
//            }
//
//            @Override
//            public void onAdImpression() {
//                // Called when an impression is recorded for an ad.
//                Log.d(TAG, "Ad recorded an impression.");
//            }
//
//            @Override
//            public void onAdShowedFullScreenContent() {
//                // Called when ad is shown.
//                Log.d(TAG, "Ad showed fullscreen content.");
//            }
//        });
//    }

    public void setAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }
public void AdsData(){

    AdRequest adRequest = new AdRequest.Builder().build();
    AdView adView = new AdView(this);
    adView.setAdSize(AdSize.BANNER);
    adView.setAdUnitId(banner_id);
    linearLayout.addView(adView);
    adView.loadAd(adRequest);
    InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            Toast.makeText(MainActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();

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
                        mInterstitialAd.show(MainActivity.this);
                    else
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            },1000);
        }
    });
}
}