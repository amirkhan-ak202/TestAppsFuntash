package com.example.testappsfuntash;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VideoShareManager
{
    private static final String DOWNLOAD_DIRECTORY = "MyVideos";

    private final Context context;
    private final String videoUrl;
    private DownloadManager downloadManager;
    private long downloadId;

    public VideoShareManager(Context context, String videoUrl) {
        this.context = context;
        this.videoUrl = videoUrl;
    }

    public void downloadAndShare() {
        Uri videoUri = Uri.parse(videoUrl);
        DownloadManager.Request request = new DownloadManager.Request(videoUri);

        // Set the destination directory for the downloaded video
        String fileName = "video.mp4";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, DOWNLOAD_DIRECTORY + "/" + fileName);

        // Set notification visibility to show download progress
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Enqueue the download
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadId = downloadManager.enqueue(request);

            // Register a BroadcastReceiver to listen for download completion
            context.registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (completedDownloadId == downloadId) {
                // Unregister the BroadcastReceiver
                context.unregisterReceiver(downloadCompleteReceiver);

                // Get the URI of the downloaded video
                Uri videoUri = downloadManager.getUriForDownloadedFile(downloadId);

                // Share the video
                shareVideo(videoUri);
            }
        }
    };

    private void shareVideo(Uri videoUri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);

        context.startActivity(Intent.createChooser(shareIntent, "Share Video"));
    }
}
