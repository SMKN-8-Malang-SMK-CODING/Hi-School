package com.hischool.hischool.lapor

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Report
import com.hischool.hischool.utils.AuthHelper
import com.hischool.hischool.utils.ButtonHelper
import com.hischool.hischool.utils.ShimmerHelper
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_report_list.*

class ReportListActivity : AppCompatActivity() {

    private val firestore = Firebase.firestore

    private val reportListAdapter = ReportListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_list)

        val currentUser = AuthHelper.loginCheck(this)!!

        ButtonHelper.setupBackButton(this, btnListReportBack)

        rv_list_report_container.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = reportListAdapter
        }

        loadReportsData(currentUser.uid)
    }

    private fun loadReportsData(uid: String) {
        firestore.collection("reports").whereEqualTo("userId", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING).limit(10)
            .addSnapshotListener { _, error ->
                if (error != null) {
                    Toasty.error(this, error.message.toString()).show()
                    return@addSnapshotListener
                }

                firestore.collection("reports").whereEqualTo("userId", uid)
                    .orderBy("timestamp", Query.Direction.DESCENDING).limit(10).get()
                    .addOnSuccessListener {
                        ShimmerHelper.stopShimmer(shimmer_list_report)

                        if (it.size() > 0) {
                            tvEmptyReport.visibility = View.GONE
                        } else {
                            tvEmptyReport.visibility = View.VISIBLE
                        }

                        if(it.size() == 1) {
                            rv_list_report_container.layoutManager = LinearLayoutManager(this)
                        }

                        val reports: List<Report> = it.toObjects()

                        reportListAdapter.setListReports(reports)
                    }
            }
    }

}
