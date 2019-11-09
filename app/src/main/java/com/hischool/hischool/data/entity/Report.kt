package com.hischool.hischool.data.entity

import com.google.firebase.firestore.ServerTimestamp

import java.util.*

data class Report(
    var schoolId: Int?,
    var userId: String?,
    var reportId: String?,
    var description: String?,
    var type: String?,
    var status: String? = "pending",
    @ServerTimestamp
    var timestamp: Date? = null
)
