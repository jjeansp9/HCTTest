package kr.co.testapp0501.view.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import kr.co.testapp0501.NormalUser
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivitySignUpBinding
import kr.co.testapp0501.viewmodel.UserViewModel

class SignUpActivity : AppCompatActivity() {

    val binding : ActivitySignUpBinding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        Log.i("gender", gender)

        clickedNext()
        //clickedBackGround()
        selectInput()

    }

    // editText 클릭시 배경색 변경
    private fun selectInput(){
        var selectedEditText: EditText? = null
        // list에 editText 저장
        val editTextList = listOf(
            binding.etId,
            binding.etPassword,
            binding.etPasswordConfirm,
            binding.etName,
            binding.etPhoneNum,
            binding.etResponseNum
        )

        // EditText 클릭 시, 배경색 변경
        editTextList.forEach {
            it.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    selectedEditText = it
                    it.setBackgroundResource(R.drawable.bg_edit_input)
                } else {
                    it.setBackgroundResource(R.drawable.bg_edit)
                }
            }
        }

        // 다른 곳 클릭 시, 입력 모드 해제
        binding.root.setOnClickListener {
            selectedEditText?.apply {
                clearFocus()
                selectedEditText = null
            }
        }
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

    private var gender = "man"

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor", "ResourceType")
    private fun clickedNext(){

        // 성별 버튼 클릭 [남자]
        binding.btnMan.setOnClickListener{
            it.setBackgroundResource(R.drawable.bg_gender_man)
            binding.btnMan.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnGirl.setBackgroundResource(R.drawable.bg_gender_girl_white)
            binding.btnGirl.setTextColor(ContextCompat.getColor(this, R.color.black))
            gender = "man"
        }
        // 성별 버튼 클릭 [여자]
        binding.btnGirl.setOnClickListener{
            it.setBackgroundResource(R.drawable.bg_gender_girl)
            binding.btnGirl.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnMan.setBackgroundResource(R.drawable.bg_gender_man_white)
            binding.btnMan.setTextColor(ContextCompat.getColor(this, R.color.black))
            gender = "girl"
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

                    val id = binding.etId.text.toString().trim()
                    val pw = binding.etPassword.text.toString().trim()
                    val name = binding.etName.text.toString().trim()

                    val user = NormalUser(id, pw, name, gender)

                    userViewModel.normalLogin(this, user)


                    userViewModel.addNormalUser(this, user).observe(this){ user ->
                        Log.i("SignUpActivity normalUser", "id: ${user.id} , pw: ${user.pw} , name: ${user.name} , gender: ${user.gender}")
                    }

                    true
                }
                else -> false
            }
        }
    }



    // 키보드가 열린 상태일 때 키보드 닫으면서 et에 입력한 문자열 받아오기, 닫힌 상태라면 Background를 클릭해도 문자열 안받아옴
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val rect = Rect()
                view.getGlobalVisibleRect(rect)
                if (!rect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)

                    val id = binding.etId.text.toString().trim()
                    val pw = binding.etPassword.text.toString().trim()
                    val pwConfirm = binding.etPasswordConfirm.text.toString().trim()
                    val name = binding.etName.text.toString().trim()

                    userViewModel.updateText(id)

                    Log.i("tests", "id: $id, pw: $pw, pwConfirm: $pwConfirm, name: $name")
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }




    // 키보드가 열린 상태일 때
    // 키보드 외의 바깥 영역을 클릭하면 키보드 내려가게하는 메소드
//    @SuppressLint("ClickableViewAccessibility")
//    private fun clickedBackGround(){
//        binding.signUpRoot.setOnTouchListener(View.OnTouchListener { v, event ->
//            if (v.hasFocus()) {
//                if (isKeyboardOpen()){
//                    val inputManager: InputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    inputManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
//
//
//                }
//
//                false
//
//            } else {
//
//                true
//            }
//        })
//
//    }



//    private fun isKeyboardOpen(): Boolean {
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        return imm.isAcceptingText
//    }
}