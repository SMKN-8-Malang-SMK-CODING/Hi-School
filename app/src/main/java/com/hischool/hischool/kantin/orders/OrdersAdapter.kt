package com.hischool.hischool.kantin.orders

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Order
import com.hischool.hischool.data.entity.User
import com.hischool.hischool.kantin.orders.detail.OrderDetailActivity
import com.hischool.hischool.utils.DialogHelper
import com.hischool.hischool.utils.NumberFormatter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.item_row_order.view.*


class OrdersAdapter(
    private val context: Context,
    private val firestore: FirebaseFirestore,
    private val userId: String
) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val listOrder = ArrayList<Order>()
    private val listOrderId = ArrayList<String>()

    var loadingView: ProgressBar? = null

    fun setListOrders(orders: ArrayList<Order>, ids: ArrayList<String>) {
        listOrder.clear()
        listOrderId.clear()

        listOrder.addAll(orders)
        listOrderId.addAll(ids)

        notifyDataSetChanged()
    }


    fun clearData() {
        listOrder.clear()
        listOrderId.clear()

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_order, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = listOrder[position]

        firestore.collection("users").document(order.userId!!).get().addOnSuccessListener {
            val orderUser: User = it.toObject()!!

            holder.itemView.tv_order_item_name.text = orderUser.fullName
            holder.itemView.tv_order_item_class_room.text = orderUser.classRoom
        }

        val totalPrice = Integer.parseInt(order.totalPrice!!)
        val foodPrice = NumberFormatter.formatRupiah(totalPrice - 2000)

        holder.itemView.tv_order_item_desc.text = order.orderMessage!!.replace("|||", "\n")
        holder.itemView.tv_order_item_food_price.text = foodPrice
        holder.itemView.tv_order_item_total_price.text = NumberFormatter.formatRupiah(totalPrice)

        holder.itemView.btn_claim_order_item.setOnClickListener {
            DialogHelper.yesNoDialog(
                context,
                "Pastikan uang kamu cukup, harga di aplikasi ${foodPrice}, mungkin tidak sesuai dengan harga sebenarnya!",
                listener = object : DialogHelper.YesNoListener {
                    override fun onYes(dialog: DialogInterface, id: Int) {
                        checkAndClaimOrder(listOrderId[position])
                    }
                }
            )
        }
    }

    private fun checkAndClaimOrder(orderId: String) {
        listOrder.clear()
        listOrderId.clear()

        loadingView?.visibility = View.VISIBLE

        val ref = firestore.collection("orders").document(orderId)

        ref.get().addOnSuccessListener {
            val order: Order = it.toObject()!!

            if (order.status != "pending") {
                Toasty.warning(context, "Gagal, orderan mungkin sudah diambil orang lain...").show()
                return@addOnSuccessListener
            }

            val batch = firestore.batch()

            batch.update(ref, "courierId", userId)
            batch.update(ref, "status", "ordering")

            batch.commit().addOnSuccessListener {
                Toasty.success(context, "Pesanan berhasil diambil...").show()

                val intent = Intent(context, OrderDetailActivity::class.java)

                intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, orderId)
                intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                context.startActivity(intent)
            }
        }
    }

}