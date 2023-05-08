package kr.co.testapp0501.view.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityGroupBinding
import kr.co.testapp0501.model.group.GroupMatching
import kr.co.testapp0501.model.recycler.RecyclerGroupData
import kr.co.testapp0501.view.adapter.RecyclerGroupActivityAdapter
import kr.co.testapp0501.viewmodel.GroupViewModel

class GroupActivity : AppCompatActivity() {

    private val binding : ActivityGroupBinding by lazy { ActivityGroupBinding.inflate(layoutInflater) }

    private val groupItems = ArrayList<RecyclerGroupData>() // 그룹목록 아이템
    private val adapter = RecyclerGroupActivityAdapter(this, groupItems)

    private lateinit var groupViewModel: GroupViewModel // 뷰모델

    private val swipeRefreshLayout : SwipeRefreshLayout by lazy { findViewById(R.id.swipe_refresh_layout) }
    private lateinit var jwtToken: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerGroup.adapter = adapter
        groupViewModel = ViewModelProvider(this)[GroupViewModel::class.java]

        // 툴바 생성
        setToolbar()
        jwtToken = intent.getStringExtra("token")!!

        swipeRefreshLayout.setOnRefreshListener{
            groupItems.clear()
            groupList(jwtToken)
        }
        groupList(jwtToken)

        // 설정 버튼 클릭 [ 설정 화면으로 이동 ]
        moveGroupCreateActivity()

        // 그룹을 클릭하여 메인화면으로 이동
        clickedGroupAdd()
    }

    private fun groupList(jwtToken: String){
        groupViewModel.loadGroupList(jwtToken).observe(this){
            for (i in 0 until it.data.size){
                if (it.data[i].filePaths.isNotEmpty()) {
                    groupItems.add(RecyclerGroupData(it.data[i].filePaths[0], it.data[i].groupName))
                    Log.i("iii if", i.toString() + it.data[i].filePaths[0])
                } else {
                    // filePaths가 비어있는 경우, 기본 이미지를 사용하도록 설정
                    groupItems.add(RecyclerGroupData("", it.data[i].groupName))
                    Log.i("iii else", i.toString())
                }
            }
            groupItems.add(groupItems.size, RecyclerGroupData("add", ""))
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    // 툴바 설정 [ 메인화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    // 그룹 [ + ] 버튼 클릭
    private fun clickedGroupAdd(){
        adapter.setItemClickListener(object : RecyclerGroupActivityAdapter.OnItemClickListener{
            override fun groupClick(v: View, position: Int) {
                if (position == groupItems.size -1){
                    groupDialog(R.layout.dialog_group_add)
                }
            }
        })
    }

    // 그룹생성, 그룹추가 다이얼로그
    private fun groupDialog(xml : Int){
        val dialog = Dialog(this)
        dialog.setContentView(xml)

        // 다이얼로그 사이즈조절
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 텍스트,이미지 설정
        dialog.show()

        val btnGroupCreate : RelativeLayout = dialog.findViewById(R.id.btn_group_create)
        val btnGroupConnect : RelativeLayout = dialog.findViewById(R.id.btn_group_connect)

        // 그룹생성 버튼
        btnGroupCreate.setOnClickListener{
            val token = intent.getStringExtra("token")
            val intent = Intent(this, GroupCreateActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
            dialog.dismiss()
        }
        // 그룹추가 버튼
        btnGroupConnect.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            dialog.dismiss()
            // 그룹코드 다이얼로그
            groupCodeDialog(R.layout.dialog_group_code_input)
        }

    }

    // 그룹코드 다이얼로그
    private fun groupCodeDialog(xml: Int){
        val dialog = Dialog(this)
        dialog.setContentView(xml)

        // 다이얼로그 사이즈조절
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 텍스트,이미지 설정
        dialog.show()

        val btnConfirm : RelativeLayout = dialog.findViewById(R.id.btn_code_confirm)
        val etCodeInput : EditText = dialog.findViewById(R.id.et_code_input)
        val groupCode = etCodeInput.text.toString().trim()

        val groupMatching = GroupMatching("cQvvqnKLEG", "2")

        btnConfirm.setOnClickListener{
            // 통신을 위해 et에 입력한 그룹코드 값 보내기
            groupViewModel.groupMatching(jwtToken, groupMatching).observe(this){
                if (it == 200){
                    Toast.makeText(this, "그룹코드가 일치합니다", Toast.LENGTH_SHORT).show()
                }else if (it == 400){
                    Toast.makeText(this, "그룹코드가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                }
            }
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