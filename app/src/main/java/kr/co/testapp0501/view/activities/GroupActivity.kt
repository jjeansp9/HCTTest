package kr.co.testapp0501.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityGroupBinding
import kr.co.testapp0501.model.RecyclerGroupData
import kr.co.testapp0501.view.adapters.RecyclerGroupActivityAdapter

class GroupActivity : AppCompatActivity() {

    val binding : ActivityGroupBinding by lazy { ActivityGroupBinding.inflate(layoutInflater) }
    val groupItems = mutableListOf<RecyclerGroupData>()
    val adapter = RecyclerGroupActivityAdapter(this, groupItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerGroup.adapter = adapter

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (groupItems.size == 0){
            //groupItems.add(RecyclerGroupData())
        }
        //for (i in 0 .. 20) groupItems.add(RecyclerGroupData(R.drawable.bg_edit_input, "이씨네 가족"))

        // 그룹생성 화면으로 이동
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