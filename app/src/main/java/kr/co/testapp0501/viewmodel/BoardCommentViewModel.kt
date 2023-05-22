package kr.co.testapp0501.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.testapp0501.base.BaseViewModel
import kr.co.testapp0501.model.album.AlbumCommentItemModel
import kr.co.testapp0501.model.comment.CommentSend
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardCommentViewModel : ViewModel() {
    companion object {
        private const val TAG = "boardCommentVM"
    }
    val albumCommentItems: MutableLiveData<List<AlbumCommentItemModel>> = MutableLiveData()

    // 댓글 작성
    fun commentSend(jwtToken: String, commentSend: CommentSend): LiveData<Int>{
        val resultCode = MutableLiveData<Int>()
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstanceFirst()!!.create(ApiService::class.java)

        apiService.commentSend(jwtToken, commentSend).enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i(TAG +" Code", response.code().toString())
                if (response.isSuccessful){
                    resultCode.value = response.code()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })
        return resultCode
    }

    // 댓글 리스트 조회
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