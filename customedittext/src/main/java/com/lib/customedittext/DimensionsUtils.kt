package com.lib.customedittext

import android.content.Context
import android.support.annotation.DimenRes

/**
 * @author Kailash Chouhan
 */

object DimensionsUtils {

    private var scale: Float = 0.toFloat()

    fun getDimension(context: Context, @DimenRes resourceId: Int): Float {
        return context.resources.getDimension(resourceId)
    }

    internal fun getDimensionPixelSize(context: Context, @DimenRes resourceId: Int): Int {
        return context.resources.getDimensionPixelSize(resourceId)
    }

    fun dpToPixel(dp: Float, context: Context): Int {
        if (scale == 0f) {
            scale = context.resources.displayMetrics.density
        }
        return (dp * scale).toInt()
    }
}
