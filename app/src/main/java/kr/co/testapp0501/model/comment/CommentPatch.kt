package kr.co.testapp0501.model.comment

data class CommentPatch(
    val bbsId: String,
    val boardSeq: Int, //게시글 seq
    val content: String, // 내용
    val seq: Int // 댓글 seq
)
