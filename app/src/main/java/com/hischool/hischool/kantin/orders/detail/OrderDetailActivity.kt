package com.hischool.hischool.kantin.orders.detail

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Order
import com.hischool.hischool.data.entity.User
import com.hischool.hischool.home.HomeActivity
import com.hischool.hischool.kantin.KantinActivity
import com.hischool.hischool.utils.AuthHelper
import com.hischool.hischool.utils.ButtonHelper
import com.hischool.hischool.utils.DialogHelper
import com.hischool.hischool.utils.NumberFormatter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_order_detail.*

class OrderDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ORDER_ID: String = "extra_order_id"
    }

    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        val orderId = intent.getStringExtra(EXTRA_ORDER_ID)!!

        val orderRef = firestore.collection("orders").document(orderId)

        ButtonHelper.setupBackButton(this, btnDetailOrderBack)

        val btnCancelOrder = View.OnClickListener {
            DialogHelper.yesNoDialog(
                this,
                "Batalkan menjadi pengantar?",
                listener = object : DialogHelper.YesNoListener {
                    override fun onYes(dialog: DialogInterface, id: Int) {
                        orderRef.update("status", "pending")
                    }
                })
        }

        val btnDoneOrdering = View.OnClickListener {
            DialogHelper.yesNoDialog(
                this,
                "Makanan sudah di tangan dan siap mengantarkan?",
                listener = object : DialogHelper.YesNoListener {
                    override fun onYes(dialog: DialogInterface, id: Int) {
                        orderRef.update("status", "sending")
                    }
                })
        }

        val btnCompleteOrder = View.OnClickListener {
            DialogHelper.yesNoDialog(
                this,
                "Pembayaran lunas dan barang sudah sampai?",
                listener = object : DialogHelper.YesNoListener {
                    override fun onYes(dialog: DialogInterface, id: Int) {
                        val batch = firestore.batch()

                        batch.update(orderRef, "status", "completed")
                        batch.update(orderRef, "completeTime", FieldValue.serverTimestamp())

                        batch.commit()
                    }
                })
        }

        orderRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toasty.error(this, error.message.toString(), Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val order: Order = snapshot.toObject()!!

                if (order.courierId != AuthHelper.loginCheck(this)?.uid) {
                    Toasty.error(this, "Orderan ini mungkin sudah diambil orang lain").show()
                    moveToKantin()
                }

                firestore.collection("users").document(order.userId!!).get().addOnSuccessListener {
                    val user: User = it.toObject()!!

                    tv_order_detail_recipient_name.text = user.fullName
                    tv_order_detail_recipient_class.text = user.classRoom
                }

                val totalPrice = Integer.parseInt(order.totalPrice!!)

                tv_order_detail_desc.text = order.orderMessage!!.replace("|||", "\n")
                tv_order_detail_food_price.text = NumberFormatter.formatRupiah(totalPrice - 2000)
                tv_order_detail_id.text = orderId
                tv_order_detail_order_time.text = NumberFormatter.formatDate(order.orderTime!!)
                tv_order_detail_total_price.text = NumberFormatter.formatRupiah(totalPrice)

                when (order.status) {
                    "ordering" -> {
                        btn_cancel_order.visibility = View.VISIBLE
                        btn_done_order.visibility = View.VISIBLE
                        btn_complete_order.visibility = View.GONE

                        btn_cancel_order.setOnClickListener(btnCancelOrder)
                        btn_done_order.setOnClickListener(btnDoneOrdering)
                    }
                    "sending" -> {
                        btn_cancel_order.visibility = View.GONE
                        btn_done_order.visibility = View.GONE
                        btn_complete_order.visibility = View.VISIBLE

                        btn_complete_order.setOnClickListener(btnCompleteOrder)
                    }
                    else -> {
                        btn_cancel_order.visibility = View.GONE
                        btn_done_order.visibility = View.GONE
                        btn_complete_order.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(
            Intent(
                this@OrderDetailActivity,
                HomeActivity::class.java
            )
        )
    }

    private fun moveToKantin(clearTask: Boolean = false) {
        val intent = Intent(this@OrderDetailActivity, KantinActivity::class.java)

        intent.putExtra(KantinActivity.EXTRA_FROM_ORDER, true)

        if (clearTask) {
            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        startActivity(intent)
    }
}
