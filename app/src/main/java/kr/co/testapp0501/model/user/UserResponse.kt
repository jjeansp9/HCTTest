package kr.co.testapp0501.model.user

data class UserResponse(
    val msg : String,
    val data : Data
)

data class Data(
    val seq: Int,
    val jwtToken: String
)
