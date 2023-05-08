package kr.co.testapp0501.view.activity

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityProfileBinding
import kr.co.testapp0501.view.adapter.ViewPagerFragmentAdapter
import kr.co.testapp0501.viewmodel.ProfileViewModel

class ProfileActivity : BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vmProfile = ProfileViewModel()
        viewDataBinding.lifecycleOwner = this

        // FragmentStateAdapter 생성 : Fragment 여러개를 ViewPager2에 연결해주는 역할
        val pagerAdapter = ViewPagerFragmentAdapter(this)

        viewDataBinding.viewPager.adapter = pagerAdapter

        val tabTitle = listOf("정보", "내 이야기", "앨범")
        TabLayoutMediator(viewDataBinding.tabLayout, viewDataBinding.viewPager) { tab, position ->
            tab.text = tabTitle[position]

        }.attach()

        tabChanged() // 탭 전환 이벤트
    }

    // 2번,3번 탭에서는 [수정하기] 버튼 숨김
    private fun tabChanged(){
        viewDataBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when(position){
                    0 ->{viewDataBinding.btnProfileUpdate.visibility = View.VISIBLE}
                    1 ->{viewDataBinding.btnProfileUpdate.visibility = View.GONE}
                    2 ->{viewDataBinding.btnProfileUpdate.visibility = View.GONE}
                }
            }
        })
    }
}