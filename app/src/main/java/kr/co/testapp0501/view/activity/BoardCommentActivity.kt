package kr.co.testapp0501.view.activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityAlbumCommentBinding
import kr.co.testapp0501.model.comment.CommentSend
import kr.co.testapp0501.view.adapter.BoardCommentAdapter
import kr.co.testapp0501.viewmodel.BoardCommentViewModel

class BoardCommentActivity : BaseActivity<ActivityAlbumCommentBinding>(R.layout.activity_album_comment) {
    companion object{
        private const val TAG = "boardComment"
    }

    private lateinit var adapter: BoardCommentAdapter

    private var groupSeq : Int = -1
    private var memberSeq : Int = -1
    private var jwtToken : String = ""
    private var boardType = ""
    private var boardSeq : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmBoardComment = BoardCommentViewModel()
        viewDataBinding.lifecycleOwner = this

        jwtToken = intent.getStringExtra("jwtToken")!!
        groupSeq = intent.getIntExtra("groupSeq", groupSeq)
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)
        boardSeq = intent.getIntExtra("boardSeq", boardSeq)
        boardType = intent.getStringExtra("boardType")!!

        initViews()
        setToolbar()
        btnCommentSend()
    }

    // 댓글 등록 버튼
    private fun btnCommentSend(){
        viewDataBinding.btnCommentSend.setOnClickListener {
            val bbsId = boardType
            val boardSeq = boardSeq
            val ntcrSeq = memberSeq
            val groupSeq = groupSeq
            val content = viewDataBinding.etCommentInput.text.toString()

            val commentSend = CommentSend(bbsId, boardSeq, content, ntcrSeq, groupSeq)

            viewDataBinding.vmBoardComment?.commentSend(jwtToken, commentSend)?.observe(this){
                Log.i(TAG +" Code", it.toString())
            }
        }
    }

    private fun setToolbar(){
        CommonUtil.setToolbar(
            this,
            javaClass,
            "댓글",
            0,
            0,
            firstMenuOn = false,
            secondMenuOn = false
        )
    }

    override fun onResume() {
        super.onResume()

    }

    override fun initObservers() {
        viewDataBinding.vmBoardComment?.albumCommentItems?.observe(this){ items ->
            val albumComment = items.toMutableList() ?: mutableListOf()
            adapter.submitList(albumComment)
        }
    }

    fun initViews(){
        adapter = BoardCommentAdapter(
            onAlbumCommentItemClick = {

            }
        )
        viewDataBinding.recyclerAlbumComment.adapter = adapter
        viewDataBinding.recyclerAlbumComment.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        viewDataBinding.vmBoardComment?.loadCommentList()
    }


}
























