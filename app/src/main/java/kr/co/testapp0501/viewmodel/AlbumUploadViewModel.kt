package kr.co.testapp0501.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    fun addPhotoToAlbum(uri: String){

        val photoList = albumUploadPhotos.value?.toMutableList() ?: mutableListOf()
        photoList.add(0, AlbumUploadPhotoModel(uri))
        albumUploadPhotos.value = photoList.toMutableList()
        Log.i(TAG, albumUploadPhotos.postValue(photoList).toString())
    }

    fun clickedComplete (
        token: String,
        board: RequestBody,
        albumImg: MutableList<MultipartBody.Part>
    ): LiveData<Int>{
        val resultCode = MutableLiveData<Int>()
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        apiService.albumBoardUpload(token, board, albumImg).enqueue(object : Callback<String>{
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
}