package kr.co.testapp0501.view.activity

import android.os.Bundle
import android.view.MenuItem
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.CommonUtil
import kr.co.testapp0501.databinding.ActivityTreeBinding
import kr.co.testapp0501.viewmodel.TreeViewModel

class TreeActivity : BaseActivity<ActivityTreeBinding>(R.layout.activity_tree) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vmTree = TreeViewModel()
        viewDataBinding.lifecycleOwner = this

        CommonUtil.setToolbar(this, "가계도")
    }

    // 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}