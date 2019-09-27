package com.hischool.hischool.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object NumberFormatter {
    fun formatRupiah(number: Int): String {
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        return formatRupiah.format(number.toDouble())
    }

    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("EEE, d MMM hh:mm", Locale.getDefault())

        return dateFormat.format(date)
    }
}
