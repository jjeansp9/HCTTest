package kr.co.testapp0501.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityAlbumBinding
import kr.co.testapp0501.model.album.*
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.view.adapter.RecyclerAlbumActivityAdapter
import kr.co.testapp0501.viewmodel.AlbumViewModel

class AlbumActivity : BaseActivity<ActivityAlbumBinding>(R.layout.activity_album) {

    private var albumItems = mutableListOf<AlbumModel>()
//    private val albumPicture = mutableListOf<Int>()
//    private val albumPicture2 = mutableListOf<Int>()
//    private val albumPicture3 = mutableListOf<Int>()
//    private val albumPicture4 = mutableListOf<Int>()

    private var groupSeq : Int = -1
    private var memberSeq : Int = -1
    private var jwtToken : String = ""
    private var boardType = "album"


    private val albumAdapter = RecyclerAlbumActivityAdapter(this, albumItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbum = AlbumViewModel()
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.recyclerAlbum.adapter = albumAdapter

        jwtToken = intent.getStringExtra("jwtToken")!!
        groupSeq = intent.getIntExtra("groupSeq", groupSeq)
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)
        initViews()

        setToolbar()

        Log.i("dddddd", Color.GRAY.toString() +", "+ Color.BLACK)

        updateAlbumList() // 앨범 글목록 아래로 당겨서 새로고침

        clickAlbumUpload()
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

        viewDataBinding.vmAlbum?.boardListRequest(jwtToken, boardType, groupSeq, 0)
    }

    // 앨범목록 새로고침
    private fun updateAlbumList(){
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener {
            initObservers()
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
            intent.putExtra("boardType", boardType)
            startActivity(intent)
        }
    }

    private fun initViews(){
        albumAdapter.setItemClickListener(object: RecyclerAlbumActivityAdapter.OnItemClickListener{
            // 프로필사진 클릭
            override fun profileImgClick(v: View, position: Int) {

            }
            // 업로드 이미지 클릭
            override fun pictureClick(v: View, position: Int) {

            }
            // 앨범 설정 클릭
            override fun albumSetClick(v: View, position: Int) {
                val intent = Intent(this@AlbumActivity, AlbumUploadActivity::class.java)
                intent.putExtra("jwtToken", jwtToken)
                intent.putExtra("boardSeq", albumItems[position].boardSeq)
                intent.putExtra("boardType", boardType)
                startActivity(intent)
            }
            // 좋아요 클릭
            override fun likeClick(v: View, position: Int) {

            }
            // 댓글 클릭
            override fun commentClick(v: View, position: Int) {
                startActivity(Intent(this@AlbumActivity, AlbumCommentActivity::class.java))
            }

        })

    }

    override fun initObservers() {
        if (viewDataBinding.progressBar.visibility == View.GONE){

            viewDataBinding.swipeRefreshLayout.isRefreshing = false
            viewDataBinding.progressBar.visibility = View.VISIBLE

            viewDataBinding.vmAlbum?.albumBoardList?.observe(this){
                if (it.size > 0){
                    albumItems.clear()

                    for (i in it.indices){

                        val profileImgObj: FileVOList
                        var imageUrl = ""

                        if (it[i].memberVO.fileVOList.isNotEmpty()){
                            profileImgObj= it[i].memberVO.fileVOList[0]
                            imageUrl = profileImgObj.path + "/" + profileImgObj.saveName
                        }else{
                            imageUrl = ""
                        }

                        val fileAlbum = it[i].fileList[0]
                        val albumUrl = fileAlbum.path + "/" + fileAlbum.saveName

                        val formattedTime = CommonUtil.convertDateTimeString(it[i].insertDate)

                        albumItems.add(0,
                            AlbumModel(
                                it[i].seq,
                                ApiService.FILE_SUFFIX_URL+imageUrl,
                                it[i].memberVO.name,
                                formattedTime,
                                ApiService.FILE_SUFFIX_URL+albumUrl,
                                it[i].title,
                                it[i].content,
                                it[i].like,
                                it[i].commentCnt
                            ))
                    }
                    viewDataBinding.recyclerAlbum.scrollToPosition(albumItems.size -1)
                    albumAdapter.notifyDataSetChanged()
                    viewDataBinding.progressBar.visibility = View.GONE
                    Log.i("qqqqqq", it[0].fileId)
                }else{
                    viewDataBinding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}