package kr.co.testapp0501.view.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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

    private var groupSeq : Int = -1
    private var memberSeq : Int = -1
    private var jwtToken : String = ""
    private var memberLevel : Int = -1
    private var boardType = "album"


    private val albumAdapter by lazy { RecyclerAlbumActivityAdapter(this, albumItems, memberLevel, memberSeq) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jwtToken = intent.getStringExtra("jwtToken")!!
        groupSeq = intent.getIntExtra("groupSeq", groupSeq)
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)
        memberLevel = intent.getIntExtra("memberLevel", memberLevel)

        viewDataBinding.vmAlbum = AlbumViewModel()
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.recyclerAlbum.adapter = albumAdapter

        initViews()

        setToolbar()
        setView()

        Log.i("dddddd", Color.GRAY.toString() +", "+ Color.BLACK)

        updateAlbumList() // 앨범 글목록 아래로 당겨서 새로고침

        clickAlbumUpload()
    }

    private fun setView(){

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
                val intent = Intent(this@AlbumActivity, BoardDetailActivity::class.java)
                intent.putExtra("jwtToken", jwtToken)
                intent.putExtra("boardSeq", albumItems[position].boardSeq)
                intent.putExtra("boardType", boardType)
                startActivity(intent)
            }
            // 앨범 설정 클릭
            override fun albumSetClick(v: View, position: Int) {
                dialogForDeleteOrUpdate(position) // 수정, 삭제 다이얼로그 오픈
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

    // 다이얼로그 삭제 또는 수정
    private fun dialogForDeleteOrUpdate(position: Int){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_board_delete_update)

        // 다이얼로그 사이즈조절
        val params = dialog.window!!.attributes
        params.width = 650
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 텍스트,이미지 설정
        dialog.show()
        val btnUpdate : ConstraintLayout = dialog.findViewById(R.id.btn_board_update)
        val btnDelete : ConstraintLayout = dialog.findViewById(R.id.btn_board_delete)

        btnUpdate.setOnClickListener{
            val intent = Intent(this@AlbumActivity, AlbumUploadActivity::class.java)
            intent.putExtra("jwtToken", jwtToken)
            intent.putExtra("boardSeq", albumItems[position].boardSeq)
            intent.putExtra("boardType", boardType)
            startActivity(intent)
            dialog.dismiss()
        }
        btnDelete.setOnClickListener{
            boardDelete(dialog, position) //
        }
    }

    // 게시글 삭제
    private fun boardDelete(dialog: Dialog, position: Int){
        viewDataBinding.vmAlbum?.boardDelete(jwtToken, albumItems[position].boardSeq)?.observe(this){
            Log.i("DeleteCode", it.toString())
            if (it == 200){
                albumItems.removeAt(position)
                albumAdapter.notifyDataSetChanged()
                Toast.makeText(this, "해당 게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
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
                        Log.i("AlbumActivity", ApiService.FILE_SUFFIX_URL+imageUrl)
                        albumItems.add(0,
                            AlbumModel(
                                it[i].seq,
                                ApiService.FILE_SUFFIX_URL+imageUrl,
                                it[i].memberVO.seq,
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