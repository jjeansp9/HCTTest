package kr.co.testapp0501.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.avad.android.humancaretree.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityAlbumBinding

class AlbumActivity : BaseActivity<ActivityAlbumBinding>(R.layout.activity_album) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding
    }
}