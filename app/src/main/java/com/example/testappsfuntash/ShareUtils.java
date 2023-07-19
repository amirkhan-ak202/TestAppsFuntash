package com.example.testappsfuntash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class ShareUtils
{
    public static void shareGifImage(Context context, String filePath, String description) {
        File gifFile = new File(filePath);

        // Create the URI from the file path
        Uri gifUri = Uri.fromFile(gifFile);

        // Create a share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/gif");
        shareIntent.putExtra(Intent.EXTRA_STREAM, gifUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, description);

        // Start the share activity
        context.startActivity(Intent.createChooser(shareIntent, "Share GIF Image"));
    }
}
