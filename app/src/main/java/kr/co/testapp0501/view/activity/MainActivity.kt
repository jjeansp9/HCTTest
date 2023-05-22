package kr.co.testapp0501.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
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
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.recycler.RecyclerTab3AlbumData
import kr.co.testapp0501.view.adapter.RecyclerTab3AlbumAdapter
import kr.co.testapp0501.viewmodel.AlbumViewModel
import kr.co.testapp0501.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    companion object{
        const val TAG = "MainActivity"
    }
    private var groupName : String = ""
    private var boardTypeAlbum = "album"
    private val setViewType = 0 // [프로필 3번째 탭], [메인화면 앨범 새글] 구분하기 위한 변수

    private val items = mutableListOf<RecyclerTab3AlbumData>()
    private val adapter by lazy { RecyclerTab3AlbumAdapter(this, items, setViewType)  }
    private lateinit var albumViewModel: AlbumViewModel

    private var groupSeq : Int = -1
    private var memberSeq : Int = -1
    private var jwtToken : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        jwtToken = intent.getStringExtra("jwtToken")!!
        groupSeq = intent.getIntExtra("groupSeq", -1)
        memberSeq = intent.getIntExtra("memberSeq", -1)
        val memberLevel = intent.getIntExtra("memberLevel", -1)
        groupName = intent.getStringExtra("groupName")!!

        viewDataBinding.vmMain = MainViewModel(this, jwtToken, groupSeq, memberSeq, memberLevel)
        albumViewModel = ViewModelProvider(this).get(AlbumViewModel::class.java)
        viewDataBinding.lifecycleOwner = this

        viewDataBinding.recyclerAlbumUpdate.adapter = adapter
        viewDataBinding.recyclerAlbumUpdate.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        // 툴바 설정 [ 메인 화면 ]
        setToolbar()
        setTitleLayout()

        albumUpdateClick() // 하단에 앨범 새글에 게시판 글목록 클릭
        // AlbumViewModel의 LiveData 관찰 예시
    }

    // 하단에 앨범 새글에 게시판 글목록 클릭
    private fun albumUpdateClick(){
        adapter.setItemClickListener(object : RecyclerTab3AlbumAdapter.OnItemClickListener{
            override fun rootClick(v: View, position: Int) {
                viewDataBinding.vmMain?.onClickAlbum() // 앨범 화면으로 이동
            }

        })
    }

    override fun onResume() {
        super.onResume()
        albumViewModel.boardListRequest(jwtToken, boardTypeAlbum, groupSeq, 0)
    }
    override fun initObservers() {
        albumViewModel.albumBoardList.observe(this) { albumUpdate ->
            if (albumUpdate.size > 0) {
                items.clear()
                if (albumUpdate.size < 5){
                    for (i in 0 until albumUpdate.size){

                        val fileAlbum = albumUpdate[i].fileList[0]
                        val albumUrl = fileAlbum.path + "/" + fileAlbum.saveName
                        val formattedTime = CommonUtil.convertDateTimeString(albumUpdate[i].insertDate)
                        items.add(0,
                            RecyclerTab3AlbumData(
                                albumUpdate[i].title,
                                formattedTime,
                                albumUpdate[i].commentCnt,
                                ApiService.FILE_SUFFIX_URL+albumUrl,
                            ))
                    }
                }else{
                    for (i in 0 .. 4){
                        val fileAlbum = albumUpdate[i].fileList[0]
                        val albumUrl = fileAlbum.path + "/" + fileAlbum.saveName
                        val formattedTime = CommonUtil.convertDateTimeString(albumUpdate[i].insertDate)
                        items.add(0,
                            RecyclerTab3AlbumData(
                                albumUpdate[i].title,
                                formattedTime,
                                albumUpdate[i].commentCnt,
                                ApiService.FILE_SUFFIX_URL+albumUrl,
                            ))
                    }
                }

                viewDataBinding.recyclerAlbumUpdate.scrollToPosition(items.size -1)
                adapter.notifyDataSetChanged()

                Log.i(TAG, items.size.toString())
            }

        }
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
}