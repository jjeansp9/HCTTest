package kr.co.testapp0501.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.testapp0501.model.album.AlbumCommentItemModel

class AlbumUploadViewModel : ViewModel() {
    companion object {
        private const val TAG = "albumUploadVM"
    }
    val albumUploadPhotos: MutableLiveData<List<String>> = MutableLiveData()

    fun getPhoto(){
        val commentList = mutableListOf<String>()
        commentList.add("")
        albumUploadPhotos.postValue(commentList)
    }
}