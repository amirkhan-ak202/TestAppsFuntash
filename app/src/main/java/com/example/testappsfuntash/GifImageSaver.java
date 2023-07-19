package com.example.testappsfuntash;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GifImageSaver {

    public static void saveGifImageFromUrl(Context context, String imageUrl, String fileName) {
        try {
            // Create a connection to the URL
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            // Read the input stream and decode the GIF image
            InputStream inputStream = connection.getInputStream();
            Bitmap gifImage = BitmapFactory.decodeStream(inputStream);

            // Get the directory path for saving the image
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it doesn't exist
            }

            File file = new File(directory, fileName);

            // Save the GIF image to file
            OutputStream outputStream = new FileOutputStream(file);
            gifImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Add the saved image to the gallery
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, fileName);
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/gif");
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());

            Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            if (imageUri != null) {
                // Image saved successfully
                Toast.makeText(context, "GIF image saved to Gallery", Toast.LENGTH_SHORT).show();
            } else {
                // Failed to save image
                Toast.makeText(context, "Failed to save GIF image", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save GIF image", Toast.LENGTH_SHORT).show();
        }
    }
}

