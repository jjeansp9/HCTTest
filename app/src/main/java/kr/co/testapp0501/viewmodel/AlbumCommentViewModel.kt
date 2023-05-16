package kr.co.testapp0501.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.testapp0501.base.BaseViewModel
import kr.co.testapp0501.model.album.AlbumCommentItemModel

class AlbumCommentViewModel : ViewModel() {
    companion object {
        private const val TAG = "albumCommentVM"
    }
    val albumCommentItems: MutableLiveData<List<AlbumCommentItemModel>> = MutableLiveData()

    fun loadCommentList(){

        // ui 테스트 하기위한 더미데이터
        val commentList = mutableListOf<AlbumCommentItemModel>()

        for (i in 0 .. 5){
            commentList.add(AlbumCommentItemModel(
                "홍길동",
                "",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "2023.03.21.22:30",
                "12"
            ))
            commentList.add(AlbumCommentItemModel(
                "홍길동1",
                "",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "2023.03.21.22:30",
                "12"
            ))
        }

        albumCommentItems.postValue(commentList)
    }
}