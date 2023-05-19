package kr.co.testapp0501.model.album

data class AlbumModel(
    val boardSeq: Int,
    val albumProfileImg: String,
    val albumMemberSeq: Int,
    val albumName: String,
    val albumUploadDate: String,
    val albumUploadPicture: String,
    val albumTitle: String,
    val albumContents: String,
    val albumLike: Int,
    val albumComment: Int,
)
