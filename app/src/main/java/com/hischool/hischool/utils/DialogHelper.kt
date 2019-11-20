package com.hischool.hischool.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import com.hischool.hischool.R
import kotlinx.android.synthetic.main.dialog_yesno.view.*

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

        @SuppressLint("InflateParams")
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_yesno, null)

        builder.setView(dialogView)

        if (title.isNotEmpty()) {
            dialogView.tv_dialog_title.text = title
        } else {
            dialogView.tv_dialog_title.visibility = View.GONE
        }

        dialogView.tv_dialog_message.text = message

        dialogView.btn_dialog_no.text = noMessage
        dialogView.btn_dialog_yes.text = yesMessage

        val dialog = builder.show()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.btn_dialog_no.setOnClickListener {
            dialog.dismiss()
        }

        dialogView.btn_dialog_yes.setOnClickListener {
            listener.onYes(dialog)
            dialog.dismiss()
        }
    }

    fun alert(context: Context, message: String, okText: String = "Oke!") {
        val builder = AlertDialog.Builder(context)

        @SuppressLint("InflateParams")
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_yesno, null)

        builder.setView(dialogView)

        dialogView.tv_dialog_title.visibility = View.GONE
        dialogView.btn_dialog_no.visibility = View.GONE


        dialogView.tv_dialog_message.text = message

        dialogView.btn_dialog_yes.text = okText

        val dialog = builder.show()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogView.btn_dialog_yes.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface YesNoListener {
        fun onYes(dialog: DialogInterface)
//        fun onNo(dialog: DialogInterface, id: Int)
    }
}