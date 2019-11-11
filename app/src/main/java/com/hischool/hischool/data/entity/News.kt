package com.hischool.hischool.data.entity

import java.util.*

data class News(
    var schoolId: Int? = 0,
    var adminId: String? = "",
    var publishTime: Date? = null,
    var news: String? = ""
)
