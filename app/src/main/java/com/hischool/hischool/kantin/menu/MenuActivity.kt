package com.hischool.hischool.kantin.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Menu
import com.hischool.hischool.kantin.chart.ChartActivity
import com.hischool.hischool.utils.AuthHelper
import com.hischool.hischool.utils.ButtonHelper
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_TITLE: String = "extra_title"
        const val EXTRA_KANTIN_ID: String = "extra_kantin_id"
    }

    private var currentUser: FirebaseUser? = null

    private val firestore = Firebase.firestore

    private val menuAdapter = ListMenuAdapter(this, firestore)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        currentUser = AuthHelper.loginCheck(this)

        ButtonHelper.setupBackButton(this, btnKantinMenuBack)

        ButtonHelper.setupWideClick(btnOpenChart, View.OnClickListener {
            val intent = Intent(this, ChartActivity::class.java)

            intent.putExtra(ChartActivity.EXTRA_USER_ID, currentUser?.uid!!)

            startActivity(intent)
        })

        tvMenuTitle.text = intent.getStringExtra(EXTRA_TITLE)!!

        val kantinId = intent.getIntExtra(EXTRA_KANTIN_ID, 0)

        menuAdapter.setUserId(currentUser?.uid!!)

        rv_list_menu_container.apply {
            layoutManager = LinearLayoutManager(this@MenuActivity)
            adapter = menuAdapter
        }

        loadKantinMenus(kantinId)
    }

    private fun loadKantinMenus(kantinId: Int) {
        firestore.collection("menus").whereEqualTo("kantinId", kantinId).get()
            .addOnSuccessListener {
                logi(it.size().toString())

                val menu: List<Menu> = it.toObjects()
                val ids = ArrayList<String>()

                for (d in it) {
                    ids.add(d.id)
                }

//                ShimmerHelper.stopShimmer(shimmer_list_kantin)

                menuAdapter.setMenus(menu as ArrayList<Menu>, ids)
            }
    }

    inline fun <reified T> T.logi(message: String) = Log.i(T::class.java.simpleName, message)
}
