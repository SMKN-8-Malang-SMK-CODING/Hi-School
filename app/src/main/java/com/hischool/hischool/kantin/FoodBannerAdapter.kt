package com.hischool.hischool.kantin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.FoodRecommendation
import kotlinx.android.synthetic.main.item_food_banner.view.*

class FoodBannerAdapter(
    private val context: Context
) : PagerAdapter() {

    private val foodRecommendations = ArrayList<FoodRecommendation>()

    fun setRecommendation(foodRecommendations: ArrayList<FoodRecommendation>) {
        this.foodRecommendations.clear()
        this.foodRecommendations.addAll(foodRecommendations)
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return foodRecommendations.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_food_banner, container, false)

        Glide.with(context)
            .load(foodRecommendations[position].imageUrl)
            .apply(RequestOptions().override(600, 300))
            .into(view.iv_image_food_banner)

        container.addView(view, 0)

        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}