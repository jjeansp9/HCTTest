package kr.co.testapp0501.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kr.co.testapp0501.base.BaseViewModel
import kr.co.testapp0501.model.group.GroupMatchingAccept
import kr.co.testapp0501.model.group.GroupMemberList
import kr.co.testapp0501.model.group.MatchingWaitingList
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import kr.co.testapp0501.view.activity.MemberRequestActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

class MemberViewModel(
    context: Context,
    private val jwtToken: String,
    private val groupSeq: Int,
    private val memberSeq: Int,
    private val memberLevel: Int,
    private val updateMember: Int
    ) : BaseViewModel() {

    // 이와 같은 방법으로 context를 받아야 메모리가 누수되는 현상 방지됨
    private val contextRef = WeakReference(context)

    // 받아온 Activity 에 따라 화면 전환
    private fun startNewActivity(cls: Class<*>) {
        val context = contextRef.get() ?: return
        val intent = Intent(context, cls)

        intent.putExtra("jwtToken", jwtToken)
        intent.putExtra("groupSeq", groupSeq)
        intent.putExtra("memberSeq", memberSeq)
        intent.putExtra("memberLevel", memberLevel)
        context.startActivity(intent)
    }


    // 멤버 요청 대기화면으로 이동
    fun onClickMemberRequest() {
        if (updateMember == 1){

        }
        startNewActivity(MemberRequestActivity::class.java)
        Log.i("ssss", updateMember.toString())
    }

    // 그룹 매칭 대기 회원 조회
    fun groupMatchingList(jwtToken: String, groupSeq: Int): LiveData<MatchingWaitingList>{

        val memberWaitingList = MutableLiveData<MatchingWaitingList>()

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstanceFirst()!!.create(ApiService::class.java)
        val requestUrl = ApiService.BASE_URL_FIRST + ApiService.GROUP_MATCHING_WAITING_LIST

        Log.i("MemberViewModel groupMatchingList value", "$groupSeq, $jwtToken")
        Log.i("MemberViewModel groupMatchingList Url", requestUrl) // 요청 url

        apiService.groupMatchingList(jwtToken, groupSeq).enqueue(object : Callback<MatchingWaitingList>{
            override fun onResponse(
                call: Call<MatchingWaitingList>,
                response: Response<MatchingWaitingList>
            ) {
                Log.i("MemberViewModel groupMatchingList code", response.code().toString())
                if (response.isSuccessful){
                    memberWaitingList.value = response.body()
                    memberWaitingList.value?.msg = response.code().toString()
                }
            }

            override fun onFailure(call: Call<MatchingWaitingList>, t: Throwable) {

            }
        })
        return memberWaitingList
    }

    // 그룸 회원 조회
    fun groupMemberList(jwtToken: String, groupSeq: Int): LiveData<GroupMemberList> {

        val memberList = MutableLiveData<GroupMemberList>()

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstanceFirst()!!.create(ApiService::class.java)
        val requestUrl = ApiService.BASE_URL_FIRST + ApiService.GROUP_MEMBER_LIST

        Log.i("MemberViewModel groupMemberList value", "$jwtToken, $groupSeq")
        Log.i("MemberViewModel groupMemberList Url", requestUrl) // 요청 url

        apiService.groupMemberList(jwtToken, groupSeq).enqueue(object : Callback<GroupMemberList>{
            override fun onResponse(
                call: Call<GroupMemberList>,
                response: Response<GroupMemberList>
            ) {
                Log.i("MemberViewModel groupMemberList code", response.code().toString())
                if (response.isSuccessful){
                    memberList.value = response.body()
                }
            }

            override fun onFailure(call: Call<GroupMemberList>, t: Throwable) {

            }

        })

        return memberList
    }

    // 그룸 매칭 대기 회원 수락
    fun groupMatchingAccept(jwtToken: String, memberSeq: GroupMatchingAccept): LiveData<String> {

        val memberList = MutableLiveData<String>()

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstanceFirst()!!.create(ApiService::class.java)
        val requestUrl = ApiService.BASE_URL_FIRST + ApiService.GROUP_MATCHING_WAITING_ACCEPT

        Log.i("MemberViewModel groupMatchingAccept value", "$jwtToken, $memberSeq")
        Log.i("MemberViewModel groupMatchingAccept Url", requestUrl) // 요청 url

        apiService.groupMatchingAccept(jwtToken, memberSeq).enqueue(object : Callback<String>{
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.i("MemberViewModel groupMatchingAccept code", response.code().toString())
                if (response.isSuccessful){
                    memberList.value = response.body()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })

        return memberList
    }

}

private fun <T> LiveData<T>.observe(context: Context, observer: Observer<T>) {

}
