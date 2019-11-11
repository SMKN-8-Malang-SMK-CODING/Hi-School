package com.hischool.hischool.kantin.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Cart
import com.hischool.hischool.data.entity.Menu
import com.hischool.hischool.utils.NumberFormatter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.item_row_menu.view.*

class ListMenuAdapter(private val context: Context, private val firestore: FirebaseFirestore) :
    RecyclerView.Adapter<ListMenuAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private var listMenu = ArrayList<Menu>()
    private var listMenuId = ArrayList<String>()

    private var userId: String? = null

    fun setUserId(uid: String) {
        userId = uid
    }

    fun setMenus(menus: ArrayList<Menu>, ids: ArrayList<String>) {
        listMenu.clear()
        listMenuId.clear()

        listMenu.addAll(menus)
        listMenuId.addAll(ids)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_menu, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMenu.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = listMenu[position]

        Glide.with(context).load(menu.imageUrl)
            .apply(RequestOptions().override(550, 280))
            .into(holder.itemView.iv_menu_image_banner)

        holder.itemView.tv_menu_name.text = menu.name
        holder.itemView.tv_menu_price.text =
            NumberFormatter.formatRupiah(Integer.parseInt(menu.price!!))

        holder.itemView.iv_menu_image_banner.setOnClickListener {
            if (userId != null) {
                Toasty.success(
                    context,
                    "${menu.name} ditambahkan ke keranjang!",
                    Toast.LENGTH_SHORT,
                    true
                ).show()

                firestore.collection("carts").add(
                    Cart(menu.schoolId, userId, listMenuId[position])
                ).addOnFailureListener {
                    Toasty.error(context, it.message.toString(), Toast.LENGTH_SHORT, true).show()
                }
            }
        }
    }

//    inline fun <reified T> T.logi(message: String) = Log.i(T::class.java.simpleName, message)
}