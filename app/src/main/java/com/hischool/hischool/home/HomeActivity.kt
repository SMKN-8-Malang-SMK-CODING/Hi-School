package com.hischool.hischool.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.News
import com.hischool.hischool.kantin.KantinActivity
import com.hischool.hischool.lapor.LaporActivity
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnFood.setOnClickListener {
            startActivity(Intent(this, KantinActivity::class.java))
        }

        btnReport.setOnClickListener {
            startActivity(Intent(this, LaporActivity::class.java))
        }

        val news = arrayListOf<News>(
//            News("", Date(), "")
//            News("", Date(), ""),
//            News("", Date(), ""),
//            News("", Date(), ""),
//            News("", Date(), "")
        )

        rv_list_news_container.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = AlphaInAnimationAdapter(NewsAdapter(this@HomeActivity, news)).apply {
                setFirstOnly(false)
            }

            setHasFixedSize(true)
        }
    }
}
