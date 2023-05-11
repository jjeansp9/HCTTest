package kr.co.testapp0501.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityProfileBinding
import kr.co.testapp0501.model.user.UserModel
import kr.co.testapp0501.view.adapter.ViewPagerFragmentAdapter
import kr.co.testapp0501.viewmodel.ProfileViewModel

class ProfileActivity : BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {

    private var users = UserModel(this)

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
        setToolbar()

        // 프로필화면 이름설정
        setProfileName()

        tabChanged() // 탭 전환 이벤트
    }

    // 프로필화면 이름설정
    private fun setProfileName(){
        if (intent.getStringExtra("memberName") == null){
            viewDataBinding.tvProfileName.text = users.loadNormalData().name
        }else{
            viewDataBinding.tvProfileName.text = intent.getStringExtra("memberName")
        }
    }

    // 툴바 설정 [ 프로필 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.visibility = View.VISIBLE
        tv.text = "프로필"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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