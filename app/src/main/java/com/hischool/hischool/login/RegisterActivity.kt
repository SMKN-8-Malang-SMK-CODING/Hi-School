package com.hischool.hischool.login

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.hischool.hischool.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val schoolList = arrayOf("SMKN 8 Malang", "SMKN 4 Malang")

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, schoolList)

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spn_register_input_school.adapter = arrayAdapter

        spn_register_input_school.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Toasty.success(this@RegisterActivity, "Selected $position").show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }
}
