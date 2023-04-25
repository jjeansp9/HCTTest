package kr.co.testapp0501.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityMemberBinding

class MemberActivity : AppCompatActivity() {
    
    val binding : ActivityMemberBinding by lazy { ActivityMemberBinding.inflate(layoutInflater) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 툴바 설정 [ 구성원 화면 ]
        setToolbar()
    }

    // 툴바 설정 [ 구성원 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.text = "구성원"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}