package com.hischool.hischool.data.entity

data class User(
    var schoolId: Int? = 0,
    var fullName: String? = "",
    var nickname: String? = "",
    var classRoom: String? = "",
    var email: String? = "",
    var photoUrl: String? = ""
)
