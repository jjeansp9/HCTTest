package kr.co.testapp0501.model.recycler

data class RecyclerAlbumData(
    val albumProfileImg: String,
    val albumName: String,
    val albumUploadDate: String,
    val albumUploadPicture: List<Int>,
    val albumTitle: String,
    val albumContents: String,
    val albumLike: Int,
    val albumComment: Int,
)
