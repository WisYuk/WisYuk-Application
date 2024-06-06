package com.wisyuk.data.pref

data class UserModel (
    val email: String,
    val name: String,
    val id: Int,
    val isLogin: Boolean = false
)