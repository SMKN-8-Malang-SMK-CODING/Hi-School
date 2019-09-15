package com.hischool.hischool.kantin.chart

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.ChartItem
import com.hischool.hischool.utils.ButtonHelper
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : AppCompatActivity() {

    private val chartAdapter = ChartAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        ButtonHelper.setupBackButton(this, btnChartBack)

        ButtonHelper.setupWideClick(btn_clear_chart, View.OnClickListener {
            val size = chartAdapter.chartItems.size

            val handler = Handler()

            for (i in 0 until size) {
                handler.postDelayed({
                    chartAdapter.deleteFirstItem()
                }, (100 * (i + 1)).toLong())
            }
        })

        rv_list_chart_container.apply {
            layoutManager = LinearLayoutManager(this@ChartActivity)
            adapter = chartAdapter
            itemAnimator = SlideInLeftAnimator()
        }

        val charts = arrayListOf(
            ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ),
            ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ), ChartItem(
                "Bakso",
                "Enak cuy",
                5000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            )
        )

        chartAdapter.setChartItems(charts)
    }
}
