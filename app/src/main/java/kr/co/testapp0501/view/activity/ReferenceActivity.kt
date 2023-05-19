package kr.co.testapp0501.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityReferenceBinding
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.recycler.RecyclerTab3AlbumData
import kr.co.testapp0501.view.adapter.RecyclerTab3AlbumAdapter
import kr.co.testapp0501.viewmodel.AlbumViewModel

class ReferenceActivity : BaseActivity<ActivityReferenceBinding>(R.layout.activity_reference) {
    companion object{
        const val TAG = "referenceActivity"
    }

    private var groupSeq : Int = -1
    private var memberSeq : Int = -1
    private var jwtToken : String = ""
    private var boardType = "reference"

    private val items = mutableListOf<RecyclerTab3AlbumData>()
    private val type = 1
    private val adapter = RecyclerTab3AlbumAdapter(this, items, type)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmBoard = AlbumViewModel()
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.recyclerReference.adapter = adapter
        viewDataBinding.recyclerReference.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        setToolbar() // 툴바
        getIntentData() // intent로 데이터 가져오기

        clickUpload() // 게시글 작성화면으로 이동

        updateAlbumList() // 목록 새로고침
    }

    // 목록 새로고침
    private fun updateAlbumList(){
        viewDataBinding.swipeRefreshLayout.setOnRefreshListener {
            initObservers()
        }
    }

    private fun setToolbar(){
        CommonUtil.setToolbar(
            this,
            ReferenceActivity::class.java,
            "자료실",
            R.drawable.btn_album_upload_selector,
            0,
            firstMenuOn = false,
            secondMenuOn = false
        )
    }

    // 툴바에 있는 [+] 버튼 클릭
    private fun clickUpload(){
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

    override fun onResume() {
        super.onResume()
        viewDataBinding.vmBoard?.boardListRequest(jwtToken, boardType, groupSeq, 0)
    }

    private fun getIntentData(){
        jwtToken = intent.getStringExtra("jwtToken")!!
        groupSeq = intent.getIntExtra("groupSeq", groupSeq)
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)
    }

    override fun initObservers() {
        if (viewDataBinding.progressBar.visibility == View.GONE){
            viewDataBinding.swipeRefreshLayout.isRefreshing = false
            viewDataBinding.progressBar.visibility = View.VISIBLE

            viewDataBinding.vmBoard?.albumBoardList?.observe(this){ albumUpdate ->
                if (albumUpdate.size > 0) {
                    items.clear()
                    if (albumUpdate.size < 5){
                        for (i in 0 until albumUpdate.size){

                            val fileAlbum = albumUpdate[i].fileList[0]
                            val albumUrl = fileAlbum.path + "/" + fileAlbum.saveName
                            val formattedTime = CommonUtil.convertDateTimeString(albumUpdate[i].insertDate)
                            items.add(0,
                                RecyclerTab3AlbumData(
                                    albumUpdate[i].title,
                                    formattedTime,
                                    albumUpdate[i].commentCnt,
                                    ApiService.FILE_SUFFIX_URL+albumUrl,
                                ))
                        }
                    }else{
                        for (i in 0 .. 4){
                            val fileAlbum = albumUpdate[i].fileList[0]
                            val albumUrl = fileAlbum.path + "/" + fileAlbum.saveName
                            val formattedTime = CommonUtil.convertDateTimeString(albumUpdate[i].insertDate)
                            items.add(0,
                                RecyclerTab3AlbumData(
                                    albumUpdate[i].title,
                                    formattedTime,
                                    albumUpdate[i].commentCnt,
                                    ApiService.FILE_SUFFIX_URL+albumUrl,
                                ))
                        }
                    }

                    viewDataBinding.recyclerReference.scrollToPosition(items.size -1)
                    adapter.notifyDataSetChanged()

                    Log.i(TAG, items.size.toString())
                    viewDataBinding.progressBar.visibility = View.GONE
                }
            }
        }

    }
}




























