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
    override fun initObservers() {
    }

    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.visibility = View.VISIBLE
        tv.text = "정보"
        val complete = findViewById<TextView>(R.id.album_upload_create) // 게시글 작성 완료버튼
        complete.visibility = View.VISIBLE
        complete.setOnClickListener{
            profileUpdateComplete()
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun profileUpdateComplete(){

    }
}