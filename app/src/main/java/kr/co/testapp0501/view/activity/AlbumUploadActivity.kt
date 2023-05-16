package kr.co.testapp0501.view.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.databinding.ActivityAlbumUploadBinding
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
        requestCameraPermission() // 카메라 권한
        checkPermission() // 외부저장소 권한요청

        getImgData() // 촬영한 사진의 데이터 가져오기
        // TODO 사진촬영한 데이터 가져오기
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
        viewDataBinding.vmAlbumUpload?.getPhoto()
    }

    private fun customDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_album_upload_picture)

        // 다이얼로그 사이즈조절
        val params = dialog.window!!.attributes
        params.width = 650
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 텍스트,이미지 설정
        dialog.show()

        val btnPhotoShoot : ConstraintLayout = dialog.findViewById(R.id.btn_album_upload_photo_shoot)
        val btnPhotoSelect : ConstraintLayout = dialog.findViewById(R.id.btn_album_upload_photo_select)

        // 사진촬영 버튼
        btnPhotoShoot.setOnClickListener{
            // 사진촬영 관련 코드 수행
            dialog.dismiss()

            openCamera() // 카메라 화면 띄우기


        }
        // 사진선택 버튼
        btnPhotoSelect.setOnClickListener{
            // 사진 선택 관련 코드 수행
            dialog.dismiss()
        }
    }

    private val CAMERA_CAPTURE_REQUEST_CODE = 200
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>

    // 권한 요청을 위한 메서드
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {

        }
    }

    // 권한 요청 결과 처리를 위한 메서드
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // 권한이 거부된 경우
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // 카메라 실행을 위한 메서드
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            takePictureLauncher.launch(takePictureIntent)
        } else {
            Toast.makeText(this, "카메라 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 외부저장소 권한요청
    fun checkPermission() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, 100)
        }
    }

    // 촬영한 사진의 데이터 가져오기
    private fun getImgData(){
        // ActivityResultLauncher 초기화
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val capturedImage: Bitmap? = data?.extras?.get("data") as Bitmap?
                val imageUri: Uri? = data?.data

                // 촬영한 사진 데이터(capturedImage)를 사용하여 필요한 작업을 수행합니다.
                Log.i(TAG, capturedImage.toString()) // Bitmap 형식의 데이터
                Log.i(TAG, imageUri.toString()) // Uri 형식의 데이터
            }
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