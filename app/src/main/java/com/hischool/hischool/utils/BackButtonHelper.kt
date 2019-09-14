package com.hischool.hischool.utils

import android.app.Activity
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.widget.Button

object BackButtonHelper {
    fun setupButton(activity: Activity, button: Button) {
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

        button.setOnClickListener {
            activity.onBackPressed()
        }
    }
}