package kr.co.testapp0501.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kr.co.testapp0501.base.BaseViewModel
import kr.co.testapp0501.model.album.AlbumListResponseModel
import kr.co.testapp0501.model.album.DataItem
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumViewModel: BaseViewModel() {
    companion object{
        private const val TAG = "AlbumVM"
    }
    val albumBoardList = MutableLiveData<List<DataItem>>()

    // 앨범 게시글 목록 불러오기
    fun albumListRequest(jwtToken: String, bbsId: String, groupSeq: Int, seq: Int){

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstanceFirst()!!.create(ApiService::class.java)
        Log.i(TAG+" url", ApiService.BASE_URL_FIRST+ApiService.ALBUM_BOARD_LIST)

        Log.i(TAG+" requestData", "bbsId: $bbsId, groupSeq: $groupSeq, seq: $seq")

        apiService.albumBoardList(jwtToken, bbsId, groupSeq, seq).enqueue(object: Callback<AlbumListResponseModel>{
            override fun onResponse(
                call: Call<AlbumListResponseModel>,
                response: Response<AlbumListResponseModel>
            ) {
                Log.i(TAG+" code", response.code().toString())
                if (response.isSuccessful){
                    albumBoardList.value = response.body()?.data
                    Log.i(TAG, albumBoardList.value.toString())
                }
            }
            override fun onFailure(call: Call<AlbumListResponseModel>, t: Throwable) {

            }

        })
    }
}