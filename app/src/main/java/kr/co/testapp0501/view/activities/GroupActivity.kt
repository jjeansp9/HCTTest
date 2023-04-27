package kr.co.testapp0501.view.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityGroupBinding
import kr.co.testapp0501.model.recycler.RecyclerGroupData
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
        setToolbar()
        groupItems.add(RecyclerGroupData("", ""))

        // 설정 버튼 클릭 [ 설정 화면으로 이동 ]
        moveGroupCreateActivity()

        // 그룹접속 버튼 클릭 [ 다이얼로그 띄움 ]
        //binding.groupAccess.setOnClickListener{showDialog()}

        // 그룹을 클릭하여 메인화면으로 이동
        clickedGroupAdd()
        clickedGroupBox()
    }

    // 툴바 설정 [ 메인화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    var clicked = false

    // 그룹 [ + ] 버튼 클릭
    private fun clickedGroupAdd(){
        adapter.setItemClickListener(object : RecyclerGroupActivityAdapter.OnItemClickListener{
            override fun groupClick(v: View, position: Int) {
                //startActivity(Intent(this@GroupActivity, GroupCreateActivity::class.java)) // 임시

                if (!clicked){
                    binding.layoutGroupBox.visibility = View.VISIBLE
                    clicked = true
                }else{
                    binding.layoutGroupBox.visibility = View.GONE
                    clicked = false
                }
            }
        })
    }

    // 그룹생성 버튼 클릭
    private fun clickedGroupBox(){
        binding.tvGroupCreate.setOnClickListener{startActivity(Intent(this@GroupActivity, GroupCreateActivity::class.java))}
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

    // 설정 화면으로 이동
    private fun moveGroupCreateActivity(){
        binding.imgGroupSettings.setOnClickListener{startActivity(Intent(this, GroupSettingActivity::class.java))}
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