package kr.co.testapp0501.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityAlbumBinding
import kr.co.testapp0501.model.recycler.RecyclerAlbumData
import kr.co.testapp0501.view.adapter.RecyclerAlbumActivityAdapter
import kr.co.testapp0501.viewmodel.AlbumViewModel
import kr.co.testapp0501.viewmodel.ProfileViewModel

class AlbumActivity : BaseActivity<ActivityAlbumBinding>(R.layout.activity_album) {

    private val albumItems = mutableListOf<RecyclerAlbumData>()
    private val albumAdapter = RecyclerAlbumActivityAdapter(this, albumItems)

    // 테스트용 더미데이터
    private val testContents = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbum = AlbumViewModel()
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.recyclerAlbum.adapter = albumAdapter

        setToolbar() // 툴바 설정

        updateAlbumList() // 앨범 글목록 아래로 당겨서 새로고침
    }

    override fun onResume() {
        super.onResume()
        dummyData() // ui 테스트용
    }

    // 앨범목록 새로고침
    private fun updateAlbumList(){
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener {
            dummyData()
        }
    }

    // ui 테스트용 데이터
    @SuppressLint("NotifyDataSetChanged")
    private fun dummyData(){
        albumItems.clear()
        for (i in 0 .. 10){
            albumItems.add(
                RecyclerAlbumData(
                "",
                "홍길동",
                "2023년 2월 16일 오후 7:02",
                "",
                "title",
                    testContents
                )
            )
        }
        viewDataBinding.recyclerAlbum.adapter?.notifyDataSetChanged()
        viewDataBinding.swipeRefreshLayout.isRefreshing = false
    }

    // 툴바 설정 [ 앨범 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        val btnAlbumUpload = findViewById<ImageView>(R.id.btn_album_upload)
        tv.visibility = View.VISIBLE
        btnAlbumUpload.visibility = View.VISIBLE
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