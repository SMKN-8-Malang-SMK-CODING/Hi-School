package com.hischool.hischool.kantin.chart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.ChartItem
import kotlinx.android.synthetic.main.item_row_chart.view.*

class ChartAdapter(val context: Context) : RecyclerView.Adapter<ChartAdapter.ViewHolder>() {
    public val chartItems = ArrayList<ChartItem>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun setChartItems(items: ArrayList<ChartItem>) {
        chartItems.clear()
        chartItems.addAll(items)
        notifyDataSetChanged()
    }

    fun deleteFirstItem() {
        chartItems.removeAt(0)
        notifyItemRemoved(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_chart, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = chartItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chartItem = chartItems[position]

        holder.itemView.tv_chart_food_name.text = chartItem.foodName
        holder.itemView.tv_chart_food_desc.text = chartItem.foodDesc

        Glide.with(context).load(chartItem.imageUrl).into(holder.itemView.iv_chart_food_image)
    }


}
