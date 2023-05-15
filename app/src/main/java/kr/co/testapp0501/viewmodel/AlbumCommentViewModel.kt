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

        val commentList = mutableListOf<AlbumCommentItemModel>()

        for (i in 0 .. 15){
            commentList.add(AlbumCommentItemModel("홍길동", "", "안녕하세요", "2023.03.21.22:30", "12"))
        }

        albumCommentItems.postValue(commentList)
    }
}