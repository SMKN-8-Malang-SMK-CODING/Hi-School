package com.hischool.hischool.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.News
import com.hischool.hischool.data.entity.User
import com.hischool.hischool.kantin.KantinActivity
import com.hischool.hischool.lapor.LaporActivity
import com.hischool.hischool.utils.AuthHelper
import com.hischool.hischool.utils.DialogHelper
import com.hischool.hischool.utils.ShimmerHelper
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val firestore = Firebase.firestore

    private val newsAdapter = NewsAdapter(this, firestore)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val currentUser = AuthHelper.loginCheck(this)

        val displayNameIsSetted =
            currentUser!!.displayName != null && currentUser.displayName!!.isNotEmpty()

        if (displayNameIsSetted) {
            tv_user_name.text = currentUser.displayName
        }

        AuthHelper.getUserData(currentUser, object : AuthHelper.UserDataListener {
            override fun onUserData(userData: User) {
                if (!displayNameIsSetted) {
                    setDisplayName(userData, currentUser)
                }

                loadNews(userData.schoolId!!)
            }
        })


        btnFood.setOnClickListener {
            startActivity(Intent(this, KantinActivity::class.java))
        }

        btnReport.setOnClickListener {
            startActivity(Intent(this, LaporActivity::class.java))
        }

        btnLogout.setOnClickListener {
            DialogHelper.yesNoDialog(
                this,
                "Apakah kamu ingin keluar?",
                listener = object : DialogHelper.YesNoListener {
                    override fun onYes(dialog: DialogInterface) {
                        AuthHelper.signOut(this@HomeActivity)
                    }
                })
        }

        rv_list_news_container.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = AlphaInAnimationAdapter(newsAdapter).apply {
                setFirstOnly(false)
            }

            setHasFixedSize(true)
        }

    }

    private fun setDisplayName(
        userData: User,
        currentUser: FirebaseUser
    ) {
        tv_user_name.text = userData.nickname

        val profileUpdate = UserProfileChangeRequest.Builder()
            .setDisplayName(userData.nickname)
            .build()

        currentUser.updateProfile(profileUpdate)
    }

    private fun loadNews(schoolId: Int) {
        firestore.collection("news").whereEqualTo("schoolId", schoolId)
            .addSnapshotListener { _, error ->
                if (error != null) {
                    Toasty.error(this, error.message.toString()).show()
                    return@addSnapshotListener
                }

                firestore.collection("news").whereEqualTo("schoolId", schoolId)
                    .orderBy("publishTime", Query.Direction.DESCENDING)
                    .limit(15).get()
                    .addOnSuccessListener {
                        if (it.size() > 0) {
                            tv_no_news.visibility = View.GONE
                        } else {
                            tv_no_news.visibility = View.VISIBLE
                        }

                        val news: List<News> = it.toObjects()

                        newsAdapter.setNews(news)
                        ShimmerHelper.stopShimmer(shimmer_list_news)
                    }
            }
    }
}
