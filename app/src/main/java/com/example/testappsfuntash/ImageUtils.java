package com.example.testappsfuntash;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 123;

    public static void saveGifFromUrl(Context context, String imageUrl, String fileName) {
        if (checkWriteExternalStoragePermission(context)) {
            new DownloadGifTask(context, fileName).execute(imageUrl);
        }
    }

    private static boolean checkWriteExternalStoragePermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((GifShow_Activity) context,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }

    private static class DownloadGifTask extends AsyncTask<String, Void, Bitmap> {
        private Context context;
        private String fileName;

        DownloadGifTask(Context context, String fileName) {
            this.context = context;
            this.fileName = fileName;
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
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap gifBitmap) {
            if (gifBitmap != null) {
                saveGifToGallery(context, gifBitmap, fileName);
            } else {
                Toast.makeText(context, "Failed to download GIF image", Toast.LENGTH_SHORT).show();
            }
        }

        private void saveGifToGallery(Context context, Bitmap gifBitmap, String fileName) {
            File galleryDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(galleryDirectory, fileName);

            try {
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                gifBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
                Toast.makeText(context, "GIF image saved successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to save GIF image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
