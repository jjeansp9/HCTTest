package kr.co.testapp0501.view.activity

import android.os.Bundle
import kr.co.avad.android.humancaretree.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityProfileBinding
import kr.co.testapp0501.viewmodel.MainViewModel

class ProfileActivity : BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vmMain = MainViewModel(this)
        viewDataBinding.lifecycleOwner = this
    }
}