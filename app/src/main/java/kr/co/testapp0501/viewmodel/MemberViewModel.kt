package kr.co.testapp0501.viewmodel

import kr.co.testapp0501.base.BaseViewModel
import kr.co.testapp0501.model.group.MatchingWaitingList
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberViewModel : BaseViewModel() {

    private fun groupMatchingList(){
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        apiService.groupMatchingList("", 1).enqueue(object : Callback<MatchingWaitingList>{
            override fun onResponse(
                call: Call<MatchingWaitingList>,
                response: Response<MatchingWaitingList>
            ) {

            }

            override fun onFailure(call: Call<MatchingWaitingList>, t: Throwable) {

            }

        })
    }

}