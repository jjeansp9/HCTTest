package kr.co.testapp0501.model.comment

data class CommentSend(
    val bbsId: String, // board type
    val boardSeq: Int, // board seq
    val content: String, // board content
    val ntcrSeq: Int, // member seq
    val groupSeq: Int // group seq
)
