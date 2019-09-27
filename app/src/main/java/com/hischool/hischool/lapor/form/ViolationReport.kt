package com.hischool.hischool.lapor.form

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hischool.hischool.R
import com.hischool.hischool.utils.ButtonHelper
import kotlinx.android.synthetic.main.activity_violation_report.*

class ViolationReport : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_violation_report)

        ButtonHelper.setupBackButton(this, btnBackViolation)
    }
}
