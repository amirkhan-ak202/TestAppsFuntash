package com.example.testappsfuntash;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testappsfuntash.Adapter.PicAdapter;
import com.example.testappsfuntash.Adapter.videosAdapter;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ApiInterface apiInterface;
    private videosAdapter videoAdapter;
    private List<VideoModel.Datum> videoUrls;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    private InterstitialAd mInterstitialAd;
    LinearLayout videoContainer;
    SharedPreferences sharedPreferences;
    String banner_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        apiInterface = Retrofit_Clients.getRetrofitInstance().create(ApiInterface.class);
        recyclerView = findViewById(R.id.recyView);
        MobileAds.initialize(this);
        videoContainer = findViewById(R.id.videoContainer);
        sharedPreferences =getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        banner_id = sharedPreferences.getString("banner_id", "");
        Toast.makeText(this, banner_id, Toast.LENGTH_SHORT).show();

        AdsVideosdata();



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Requestpermsion();


        Call<VideoModel> videoslist = apiInterface.getVideos("video");
        videoslist.enqueue(new Callback<VideoModel>() {
            @Override
            public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                if (response.isSuccessful()) {
                    VideoModel videoModel = response.body();
                    // Log.e(TAG, "onResponse: " + VideoModel.toString());
                    videoUrls = videoModel.getData();
                    if (videoUrls != null) {
                        videoAdapter = new videosAdapter(videoUrls, VideosActivity.this);
                        recyclerView.setAdapter(videoAdapter);


                        videoAdapter.setVidesclickListner(new videosAdapter.VidesclickListner() {
                            @Override
                            public void onClick(View v, int position) {
                                Intent intent = new Intent(VideosActivity.this, VidesoShowActivity.class);
                                // Get the URL as a string
                                // Add the URL as an extra to the intent
                                intent.putExtra("urlExtra", Constants.images_path + videoUrls.get(position).getFile());
                                // Start the receiving activity
                                startActivity(intent);
                            }

                            @Override
                            public void onSaveClick(View v, int position) {
                                String videoUrl = Constants.images_path + videoUrls.get(position).getFile();
                                downloadVideo(VideosActivity.this,videoUrl,"hello.mp4");
                               // DownloadVideoTask.downloadVideo(getApplicationContext(), videoUrl);
                               // Toast.makeText(VideosActivity.this, "Error", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onShareClick(View v, int position) {
                                String videoUrl = Constants.images_path + videoUrls.get(position).getFile();
//                                VideoShareManager.shareVideoFromUrl(getApplicationContext(), videoUrl);
                                //shareVideo();
                                VideoShareManager videoDownloader = new VideoShareManager(VideosActivity.this, videoUrl);
                                videoDownloader.downloadAndShare();
                                //new VideoShareManager(VideosActivity.this,videoUrl);
                                Toast.makeText(VideosActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                            }


                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<VideoModel> call, Throwable t) {
                Toast.makeText(VideosActivity.this, "Error showing", Toast.LENGTH_SHORT).show();

            }
        });


    }

        private static class DownloadVideoTask extends AsyncTask<String, Void, File> {
            private Context context;

            public DownloadVideoTask(Context context) {
                this.context = context;
            }

            @Override
            protected File doInBackground(String... urls) {
                String videoUrl = urls[0];
                try {
                    // Create a connection to the URL
                    URL url = new URL(videoUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();

                    // Create a temporary file to save the video
                    File videoFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), "temp.mp4");
                    FileOutputStream outputStream = new FileOutputStream(videoFile);

                    // Read the input stream and save the video file
                    InputStream inputStream = connection.getInputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.close();
                    inputStream.close();

                    return videoFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(File videoFile) {
                if (videoFile != null) {
                    Toast.makeText(context, "Video downloaded successfully", Toast.LENGTH_SHORT).show();
                    // You can now use the downloaded video file as needed
                } else {
                    Toast.makeText(context, "Failed to download video", Toast.LENGTH_SHORT).show();
                }
            }


        public static void downloadVideo(Context context, String videoUrl) {
            new DownloadVideoTask(context).execute(videoUrl);
        }
    }
    public static void downloadVideo(Context context, String videoUrl, String fileName) {
        Uri uri = Uri.parse(videoUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, fileName);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }
    public void Requestpermsion(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted, continue with your code
           // saveVideo();

        }


}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, continue with your code
                //  saveVideo();
            } else {
                // Permission denied
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void shareVideo() {
        // Replace "videoPath" with the actual path of the video file you want to share
        String videoPath = "/path/to/video.mp4";

        // Create a file object from the video path
        File videoFile = new File(videoPath);

        // Get the URI of the video file
        Uri videoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", videoFile);

        // Create an intent with ACTION_SEND
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Start the activity with the share intent
        startActivity(Intent.createChooser(shareIntent, "Share Video"));
    }
public void AdsVideosdata(){
    AdRequest adRequest = new AdRequest.Builder().build();
    AdView adView = new AdView(this);
    adView.setAdSize(AdSize.BANNER);
    adView.setAdUnitId(banner_id);
    videoContainer.addView(adView);
    adView.loadAd(adRequest);
    InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            Toast.makeText(VideosActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();

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
                        mInterstitialAd.show(VideosActivity.this);
                    else
                        Toast.makeText(VideosActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            },1000);
        }
    });
}
}


