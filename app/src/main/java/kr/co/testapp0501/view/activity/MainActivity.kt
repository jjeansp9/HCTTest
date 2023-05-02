package kr.co.testapp0501.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import kr.co.avad.android.humancaretree.base.BaseActivity
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityAlbumBinding
import kr.co.testapp0501.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(binding.root)

        viewDataBinding.vmMain = getViewModel()
        viewDataBinding.lifecycleOwner = this

        // 툴바 설정 [ 메인 화면 ]
        setToolbar()

        binding.btnMember.setOnClickListener{startActivity(Intent(this, MemberActivity::class.java))}
    }

    // 툴바 설정 [ 메인 화면 ]
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.setText(R.string.logo_name)
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