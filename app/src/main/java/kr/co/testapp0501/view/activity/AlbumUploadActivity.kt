package kr.co.testapp0501.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityAlbumCommentBinding
import kr.co.testapp0501.databinding.ActivityAlbumUploadBinding
import kr.co.testapp0501.view.adapter.AlbumCommentAdapter
import kr.co.testapp0501.view.adapter.AlbumUploadAdapter
import kr.co.testapp0501.viewmodel.AlbumUploadViewModel

class AlbumUploadActivity : BaseActivity<ActivityAlbumUploadBinding>(R.layout.activity_album_upload) {

    companion object{ private const val TAG = "albumUpload" }
    private lateinit var adapter: AlbumUploadAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbumUpload = AlbumUploadViewModel()
        viewDataBinding.lifecycleOwner = this

        setToolbar()
        initViews()
    }

    override fun initObservers() {
        viewDataBinding.vmAlbumUpload?.albumUploadPhotos?.observe(this) { it ->
            adapter.submitList(it)
        }

    }

    private fun initViews(){
        adapter = AlbumUploadAdapter(
            onAlbumFooterClick = {
                customDialog()
            }
        )

        viewDataBinding.recyclerAlbumUpload.adapter = adapter
        viewDataBinding?.vmAlbumUpload?.getPhoto()
    }

    private fun customDialog(){

    }

    private fun setToolbar(){
        CommonUtil.setToolbar(
            this,
            javaClass,
            "게시물 작성",
            0,
            0,
            firstMenuOn = false,
            secondMenuOn = false
        )
    }

}