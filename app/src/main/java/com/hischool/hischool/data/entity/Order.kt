package com.hischool.hischool.data.entity

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Order(
    var schoolId: Int? = 0,
    var userId: String? = "",
    var orderMessage: String? = "",
    var totalPrice: String? = "",
    var status: String? = "",
    @ServerTimestamp
    var orderTime: Date? = null,
    var courierId: String? = null,
    var completeTime: Date? = null
)
