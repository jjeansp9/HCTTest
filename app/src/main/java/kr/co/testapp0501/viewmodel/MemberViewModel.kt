package kr.co.testapp0501.viewmodel

import android.util.Log
import kr.co.testapp0501.base.BaseViewModel
import kr.co.testapp0501.model.group.GroupMemberList
import kr.co.testapp0501.model.group.MatchingWaitingList
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberViewModel : BaseViewModel() {

    // 그룹 매칭 대기 회원 조회
    fun groupMatchingList(jwtToken: String, groupSeq: Int){
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)
        val requestUrl = ApiService.BASE_URL + ApiService.GROUP_MATCHING_WAITING_LIST

        Log.i("MemberViewModel groupMatchingList value", "$jwtToken, $groupSeq")
        Log.i("MemberViewModel groupMatchingList Url", requestUrl) // 요청 url

        apiService.groupMatchingList(jwtToken, groupSeq).enqueue(object : Callback<MatchingWaitingList>{
            override fun onResponse(
                call: Call<MatchingWaitingList>,
                response: Response<MatchingWaitingList>
            ) {
                Log.i("MemberViewModel groupMatchingList code", response.code().toString())
                if (response.isSuccessful){

                }
            }

            override fun onFailure(call: Call<MatchingWaitingList>, t: Throwable) {

            }

        })
    }

    // 그룸 회원 조회
    fun groupMemberList(jwtToken: String, groupSeq: Int){
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)
        val requestUrl = ApiService.BASE_URL + ApiService.GROUP_MEMBER_LIST

        Log.i("MemberViewModel groupMemberList value", "$jwtToken, $groupSeq")
        Log.i("MemberViewModel groupMemberList Url", requestUrl) // 요청 url

        apiService.groupMemberList(jwtToken, groupSeq).enqueue(object : Callback<GroupMemberList>{
            override fun onResponse(
                call: Call<GroupMemberList>,
                response: Response<GroupMemberList>
            ) {
                Log.i("MemberViewModel groupMemberList code", response.code().toString())
                if (response.isSuccessful){

                }
            }

            override fun onFailure(call: Call<GroupMemberList>, t: Throwable) {

            }

        })
    }

}