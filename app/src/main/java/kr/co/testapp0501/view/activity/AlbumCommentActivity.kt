package kr.co.testapp0501.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.databinding.ActivityAlbumCommentBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class AlbumCommentActivity : BaseActivity<ActivityAlbumCommentBinding>(R.layout.activity_album_comment) {
    companion object{
        private const val TAG = "albumComent"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbumComment = getViewModel()
        viewDataBinding.lifecycleOwner = this
    }
}