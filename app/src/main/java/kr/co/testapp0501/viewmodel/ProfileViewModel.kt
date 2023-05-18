package kr.co.testapp0501.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import kr.co.testapp0501.model.profile.ProfileInfoResponse
import kr.co.testapp0501.model.profile.ProfileUpdateRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel: ViewModel() {

    companion object{
        const val TAG = "profileVM"
    }

    val profileInfo = MutableLiveData<ProfileInfoResponse>()
    val profileGender = MutableLiveData<String>()

    // 회원 조회 [프로필]
    fun requestMemberInfo(jwtToken: String, memberSeq: Int){
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstanceFirst()!!.create(ApiService::class.java)
        val requestUrl = ApiService.BASE_URL_FIRST + ApiService.PROFILE_MEMBER_INFO

        Log.i("MemberViewModel groupMatchingAccept value", "$jwtToken, $memberSeq")
        Log.i("MemberViewModel groupMatchingAccept Url", requestUrl) // 요청 url

        apiService.profileMemberInfo(jwtToken, memberSeq).enqueue(object: Callback<ProfileInfoResponse>{
            override fun onResponse(call: Call<ProfileInfoResponse>, response: Response<ProfileInfoResponse>) {
                Log.i(TAG, response.code().toString())
                if (response.isSuccessful){
                    profileInfo.value = response.body()
                    profileGender.value = response.body()?.data?.sex
                    Log.i(TAG + "data", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ProfileInfoResponse>, t: Throwable) {
            }

        })
    }

    fun profileChanged(jwtToken: String, memberSeq: Int, name: String, birth: String, gender: String, phoneNum: String): LiveData<Int> {
        val result = MutableLiveData<Int>()
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstanceFirst()!!.create(ApiService::class.java)

        val putData = ProfileUpdateRequest(memberSeq, name, phoneNum, birth, gender)
        Log.i(TAG +"putData", putData.toString())

        apiService.profileMemberInfoChange(jwtToken, putData).enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i(TAG + "code", response.code().toString())
                if (response.isSuccessful){
                    result.value= response.code()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })



        return result
    }

}






























