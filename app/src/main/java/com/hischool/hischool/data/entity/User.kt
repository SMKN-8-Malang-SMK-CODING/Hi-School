package com.hischool.hischool.data.entity

data class User(
    var schoolId: Int? = 0,
    var uid: String? = "",
    var fullName: String? = "",
    var nickname: String? = "",
    var email: String? = "",
    var photoUrl: String? = ""
)