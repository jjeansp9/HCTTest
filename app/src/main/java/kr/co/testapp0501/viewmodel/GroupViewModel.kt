package kr.co.testapp0501.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.testapp0501.model.group.Group
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupViewModel : ViewModel(){

    private val _code = MutableLiveData<String>()
    val code : LiveData<String>
        get() = _code


    fun createGroup(token : String, groupInfo : Group, groupImg : MultipartBody.Part){
        viewModelScope.launch {
            val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(
                ApiService::class.java)

            apiService.uploadData(token, groupInfo, groupImg).enqueue(object : Callback<String> {

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    _code.value = response.code().toString()
                    Log.i("GroupViewModel code", response.code().toString())
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                }

            })
        }
    }
}