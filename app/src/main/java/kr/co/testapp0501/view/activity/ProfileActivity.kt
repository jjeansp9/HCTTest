package kr.co.testapp0501.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.common.util.Util
import kr.co.testapp0501.databinding.ActivityProfileBinding
import kr.co.testapp0501.model.user.UserModel
import kr.co.testapp0501.view.adapter.ViewPagerFragmentAdapter
import kr.co.testapp0501.viewmodel.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileActivity : BaseActivity<ActivityProfileBinding>(R.layout.activity_profile) {
    companion object{
        const val TAG = "profileActivity"
    }
    private var users = UserModel(this)
    private lateinit var jwtToken: String
    private var memberSeq : Int = -1
    private var memberLevel : Int = -1

    private var who = "member"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmProfile = ProfileViewModel()
        viewDataBinding.lifecycleOwner = this

        jwtToken = intent.getStringExtra("jwtToken")!!
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)
        memberLevel = intent.getIntExtra("memberLevel", memberLevel)

        val pagerAdapter = ViewPagerFragmentAdapter(this, jwtToken, memberSeq)

        viewDataBinding.viewPager.adapter = pagerAdapter

        createFragment()
        setToolbar()
        viewDataBinding.imgProfileChange.setOnClickListener{openGallrey()}
        tabChanged() // 탭 전환 이벤트
        btnUpdate()
        clickProfileImg()
    }

    private fun clickProfileImg(){
        viewDataBinding.imgProfile.setOnClickListener{

        }
    }

    override fun onResume() {
        super.onResume()
        loadProfileInfo(jwtToken, memberSeq)
    }

    // 회원정보 불러오기
    private fun loadProfileInfo(jwtToken: String, memberSeq: Int){
        viewDataBinding.vmProfile?.requestMemberInfo(jwtToken, memberSeq)

        if (memberLevel != 1){
            viewDataBinding.tvAdmin.visibility = View.GONE
        }
    }

    private fun createFragment(){
        val tabTitle = listOf("정보", "내 이야기", "앨범")
        TabLayoutMediator(viewDataBinding.tabLayout, viewDataBinding.viewPager) { tab, position ->
            tab.text = tabTitle[position]

        }.attach()
    }

    private fun openGallrey(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityResult.launch(intent)
    }

    private var uri: Uri? = null

    var startActivityResult = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            uri = result.data!!.data!!
            val requestOptions = RequestOptions().transform(RoundedCorners(64))
            Glide.with(this).load(uri).apply(requestOptions).into(viewDataBinding.imgProfile)
            Log.d("ImgURI", uri.toString() + "")

            val file = File(CommonUtil.absolutelyPath(uri, this))
            val requestBodys = file.asRequestBody("image/*".toMediaTypeOrNull())
            val path = MultipartBody.Part.createFormData("files", file.name, requestBodys)

            viewDataBinding.vmProfile?.profileImageChanged(jwtToken, who, memberSeq, path)?.observe(this){
                if(it == 200){
                    Toast.makeText(this, R.string.profile_img_update_success, Toast.LENGTH_SHORT).show()
                    loadProfileInfo(jwtToken, memberSeq)
                }
            }
        }
    }

    override fun initObservers() {

    }

    private fun btnUpdate(){
        viewDataBinding.btnFloating.setOnClickListener{
            val intent = Intent(this, ProfileUpdateActivity::class.java)
            intent.putExtra("jwtToken", jwtToken)
            intent.putExtra("memberSeq", memberSeq)
            startActivity(intent)
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



    // 2번,3번 탭에서는 [수정하기] 버튼 숨김
    private fun tabChanged(){
        viewDataBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when(position){
//                    0 ->{viewDataBinding.btnProfileUpdate.visibility = View.VISIBLE}
//                    1 ->{viewDataBinding.btnProfileUpdate.visibility = View.GONE}
//                    2 ->{viewDataBinding.btnProfileUpdate.visibility = View.GONE}
                }
            }
        })
    }
}