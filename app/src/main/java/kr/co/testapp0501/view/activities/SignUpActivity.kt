package kr.co.testapp0501.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.content.ContextCompat
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    val binding : ActivitySignUpBinding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        clickedNext()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor", "ResourceType")
    private fun clickedNext(){

        // 성별 버튼 클릭 [남자]
        binding.btnMan.setOnClickListener{

        }
        // 성별 버튼 클릭 [여자]
        binding.btnGirl.setOnClickListener{

        }

        // 휴대폰번호 인증요청 버튼 클릭
        binding.btnRequestNum.setOnTouchListener{ view, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    view.setBackgroundResource(R.drawable.bg_btn_un_click)

                    true
                }
                MotionEvent.ACTION_UP -> {
                    view.setBackgroundResource(R.drawable.bg_btn_click)

                    true
                }
                else -> false
            }
        }

        // 다음 버튼 클릭
        binding.btnNext.setOnTouchListener{ view, event ->
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
}