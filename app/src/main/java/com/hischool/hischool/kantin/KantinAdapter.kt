package com.hischool.hischool.kantin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Kantin
import com.hischool.hischool.kantin.menu.MenuActivity
import kotlinx.android.synthetic.main.item_row_kantin.view.*

class KantinAdapter(private val context: Context) :
    RecyclerView.Adapter<KantinAdapter.ViewHolder>() {

    private var listKantin = ArrayList<Kantin>()
    private var listKantinId = ArrayList<String>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun setKantin(listKantin: List<Kantin>, listId: ArrayList<String>) {
        this.listKantin.clear()
        this.listKantinId.clear()

        this.listKantin.addAll(listKantin)
        this.listKantinId.addAll(listId)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_kantin, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listKantin.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kantin = listKantin[position]

        Glide.with(context).load(kantin.imageUrl)
            .apply(RequestOptions().override(100, 100))
            .into(holder.itemView.iv_kantin_food_image)

        holder.itemView.tv_kantin_food_name.text = kantin.name
        holder.itemView.tv_kantin_food_desc.text = kantin.description

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MenuActivity::class.java)

            intent.putExtra(MenuActivity.EXTRA_TITLE, kantin.name)
            intent.putExtra(MenuActivity.EXTRA_KANTIN_ID, listKantinId[position])

            context.startActivity(intent)
        }
    }

}