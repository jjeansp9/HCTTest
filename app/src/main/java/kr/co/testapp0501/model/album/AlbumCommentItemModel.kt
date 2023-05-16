package kr.co.testapp0501.model.album

data class AlbumCommentItemModel(
    val tvCommentName: String,
    val imgCommentProfile: String,
    val tvCommentContents: String,
    val tvCommentDate: String,
    val tvCommentLike: String,
    var replyClick: String,
): java.io.Serializable
