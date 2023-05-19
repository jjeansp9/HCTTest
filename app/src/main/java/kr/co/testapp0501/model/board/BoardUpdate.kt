package kr.co.testapp0501.model.board

data class BoardUpdate(
    val seq: Int,
    val title: String,
    val content: String,
    val bbsId: String,
    val fileId: String,
)
