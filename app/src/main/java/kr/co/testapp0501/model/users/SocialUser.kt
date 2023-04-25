package kr.co.testapp0501.model.users

import java.io.Serializable

data class SocialUser(
    val snsType : String,
    val snsId : String,
//    val name : String?,
//    val phoneNumber : String,
//    val birth : String,
//    val sex : String
    ) : Serializable

