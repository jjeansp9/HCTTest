package kr.co.testapp0501.model.group

data class GroupList(
    val data: List<Data>
)

data class Data(
    val groupSeq: Int,
    val groupName: String,
    val memberSeq: Int,
    val filePaths: String?
)