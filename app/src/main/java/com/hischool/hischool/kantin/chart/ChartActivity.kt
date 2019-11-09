package com.hischool.hischool.kantin.chart

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.utils.ButtonHelper
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER_ID: String = "extra_user_id"
    }

    private val firestore = Firebase.firestore

    private val chartAdapter = ChartAdapter(this, firestore)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        ButtonHelper.setupBackButton(this, btnChartBack)

        ButtonHelper.setupWideClick(btn_clear_chart, View.OnClickListener {
            chartAdapter.deleteAllItems()
        })

        rv_list_chart_container.apply {
            layoutManager = LinearLayoutManager(this@ChartActivity)
            adapter = chartAdapter
            itemAnimator = SlideInLeftAnimator()
        }

        //loadChart(intent.getStringExtra(EXTRA_USER_ID))

        chartAdapter.emptyView = tvEmptyCart
        chartAdapter.userId = intent.getStringExtra(EXTRA_USER_ID)
        chartAdapter.loadChartItems()

    }
}
