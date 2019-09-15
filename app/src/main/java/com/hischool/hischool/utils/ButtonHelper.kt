package com.hischool.hischool.utils

import android.app.Activity
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.widget.Button

object ButtonHelper {
    fun setupBackButton(activity: Activity, button: Button) {
        setupWideClick(button, View.OnClickListener {
            activity.onBackPressed()
        })
    }

    fun setupWideClick(button: View, onClickListener: View.OnClickListener) {
        val parent = button.parent as View  // button: the view you want to enlarge hit area

        parent.post {
            val rect = Rect()

            button.getHitRect(rect)

            rect.top -= 100    // increase top hit area
            rect.left -= 100   // increase left hit area
            rect.bottom += 100 // increase bottom hit area
            rect.right += 100  // increase right hit area

            parent.touchDelegate = TouchDelegate(rect, button)
        }

        button.setOnClickListener(onClickListener)
    }
}