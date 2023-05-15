package kr.co.testapp0501.view.activity

import android.os.Bundle
import android.view.MenuItem
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityHierarchyBinding
import kr.co.testapp0501.viewmodel.HierarchyViewModel

class HierarchyActivity : BaseActivity<ActivityHierarchyBinding>(R.layout.activity_hierarchy) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vmTree = HierarchyViewModel()
        viewDataBinding.lifecycleOwner = this

        setToolbar()
    }

    private fun setToolbar(){
        CommonUtil.setToolbar(
            this,
            javaClass,
            "가계도",
            0,
            0,
            firstMenuOn = false,
            secondMenuOn = false
        )
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