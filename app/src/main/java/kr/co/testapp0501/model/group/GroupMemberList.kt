package kr.co.testapp0501.model.group

data class GroupMemberList(
    val msg: String,
    val data: List<MemberListData>
)

data class MemberListData(
    val seq: Int,
    val memberVO: MemberVO,
    val memberAuthLevel: Int
)
