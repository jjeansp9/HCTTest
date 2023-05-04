package kr.co.testapp0501.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.testapp0501.view.fragment.ProfileTab1InfoFragment
import kr.co.testapp0501.view.fragment.ProfileTab2StoryFragment
import kr.co.testapp0501.view.fragment.ProfileTab3AlbumFragment

class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = listOf(
        ProfileTab1InfoFragment(),
        ProfileTab2StoryFragment(),
        ProfileTab3AlbumFragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}