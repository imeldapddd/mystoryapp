package com.imelda.mystoryapp.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val userId: String,
    val name: String,
    val isLogin: Boolean = false
)