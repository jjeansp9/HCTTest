package kr.co.testapp0501.model.group

data class Group(
    val groupName : String,
    val groupType : String,
    val masterSeq : Int,
    var memo : String
)

