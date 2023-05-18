package kr.co.testapp0501.model.album

data class AlbumListResponseModel(
    val msg: String,
    val data: List<DataItem>
)

data class DataItem(
    val seq: Int,
    val title: String,
    val content: String,
    val bbsId: String,
    val groupSeq: Int,
    val like: Int,
    val rdcnt: Int,
    val memberVO: MemberVO,
    val insertDate: String,
    val commentCnt: Int,
    val fileId: String,
    val fileList: List<FileItem>
)

data class MemberVO(
    val seq: Int,
    val name: String,
    val phoneNumber: String,
    val birth: String,
    val sex: String,
    val fileId: String,
    val fileVOList: List<FileVOList>
    // Add other member properties here
)

data class FileVOList(
    val seq: Int,
    val fileId: String,
    val orgName: String,
    val saveName: String,
    val path: String
)

data class FileItem(
    val seq: Int,
    val fileId: String,
    val orgName: String,
    val saveName: String,
    val contentType: String,
    val extension: String,
    val path: String,
    val size: Int,
    val insertDate: String
    // Add other file properties here
)