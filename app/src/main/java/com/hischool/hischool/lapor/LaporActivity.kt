package com.hischool.hischool.lapor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hischool.hischool.R
import com.hischool.hischool.utils.ButtonHelper
import kotlinx.android.synthetic.main.activity_lapor.*

class LaporActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lapor)

        ButtonHelper.setupBackButton(this, btnReportBack)
    }
}
