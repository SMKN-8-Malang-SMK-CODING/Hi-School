package com.hischool.hischool.kantin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Menu
import com.hischool.hischool.kantin.menu.MenuActivity
import kotlinx.android.synthetic.main.item_food_banner.view.*

class FoodBannerAdapter(
    private val context: Context
) : PagerAdapter() {

    private val foodRecommendations = ArrayList<Menu>()

    fun setRecommendation(foodRecommendations: List<Menu>) {
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

        val menu = foodRecommendations[position]

        Glide.with(context)
            .load(menu.imageUrl)
            .apply(RequestOptions().override(600, 300))
            .into(view.iv_image_food_banner)

        container.addView(view, 0)

        view.setOnClickListener {
            val intent = Intent(context, MenuActivity::class.java)

            intent.putExtra(MenuActivity.EXTRA_KANTIN_ID, menu.kantinId)

            context.startActivity(intent)
        }

        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}