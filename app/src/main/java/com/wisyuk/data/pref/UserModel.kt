package com.wisyuk.data.pref

data class UserModel (
    val email: String,
    val image: String,
    val name: String,
    val isLogin: Boolean = false
)