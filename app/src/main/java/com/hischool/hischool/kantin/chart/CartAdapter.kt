package com.hischool.hischool.kantin.chart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Cart
import com.hischool.hischool.data.entity.Menu
import com.hischool.hischool.utils.NumberFormatter
import kotlinx.android.synthetic.main.item_row_chart.view.*

class CartAdapter(val context: Context, val firestore: FirebaseFirestore) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    val cartItems = ArrayList<Cart>()
    val cartItemMenus = ArrayList<Menu>()

    private val cartItemIds = ArrayList<String>()

    var userId: String? = null
    var emptyView: TextView? = null

    private var stillDeleting = false

    fun loadChartItems() {
        firestore.collection("carts").whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener {
                val listCart: List<Cart> = it.toObjects()

                cartItems.clear()
                cartItemMenus.clear()
                cartItemIds.clear()

                for (d in it) {
                    cartItemIds.add(d.id)
                }

                cartItems.addAll(listCart)

                stillDeleting = false

                if (cartItems.size <= 0) {
                    emptyView?.visibility = View.VISIBLE
                } else {
                    emptyView?.visibility = View.GONE
                }

                notifyDataSetChanged()
            }
    }

    private fun deleteItemAt(position: Int, deleteItem: Boolean = false) {
        try {
            if (deleteItem) {
                firestore.collection("carts").document(cartItemIds[position]).delete()
                    .addOnSuccessListener {
                        loadChartItems()
                    }.addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        loadChartItems()
                    }
            }

            cartItems.removeAt(position)
            cartItemIds.removeAt(position)
            notifyItemRemoved(position)
        } catch (e: IndexOutOfBoundsException) {

        }
    }

    fun deleteAllItems() {
        val batch = firestore.batch()
        val idClones = ArrayList<String>()

        idClones.addAll(cartItemIds)

        for (id in idClones) {
            deleteItemAt(0)

            batch.delete(firestore.collection("carts").document(id))
        }

        idClones.clear()

        batch.commit().addOnSuccessListener {
            emptyView?.visibility = View.VISIBLE
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_chart, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chartItem = cartItems[position]

        firestore.collection("menus").document(chartItem.menuId!!).get().addOnSuccessListener {
            val menu: Menu = it.toObject()!!

            cartItemMenus.add(menu)

            holder.itemView.tv_chart_food_name.text = menu.name
            holder.itemView.tv_chart_food_price.text =
                NumberFormatter.formatRupiah(Integer.parseInt(menu.price!!))

            if (!(context as ChartActivity).isFinishing) {
                Glide.with(context).load(menu.imageUrl).into(holder.itemView.iv_chart_food_image)
            }
        }

        holder.itemView.btnDeleteSingleItem.setOnClickListener {
            if (!stillDeleting) {
                stillDeleting = true

                deleteItemAt(position, true)
            }
        }
    }

}
