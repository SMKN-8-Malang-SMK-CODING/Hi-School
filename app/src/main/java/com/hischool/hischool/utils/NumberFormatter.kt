package com.hischool.hischool.utils

import java.text.NumberFormat
import java.util.*

object NumberFormatter {
    fun formatRupiah(number: Int): String {
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        return formatRupiah.format(number.toDouble())
    }
}
