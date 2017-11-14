package com.ruili.personalcapitaldemo.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;

/**
 * Created by Rui Li on 10/30/17.
 */

public class Utilities {
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

    /**
     *
     * @return This method creates a white rectangle frame drawable
     */
    public static Drawable getGrayFrameBackground()
    {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(Color.WHITE);
        shape.setStroke(2, Color.DKGRAY);
        return shape;
    }
}
