package kr.co.testapp0501.model.group

data class Group(
    val info: Info

//    val groupName : String,
//    val groupType : String,
//    val memberSeq : Int,
//    var memo : String
)
data class Info(
    val groupName : String,
    val groupType : String,
    val memberSeq : Int,
    var memo : String
)


