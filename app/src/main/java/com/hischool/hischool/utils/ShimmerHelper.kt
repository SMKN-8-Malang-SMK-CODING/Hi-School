package com.hischool.hischool.utils

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout

object ShimmerHelper {
    fun stopShimmer(shimmer: ShimmerFrameLayout) {
        shimmer.stopShimmer()
        shimmer.clearAnimation()
        shimmer.visibility = View.GONE
    }
}