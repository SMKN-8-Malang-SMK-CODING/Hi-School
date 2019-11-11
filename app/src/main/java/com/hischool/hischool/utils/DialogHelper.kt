package com.hischool.hischool.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

object DialogHelper {
    fun yesNoDialog(
        context: Context,
        message: String,
        yesMessage: String = "Ya",
        noMessage: String = "Tidak",
        title: String = "",
        listener: YesNoListener
    ) {
        val builder = AlertDialog.Builder(context)

        builder.setMessage(message).setCancelable(true)
            .setPositiveButton(yesMessage) { dialog, which ->
                listener.onYes(dialog, which)
            }
            .setNegativeButton(noMessage) { dialog, which ->
                //                listener.onNo(dialog, which)
            }

        if (title.isNotEmpty()) {
            builder.setTitle(title)
        }

        builder.create().show()
    }

    interface YesNoListener {
        fun onYes(dialog: DialogInterface, id: Int)
//        fun onNo(dialog: DialogInterface, id: Int)
    }
}