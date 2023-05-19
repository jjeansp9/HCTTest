package kr.co.testapp0501.view.activity

import android.os.Bundle
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.databinding.ActivityReferenceBinding
import kr.co.testapp0501.viewmodel.AlbumViewModel

class ReferenceActivity : BaseActivity<ActivityReferenceBinding>(R.layout.activity_reference) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbum = AlbumViewModel()
        viewDataBinding.lifecycleOwner = this
    }

    override fun initObservers() {

    }
}