package kr.co.testapp0501.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityAlbumCommentBinding
import kr.co.testapp0501.view.adapter.AlbumCommentAdapter
import kr.co.testapp0501.viewmodel.AlbumCommentViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class AlbumCommentActivity : BaseActivity<ActivityAlbumCommentBinding>(R.layout.activity_album_comment) {
    companion object{
        private const val TAG = "albumComent"
    }

    private lateinit var adapter: AlbumCommentAdapter

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
        adapter = AlbumCommentAdapter(
            onAlbumCommentItemClick = {clickedItem ->
                Toast.makeText(this, clickedItem.toString(), Toast.LENGTH_SHORT).show()
            }
        )
        viewDataBinding.recyclerAlbumComment.adapter = adapter
        viewDataBinding.recyclerAlbumComment.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        viewDataBinding.vmAlbumComment?.loadCommentList()
    }


}
























