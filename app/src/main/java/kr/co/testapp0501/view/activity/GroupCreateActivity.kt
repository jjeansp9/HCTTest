package kr.co.testapp0501.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityGroupCreateBinding
import kr.co.testapp0501.model.group.Group
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import kr.co.testapp0501.viewmodel.GroupViewModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class GroupCreateActivity : AppCompatActivity() {

    private val binding : ActivityGroupCreateBinding by lazy { ActivityGroupCreateBinding.inflate(layoutInflater) }
    private lateinit var groupViewModel: GroupViewModel
    var imgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setToolbar() // 툴바 생성
        checkPermission() // 외부저장소 권한요청
        groupViewModel = ViewModelProvider(this).get(GroupViewModel::class.java)

        binding.imgAdd.setOnClickListener{imageAdd()}

        clickedComplete()
    }

    // 그룹생성 항목들을 모두 작성 후에 확인버튼을 눌렀을 때
    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    private fun clickedComplete(){
        binding.btnComplete.setOnTouchListener{ view, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    view.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_un_click))

                    true
                }
                MotionEvent.ACTION_UP -> {
                    view.setBackgroundColor( ContextCompat.getColor(this, R.color.btn_click))

                    if (imgUri != null){



                        val token = intent.getStringExtra("token")!!

                        val file = File(imgPath)
                        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

                        val groupImg= MultipartBody.Part.createFormData("files", file.name, requestBody)
                        val groupName = binding.etGroupName.text.toString()
                        val groupType = binding.spinGroupType.selectedItem.toString()
                        Log.i("ss", groupName + groupType + groupImg + token)

//                        val dataPart: MutableMap<String, String> = HashMap()
//                        dataPart["groupName"] = groupName
//                        dataPart["groupType"] = groupType
//                        dataPart["masterSeq"] = "1"
//                        dataPart["memo"] = ""

                        val groupInfo = Group(groupName, groupType, 1, "")

                        groupViewModel.createGroup(token, groupInfo, groupImg)
                        groupViewModel.code.observe(this){
                            Log.i("GroupCreateaActivity code", it)
                        }
                    }else{
                        Toast.makeText(this, "이미지를 추가해주세요", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }
    }

    // 이미지 추가 버튼 눌렀을 때
    private fun imageAdd(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    var imgPath = ""

    // 이미지 관련 코드
    var resultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != RESULT_CANCELED) {
            imgUri = result.data!!.data!!
            Glide.with(this).load(imgUri).into(binding.imgAdd)

            imgPath = getRealPathFromUri(imgUri)!!
        }
    }

    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    fun getRealPathFromUri(uri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(
            this,
            uri!!, proj, null, null, null
        )
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

    // 외부저장소 권한요청
    fun checkPermission() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) { // 허용되지 않았을 때
            requestPermissions(permissions, 100)
        }else{ // 허용됐을 때

        }
    }

    // 툴바 설정 [ 메인화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.text = "그룹 생성"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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