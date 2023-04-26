package kr.co.testapp0501.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityMemberBinding
import kr.co.testapp0501.model.recycler.RecyclerGroupData
import kr.co.testapp0501.model.recycler.RecyclerMemberData
import kr.co.testapp0501.view.adapters.RecyclerMemberActivityAdapter

class MemberActivity : AppCompatActivity() {
    
    private val binding : ActivityMemberBinding by lazy { ActivityMemberBinding.inflate(layoutInflater) }
    private val memberItems = mutableListOf<RecyclerMemberData>()
    private val memberAdapter = RecyclerMemberActivityAdapter(this, memberItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerMember.adapter = memberAdapter

        // 툴바 설정 [ 구성원 화면 ]
        setToolbar()

        // 더미데이터 추가해서 테스트
        for (i in 0 .. 10) {
            memberItems.add(RecyclerMemberData(R.drawable.bg_edit, "홍길동"))
            memberItems.add(RecyclerMemberData(R.drawable.bg_edit, "김씨"))
            memberItems.add(RecyclerMemberData(R.drawable.bg_edit, "황씨"))
        }
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