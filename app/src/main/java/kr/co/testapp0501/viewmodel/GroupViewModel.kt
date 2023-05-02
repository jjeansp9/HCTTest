package kr.co.testapp0501.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.testapp0501.model.group.Group
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupViewModel : ViewModel(){

    private val _code = MutableLiveData<String>()
    val code : LiveData<String>
        get() = _code

    // 그룹 생성
    fun createGroup(token : String, groupInfo : RequestBody, groupImg : MultipartBody.Part){
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        apiService.uploadData2(token, groupInfo, groupImg).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                _code.value = response.code().toString()
                Log.i("GroupViewModel code", "code : ${response.code()}")
                Log.i("GroupViewModel code", response.headers().name(0))
                Log.i("GroupViewModel code", response.body().toString())
                Log.i("GroupViewModel code", response.headers().value(0))
                Log.i("GroupViewModel code", response.errorBody().toString())
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("GroupViewModel error", "error : ${t.message}")
            }
        })

//        apiService.uploadData3(token, groupImg).enqueue(object : Callback<String> {
//            override fun onResponse(
//                call: Call<String>,
//                response: Response<String>
//            ) {
//                _code.value = response.code().toString()
//                Log.i("GroupViewModel code", "code : ${response.code()}")
//            }
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.e("GroupViewModel error", "error : ${t.message}")
//            }
//        })

//        val dataJsonObject = JSONObject()
//        dataJsonObject.put()
    }
}