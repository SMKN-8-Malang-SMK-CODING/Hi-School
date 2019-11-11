package com.hischool.hischool.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Admin
import com.hischool.hischool.data.entity.News
import com.hischool.hischool.utils.NumberFormatter
import kotlinx.android.synthetic.main.item_row_news.view.*

class NewsAdapter(private val context: Context, private val firestore: FirebaseFirestore) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val news = ArrayList<News>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun setNews(news: List<News>) {
        this.news.clear()
        this.news.addAll(news)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_news, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNews = this.news[position]

        firestore.collection("school_admins").document(currentNews.adminId!!).get()
            .addOnSuccessListener {
                val admin: Admin = it.toObject()!!

                holder.itemView.tv_news_poster_name.text = admin.division
                Glide.with(context).load(admin.imageUrl).placeholder(R.color.colorShimmerBase)
                    .into(holder.itemView.iv_news_profile_picture)
            }

        holder.itemView.tv_news_publish_time.text =
            NumberFormatter.formatDate(currentNews.publishTime!!)
        holder.itemView.tv_news_news.text = currentNews.news

    }
}