package com.hischool.hischool.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.News
import kotlinx.android.synthetic.main.item_row_news.view.*

class NewsAdapter(private val context: Context, private val news: ArrayList<News>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_news, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_news_news.text = context.getString(R.string.lipsum_news)
        holder.itemView.tv_news_poster_name.text = "Diva"
        holder.itemView.tv_news_publish_time.text = "18.04 WIB"
    }
}