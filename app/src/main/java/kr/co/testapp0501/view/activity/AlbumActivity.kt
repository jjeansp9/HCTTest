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
import kr.co.testapp0501.model.recycler.RecyclerAlbumData
import kr.co.testapp0501.view.adapter.RecyclerAlbumActivityAdapter
import kr.co.testapp0501.viewmodel.AlbumViewModel
import kr.co.testapp0501.viewmodel.ProfileViewModel

class AlbumActivity : BaseActivity<ActivityAlbumBinding>(R.layout.activity_album) {

    private val albumItems = mutableListOf<RecyclerAlbumData>()
    private val albumPicture = mutableListOf<Int>()
    private val albumPicture2 = mutableListOf<Int>()
    private val albumPicture3 = mutableListOf<Int>()
    private val albumPicture4 = mutableListOf<Int>()
    private val albumAdapter = RecyclerAlbumActivityAdapter(this, albumItems)

    // 테스트용 더미데이터
    private val testContents = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbum = AlbumViewModel()
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.recyclerAlbum.adapter = albumAdapter

        setToolbar()

        Log.i("dddddd", Color.GRAY.toString() +", "+ Color.BLACK)

        updateAlbumList() // 앨범 글목록 아래로 당겨서 새로고침

        albumItemClick()
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
            firstMenuOn = true,
            secondMenuOn = false
        )
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

        albumPicture.add(0, R.drawable.test_010)

        albumPicture2.add(0, R.drawable.img_group_sports)
        albumPicture2.add(1, R.drawable.img_group_family)

        albumPicture3.add(0, R.drawable.img_group_general)
        albumPicture3.add(1, R.drawable.test_010)
        albumPicture3.add(2, R.drawable.img_group_family)

        albumPicture4.add(0, R.drawable.img_group_general)
        albumPicture4.add(1, R.drawable.test_010)
        albumPicture4.add(2, R.drawable.img_group_family)
        albumPicture4.add(3, R.drawable.img_group_family)

        for (i in 0 .. 10){
            albumItems.add(
                RecyclerAlbumData(
                "",
                "홍길동1",
                "2023년 2월 16일 오후 7:02",
                albumPicture,
                "title",
                    testContents,
                    0,
                    0
                )
            )
            albumItems.add(
                RecyclerAlbumData(
                    "",
                    "홍길동2",
                    "2023년 2월 16일 오후 7:02",
                    albumPicture2,
                    "title",
                    testContents,
                    0,
                    364
                )
            )
            albumItems.add(
                RecyclerAlbumData(
                    "",
                    "홍길동3",
                    "2023년 2월 16일 오후 7:02",
                    albumPicture3,
                    "title",
                    testContents,
                    7,
                    0
                )
            )
            albumItems.add(
                RecyclerAlbumData(
                    "",
                    "홍길동4",
                    "2023년 2월 16일 오후 7:02",
                    albumPicture4,
                    "title",
                    testContents,
                    4,
                    12
                )
            )
        }
        viewDataBinding.recyclerAlbum.adapter?.notifyDataSetChanged()
        viewDataBinding.swipeRefreshLayout.isRefreshing = false
    }


    // 앨범 툴바에 있는 [+] 버튼 클릭
    private fun clickAlbumUpload(){
        startActivity(Intent(this, AlbumUploadActivity::class.java))
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