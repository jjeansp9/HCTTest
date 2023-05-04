package kr.co.testapp0501.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.testapp0501.model.group.Group
import kr.co.testapp0501.model.group.GroupList
import kr.co.testapp0501.model.group.GroupMatching
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import kr.co.testapp0501.model.user.UserModel
import kr.co.testapp0501.model.user.UserResponse
import kr.co.testapp0501.view.activity.GroupActivity
import kr.co.testapp0501.view.activity.LoginActivity
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

        apiService.uploadData(token, groupInfo, groupImg).enqueue(object : Callback<String> {
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
    }

    // 그룹 목록
    fun loadGroupList(jwtToken: String) : LiveData<GroupList>{
        val groupList = MutableLiveData<GroupList>()
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        Log.i("GroupActivity before response", "before")

        apiService.loadGroupList(jwtToken, 1).enqueue(object : Callback<GroupList>{
            override fun onResponse(call: Call<GroupList>, response: Response<GroupList>) {
                groupList.value = response.body()
                Log.i("GroupActivity after response", response.code().toString())
                Log.i("GroupActivity Http",response.body().toString())
                Log.i("GroupActivity Http",response.message() +"," + call.toString())
            }
            override fun onFailure(call: Call<GroupList>, t: Throwable) {
            }
        })

        return groupList
    }

    // 그룹 코드
    fun groupMatching(jwtToken: String, groupMatching: GroupMatching) : LiveData<Int>{
        var serverResponse = MutableLiveData<Int>()
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        Log.i("GroupViewModel groupMatching jwtToken", jwtToken)

        apiService.groupMatching(jwtToken, groupMatching).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                serverResponse.value = response.code()
                Log.i("item", response.code().toString())
                if (response.isSuccessful){

                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
            }

        })

        return serverResponse
    }





}