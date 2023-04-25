package kr.co.testapp0501.view.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityGroupBinding
import kr.co.testapp0501.model.RecyclerGroupData
import kr.co.testapp0501.view.adapters.RecyclerGroupActivityAdapter

class GroupActivity : AppCompatActivity() {

    private val binding : ActivityGroupBinding by lazy { ActivityGroupBinding.inflate(layoutInflater) }
    private val groupItems = mutableListOf<RecyclerGroupData>()
    private val adapter = RecyclerGroupActivityAdapter(this, groupItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerGroup.adapter = adapter

        // 툴바 생성
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // 더미데이터 추가해서 테스트
        for (i in 0 .. 20) groupItems.add(RecyclerGroupData(R.drawable.bg_edit, "이씨네 가족"))

        // 그룹생성 버튼 클릭 [ 그룹생성 화면으로 이동 ]
        moveGroupCreateActivity()

        // 그룹접속 버튼 클릭 [ 다이얼로그 띄움 ]
        binding.groupAccess.setOnClickListener{showDialog()}

    }

    // 그룹코드 입력하는 다이얼로그
    private fun showDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_group_code_input)

        // 다이얼로그 사이즈조절
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 텍스트,이미지 설정
        dialog.show()

        val tv : TextView = dialog.findViewById(R.id.tv_code_confirm)
        tv.setOnClickListener{
            Toast.makeText(this, "확인버튼 클릭", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java)) // 임시
        }
    }

    // 그룹생성 화면으로 이동
    private fun moveGroupCreateActivity(){
        binding.imgGroupCreate.setOnClickListener{startActivity(Intent(this, GroupCreateActivity::class.java))}
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