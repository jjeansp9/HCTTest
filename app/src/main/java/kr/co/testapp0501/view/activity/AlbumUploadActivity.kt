package kr.co.testapp0501.view.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityAlbumCommentBinding
import kr.co.testapp0501.databinding.ActivityAlbumUploadBinding
import kr.co.testapp0501.view.adapter.AlbumCommentAdapter
import kr.co.testapp0501.view.adapter.AlbumUploadAdapter
import kr.co.testapp0501.viewmodel.AlbumUploadViewModel

class AlbumUploadActivity : BaseActivity<ActivityAlbumUploadBinding>(R.layout.activity_album_upload) {

    companion object{ private const val TAG = "albumUpload" }
    private lateinit var adapter: AlbumUploadAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbumUpload = AlbumUploadViewModel()
        viewDataBinding.lifecycleOwner = this

        setToolbar()
        initViews()
    }

    override fun initObservers() {
        viewDataBinding.vmAlbumUpload?.albumUploadPhotos?.observe(this) { it ->
            adapter.submitList(it)
        }

    }

    private fun initViews(){
        adapter = AlbumUploadAdapter(
            onAlbumFooterClick = {
                customDialog()
            }
        )

        viewDataBinding.recyclerAlbumUpload.adapter = adapter
        viewDataBinding?.vmAlbumUpload?.getPhoto()
    }

    private fun customDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_album_upload_picture)

        // 다이얼로그 사이즈조절
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 텍스트,이미지 설정
        dialog.show()

        val btnPhotoShoot : ConstraintLayout = dialog.findViewById(R.id.btn_album_upload_photo_shoot)
        val btnPhotoSelect : ConstraintLayout = dialog.findViewById(R.id.btn_album_upload_photo_select)


        // 사진촬영 버튼
        btnPhotoShoot.setOnClickListener{
            val token = intent.getStringExtra("jwtToken")
            val memberSeq = intent.getIntExtra("memberSeq", -1)

            val intent = Intent(this, GroupCreateActivity::class.java)
            intent.putExtra("jwtToken", token)
            intent.putExtra("memberSeq", memberSeq)
            startActivity(intent)
            dialog.dismiss()
        }
        // 사진선택 버튼
        btnPhotoSelect.setOnClickListener{
            dialog.dismiss()
            // 그룹코드 다이얼로그
        }
    }

    private fun setToolbar(){
        CommonUtil.setToolbar(
            this,
            javaClass,
            "게시물 작성",
            0,
            0,
            firstMenuOn = false,
            secondMenuOn = false
        )
    }

}