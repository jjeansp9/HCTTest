package kr.co.testapp0501.viewmodel

import android.content.Context
import android.content.Intent
import kr.co.testapp0501.base.BaseViewModel
import kr.co.testapp0501.view.activity.*
import java.lang.ref.WeakReference

class MainViewModel(
    context: Context,
    private val jwtToken: String,
    private val groupSeq: Int,
    private val memberSeq: Int,
    private val memberLevel: Int
    ) : BaseViewModel(){

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

    fun onClickMember() {startNewActivity(MemberActivity::class.java)} // 구성원 메뉴 클릭
    fun onClickAlbum() {startNewActivity(AlbumActivity::class.java)} // 앨범 메뉴 클릭
    fun onClickProfile() {startNewActivity(ProfileActivity::class.java)} // 프로필 메뉴 클릭

}