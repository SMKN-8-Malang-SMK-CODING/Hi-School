package com.hischool.hischool.data.entity

data class ChartItem(
    var schoolId: Int,
    var foodName: String = "",
    var foodDesc: String = "",
    var price: Int = 0,
    var imageUrl: String = "",
    var kantinName: String = ""
)