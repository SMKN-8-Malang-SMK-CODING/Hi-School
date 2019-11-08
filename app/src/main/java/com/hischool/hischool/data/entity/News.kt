package com.hischool.hischool.data.entity

import com.google.firebase.Timestamp

data class News(
    var schoolId: Int?,
    var poster: String?,
    var posterImageUrl: String?,
    var posterId: Int?,
    var publishTime: Timestamp,
    var news: String?
)
