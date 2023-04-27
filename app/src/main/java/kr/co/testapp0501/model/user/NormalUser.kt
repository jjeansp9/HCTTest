package kr.co.testapp0501.model.user

data class NormalUser(
    val id: String,
    val pw: String,
    val name: String,
    val phoneNumber: String,
    val birth: String,
    val sex: String
)
