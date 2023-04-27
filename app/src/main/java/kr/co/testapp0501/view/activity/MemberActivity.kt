package kr.co.testapp0501.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityMemberBinding
import kr.co.testapp0501.model.recycler.RecyclerMemberData
import kr.co.testapp0501.view.adapter.RecyclerMemberActivityAdapter

class MemberActivity : AppCompatActivity() {
    
    private val binding : ActivityMemberBinding by lazy { ActivityMemberBinding.inflate(layoutInflater) }

    private val matchingItems = mutableListOf<RecyclerMemberData>()
    private val adminItems = mutableListOf<RecyclerMemberData>()
    private val memberItems = mutableListOf<RecyclerMemberData>()

    private val matchingAdapter = RecyclerMemberActivityAdapter(this, matchingItems)
    private val adminAdapter = RecyclerMemberActivityAdapter(this, adminItems)
    private val memberAdapter = RecyclerMemberActivityAdapter(this, memberItems)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerMatchingWait.adapter = matchingAdapter
        binding.recyclerAdmin.adapter = adminAdapter
        binding.recyclerMember.adapter = memberAdapter

        // 툴바 설정 [ 구성원 화면 ]
        setToolbar()

        // 더미데이터 추가해서 테스트 [ 매칭 ]
        for (i in 0 .. 2) {
            matchingItems.add(RecyclerMemberData(R.drawable.bg_edit, "길동이", "매칭하기",  "매칭대기"))
            matchingItems.add(RecyclerMemberData(R.drawable.bg_edit, "춘삼이", "매칭하기",  "매칭대기"))
            matchingItems.add(RecyclerMemberData(R.drawable.bg_edit, "돌석이", "매칭하기",  "매칭대기"))
        }

        // 더미데이터 추가해서 테스트 [ 관리자 ]
        for (i in 0 .. 2) {
            adminItems.add(RecyclerMemberData(R.drawable.bg_edit, "말동이", "admin",  "admin"))
            adminItems.add(RecyclerMemberData(R.drawable.bg_edit, "강순이", "admin",  "admin"))
        }

        // 더미데이터 추가해서 테스트 [ 멤버목록 ]
        for (i in 0 .. 10) {
            memberItems.add(RecyclerMemberData(R.drawable.bg_edit, "홍길동", "A",  "A"))
            memberItems.add(RecyclerMemberData(R.drawable.bg_edit, "김씨", "A",  "A"))
            memberItems.add(RecyclerMemberData(R.drawable.bg_edit, "황씨", "A",  "A"))
        }

        clickedMatching() // 매칭대기 클릭
        clickedAdmin() // 관리자 클릭
        clickedMember() // 멤버 클릭
    }

    // 매칭대기중인 사용자 클릭
    private fun clickedMatching(){
        matchingAdapter.setItemClickListener(object: RecyclerMemberActivityAdapter.OnItemClickListener{
            override fun itemClick(v: View, position: Int) {
                Toast.makeText(this@MemberActivity, matchingItems[position].tvName, Toast.LENGTH_SHORT).show()
            }

        })
    }

    // 관리자 클릭
    private fun clickedAdmin(){
        adminAdapter.setItemClickListener(object: RecyclerMemberActivityAdapter.OnItemClickListener{
            override fun itemClick(v: View, position: Int) {
                Toast.makeText(this@MemberActivity, adminItems[position].tvName, Toast.LENGTH_SHORT).show()
            }

        })
    }

    // 멤버 클릭
    private fun clickedMember(){
        memberAdapter.setItemClickListener(object: RecyclerMemberActivityAdapter.OnItemClickListener{
            override fun itemClick(v: View, position: Int) {
                Toast.makeText(this@MemberActivity, memberItems[position].tvName, Toast.LENGTH_SHORT).show()
            }

        })
    }

    // 툴바 설정 [ 구성원 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.text = "구성원"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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