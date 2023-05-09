package kr.co.testapp0501.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityAlbumBinding
import kr.co.testapp0501.viewmodel.AlbumViewModel
import kr.co.testapp0501.viewmodel.ProfileViewModel

class AlbumActivity : BaseActivity<ActivityAlbumBinding>(R.layout.activity_album) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbum = AlbumViewModel()
        viewDataBinding.lifecycleOwner = this

        setToolbar() // 툴바 설정
    }

    // 툴바 설정 [ 앨범 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.text = "앨범"
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