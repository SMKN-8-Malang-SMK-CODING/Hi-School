package com.hischool.hischool.kantin.orders

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Order
import com.hischool.hischool.kantin.orders.detail.OrderDetailActivity
import com.hischool.hischool.utils.AuthHelper
import com.hischool.hischool.utils.ButtonHelper
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_orders.*

class OrdersActivity : AppCompatActivity() {
    private val firestore = Firebase.firestore

    private val currentUser = AuthHelper.loginCheck(this)

    private val ordersAdapter = OrdersAdapter(this, firestore, currentUser?.uid!!)

    private val orders = ArrayList<Order>()
    private val ids = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        firestore.collection("orders").whereEqualTo("courierId", currentUser?.uid).get()
            .addOnSuccessListener {
                if (it.size() > 0) {
                    for (orderDoc in it.documents) {
                        val order: Order = orderDoc.toObject()!!

                        if (!(order.status == "completed" || order.status == "pending")) {
                            moveToOrderDetail(orderDoc.id)
                            return@addOnSuccessListener
                        }
                    }

                    setupOrdersActivity()
                } else {
                    setupOrdersActivity()
                }
            }.addOnFailureListener {
                Toasty.error(this, it.message.toString()).show()
            }
    }

    private fun setupOrdersActivity() {
        ButtonHelper.setupBackButton(this, btnOrdersBack)

        ordersAdapter.loadingView = pb_loading_indicator

        rv_list_orders_container.apply {
            layoutManager = LinearLayoutManager(this@OrdersActivity)
            adapter = ordersAdapter
        }

        setupOrdersData()
    }

    private fun moveToOrderDetail(orderId: String) {
        val intent = Intent(this, OrderDetailActivity::class.java)

        intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, orderId)
        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(intent)
    }

    private fun setupOrdersData() {
        firestore.collection("orders").whereEqualTo("status", "pending")
            .addSnapshotListener { _, error ->
                if (error != null) {
                    Toasty.error(this, error.message.toString()).show()
                    return@addSnapshotListener
                }

                firestore.collection("orders").whereEqualTo("status", "pending")
                    .orderBy("orderTime", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener {
                        orders.clear()
                        ids.clear()

                        pb_loading_indicator.visibility = View.GONE

                        if (it.size() <= 0) {
                            tvEmptyOrder.visibility = View.VISIBLE

                            ordersAdapter.clearData()

                            return@addOnSuccessListener
                        }

                        tvEmptyOrder.visibility = View.GONE

                        for (doc in it.documents) {
                            orders.add(doc.toObject()!!)
                            ids.add(doc.id)
                        }

                        ordersAdapter.setListOrders(orders, ids)
                    }
            }
    }
}
