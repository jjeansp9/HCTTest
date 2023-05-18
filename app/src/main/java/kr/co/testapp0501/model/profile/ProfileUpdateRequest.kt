package kr.co.testapp0501.model.profile

import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("memberSeq") val memberSeq: Int,
    @SerializedName("name") val name: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("birth") val birth: String,
    @SerializedName("sex") val sex: String
)
