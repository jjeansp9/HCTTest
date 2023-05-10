package kr.co.testapp0501.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.databinding.ActivityMemberRequestBinding
import kr.co.testapp0501.databinding.ActivityProfileBinding
import kr.co.testapp0501.model.group.GroupMatchingAccept
import kr.co.testapp0501.model.recycler.RecyclerMemberData
import kr.co.testapp0501.view.adapter.RecyclerMemberActivityAdapter
import kr.co.testapp0501.viewmodel.MemberViewModel

class MemberRequestActivity : BaseActivity<ActivityMemberRequestBinding>(R.layout.activity_member_request) {

    private val matchingItems = mutableListOf<RecyclerMemberData>()
    private val matchingAdapter = RecyclerMemberActivityAdapter(this, matchingItems)

    private lateinit var jwtToken : String
    private var groupSeq : Int = -1
    private var memberSeq: Int = -1
    private var memberLevel: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jwtToken = intent.getStringExtra("jwtToken")!!
        groupSeq = intent.getIntExtra("groupSeq", groupSeq)
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)
        memberLevel = intent.getIntExtra("memberLevel", memberLevel)
        Log.i("memberRequest groupSeq", groupSeq.toString())

        viewDataBinding.vmMember = MemberViewModel(this, jwtToken, groupSeq, memberSeq, memberLevel)
        viewDataBinding.lifecycleOwner = this

        viewDataBinding.recyclerMatchingWait.adapter = matchingAdapter

        setToolbar()
        requestMemberList()
        clickedMatching()
    }

    // 매칭대기중인 사용자 클릭
    private fun clickedMatching(){
        matchingAdapter.setItemClickListener(object: RecyclerMemberActivityAdapter.OnItemClickListener{
            override fun itemClick(v: View, position: Int) {
                return
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun acceptClick(v: View, position: Int) {

                val seq = GroupMatchingAccept(matchingItems[position].groupSeq, matchingItems[position].memberSeq, matchingItems[position].matchingSeq)
                viewDataBinding.vmMember?.groupMatchingAccept(jwtToken, seq)

                Toast.makeText(this@MemberRequestActivity, matchingItems[position].tvName+"님의 요청을 수락하였습니다.", Toast.LENGTH_SHORT).show()

                matchingItems.removeAt(position)
                viewDataBinding.recyclerMatchingWait.adapter?.notifyDataSetChanged()


            }

            override fun cancelClick(v: View, position: Int) {
                Toast.makeText(this@MemberRequestActivity, matchingItems[position].tvName+"님의 요청을 거절하시겠습니까?", Toast.LENGTH_SHORT).show()
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun requestMemberList(){
        Log.i("memberRequestActivity", "$jwtToken, $groupSeq, $memberLevel")
        if (memberLevel == 1) {
            viewDataBinding.vmMember?.groupMatchingList(jwtToken, groupSeq)?.observe(this){
                for (i in it.data.indices){
                    matchingItems.add(RecyclerMemberData(
                        -1,
                        it.data[i].memberVO.name,
                        it.data[i].memberVO.birth,
                        it.data[i].groupSeq,
                        it.data[i].memberVO.seq,
                        it.data[i].seq,
                        it.data[i].permission,
                        10
                    ))
                }
                //Log.i("wwwwww", matchingItems.size.toString() + it.data[0].memberVO.name)
                viewDataBinding.recyclerMatchingWait.adapter?.notifyDataSetChanged()
            }
        }
    }

    // 툴바 설정 [ 구성원 요청 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.visibility = View.VISIBLE
        tv.text = "구성원 요청"
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