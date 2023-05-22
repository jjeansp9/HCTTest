package kr.co.testapp0501.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityAlbumCommentBinding
import kr.co.testapp0501.view.adapter.BoardCommentAdapter
import kr.co.testapp0501.viewmodel.AlbumCommentViewModel

class BoardCommentActivity : BaseActivity<ActivityAlbumCommentBinding>(R.layout.activity_album_comment) {
    companion object{
        private const val TAG = "boardComment"
    }

    private lateinit var adapter: BoardCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbumComment = AlbumCommentViewModel()
        viewDataBinding.lifecycleOwner = this

        initViews()
        setToolbar()
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
        viewDataBinding.vmAlbumComment?.albumCommentItems?.observe(this){ items ->
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
        viewDataBinding.vmAlbumComment?.loadCommentList()
    }


}
























