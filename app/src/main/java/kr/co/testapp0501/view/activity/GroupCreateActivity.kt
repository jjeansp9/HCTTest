package kr.co.testapp0501.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityGroupCreateBinding

class GroupCreateActivity : AppCompatActivity() {

    private val binding : ActivityGroupCreateBinding by lazy { ActivityGroupCreateBinding.inflate(layoutInflater) }
    lateinit var imgUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setToolbar() // 툴바 생성
        checkPermission() // 외부저장소 권한요청

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

    // 이미지 관련 코드
    var resultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != RESULT_CANCELED) {
            imgUri = result.data!!.data!!
            Glide.with(this).load(imgUri).into(binding.imgAdd)
        }
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