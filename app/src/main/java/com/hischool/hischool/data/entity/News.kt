package com.hischool.hischool.data.entity

import com.google.firebase.Timestamp

data class News(
    var schoolId: Int? = 0,
    var poster: String?,
    var posterImageUrl: String?,
    var posterId: Int? = 0,
    var publishTime: Timestamp,
    var news: String?
)
