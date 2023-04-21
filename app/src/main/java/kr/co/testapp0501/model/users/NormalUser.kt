package kr.co.testapp0501.model.users

import com.google.gson.annotations.Expose

data class NormalUser(
    val id: String,
    val pw: String,
    val name: String,
    val phoneNumber: String,
    val birth: String,
    val sex: String
)
