package kr.co.testapp0501.view.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.common.util.CommonUtil
import kr.co.testapp0501.common.util.Util
import kr.co.testapp0501.databinding.ActivityAlbumUploadBinding
import kr.co.testapp0501.model.album.AlbumUploadModel
import kr.co.testapp0501.model.album.AlbumUploadPhotoModel
import kr.co.testapp0501.model.group.GroupCreate
import kr.co.testapp0501.view.adapter.AlbumUploadAdapter
import kr.co.testapp0501.viewmodel.AlbumUploadViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AlbumUploadActivity : BaseActivity<ActivityAlbumUploadBinding>(R.layout.activity_album_upload) {

    companion object{ private const val TAG = "albumUpload" }
    private lateinit var adapter: AlbumUploadAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmAlbumUpload = AlbumUploadViewModel()
        viewDataBinding.lifecycleOwner = this

        setToolbar()

        //requestCameraPermission() // 카메라 권한
        checkPermission() // 외부저장소 권한요청
        initViews()

        //getImgData() // 촬영한 사진의 데이터 가져오기
        // TODO 사진촬영한 데이터 저장 및 가져오기
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.vmAlbumUpload?.addPhotoToAlbum("")
    }


    private lateinit var albumPhotos : MutableList<AlbumUploadPhotoModel>

    override fun initObservers() {
        viewDataBinding.vmAlbumUpload?.albumUploadPhotos?.observe(this) { it ->
            albumPhotos = it.toMutableList() ?: mutableListOf()
            adapter.submitList(albumPhotos)

            Log.i(TAG, it.toString() + albumPhotos.size + ": " + albumPhotos[0].photo)
        }
    }

    private fun initViews(){
        adapter = AlbumUploadAdapter(
            onAlbumFooterClick = {
                customDialog()
            },
            onAlbumPhotoClick = {

                Toast.makeText(this, "photo clicked", Toast.LENGTH_SHORT).show()
            }
        )

        viewDataBinding.recyclerAlbumUpload.adapter = adapter
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

        //val btnPhotoShoot : ConstraintLayout = dialog.findViewById(R.id.btn_album_upload_photo_shoot)
        val btnPhotoSelect : ConstraintLayout = dialog.findViewById(R.id.btn_album_upload_photo_select)

        // 사진촬영 버튼
//        btnPhotoShoot.setOnClickListener{
//            // 사진촬영 관련 코드 수행
//            dialog.dismiss()
//
//            openCamera() // 카메라 화면 띄우기
//
//
//        }
        // 사진선택 버튼
        btnPhotoSelect.setOnClickListener{
            // 사진 선택 관련 코드 수행
            dialog.dismiss()
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        //intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        startActivityResult.launch(intent)
    }

    private var uri: Uri? = null

    var startActivityResult = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val data = result.data!!
            val clipData = data.clipData
            val img: ImageView = findViewById(R.id.img_album_upload)

            if (clipData != null && clipData.itemCount > 0 && clipData.itemCount <= 5) {
                // 여러 개의 이미지가 선택된 경우
                val selectedImages = mutableListOf<Uri>()
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    selectedImages.add(imageUri)
                }

                for (uri in selectedImages) {
                    viewDataBinding.vmAlbumUpload?.addPhotoToAlbum(uri.toString())?.observe(this) { responseCode ->
                        Log.i(TAG+" code", responseCode.toString()+ uri.toString())
                        if (responseCode == 200) {

                        } else if (responseCode == 409) {
                            Toast.makeText(this, R.string.profile_img_duplicate, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else if (clipData == null) {
                // 단일 이미지가 선택된 경우
                val uri = data.data
                if (uri != null) {
                    viewDataBinding.vmAlbumUpload?.addPhotoToAlbum(uri.toString())?.observe(this) { responseCode ->
                        if (responseCode == 200) {

                        } else if (responseCode == 409) {
                            Toast.makeText(this, R.string.profile_img_duplicate, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // 선택된 이미지의 개수가 5개를 초과하는 경우
                Toast.makeText(this, "최대 5개의 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }

            Log.d("ImgURI", uri.toString())
        }
    }

    private val CAMERA_CAPTURE_REQUEST_CODE = 200
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>

    // 카메라촬영 권한 요청을 위한 메서드
//    private fun requestCameraPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
//        } else {
//
//        }
//    }
//
//    // 카메라촬영 권한 요청 결과 처리를 위한 메서드
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//                // 권한이 거부된 경우
//                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
    // 카메라 실행을 위한 메서드
//    private fun openCamera() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(packageManager) != null) {
//            takePictureLauncher.launch(takePictureIntent)
//        } else {
//            Toast.makeText(this, "카메라 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // 촬영한 사진의 데이터 가져오기
//    private fun getImgData(){
//        // ActivityResultLauncher 초기화
//        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data: Intent? = result.data
//                val capturedImage: Bitmap? = data?.extras?.get("data") as Bitmap?
//                val imageUri: Uri? = data?.data
//
//                // 촬영한 사진 데이터(capturedImage)를 사용하여 필요한 작업을 수행합니다.
//                Log.i(TAG, capturedImage.toString()) // Bitmap 형식의 데이터
//                Log.i(TAG, imageUri.toString()) // Uri 형식의 데이터
//            }
//        }
//    }

    // 외부저장소 권한요청
    fun checkPermission() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, 100)
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
        val complete = findViewById<TextView>(R.id.album_upload_create) // 게시글 작성 완료버튼
        complete.visibility = View.VISIBLE
        complete.setOnClickListener{
            albumPostUpload()
        }
    }

    // 게시글 작성 후 완료버튼 눌러서 서버로 게시글 데이터 보내기
    private fun albumPostUpload(){
        if (viewDataBinding.progressBar.visibility == View.GONE){
            if (albumPhotos.size > 0){
                viewDataBinding.progressBar.visibility = View.VISIBLE

                val title = viewDataBinding.etAlbumUploadTitle.text.toString()
                val content = viewDataBinding.etAlbumUploadContents.text.toString()
                val bbsId = "album"
                val groupSeq = intent.getIntExtra("groupSeq", -1)
                val ntcrSeq = intent.getIntExtra("memberSeq", -1)
                val jwtToken = intent.getStringExtra("jwtToken")!!

                val pathList= ArrayList<MultipartBody.Part>()
                for (i in 0 until albumPhotos.size-1){
                    val file = File(CommonUtil.absolutelyPath(albumPhotos[i].photo.toUri(), this))
                    val requestBodys = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val path = MultipartBody.Part.createFormData("files", file.name, requestBodys)

                    pathList.add(path)
                }

//                val file = File(CommonUtil.absolutelyPath(albumPhotos[0].photo.toUri(), this))
//                val requestBodys = file.asRequestBody("image/*".toMediaTypeOrNull())
//                val path = MultipartBody.Part.createFormData("files", file.name, requestBodys)

                val board = AlbumUploadModel(title, content, bbsId, groupSeq, ntcrSeq)
                val json = Gson().toJson(board)
                val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

                //Log.i(TAG, "$title, $content //// path: " + path)
                Log.i(TAG, pathList.toString())

                viewDataBinding.vmAlbumUpload?.clickedComplete(jwtToken, requestBody, pathList)?.observe(this){
                    Log.i(TAG+" code", it.toString())
                    when (it) {
                        200 -> { // Success
                            finish()
                            viewDataBinding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "게시물 작성 완료", Toast.LENGTH_SHORT).show()
                            return@observe
                        }
                        400 -> { // 파라미터 오류
                            viewDataBinding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "제목 또는 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                            return@observe
                        }
                        500 -> { // 서버 내부오류
                            viewDataBinding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                            return@observe
                        }
                    }

                }


            }else{
                Toast.makeText(this, R.string.please_add_img, Toast.LENGTH_SHORT).show()
            }

        }

    }

}