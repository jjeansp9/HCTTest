package kr.co.testapp0501.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.databinding.ActivityProfileBinding
import kr.co.testapp0501.databinding.ActivityProfileUpdateBinding
import kr.co.testapp0501.viewmodel.ProfileViewModel

class ProfileUpdateActivity : BaseActivity<ActivityProfileUpdateBinding>(R.layout.activity_profile_update) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmProfile = ProfileViewModel()
        viewDataBinding.lifecycleOwner = this

        setToolbar()
    }

    // 툴바 설정 [ 프로필 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.visibility = View.VISIBLE
        tv.text = "프로필"
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