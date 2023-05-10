package kr.co.testapp0501.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.common.CommonUtil
import kr.co.testapp0501.databinding.ActivityMainBinding
import kr.co.testapp0501.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jwtToken = intent.getStringExtra("jwtToken")!!
        val groupSeq = intent.getIntExtra("groupSeq", -1)
        val memberSeq = intent.getIntExtra("memberSeq", -1)
        val memberLevel = intent.getIntExtra("memberLevel", -1)

        viewDataBinding.vmMain = MainViewModel(this, jwtToken, groupSeq, memberSeq, memberLevel)
        viewDataBinding.lifecycleOwner = this
//        viewDataBinding.vmMain = getViewModel()

        // 툴바 설정 [ 메인 화면 ]
        setToolbar()
        //CommonUtil.setToolbar(this, )
    }

    // 툴바 설정 [ 메인 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tv = findViewById<TextView>(R.id.tv_toolber_title) // 타이틀 뷰
        findViewById<ImageView>(R.id.btn_notice).visibility = View.VISIBLE // 알림 아이콘

        tv.setText(R.string.logo_name)
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