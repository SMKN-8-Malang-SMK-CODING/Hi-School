package com.hischool.hischool.kantin.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Menu
import com.hischool.hischool.utils.NumberFormatter
import kotlinx.android.synthetic.main.item_row_menu.view.*

class ListMenuAdapter(private val context: Context, private val listMenu: ArrayList<Menu>) :
    RecyclerView.Adapter<ListMenuAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

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
        holder.itemView.tv_menu_price.text = NumberFormatter.formatRupiah(menu.price)
    }

}