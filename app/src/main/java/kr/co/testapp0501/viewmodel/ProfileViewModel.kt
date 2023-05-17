package kr.co.testapp0501.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.testapp0501.base.BaseViewModel
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import kr.co.testapp0501.model.profile.ProfileInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel: ViewModel() {

    companion object{
        const val TAG = "profileVM"
    }

    val profileInfo = MutableLiveData<ProfileInfoResponse>()

    // 회원 조회 [프로필]
    fun requestMemberInfo(jwtToken: String, memberSeq: Int){

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)
        val requestUrl = ApiService.BASE_URL_FIRST + ApiService.PROFILE_MEMBER_INFO

        Log.i("MemberViewModel groupMatchingAccept value", "$jwtToken, $memberSeq")
        Log.i("MemberViewModel groupMatchingAccept Url", requestUrl) // 요청 url

        apiService.profileMemberInfo(jwtToken, memberSeq).enqueue(object: Callback<ProfileInfoResponse>{
            override fun onResponse(call: Call<ProfileInfoResponse>, response: Response<ProfileInfoResponse>) {
                Log.i(TAG, response.code().toString())
                if (response.isSuccessful){
                    profileInfo.value = response.body()
                    Log.i(TAG + "data", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ProfileInfoResponse>, t: Throwable) {
            }

        })
    }

}






























