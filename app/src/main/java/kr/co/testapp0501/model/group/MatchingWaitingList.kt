package kr.co.testapp0501.model.group

data class MatchingWaitingList(
    var msg: String,
    val data: List<MatchingListData>
)

data class MatchingListData(
    val seq: Int,
    val groupSeq: Int,
    val memberVO: MemberVO,
    val permission: String,
    val insertDate: String
)

data class MemberVO(
    val seq: Int,
    val name: String,
    val phoneNumber: String,
    val birth: String,
    val sex: String
)

