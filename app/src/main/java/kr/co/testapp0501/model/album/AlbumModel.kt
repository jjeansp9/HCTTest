package kr.co.testapp0501.model.album

data class AlbumModel(
    val albumProfileImg: String,
    val albumName: String,
    val albumUploadDate: String,
    val albumUploadPicture: String,
    val albumTitle: String,
    val albumContents: String,
    val albumLike: Int,
    val albumComment: Int,
)
