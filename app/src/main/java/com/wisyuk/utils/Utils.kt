package com.wisyuk.utils

object Utils {
    fun isEmailValid(email: CharSequence): Boolean {
        val regex = Regex("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        return regex.matches(email)
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    fun Boolean.toInt() = if (this) 1 else 0
}