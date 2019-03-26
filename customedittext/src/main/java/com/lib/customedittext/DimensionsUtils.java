package com.lib.customedittext;

import android.content.Context;
import android.support.annotation.DimenRes;

/**
 * @author Kailash Chouhan
 * creationDate 07-Mar-18
 * Copyright © 2018 SynsoftGlobal. All rights reserved.
 */

public class DimensionsUtils {
    private DimensionsUtils() throws InstantiationException {
        throw new InstantiationException("This utility class is created for instantiation");
    }

    public static float getDimension(Context context, @DimenRes int resourceId) {
        return context.getResources().getDimension(resourceId);
    }

    public static int getDimensionPixelSize(Context context, @DimenRes int resourceId) {
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
