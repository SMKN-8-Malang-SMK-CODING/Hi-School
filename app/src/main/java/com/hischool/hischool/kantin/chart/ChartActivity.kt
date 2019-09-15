package com.hischool.hischool.kantin.chart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.ChartItem
import com.hischool.hischool.utils.ButtonHelper
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : AppCompatActivity() {

    private val chartAdapter = ChartAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        ButtonHelper.setupBackButton(this, btnChartBack)

        rv_list_chart_container.apply {
            layoutManager = LinearLayoutManager(this@ChartActivity)
            adapter = chartAdapter
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
