package kr.co.testapp0501.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import kr.co.testapp0501.base.BaseViewModel
import kr.co.testapp0501.model.group.GroupMemberList
import kr.co.testapp0501.model.group.MatchingWaitingList
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import kr.co.testapp0501.view.activity.MemberActivity
import kr.co.testapp0501.view.activity.MemberRequestActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

class MemberViewModel(context: Context, private val jwtToken: String, private val groupSeq: Int) : BaseViewModel() {

    // 이와 같은 방법으로 context를 받아야 메모리가 누수되는 현상 방지됨
    private val contextRef = WeakReference(context)

    // 받아온 Activity 에 따라 화면 전환
    private fun startNewActivity(cls: Class<*>) {
        val context = contextRef.get() ?: return
        val intent = Intent(context, cls)
        intent.putExtra("jwtToken", jwtToken)
        intent.putExtra("groupSeq", groupSeq)
        context.startActivity(intent)
    }

    fun onClickMemberRequest() {
        startNewActivity(MemberRequestActivity::class.java)
        Log.i("click", "clicked")
    } // 멤버 요청 대기화면으로 이동

    // 그룹 매칭 대기 회원 조회
    fun groupMatchingList(jwtToken: String, groupSeq: Int){
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)
        val requestUrl = ApiService.BASE_URL + ApiService.GROUP_MATCHING_WAITING_LIST

        Log.i("MemberViewModel groupMatchingList value", "$groupSeq, $jwtToken")
        Log.i("MemberViewModel groupMatchingList Url", requestUrl) // 요청 url
        apiService.groupMatchingList(jwtToken, groupSeq).enqueue(object : Callback<String>{
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.i("MemberViewModel groupMatchingList code", response.code().toString())
                if (response.isSuccessful){

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

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