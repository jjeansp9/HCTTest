package kr.co.testapp0501.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.testapp0501.model.album.AlbumResponseModel
import kr.co.testapp0501.model.album.AlbumUploadPhotoModel
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumUploadViewModel : ViewModel() {
    companion object {
        private const val TAG = "albumUploadVM"
    }
    val albumUploadPhotos: MutableLiveData<MutableList<AlbumUploadPhotoModel>> = MutableLiveData()
    val getBoardDetailInfo: MutableLiveData<AlbumResponseModel> = MutableLiveData()

    // 사진 한장씩 가져오기
    fun addPhotoToAlbum(uri: String): LiveData<Int>{
        val duplicatePhoto: MutableLiveData<Int> = MutableLiveData()

        Log.i(TAG+ " Uri", uri)

        val photoList = albumUploadPhotos.value?.toMutableList() ?: mutableListOf()

        // 중복된 uri 값이 있는지 확인하여 추가하지 않음
        val hasDuplicate = photoList.any { it.photo == uri }
        if (!hasDuplicate) {
            photoList.add(0, AlbumUploadPhotoModel(uri))
            //albumUploadPhotos.value = photoList.toMutableList()
            albumUploadPhotos.postValue(photoList)
            Log.i(TAG+ " Uri1", uri)
            duplicatePhoto.value = 200 // 중복되지 않음
        }else{
            duplicatePhoto.value = 409 // 중복된 사진
        }
        return duplicatePhoto
    }

    // 사진 리스트로 가져오기
    fun addPhotoListToAlbum(uri: MutableList<Uri>): LiveData<Int> {
        val duplicatePhoto: MutableLiveData<Int> = MutableLiveData()

        val photoList = albumUploadPhotos.value?.toMutableList() ?: mutableListOf()

        // 중복된 uri 값이 있는지 확인하여 추가하지 않음
        var hasDuplicate = true
        for (i in 0 until uri.size){
            hasDuplicate = photoList.any { it.photo == uri[i].toString() }
        }
        if (!hasDuplicate) {
            for (i in 0 until uri.size){
                photoList.add(0, AlbumUploadPhotoModel(uri[i].toString()))
            }
            albumUploadPhotos.value = photoList
            duplicatePhoto.value = 200 // 중복되지 않음
        } else {
            duplicatePhoto.value = 409 // 중복된 사진
        }

        return duplicatePhoto
    }

    // 게시글 등록
    fun clickedComplete (
        token: String,
        board: RequestBody,
        albumImg: MutableList<MultipartBody.Part>
    ): LiveData<Int>{
        val resultCode = MutableLiveData<Int>()
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstanceFirst()!!.create(ApiService::class.java)

        apiService.boardUpload(token, board, albumImg).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                resultCode.value = response.code()
                if (response.isSuccessful){

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
            }

        })
        return resultCode
    }

    // 게시글 수정
    fun boardUpdate(jwtToken: String, board: RequestBody, img: MutableList<MultipartBody.Part>): LiveData<Int>{
        val resultCode = MutableLiveData<Int>()
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstanceFirst()!!.create(ApiService::class.java)

        apiService.boardUpdate(jwtToken, board, img).enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i(TAG+" updateCode", response.code().toString())
                resultCode.value = response.code()
                if (response.isSuccessful){

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })
        return resultCode
    }

    // 게시글 상세정보
    fun getBoardDetailInfo(jwtToken: String, boardSeq: Int){
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstanceFirst()!!.create(ApiService::class.java)

        apiService.boardDetailInfo(jwtToken, boardSeq).enqueue(object: Callback<AlbumResponseModel>{
            override fun onResponse(call: Call<AlbumResponseModel>, response: Response<AlbumResponseModel>) {
                Log.i(TAG+ " detailInfo code", response.code().toString())
                if (response.isSuccessful){
                    getBoardDetailInfo.value = response.body()
                }
            }

            override fun onFailure(call: Call<AlbumResponseModel>, t: Throwable) {
            }

        })
    }
}































