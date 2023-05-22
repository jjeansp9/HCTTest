package kr.co.testapp0501.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityBoardDetailBinding
import kr.co.testapp0501.model.board.BoardDetailModel
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.recycler.RecyclerGroupData
import kr.co.testapp0501.view.adapter.BoardDetailAdapter
import kr.co.testapp0501.view.adapter.RecyclerGroupActivityAdapter
import kr.co.testapp0501.viewmodel.AlbumUploadViewModel
import kr.co.testapp0501.viewmodel.AlbumViewModel

class BoardDetailActivity : BaseActivity<ActivityBoardDetailBinding>(R.layout.activity_board_detail) {

    private val items = ArrayList<BoardDetailModel>()
    private val adapter = BoardDetailAdapter(this, items)

    private lateinit var jwtToken: String
    private var memberSeq: Int = -1
    private var boardSeq: Int = -1
    private var boardType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jwtToken = intent.getStringExtra("jwtToken")!!
        boardType = intent.getStringExtra("boardType")!!
        boardSeq = intent.getIntExtra("boardSeq", boardSeq)
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)

        viewDataBinding.vmAlbumUpload = AlbumUploadViewModel()
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.recyclerBoardDetail.adapter = adapter

        setToolbar()

        getBoardDetail()
    }

    private fun getBoardDetail(){
        viewDataBinding.vmAlbumUpload?.getBoardDetailInfo(jwtToken, boardSeq)
    }

    override fun initObservers() {

        viewDataBinding.vmAlbumUpload?.getBoardDetailInfo?.observe(this){
            viewDataBinding.tvTitle.text = it.data.title
            viewDataBinding.tvName.text = it.data.memberVO.name
            val formattedTime = CommonUtil.convertDateTimeString(it.data.insertDate)
            viewDataBinding.tvDate.text = formattedTime
            viewDataBinding.tvLike.text = it.data.like.toString()
            viewDataBinding.tvComment.text = it.data.commentCnt.toString()

            for (i in 0 until it.data.fileList.size){
                items.add(BoardDetailModel(
                    it.data.fileList[i].saveName
                ))
            }
            viewDataBinding.tvContent.text = it.data.content
            adapter.notifyDataSetChanged()
        }
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


}