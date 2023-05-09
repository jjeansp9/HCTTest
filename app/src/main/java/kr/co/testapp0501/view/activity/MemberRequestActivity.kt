package kr.co.testapp0501.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.databinding.ActivityMemberRequestBinding
import kr.co.testapp0501.databinding.ActivityProfileBinding
import kr.co.testapp0501.viewmodel.MemberViewModel

class MemberRequestActivity : BaseActivity<ActivityMemberRequestBinding>(R.layout.activity_member_request) {

    private lateinit var jwtToken : String
    private var groupSeq : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_request)

        jwtToken = intent.getStringExtra("jwtToken")!!
        groupSeq = intent.getIntExtra("groupSeq", groupSeq)
        Log.i("memberRequest", groupSeq.toString())

        viewDataBinding.vmMember = MemberViewModel(this, jwtToken, groupSeq)
        viewDataBinding.lifecycleOwner = this

        setToolbar()
        requestMemberList()
    }

    private fun requestMemberList(){
        Log.i("memberRequest", groupSeq.toString())
        viewDataBinding.vmMember?.groupMatchingList(jwtToken, groupSeq)
    }

    // 툴바 설정 [ 구성원 요청 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
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