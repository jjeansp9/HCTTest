package kr.co.testapp0501.model.group

data class Group(
    val info: Info
)
data class Info(
    val groupName : String,
    val groupType : String,
    val masterSeq : Int,
    var memo : String
)


