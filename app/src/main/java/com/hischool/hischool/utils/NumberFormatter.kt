package com.hischool.hischool.utils

import android.content.Context
import android.util.DisplayMetrics
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


object NumberFormatter {
    fun formatRupiah(number: Int): String {
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        return formatRupiah.format(number.toDouble())
    }

    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("EEE, d MMM hh:mm", Locale.getDefault())

        return dateFormat.format(date)
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}
