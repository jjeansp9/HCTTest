package kr.co.testapp0501.view.activity

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.avad.android.humancaretree.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityProfileBinding
import kr.co.testapp0501.view.adapter.ViewPagerFragmentAdapter
import kr.co.testapp0501.viewmodel.MainViewModel

class ProfileActivity : BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vmMain = MainViewModel(this)
        viewDataBinding.lifecycleOwner = this

        // FragmentStateAdapter 생성 : Fragment 여러개를 ViewPager2에 연결해주는 역할
        val pagerAdapter = ViewPagerFragmentAdapter(this)

        viewDataBinding.viewPager.adapter = pagerAdapter

        val tabTitle = listOf("정보", "내 이야기", "앨범")
        TabLayoutMediator(viewDataBinding.tabLayout, viewDataBinding.viewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }
}