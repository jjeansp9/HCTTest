package kr.co.testapp0501.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityAlbumBinding
import kr.co.testapp0501.model.album.AlbumModel
import kr.co.testapp0501.view.adapter.RecyclerAlbumActivityAdapter
import kr.co.testapp0501.viewmodel.AlbumViewModel
import kr.co.testapp0501.viewmodel.ProfileViewModel

class AlbumActivity : BaseActivity<ActivityAlbumBinding>(R.layout.activity_album) {

    private val albumItems = mutableListOf<AlbumModel>()
//    private val albumPicture = mutableListOf<Int>()
//    private val albumPicture2 = mutableListOf<Int>()
//    private val albumPicture3 = mutableListOf<Int>()
//    private val albumPicture4 = mutableListOf<Int>()

    private var groupSeq : Int = -1
    private var memberSeq : Int = -1
    private var jwtToken : String = ""
    private var boardTypeAlbum = "album"

    private val albumAdapter = RecyclerAlbumActivityAdapter(this, albumItems)

    // 테스트용 더미데이터
    private val testContents = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbum = AlbumViewModel()
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.recyclerAlbum.adapter = albumAdapter

        jwtToken = intent.getStringExtra("jwtToken")!!
        groupSeq = intent.getIntExtra("groupSeq", groupSeq)
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)

        setToolbar()

        Log.i("dddddd", Color.GRAY.toString() +", "+ Color.BLACK)

        updateAlbumList() // 앨범 글목록 아래로 당겨서 새로고침

        albumItemClick()
        clickAlbumUpload()
    }

    private fun albumItemClick(){
        albumAdapter.setItemClickListener(object: RecyclerAlbumActivityAdapter.OnItemClickListener{
            // 프로필 이미지
            override fun profileImgClick(v: View, position: Int) {

            }
            // 앨범글 이미지
            override fun pictureClick(v: View, position: Int) {
            }
            // 앨범 글마다 오른쪽 상단 앨범 설정
            override fun albumSetClick(v: View, position: Int) {
            }
            // 좋아요
            override fun likeClick(v: View, position: Int) {
            }
            // 댓글
            override fun commentClick(v: View, position: Int) {
                startActivity(Intent(this@AlbumActivity, AlbumCommentActivity::class.java))
            }
        })
    }

    private fun setToolbar(){
        CommonUtil.setToolbar(
            this,
            AlbumUploadActivity::class.java,
            "앨범",
            R.drawable.btn_album_upload_selector,
            0,
            firstMenuOn = false,
            secondMenuOn = false
        )
    }

    override fun onResume() {
        super.onResume()
        loadAlbumList()
    }

    private fun loadAlbumList(){
        viewDataBinding.vmAlbum?.albumListRequest(jwtToken, boardTypeAlbum, groupSeq, 0)
    }

    // 앨범목록 새로고침
    private fun updateAlbumList(){
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener {
            //dummyData()
        }
    }


    // 앨범 툴바에 있는 [+] 버튼 클릭
    private fun clickAlbumUpload(){
        val albumUploadClick = findViewById<ImageView>(R.id.btn_menu_first)
        albumUploadClick.setImageResource(R.drawable.btn_album_upload_selector)
        albumUploadClick.visibility = View.VISIBLE
        albumUploadClick.setOnClickListener{
            val intent = Intent(this, AlbumUploadActivity::class.java)
            intent.putExtra("jwtToken", jwtToken)
            intent.putExtra("groupSeq", groupSeq)
            intent.putExtra("memberSeq", memberSeq)
            startActivity(intent)
        }
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

    override fun initObservers() {
    }
}