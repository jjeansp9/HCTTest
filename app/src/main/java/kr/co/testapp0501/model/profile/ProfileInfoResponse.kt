package kr.co.testapp0501.model.profile

data class ProfileInfoResponse(
    val msg: String,
    val data: ProfileResponse
)

data class ProfileResponse(
    val seq: Int,
    val name: String,
    val phoneNumber: String,
    val birth: String,
    val sex: String,
    val fileId: String,
    val fileVOList: List<FileVO>
)

data class FileVO(
    val seq: Int,
    val fileId: String,
    val orgName: String,
    val saveName: String,
    val path: String
)


