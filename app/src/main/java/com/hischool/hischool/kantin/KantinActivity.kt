package com.hischool.hischool.kantin

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Kantin
import com.hischool.hischool.data.entity.Menu
import com.hischool.hischool.data.entity.Order
import com.hischool.hischool.kantin.order.OrderActivity
import com.hischool.hischool.kantin.orders.OrdersActivity
import com.hischool.hischool.kantin.orders.detail.OrderDetailActivity
import com.hischool.hischool.utils.AuthHelper
import com.hischool.hischool.utils.ButtonHelper
import com.hischool.hischool.utils.KeyboardHelper
import com.hischool.hischool.utils.ShimmerHelper
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_kantin.*

class KantinActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_FROM_ORDER: String = "extra_from_order"
    }

    private val firestore = Firebase.firestore

    private val kantinAdapter = KantinAdapter(this)
    private val bannerAdapter = FoodBannerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kantin)

        val currentUser = AuthHelper.loginCheck(this)

        val userId = currentUser?.uid!!

        if (intent.getBooleanExtra(EXTRA_FROM_ORDER, false)) {
            setupKantin()
        } else {
            firestore.collection("orders").whereEqualTo("courierId", userId).get()
                .addOnSuccessListener {
                    if (it.size() > 0) {
                        for (orderDoc in it.documents) {
                            val order: Order = orderDoc.toObject()!!

                            if (!(order.status == "completed" || order.status == "pending")) {
                                moveToOrderDetail(orderDoc.id)
                                return@addOnSuccessListener
                            }
                        }

                        checkUserOrder(userId)
                    } else {
                        checkUserOrder(userId)
                    }
                }.addOnFailureListener {
                    Toasty.error(this, it.message.toString()).show()
                }
        }
    }

    private fun checkUserOrder(userId: String) {
        firestore.collection("orders").whereEqualTo("userId", userId).get()
            .addOnSuccessListener {
                if (it.size() > 0) {
                    for (orderDoc in it.documents) {
                        val order: Order = orderDoc.toObject()!!

                        if (order.status != "completed") {
                            moveToOrder(orderDoc.id)
                            return@addOnSuccessListener
                        }
                    }

                    setupKantin()
                } else {
                    setupKantin()
                }
            }.addOnFailureListener {
                Toasty.error(this, it.message.toString()).show()
            }
    }

    private fun setupKantin() {
        ButtonHelper.setupBackButton(this, btnKantinBack)

        btnOrders.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }

        et_kantin_search.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                KeyboardHelper.hideKeyboard(this)

                Toast.makeText(this, "Searching ${et_kantin_search.text}", Toast.LENGTH_SHORT)
                    .show()

                return@OnEditorActionListener true
            }

            return@OnEditorActionListener false
        })

        rv_list_kantin_container.apply {
            layoutManager = LinearLayoutManager(this@KantinActivity)
            adapter = kantinAdapter
        }

        vp_food_banner_container.apply {
            adapter = bannerAdapter
            pageMargin = 20

            setPadding(50, 20, 50, 20)
        }

        loadBannerData()
        loadKantinData()
    }

    private fun loadKantinData() {
        firestore.collection("kantin").get()
            .addOnSuccessListener {
                val kantins: List<Kantin> = it.toObjects()

                val ids = ArrayList<String>()

                for (doc in it.documents) {
                    ids.add(doc.id)
                }

                ShimmerHelper.stopShimmer(shimmer_list_kantin)

                kantinAdapter.setKantin(kantins, ids)
            }
    }

    private fun loadBannerData() {
        firestore.collection("menus").limit(5).get()
            .addOnSuccessListener {
                val foodRecommendations: List<Menu> = it.toObjects()

                ShimmerHelper.stopShimmer(shimmer_food_banner)

                bannerAdapter.setRecommendation(foodRecommendations)
            }
    }

    private fun moveToOrder(orderId: String) {
        val intent = Intent(this, OrderActivity::class.java)

        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(OrderActivity.EXTRA_ORDER_ID, orderId)

        startActivity(intent)

        finish()
    }

    private fun moveToOrderDetail(orderId: String) {
        val intent = Intent(this, OrderDetailActivity::class.java)

        intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, orderId)
        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(intent)

        finish()
    }

}
