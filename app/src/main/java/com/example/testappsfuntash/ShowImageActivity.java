package com.example.testappsfuntash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShowImageActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    LinearLayout linear1_shareapp, linear2_saveimg, linear3_apply;
    String murl;
    ImageView imageView;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private AdView adView;
    //bannerAdView.setAdUnitId("your_interstitial_ad_unit_id");
    private InterstitialAd mInterstitialAd;

    LinearLayout pictureshowContianer;
    SharedPreferences sharedPreferences;
    String banner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        linear1_shareapp = findViewById(R.id.linear1);
        linear2_saveimg = findViewById(R.id.linear2);
        linear3_apply = findViewById(R.id.linear3);
        imageView = findViewById(R.id.imageshow);
        pictureshowContianer = findViewById(R.id.picshowcontainer);
        sharedPreferences =getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        banner_id = sharedPreferences.getString("banner_id", "");
        Toast.makeText(this, banner_id, Toast.LENGTH_SHORT).show();
        MobileAds.initialize(this);

        //loads ads
        AdsLoad_Data();

        murl =getIntent().getStringExtra("url");

        linear3_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply the effect when the button is clicked
                setWallpapers();
                Toast.makeText(ShowImageActivity.this, "Wallpaper set Sucessfully", Toast.LENGTH_SHORT).show();
            }
        });


        //save image in gallery
        linear2_saveimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                saveImageToGallery(bitmap);

                Toast.makeText(ShowImageActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();

            }
        });



        // Load the image using Glide
        Glide.with(this)
                .load(murl)
                .apply(RequestOptions.centerCropTransform())
                .into(imageView);


        linear1_shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //shareApp();
                shareImage();
            }
        });


    }



    private void shareImage() {
        // Get the bitmap from the ImageView
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        try {
            // Create a temporary file to store the image
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs(); // Make sure directory exists
            File imageFile = new File(cachePath, "image.jpg");
            String packageName = getPackageName();
            String playStoreLink = "https://play.google.com/store/apps/details?id=" + packageName;
            // Write the bitmap to the file
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();

            // Get the content URI of the image file
            Uri imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", imageFile);

            // Create an intent to share the image
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_TEXT, "Share this awesome apps");
            intent.putExtra(Intent.EXTRA_TEXT,playStoreLink);

            // Start the sharing activity
            startActivity(Intent.createChooser(intent, "Share Image"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Save image in gallery
    private void requestWriteExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                //saveImageToGallery();
            }
        } else {
            //saveImageToGallery();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // saveImageToGallery();
            } else {
                Toast.makeText(this, "Write external storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap getImageBitmap() {
        // Implement the logic to retrieve the bitmap of the image you want to save
        // Replace this with your actual implementation
        // For example, you can load the image from a file or URL
        try {
            return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveImageToGallery(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName);

        try {
            OutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Refresh the gallery to display the saved image
            MediaScannerConnection.scanFile(
                    getApplicationContext(),
                    new String[]{imageFile.getAbsolutePath()},
                    null,
                    null
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWallpapers(){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(bitmap);


        }catch (Exception e){

        }
    }
    private void AdsLoad_Data() {
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
                Toast.makeText(ShowImageActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();

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
                            mInterstitialAd.show(ShowImageActivity.this);
                        else
                            Toast.makeText(ShowImageActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                },1000);
            }
        });
    }
}