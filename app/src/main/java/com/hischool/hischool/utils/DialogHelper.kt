package com.hischool.hischool.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.hischool.hischool.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.dialog_image_desc.view.*
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

    fun imageDescription(
        context: Context,
        message: String,
        bitmap: Bitmap
    ) {
        val builder = AlertDialog.Builder(context)

        @SuppressLint("InflateParams")
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image_desc, null)

        builder.setView(dialogView)

        dialogView.tv_dialog_description.text = message

        val loadImage = Glide.with(context).load(bitmap)

        loadImage.into(dialogView.iv_dialog_image)
        loadImage.into(dialogView.iv_dialog_image_full)

        val dialog = builder.show()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

//        val bitmapHeight = NumberFormatter.convertPixelsToDp(bitmap.height.toFloat(), context)

        Toasty.success(
            context,
            dialogView.iv_dialog_image.scaleType.toString() + " -- " + dialogView.iv_dialog_image.maxHeight.toString() + " -- " + dialogView.iv_dialog_image.adjustViewBounds.toString()
        ).show()


        dialogView.iv_dialog_image.setOnClickListener {
            dialogView.iv_dialog_image.visibility = View.GONE
            dialogView.iv_dialog_image_full.visibility = View.VISIBLE
        }

        dialogView.iv_dialog_image_full.setOnClickListener {
            dialogView.iv_dialog_image.visibility = View.VISIBLE
            dialogView.iv_dialog_image_full.visibility = View.GONE
        }
//            val maxHeightDp = NumberFormatter.convertPixelsToDp(
//                dialogView.iv_dialog_image.maxHeight.toFloat(),
//                context
//            )
//
//            if (maxHeightDp == bitmapHeight) {
//                // small
//                dialogView.iv_dialog_image.maxHeight =
//                    NumberFormatter.convertDpToPixel(250f, context).toInt()
//                dialogView.iv_dialog_image.scaleType = ImageView.ScaleType.CENTER_CROP
//
//                Toasty.success(context, "small").show()
//            } else {
//                // full-dialog
//                dialogView.iv_dialog_image.maxHeight =
//                    NumberFormatter.convertDpToPixel(bitmapHeight, context).toInt()
//                dialogView.iv_dialog_image.scaleType = ImageView.ScaleType.CENTER_INSIDE
//
//                Toasty.success(context, "bign").show()
//            }
//
//            Toasty.success(
//                context,
//                dialogView.iv_dialog_image.scaleType.toString() + " -- " + dialogView.iv_dialog_image.maxHeight.toString() + " -- " + dialogView.iv_dialog_image.adjustViewBounds.toString()
//            ).show()
//
//
//            dialogView.iv_dialog_image.requestLayout()

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