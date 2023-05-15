package kr.co.testapp0501.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityMainBinding
import kr.co.testapp0501.model.recycler.RecyclerTab3AlbumData
import kr.co.testapp0501.view.adapter.RecyclerTab3AlbumAdapter
import kr.co.testapp0501.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private var groupName : String = ""
    private val setViewType = 0 // [프로필 3번째 탭], [메인화면 앨범 새글] 구분하기 위한 변수

    private val items = mutableListOf<RecyclerTab3AlbumData>()
    private val adapter by lazy { RecyclerTab3AlbumAdapter(this, items, setViewType)  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jwtToken = intent.getStringExtra("jwtToken")!!
        val groupSeq = intent.getIntExtra("groupSeq", -1)
        val memberSeq = intent.getIntExtra("memberSeq", -1)
        val memberLevel = intent.getIntExtra("memberLevel", -1)
        groupName = intent.getStringExtra("groupName")!!

        viewDataBinding.vmMain = MainViewModel(this, jwtToken, groupSeq, memberSeq, memberLevel)
        viewDataBinding.lifecycleOwner = this

        viewDataBinding.recyclerAlbumUpdate.adapter = adapter
        viewDataBinding.recyclerAlbumUpdate.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        for (i in 0 .. 10){
            items.add(RecyclerTab3AlbumData("까망이 근황이에요 ㅎㅎ", "2023.02.18", 0, ""))
        }

        // 툴바 설정 [ 메인 화면 ]
        setToolbar()
        setTitleLayout()
        //CommonUtil.setToolbar(this, )
    }
    override fun initObservers() {
    }

    // 타이틀 Layout View 설정
    private fun setTitleLayout(){
        val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(60))
        val iv : ImageView = findViewById(R.id.img_group_title)
        viewDataBinding.tvGroupName.text = groupName
        Glide.with(this).load(R.drawable.img_main_top_01).apply(requestOptions).into(iv)

    //        if (items[position].imgGroup == "") Glide.with(this).load(R.drawable.img_group_general).apply(requestOptions).into(holder.binding.imgGroup) // 그룹목록 이미지
    //        else Glide.with(context).load(items[position].imgGroup).apply(requestOptions).into(holder.binding.imgGroup) // 그룹목록 이미지
    }

    // 툴바 설정 [ 메인 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tv = findViewById<TextView>(R.id.tv_toolber_title) // 타이틀 뷰
        findViewById<ImageView>(R.id.btn_notice).visibility = View.VISIBLE // 알림 아이콘

        tv.setText(R.string.logo_name)
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
}