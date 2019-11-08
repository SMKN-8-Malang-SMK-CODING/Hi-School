package com.hischool.hischool.data.entity

data class Menu(
    var schoolId: Int?,
    var name: String = "",
    var price: Int = 0,
    var imageUrl: String = ""
)