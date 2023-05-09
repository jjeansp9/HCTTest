package kr.co.testapp0501.model.group

data class GroupList(
    val msg: String,
    val data: List<GroupListData>
)

data class GroupListData(
    val groupSeq: Int,
    val groupName: String,
    val memberSeq: Int,
    val filePaths: List<String>,
    val memberAuthLevel: Int
)