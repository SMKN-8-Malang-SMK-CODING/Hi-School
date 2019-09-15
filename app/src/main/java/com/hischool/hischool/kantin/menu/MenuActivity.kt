package com.hischool.hischool.kantin.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hischool.hischool.R
import com.hischool.hischool.data.entity.Menu
import com.hischool.hischool.kantin.chart.ChartActivity
import com.hischool.hischool.utils.ButtonHelper
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_TITLE: String = "extra_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        ButtonHelper.setupBackButton(this, btnKantinMenuBack)

        ButtonHelper.setupWideClick(btnOpenChart, View.OnClickListener {
            startActivity(Intent(this, ChartActivity::class.java))
        })

        tvMenuTitle.text = intent.getStringExtra(EXTRA_TITLE)!!

        val listMenu = arrayListOf(
            Menu(
                "Pizza",
                260000,
                "https://media-cdn.tripadvisor.com/media/photo-s/15/c5/a4/14/pepperoni-lovers.jpg"
            ),
            Menu(
                "T-Bone Steak",
                75000,
                "https://images-gmi-pmc.edge-generalmills.com/b57ee35f-bce2-4229-8bf5-19b97876a4cb.jpg"
            ),
            Menu(
                "Kebab",
                2000,
                "https://www.palmia.co.id/media/recipe/medium/0dd0dc65af50d243a646a620cc07818f.jpg"
            ),
            Menu(
                "Southern Fried Chicken",
                20000,
                "https://sparkpeo.hs.llnwd.net/e1/resize/630m620/e4/nw/9/5/lSouthern-Fried-Chicken951607233.jpg"
            )
        )

        rv_list_menu_container.apply {
            layoutManager = LinearLayoutManager(this@MenuActivity)
            adapter = AlphaInAnimationAdapter(
                ListMenuAdapter(
                    this@MenuActivity,
                    listMenu
                )
            ).apply {
                setFirstOnly(false)
            }

            setHasFixedSize(true)
        }
    }
}
