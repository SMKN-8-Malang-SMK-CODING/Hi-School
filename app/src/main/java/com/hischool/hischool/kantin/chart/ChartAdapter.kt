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

class ChartAdapter(val context: Context, val firestore: FirebaseFirestore) :
    RecyclerView.Adapter<ChartAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val chartItems = ArrayList<Cart>()
    private val chartItemIds = ArrayList<String>()

    var userId: String? = null
    var emptyView: TextView? = null

    private var stillDeleting = false

    fun loadChartItems() {
        firestore.collection("carts").whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener {
                val listCart: List<Cart> = it.toObjects()
                val listCartId = ArrayList<String>()

                for (d in it) {
                    listCartId.add(d.id)
                }

                chartItems.clear()
                chartItemIds.clear()

                chartItems.addAll(listCart)
                chartItemIds.addAll(listCartId)

                stillDeleting = false

                if (chartItems.size <= 0) {
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
                firestore.collection("carts").document(chartItemIds[position]).delete()
                    .addOnSuccessListener {
                        loadChartItems()
                    }.addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        loadChartItems()
                    }
            }

            chartItems.removeAt(position)
            chartItemIds.removeAt(position)
            notifyItemRemoved(position)
        } catch (e: IndexOutOfBoundsException) {

        }
    }

    fun deleteAllItems() {
        val batch = firestore.batch()
        val idClones = ArrayList<String>()

        idClones.addAll(chartItemIds)

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

    override fun getItemCount(): Int = chartItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chartItem = chartItems[position]

        firestore.collection("menus").document(chartItem.menuId!!).get().addOnSuccessListener {
            val menu: Menu = it.toObject()!!

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
