package com.hischool.hischool.kantin.order

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Order
import com.hischool.hischool.data.entity.User
import com.hischool.hischool.home.HomeActivity
import com.hischool.hischool.kantin.KantinActivity
import com.hischool.hischool.utils.ButtonHelper
import com.hischool.hischool.utils.DialogHelper
import com.hischool.hischool.utils.NumberFormatter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ORDER_ID: String = "extra_order_id"
    }

    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        ButtonHelper.setupBackButton(this, btnOrderBack)

        val orderId = intent.getStringExtra(EXTRA_ORDER_ID)

        firestore.collection("orders").document(orderId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toasty.error(this, error.message.toString(), Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val order: Order = snapshot.toObject()!!

                if (order.status != "pending") {
                    btn_cancel_order.isEnabled = false
                    btn_cancel_order.visibility = View.GONE
                    view_order_courier.visibility = View.VISIBLE

                    if (order.courierId != null) {
                        getOrderCourierData(order.courierId!!)
                    }
                } else {
                    btn_cancel_order.isEnabled = true
                    btn_cancel_order.visibility = View.VISIBLE
                    view_order_courier.visibility = View.GONE
                }

                when (order.status) {
                    "pending" -> Toasty.info(this, "Mencari pengantar...").show()
                    "ordering" -> Toasty.success(
                        this,
                        "Yeay.. makananmu sedang dibeli...",
                        Toast.LENGTH_LONG
                    ).show()
                    "sending" -> Toasty.success(
                        this,
                        "Makananmu akan segera datang, siapkan uangmu...",
                        Toast.LENGTH_LONG
                    ).show()
                    "completed" -> Toasty.success(
                        this,
                        "Pesanan telah selesai diantarkan, selamat menikmati...",
                        Toast.LENGTH_LONG
                    ).show()
                }

                val totalPrice = Integer.parseInt(order.totalPrice!!)

                tv_order_status.text = order.status
                tv_order_desc.text = order.orderMessage!!.replace("|||", "\n")
                tv_order_food_price.text = NumberFormatter.formatRupiah(totalPrice - 2000)
                tv_order_fee.text = NumberFormatter.formatRupiah(2000)
                tv_order_total_price.text = NumberFormatter.formatRupiah(totalPrice)

                btn_cancel_order.setOnClickListener {
                    if (order.status == "pending") {
                        DialogHelper.yesNoDialog(
                            this,
                            "Yakin ingin membatalkan pesanan ini?",
                            listener = object : DialogHelper.YesNoListener {
                                override fun onYes(dialog: DialogInterface, id: Int) {
                                    deleteOrder(snapshot.id)
                                }
                            })
                    }
                }
            } else {
                moveToKantin()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(
            Intent(
                this@OrderActivity,
                HomeActivity::class.java
            )
        )
    }

    private fun getOrderCourierData(courierId: String) {
        firestore.collection("users").document(courierId).get().addOnSuccessListener {
            if (it.exists()) {
                val courier: User = it.toObject()!!

                tv_order_courier.text = courier.fullName
            } else {
                tv_order_courier.text = "-"
            }
        }
    }

    private fun deleteOrder(id: String) {
        firestore.collection("orders").document(id).delete()
    }

    private fun moveToKantin(clearTask: Boolean = false) {
        val intent = Intent(this@OrderActivity, KantinActivity::class.java)

        intent.putExtra(KantinActivity.EXTRA_FROM_ORDER, true)

        if (clearTask) {
            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        startActivity(intent)
    }
}
