package kr.co.testapp0501.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.databinding.ActivityAlbumCommentBinding
import kr.co.testapp0501.view.adapter.AlbumCommentAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel

class AlbumCommentActivity : BaseActivity<ActivityAlbumCommentBinding>(R.layout.activity_album_comment) {
    companion object{
        private const val TAG = "albumComent"
    }

    private lateinit var adapter: AlbumCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbumComment = getViewModel()
        viewDataBinding.lifecycleOwner = this

        initViews()
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
    }


}
























