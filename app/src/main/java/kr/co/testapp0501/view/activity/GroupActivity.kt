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
import kr.co.testapp0501.model.network.ApiService
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
    private var memberSeq: Int = -1

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerGroup.adapter = adapter
        groupViewModel = ViewModelProvider(this)[GroupViewModel::class.java]

        // 툴바 생성
        setToolbar()
        jwtToken = intent.getStringExtra("jwtToken")!!
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)

        // 아래로 당겨서 새로고침
        updateGroupList()

        groupList(jwtToken, memberSeq)

        // 설정 버튼 클릭 [ 설정 화면으로 이동 ]
        moveGroupCreateActivity()

        // 그룹을 클릭하여 메인화면으로 이동
        clickedGroupAdd()
    }

    // 아래로 당겨서 새로고침
    private fun updateGroupList(){
        swipeRefreshLayout.setOnRefreshListener{
            groupItems.clear()
            groupList(jwtToken, memberSeq)
        }
    }

    // 그룹목록 불러오기
    @SuppressLint("NotifyDataSetChanged")
    private fun groupList(jwtToken: String, memberSeq: Int){
        binding.progressBar.visibility = View.VISIBLE
        groupViewModel.loadGroupList(jwtToken, memberSeq).observe(this){
            for (i in 0 until it.data.size){
                if (it.data[i].filePaths.isNotEmpty()) {

                    groupItems.add(RecyclerGroupData(
                        ApiService.FILE_SUFFIX_URL+it.data[i].filePaths[0],
                        it.data[i].groupName,
                        it.data[i].groupSeq,
                        it.data[i].memberSeq,
                        it.data[i].memberAuthLevel
                    ))
                    Log.i("GroupActivity groupItems if",
                        i.toString() + ": "
                                + ApiService.FILE_SUFFIX_URL
                                + "/attachFile" + it.data[i].filePaths[0]
                                + ", seq: "
                                + it.data[i].groupSeq + ","
                                + it.data[i].memberSeq + ","
                                + it.data[i].memberAuthLevel
                    )
                } else {
                    // filePaths가 비어있는 경우, 기본 이미지를 사용하도록 설정
                    groupItems.add(RecyclerGroupData(
                        "",
                        it.data[i].groupName,
                        it.data[i].groupSeq,
                        it.data[i].memberSeq,
                        it.data[i].memberAuthLevel
                    ))
                    Log.i("GroupActivity groupItems else",
                        i.toString() + ": "
                                + ApiService.FILE_SUFFIX_URL
                                + ", seq: "
                                + it.data[i].groupSeq + ","
                                + it.data[i].memberSeq + ","
                                + it.data[i].memberAuthLevel
                    )
                }
            }
            groupItems.add(groupItems.size, RecyclerGroupData(
                "add",
                "",
                -1,
                -1,
                -1
            ))
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
            binding.progressBar.visibility = View.GONE
        }
    }

    // 툴바 설정 [ 그룹화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    // 그룹 목록에 있는 그룹 클릭
    private fun clickedGroupAdd(){
        adapter.setItemClickListener(object : RecyclerGroupActivityAdapter.OnItemClickListener{
            override fun groupClick(v: View, position: Int) {

                if (position == groupItems.size -1) { // 그룹 [ + ] 버튼 클릭
                    groupDialog(R.layout.dialog_group_add)

                } else { // [+]버튼을 제외한 모든 그룹목록을 클릭 시 메인액티비티로 이동
                    val token = intent.getStringExtra("jwtToken")
                    val intent = Intent(this@GroupActivity, MainActivity::class.java)
                    intent.putExtra("jwtToken", token)
                    intent.putExtra("groupSeq", groupItems[position].groupSeq)
                    intent.putExtra("groupName", groupItems[position].tvGroupName)
                    intent.putExtra("memberSeq", groupItems[position].memberSeq)
                    intent.putExtra("memberLevel", groupItems[position].memberAuthLevel)
                    startActivity(intent)
                    Log.i("positions", token!!)
                }
                Log.i("positions", groupItems[position].groupSeq.toString())
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
            val token = intent.getStringExtra("jwtToken")
            val memberSeq = intent.getIntExtra("memberSeq", -1)

            val intent = Intent(this, GroupCreateActivity::class.java)
            intent.putExtra("jwtToken", token)
            intent.putExtra("memberSeq", memberSeq)
            startActivity(intent)
            dialog.dismiss()
        }
        // 그룹추가 버튼
        btnGroupConnect.setOnClickListener{
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

        // 그룹접속코드 입력 후 확인버튼
        btnConfirm.setOnClickListener{

            val etCodeInput : EditText = dialog.findViewById(R.id.et_code_input)
            val groupCode = etCodeInput.text.toString().trim()
            val groupMatching = GroupMatching(groupCode, memberSeq)
            Log.i("GroupActivity memberSeq", memberSeq.toString())

            // 통신을 위해 et에 입력한 그룹코드 값 보내기
            groupViewModel.groupMatching(jwtToken, groupMatching).observe(this){

                if (it == 201){
                    Toast.makeText(this, "그룹코드가 일치합니다", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }else if (it == 400){
                    Toast.makeText(this, "그룹코드가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                }else if (it == 409){
                    Toast.makeText(this, "해당 코드로 이미 요청하였습니다", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }else if (it == 500){
                    Toast.makeText(this, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 설정 화면으로 이동
    private fun moveGroupCreateActivity(){
        binding.imgGroupSettings.setOnClickListener{startActivity(Intent(this, GroupSettingActivity::class.java))}
    }
}