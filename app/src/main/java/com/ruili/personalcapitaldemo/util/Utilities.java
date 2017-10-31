package com.ruili.personalcapitaldemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rui Li on 10/30/17.
 */

public class Utilities {
    /**
     * This method returns a Bitmap representation of an image from a url
     * @param src url for an image resource
     * @return bitmap for url
     */
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * This method converts pixel values to dp values
     * @param context
     * @param pixel
     * @return
     */
    public static float pxToDp(Context context, int pixel) {
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        return pixel / ((float) densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
