package kr.co.testapp0501.model.group

import com.google.gson.annotations.SerializedName

data class GroupList(
    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: List<Data>
)

data class Data(
    @SerializedName("groupSeq") val groupSeq: Int,
    @SerializedName("groupName") val groupName: String,
    @SerializedName("memberSeq") val memberSeq: Int,
    @SerializedName("filePaths") val filePaths: List<String>
)