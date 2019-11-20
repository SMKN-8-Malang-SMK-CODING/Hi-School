package com.hischool.hischool.data.entity

import com.google.firebase.firestore.ServerTimestamp

import java.util.*

data class Report(
    val schoolId: Int? = 0,
    val userId: String? = "",
    val reportId: String? = "",
    val description: String? = "",
    val imageUrl: String? = "",
    val type: String? = "",
    val status: String? = "pending",
    @ServerTimestamp
    val timestamp: Date? = null
)
