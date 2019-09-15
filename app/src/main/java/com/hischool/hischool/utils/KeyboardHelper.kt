package com.hischool.hischool.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardHelper {
    fun hideKeyboard(actvity: Activity) {
        val view = actvity.currentFocus
        view?.let { v ->
            val imm = actvity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
        }
    }
}