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

class AlbumUploadActivity : BaseActivity<ActivityAlbumUploadBinding>(R.layout.activity_album_upload) {

    companion object{ private const val TAG = "albumUpload" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_upload)

        setToolbar()
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


    override fun initObservers() {

    }
}