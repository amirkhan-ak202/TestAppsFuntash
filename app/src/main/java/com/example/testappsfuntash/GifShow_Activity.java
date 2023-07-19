package com.example.testappsfuntash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
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
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GifShow_Activity extends AppCompatActivity {
ImageView imageView;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    LinearLayout lineargif_shareapp, lineargif2_saveimg;
    LinearLayout giftshowcontainer;
    String murl;
    private InterstitialAd mInterstitialAd;
    SharedPreferences sharedPreferences;
    String banner_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_show);
        lineargif_shareapp = findViewById(R.id.linear1_gif);
        lineargif2_saveimg = findViewById(R.id.linear2_save);
       // lineargif3_apply = findViewById(R.id.linear3);
        imageView = findViewById(R.id.gifshow);
        murl =getIntent().getStringExtra("url");
        sharedPreferences =getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        MobileAds.initialize(this);
       giftshowcontainer = findViewById(R.id.giftshowcontianer);
        banner_id = sharedPreferences.getString("banner_id", "");
        Toast.makeText(this, banner_id, Toast.LENGTH_SHORT).show();
        AdsGiftShowdata();

        //load image using glide from api
        Glide.with(this)
                .load(murl)
                .apply(RequestOptions.centerCropTransform())
                .into(imageView);

//        lineargif3_apply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Apply the effect when the button is clicked
//                //setWallpapers();
//                Toast.makeText(GifShow_Activity.this, "Wallpaper set Sucessfully", Toast.LENGTH_SHORT).show();
//            }
//        });

        //save image in gallery
        lineargif2_saveimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveGifToGallery(GifShow_Activity.this,imageView);
               // saveGifImageFromUrl();

                String imageUrl = murl;
                String fileName = "my_gif_image.gif";
                ImageUtils.saveGifFromUrl(getApplicationContext(), imageUrl, fileName);



            }
        });

        lineargif_shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String filePath = "/storage/emulated/0/Pictures/my_gif_image.gif";
//                String description = "Check out this amazing GIF!";
//                ShareUtils.shareGifImage(GifShow_Activity.this, filePath, description);
               // shareGifImage();
                File imageFile = new File(getCacheDir(), "images/image.gif");

// Create the intent with ACTION_SEND
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

// Set the type of the data to "image/gif"
                shareIntent.setType("image/gif");

// Attach the GIF image file to the intent using a FileProvider
                Uri gifUri = FileProvider.getUriForFile(GifShow_Activity.this, "com.example.testappsfuntash.fileprovider", imageFile);
                shareIntent.putExtra(Intent.EXTRA_STREAM, gifUri);

// Add optional text or subject to the intent
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this GIF!");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing GIF");

// Specify that the intent should be used for sharing to social media apps only
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setPackage("com.android.providers.media");

// Start the intent chooser to allow the user to select a social media app to share the GIF image with
                startActivity(Intent.createChooser(shareIntent, "Share GIF via"));

            }
        });

    }
    private void shareGifImage() {
        String imageUrl = murl;
        String description = "Check out this cool GIF image!";
        String link = "https://example.com";

        shareGifImageFromUrl(getApplicationContext(), imageUrl, description, link);
    }
    private void saveGifToGallery(byte[] gifBytes) {
        String fileName = "GifImage_" + System.currentTimeMillis() + ".gif";
        OutputStream outputStream;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/gif");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                outputStream = resolver.openOutputStream(imageUri);
                outputStream.write(gifBytes);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File file = new File(directory, fileName);
            try {
                outputStream = new FileOutputStream(file);
                outputStream.write(gifBytes);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setWallpapers() {




//        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
//        try {
//            wallpaperManager.setBitmap(bitmap);
//
//
//        }catch (Exception e){
//
//        }
//        Drawable drawable = imageView.getDrawable();
//        if (drawable instanceof GifDrawable) {
//            GifDrawable gifDrawable = (GifDrawable) drawable;
//            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
//            try {
//                InputStream inputStream = gifDrawable.getInputStream();
//                wallpaperManager.setStream(inputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    }
    private static class DownloadAndShareTask extends AsyncTask<String, Void, Bitmap> {
        private Context context;
        private String description;
        private String link;

        public DownloadAndShareTask(Context context, String description, String link) {
            this.context = context;
            this.description = description;
            this.link = link;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            try {
                // Create a connection to the URL
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                // Read the input stream and decode the GIF image
                InputStream inputStream = connection.getInputStream();
                Bitmap gifImage = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                return gifImage;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap gifImage) {
            if (gifImage != null) {
                // Save the GIF image to a temporary file
                try {
                    File file = new File(context.getExternalCacheDir(), "temp.gif");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    gifImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    // Create an Intent to share the image
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add the FLAG_ACTIVITY_NEW_TASK flag
                    intent.setType("image/gif");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.putExtra(Intent.EXTRA_TEXT, description + "\n\n" + link);

                    // Start the sharing activity
                    context.startActivity(Intent.createChooser(intent, "Share GIF image"));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed to share GIF image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Failed to download GIF image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void shareGifImageFromUrl(Context context, String imageUrl, String description, String link) {
        new DownloadAndShareTask(context, description, link).execute(imageUrl);
    }

        private void saveGifImageFromUrl() {
            String imageUrl = murl;
            String fileName = "my_gif_image.gif";
            GifImageSaver.saveGifImageFromUrl(getApplicationContext(), imageUrl, fileName);
    }
    public void AdsGiftShowdata(){
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(banner_id);
        giftshowcontainer.addView(adView);
        adView.loadAd(adRequest);
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(GifShow_Activity.this, "Failed to load", Toast.LENGTH_SHORT).show();

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
                            mInterstitialAd.show(GifShow_Activity.this);
                        else
                            Toast.makeText(GifShow_Activity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                },1000);
            }
        });
    }
}