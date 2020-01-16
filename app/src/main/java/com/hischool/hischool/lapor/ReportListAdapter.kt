package com.hischool.hischool.lapor

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Report
import com.hischool.hischool.utils.DialogHelper
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.item_row_report.view.*

class ReportListAdapter(private val context: Context) :
    RecyclerView.Adapter<ReportListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val storage = Firebase.storage.reference

    private val listReports = ArrayList<Report>()

    fun setListReports(reports: List<Report>) {
        listReports.clear()
        listReports.addAll(reports)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_report, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listReports.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report: Report = listReports[position]

        val reportStatus = holder.itemView.tv_report_status

        holder.itemView.tv_report_description.text = report.description.toString()
        holder.itemView.tv_report_type.text = report.type.toString()

        reportStatus.text = report.status.toString()

        when (report.status) {
            "pending" -> reportStatus.backgroundTintList = colorStateList(R.color.colorStatePending)
            "solved" -> reportStatus.backgroundTintList = colorStateList(R.color.colorStateSolved)
            "rejected" -> reportStatus.backgroundTintList =
                colorStateList(R.color.colorStateRejected)
        }

        if (report.imageUrl != null && report.imageUrl.isNotEmpty()) {
            Glide.with(context).asBitmap().load(report.imageUrl).into(setupImage(holder, report))
        } else {
            storage.child("images/${report.reportId}.jpg").downloadUrl.addOnSuccessListener {
//                Glide.with(context).load(it).placeholder(R.drawable.btn_rounded).into(holder.itemView.iv_report_image_banner)

                Glide.with(context).asBitmap().load(it).placeholder(R.drawable.btn_rounded).into(setupImage(holder, report))
            }
        }
    }

    private fun setupImage(
        holder: ViewHolder,
        report: Report
    ): CustomTarget<Bitmap> {
        return object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                Glide.with(context).load(resource).into(holder.itemView.iv_report_image_banner)

                holder.itemView.setOnClickListener {
                    DialogHelper.imageDescription(context, report.description.toString(), resource)
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // this is called when imageView is cleared on lifecycle call or for
                // some other reason.
                // if you are referencing the bitmap somewhere else too other than this imageView
                // clear it here as you can no longer have the bitmap
            }
        }
    }

    private fun colorStateList(colorId: Int) =
        ColorStateList.valueOf(ContextCompat.getColor(context, colorId))

}