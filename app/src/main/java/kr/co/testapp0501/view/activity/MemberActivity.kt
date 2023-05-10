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
import kr.co.testapp0501.viewmodel.MemberViewModel

class MemberActivity : BaseActivity<ActivityMemberBinding>(R.layout.activity_member) {

//    private val matchingItems = mutableListOf<RecyclerMemberData>()
//    private val adminItems = mutableListOf<RecyclerMemberData>()
    private val memberItems = mutableListOf<RecyclerMemberData>()

//    private val matchingAdapter = RecyclerMemberActivityAdapter(this, matchingItems)
//    private val adminAdapter = RecyclerMemberActivityAdapter(this, adminItems)
    private val memberAdapter = RecyclerMemberActivityAdapter(this, memberItems)

    private lateinit var jwtToken: String
    private var groupSeq: Int = -1
    private var memberSeq: Int = -1
    private var memberLevel: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jwtToken = intent.getStringExtra("jwtToken")!!
        groupSeq = intent.getIntExtra("groupSeq", groupSeq)
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)
        memberLevel = intent.getIntExtra("memberLevel", memberLevel)

        viewDataBinding.vmMember = MemberViewModel(this, jwtToken, groupSeq, memberSeq, memberLevel)
        viewDataBinding.lifecycleOwner = this
//        viewDataBinding.recyclerMatchingWait.adapter = matchingAdapter
//        viewDataBinding.recyclerAdmin.adapter = adminAdapter
        viewDataBinding.recyclerMember.adapter = memberAdapter

        setToolbar() // 툴바 설정 [ 구성원 화면 ]
        clickedMember() // 멤버 클릭
    }

    override fun onResume() {
        super.onResume()
        groupMemberList() // 그룹 회원 목록
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun groupMemberList(){
        Log.i("jwtTokenAndGroupSeq", "$jwtToken, $groupSeq, $memberLevel")
        memberItems.clear()
//
//        viewDataBinding.vmMember?.groupMatchingList(jwtToken, groupSeq)

        // 그룹에 속한 회원 목록
        viewDataBinding.vmMember?.groupMemberList(jwtToken, groupSeq)?.observe(this){
            for(i in it.data.indices){
                memberItems.add(RecyclerMemberData(
                    -1,
                    it.data[i].memberVO.name,
                    it.data[i].memberVO.birth,
                    -1,
                    it.data[i].memberVO.seq,
                    it.data[i].seq,
                    "",
                    it.data[i].memberAuthLevel
                ))

            }
            viewDataBinding.recyclerMember.adapter?.notifyDataSetChanged()
        }
    }

    // 멤버 클릭
    private fun clickedMember(){
        memberAdapter.setItemClickListener(object: RecyclerMemberActivityAdapter.OnItemClickListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun itemClick(v: View, position: Int) {
                Toast.makeText(this@MemberActivity, memberItems[position].tvName, Toast.LENGTH_SHORT).show()
                memberItems.removeAt(position)
                viewDataBinding.recyclerMember.adapter?.notifyDataSetChanged()
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

        findViewById<ImageView>(R.id.btn_member_accept_list).visibility = View.VISIBLE
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