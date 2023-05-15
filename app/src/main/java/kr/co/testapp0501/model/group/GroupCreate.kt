package kr.co.testapp0501.model.group

data class GroupCreate(
    val groupName : String,
    val groupType : String,
    val memberSeq : Int,
    var memo : String
)


