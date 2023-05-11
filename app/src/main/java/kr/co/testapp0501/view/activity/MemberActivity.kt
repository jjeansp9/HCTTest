package kr.co.testapp0501.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.databinding.ActivityMemberBinding
import kr.co.testapp0501.model.recycler.RecyclerMemberData
import kr.co.testapp0501.view.adapter.RecyclerMemberActivityAdapter
import kr.co.testapp0501.viewmodel.MainViewModel
import kr.co.testapp0501.viewmodel.MemberViewModel

class MemberActivity : BaseActivity<ActivityMemberBinding>(R.layout.activity_member) {

    private val memberItems = mutableListOf<RecyclerMemberData>()
    private val memberAdapter = RecyclerMemberActivityAdapter(this, memberItems)

    private lateinit var jwtToken: String
    private var groupSeq: Int = -1
    private var memberSeq: Int = -1
    private var memberLevel: Int = -1
    private var updateMember: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getIntentData() // jwtToken, groupSeq, memberSeq, memberLevel

        viewDataBinding.vmMember = MemberViewModel(this, jwtToken, groupSeq, memberSeq, memberLevel, 0)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.recyclerMember.adapter = memberAdapter

        setToolbar() // 툴바 설정 [ 구성원 화면 ]
        clickedMember() // 멤버 클릭
    }

    // jwtToken, groupSeq, memberSeq, memberLevel
    private fun getIntentData(){
        jwtToken = intent.getStringExtra("jwtToken")!!
        groupSeq = intent.getIntExtra("groupSeq", groupSeq)
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)
        memberLevel = intent.getIntExtra("memberLevel", memberLevel)
    }

    // 화면에 보여질 때마다 그룹목록 데이터 갱신
    override fun onResume() {
        super.onResume()
        groupMemberList() // 그룹 회원 목록
    }

    // 그룹에 속해있는 멤버 목록 불러오기
    @SuppressLint("NotifyDataSetChanged")
    private fun groupMemberList(){
        Log.i("jwtTokenAndGroupSeq", "$jwtToken, $groupSeq, $memberLevel")
        memberItems.clear()

        // 그룹에 속한 회원 목록
        viewDataBinding.vmMember?.groupMemberList(jwtToken, groupSeq)?.observe(this){
            val sortedData = it.data.sortedBy { it.memberVO.name } // 이름으로 정렬된 리스트 생성

            var firstIndex = 0 // 첫 번째 인덱스

            for (i in sortedData.indices) {
                val data = sortedData[i]
                if (data.memberAuthLevel == 1) { // memberAuthLevel이 1이면
                    memberItems.add(0, RecyclerMemberData(
                        -1,
                        data.memberVO.name,
                        data.memberVO.birth,
                        -1,
                        data.memberVO.seq,
                        data.seq,
                        "",
                        data.memberAuthLevel
                    ))
                    firstIndex++ // 첫 번째 인덱스를 1 증가시킴
                } else { // memberAuthLevel이 1이 아니면
                    memberItems.add(
                        RecyclerMemberData(
                            -1,
                            data.memberVO.name,
                            data.memberVO.birth,
                            -1,
                            data.memberVO.seq,
                            data.seq,
                            "",
                            data.memberAuthLevel
                        )
                    )
                }
            }
            viewDataBinding.recyclerMember.adapter?.notifyDataSetChanged()
        }
    }

    // 멤버 클릭
    private fun clickedMember(){
        memberAdapter.setItemClickListener(object: RecyclerMemberActivityAdapter.OnItemClickListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun itemClick(v: View, position: Int) {
                Toast.makeText(this@MemberActivity, "${memberItems[position].tvName}님의 프로필 화면으로 이동", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MemberActivity, ProfileActivity::class.java)
                intent.putExtra("memberName", memberItems[position].tvName)
                startActivity(intent)
            }

            override fun acceptClick(v: View, position: Int) {
                return
            }

            override fun cancelClick(v: View, position: Int) {
                return
            }
        })
    }

    // 툴바 설정 [ 구성원 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        if (memberLevel == 1) findViewById<ImageView>(R.id.btn_member_accept_list).visibility = View.VISIBLE
        else findViewById<ImageView>(R.id.btn_member_accept_list).visibility = View.GONE

        findViewById<ImageView>(R.id.btn_member_search).visibility = View.VISIBLE

        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.visibility = View.VISIBLE
        tv.text = "구성원"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    // 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}