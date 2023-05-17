package kr.co.testapp0501.model.album

data class AlbumUploadModel(
    val title : String, // 타이틀
    val content : String, // 내용
    val bbsId : String, // 게시판 id
    val groupSeq : Int, // 그룹 seq
    val ntcrSeq : Int // 멤버 seq
)
