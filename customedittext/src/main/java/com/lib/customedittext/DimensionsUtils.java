package com.lib.customedittext;

import android.content.Context;
import android.support.annotation.DimenRes;

/**
 * @author Kailash Chouhan
 */

public class DimensionsUtils {
    private DimensionsUtils() throws InstantiationException {
        throw new InstantiationException("This utility class is created for instantiation");
    }

    public static float getDimension(Context context, @DimenRes int resourceId) {
        return context.getResources().getDimension(resourceId);
    }

    static int getDimensionPixelSize(Context context, @DimenRes int resourceId) {
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private static float scale;

    public static int dpToPixel(float dp, Context context) {
        if (scale == 0) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dp * scale);
    }
}
