package com.hischool.hischool.kantin.chart

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Order
import com.hischool.hischool.utils.ButtonHelper
import com.hischool.hischool.utils.DialogHelper
import com.hischool.hischool.utils.NumberFormatter
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER_ID: String = "extra_user_id"
    }

    private val firestore = Firebase.firestore

    private val chartAdapter = CartAdapter(this, firestore)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        ButtonHelper.setupBackButton(this, btnChartBack)

        val userId = intent.getStringExtra(EXTRA_USER_ID)

        ButtonHelper.setupWideClick(btn_clear_chart, View.OnClickListener {
            DialogHelper.yesNoDialog(
                this,
                "Yakin menghapus semua menu di keranjang?",
                listener = object : DialogHelper.YesNoListener {
                    override fun onYes(dialog: DialogInterface, id: Int) {
                        chartAdapter.deleteAllItems()
                    }

                    override fun onNo(dialog: DialogInterface, id: Int) {

                    }
                })

        })

        btnOrder.setOnClickListener {
            val items = chartAdapter.cartItems
            val menus = chartAdapter.cartItemMenus

            if (items.size > 0 && items.size == menus.size) {
                DialogHelper.yesNoDialog(
                    this,
                    "Harap pastikan uang kamu cukup untuk membayar semua makanan dengan ditambah biaya jasa pemesanan sebesar Rp2000",
                    yesMessage = "Pesan",
                    title = "Lanjutkan pemesanan?",
                    listener = object : DialogHelper.YesNoListener {
                        override fun onYes(dialog: DialogInterface, id: Int) {
                            val orders = mutableMapOf<String, Int>()

                            var totalPrice = 2000

                            for (menu in menus) {
                                if (orders[menu.name] != null) {
                                    orders[menu.name!!] = orders[menu.name]?.plus(1)!!
                                } else {
                                    orders[menu.name!!] = 1
                                }

                                totalPrice += Integer.parseInt(menu.price!!)
                            }

                            var orderMessage = ""

                            for (key in orders.keys) {
                                orderMessage += "$key - ${orders[key]}|||"
                            }

                            val order = Order(
                                menus[0].schoolId,
                                userId,
                                orderMessage,
                                totalPrice.toString(),
                                "open"
                            )

                            DialogHelper.yesNoDialog(
                                this@ChartActivity,
                                orderMessage.replace(
                                    "|||",
                                    "\n"
                                ) + "\nTotal ${NumberFormatter.formatRupiah(
                                    totalPrice
                                )} lanjutkan pemesanan?",
                                title = "Rekap",
                                listener = object : DialogHelper.YesNoListener {

                                    override fun onYes(dialog: DialogInterface, id: Int) {
                                        firestore.collection("orders").add(order)
                                            .addOnSuccessListener {
                                                chartAdapter.deleteAllItems()
                                            }

                                        Toasty.warning(
                                            this@ChartActivity,
                                            "Order: $order",
                                            Toast.LENGTH_LONG,
                                            true
                                        )
                                            .show()
                                    }

                                    override fun onNo(dialog: DialogInterface, id: Int) {

                                    }
                                }
                            )
                        }

                        override fun onNo(dialog: DialogInterface, id: Int) {

                        }
                    })
            }
        }

        rv_list_chart_container.apply {
            layoutManager = LinearLayoutManager(this@ChartActivity)
            adapter = chartAdapter
            itemAnimator = SlideInRightAnimator()
        }

        //loadChart(intent.getStringExtra(EXTRA_USER_ID))

        chartAdapter.emptyView = tvEmptyCart
        chartAdapter.userId = userId
        chartAdapter.loadChartItems()

    }
}
