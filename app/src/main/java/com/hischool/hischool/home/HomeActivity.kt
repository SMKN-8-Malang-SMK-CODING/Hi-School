package com.hischool.hischool.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.News
import com.hischool.hischool.data.entity.User
import com.hischool.hischool.kantin.KantinActivity
import com.hischool.hischool.lapor.LaporActivity
import com.hischool.hischool.utils.AuthHelper
import com.hischool.hischool.utils.ShimmerHelper
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : AppCompatActivity() {
    private val newsAdapter = NewsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val currentUser = AuthHelper.loginCheck(this)

        FirebaseFirestore.getInstance().collection("users").document(currentUser?.uid!!).get()
            .addOnSuccessListener {
                val data: User? = it.toObject()

                tv_user_name.text = data?.nickname
            }


        btnFood.setOnClickListener {
            startActivity(Intent(this, KantinActivity::class.java))
        }

        btnReport.setOnClickListener {
            startActivity(Intent(this, LaporActivity::class.java))
        }

        btnLogout.setOnClickListener {
            AuthHelper.signOut(this)
        }

        val news = arrayListOf(
            News("Emil", Date(), "hp saya hilang", Date()),
            News("Dillah", Date(), "hp teman saya hilang", Date()),
            News("Abdillah", Date(), "hp emil hilang", Date()),
            News("Aufal", Date(), "", Date()),
            News("Mustacskdfhk", Date(), "", Date())
        )

        rv_list_news_container.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = AlphaInAnimationAdapter(newsAdapter).apply {
                setFirstOnly(false)
            }

            setHasFixedSize(true)
        }

        newsAdapter.setNews(news)
        ShimmerHelper.stopShimmer(shimmer_list_news)
    }
}
