package com.hischool.hischool.data.entity

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Cart(
    var schoolId: Int? = 0,
    var userId: String? = "",
    var menuId: String? = "",
    @ServerTimestamp
    var timestamp: Date? = null
)
