package com.hischool.hischool.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
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

        AuthHelper.getUserData(currentUser!!, object : AuthHelper.UserDataListener {
            override fun onUserData(userData: User) {
                tv_user_name.text = userData.nickname
            }
        })

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
            News(
                1,
                "Sapras",
                "http://hotprintdesign.com/wp-content/uploads/2019/02/Sani-Sebastian.png",
                1,
                Timestamp(Date()),
                "Telah hilang atau belum dikembalikan sebuah stop kontak berwarna merah"
            ),
            News(
                1,
                "Humas",
                "http://hotprintdesign.com/wp-content/uploads/2019/02/Sani-Sebastian.png",
                1,
                Timestamp(Date()),
                "Besok akan diadakan simulasi PAS"
            ),
            News(
                1,
                "Kesiswaan",
                "http://hotprintdesign.com/wp-content/uploads/2019/02/Sani-Sebastian.png",
                1,
                Timestamp(Date()),
                "Kemarin ada laporan tentang siswa merokok di matos menggunakan seragam"
            ),
            News(
                1,
                "Kesiswaan",
                "http://hotprintdesign.com/wp-content/uploads/2019/02/Sani-Sebastian.png",
                1,
                Timestamp(Date()),
                "Telah terjadi pencurian sebuah hp iPhone X di kelas XII RPL Z"
            ),
            News(
                1,
                "Sapras",
                "http://hotprintdesign.com/wp-content/uploads/2019/02/Sani-Sebastian.png",
                1,
                Timestamp(Date()),
                "Bagi yang terlambat mengembalikan akan didenda Rp 2000 per harinya"
            )
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
