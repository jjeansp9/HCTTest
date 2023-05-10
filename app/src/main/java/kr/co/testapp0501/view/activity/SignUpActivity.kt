package kr.co.testapp0501.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivitySignUpBinding
import kr.co.testapp0501.model.user.CheckId
import kr.co.testapp0501.model.user.NormalUser
import kr.co.testapp0501.view.DatePickerFragment
import kr.co.testapp0501.viewmodel.UserViewModel


class SignUpActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel // ViewModel 초기화

    private var gender = "M" // 성별 기본 M으로 설정

    private val binding : ActivitySignUpBinding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // ViewModel 생성
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // 툴바 설정
        setToolbar()

        Log.i("gender", gender) // 회원가입 화면이 처음 열리면 기본성별 M

        clickedGender() // 버튼 [ 성별 선택, 휴대폰 인증요청, 다음 버튼 ]
        selectInput() // EditText 클릭 시 background 테두리 변경

        // 생년월일 입력란을 클릭하면 캘린더 오픈
        binding.txtCal.setOnClickListener{ showDatePicker(it) }

        binding.btnRequestNum.setOnClickListener{clickedRequestNum()} // 폰번호 인증요청 버튼 클릭
        binding.btnNext.setOnClickListener{clickedNext()} // 다음버튼 클릭

        setEtSpace()

    }

    // 폰번호 인증요청 클릭
    private fun clickedRequestNum(){

    }

    // 다음버튼 클릭했을 때
    private fun clickedNext(){
        val id = binding.etId.text.toString().trim()
        val pw = binding.etPassword.text.toString().trim()
        val name = binding.etName.text.toString().trim()
        val phoneNumber = binding.etPhoneNum.text.toString().trim()
        val birth = binding.txtCal.text.toString().trim()

        // EditText에 입력한 값들을 user에 대입
        val user = NormalUser(id, pw, name, phoneNumber, birth, gender)

        // user 에 저장된 값들을 서버로 보내고 곧바로 데이터 확인
        userViewModel.addNormalUser(this, user).observe(this){ code ->
            Log.i("SignUpActivity normalUser", "code: $code")
            // code 400 : 비밀번호 형식 등 확인
            // code 201 : Success
            // code 500 : 서버 내부오류

            // code 409 : 아이디 중복
            if (code == 409){
                binding.etId.setBackgroundResource(R.drawable.bg_edit_dupl)
                binding.tvIdDuplicate.text = "중복된 아이디입니다."
                binding.tvIdDuplicate.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.tvIdDuplicate.visibility = View.VISIBLE
            }
        }
    }

    // EditText에 스페이스바 입력 안되게 설정
    private fun setEtSpace(){
        val editTextList = arrayOf(
            binding.etId,
            binding.etPassword,
            binding.etPasswordConfirm,
            binding.etName,
            binding.etPhoneNum,
            binding.etResponseNum
        )

        for (editText in editTextList) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val string = s.toString()
                    if (string.contains(" ")) {
                        val newString = string.replace(" ", "")
                        editText.setText(newString)
                        editText.setSelection(newString.length)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    // 툴바 설정
    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.visibility = View.VISIBLE
        //val btnBack = findViewById<ImageView>(R.id.btn_back)
        //btnBack.setOnClickListener{btnBack.isSelected = !btnBack.isSelected}
        tv.text = "회원가입"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    // Date Picker
    private fun showDatePicker(view: View?) {
        val newFragment: DialogFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    fun processDatePickerResult(year: Int, month: Int, day: Int) {
        val year_string = Integer.toString(year)
        var month_string = Integer.toString(month + 1)
        var day_string = Integer.toString(day)

        if (month_string.toInt() < 10){
            month_string = "0$month_string"
        }

        if (day_string.toInt() < 10){
            day_string = "0$day_string"
        }

        val dateMessage = "$year_string-$month_string-$day_string"
        binding.txtCal.text = "$dateMessage" // 선택한 날짜를 화면에 보여주기
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

    // 버튼 [ 성별 선택, 휴대폰 인증요청, 다음 버튼 ]
    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor", "ResourceType")
    private fun clickedGender(){

        // 성별 버튼 클릭 [남자]
        binding.btnMan.setOnClickListener{
            it.setBackgroundResource(R.drawable.bg_textbox_gender_man)
            binding.btnMan.setTextColor(ContextCompat.getColor(this, R.color.gender_select))
            binding.btnGirl.setBackgroundResource(R.drawable.bg_gender_girl_white)
            binding.btnGirl.setTextColor(ContextCompat.getColor(this, R.color.gender_un_select))
            gender = "M"

        }
        // 성별 버튼 클릭 [여자]
        binding.btnGirl.setOnClickListener{
            it.setBackgroundResource(R.drawable.bg_textbox_gender_girl)
            binding.btnGirl.setTextColor(ContextCompat.getColor(this, R.color.gender_select))
            binding.btnMan.setBackgroundResource(R.drawable.bg_gender_man_white)
            binding.btnMan.setTextColor(ContextCompat.getColor(this, R.color.gender_un_select))
            gender = "F"
        }
    }


    // 키보드가 열린 상태일 때 키보드 닫으면서 et에 입력한 문자열 받아오기
    // 닫힌 상태라면 Background를 클릭해도 문자열 안받아옴
    @SuppressLint("SetTextI18n")
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
                    val responseNum = binding.etResponseNum.text.toString().trim()



                    // 휴대폰인증번호 일치한지 화인
                    if (responseNum == "123123"){
                        binding.tvResponseNumSuccess.visibility = View.VISIBLE
                    }else binding.tvResponseNumSuccess.visibility = View.GONE

                    val checkId = CheckId(id)

                    userViewModel.checkId(checkId).observe(this){ code ->
                        Log.i("SignUpActivity checkId", "code: $code")

                    }

                    // 중복된 아이디가 있는지 확인
//                    if (id == "123"){
//                        binding.etId.setBackgroundResource(R.drawable.bg_edit_dupl)
//                        binding.tvIdDuplicate.text = "중복된 아이디입니다."
//                        binding.tvIdDuplicate.setTextColor(ContextCompat.getColor(this, R.color.red))
//                        binding.tvIdDuplicate.visibility = View.VISIBLE
//
//                    }else
                    if(TextUtils.isEmpty(id)){ // 아이디 입력란이 공백인지 확인
                        binding.tvIdDuplicate.visibility = View.GONE
                    }
//                    else{
//                        // TODO : 사용 가능한 아이디일 때 로직 개선하기
//                        binding.tvIdDuplicate.text = "사용이 가능한 아이디입니다."
//                        binding.tvIdDuplicate.setTextColor(ContextCompat.getColor(this, R.color.brand_color))
//                        binding.tvIdDuplicate.visibility = View.VISIBLE
//                    }

                    val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).{8,20}$")

                    // 비밀번호와 비밀번호확인 입력란이 동일한지 확인
                    if (pw != pwConfirm && !TextUtils.isEmpty(pw) && !TextUtils.isEmpty(pwConfirm)){
                        binding.tvNotSame.text = "입력하신 비밀번호와 일치하지 않습니다."
                        binding.tvNotSame.setTextColor(ContextCompat.getColor(this, R.color.red))
                        binding.etPasswordConfirm.setBackgroundResource(R.drawable.bg_edit_dupl)
                        binding.tvPwDuplicate.visibility = View.GONE
                        binding.tvNotSame.visibility = View.VISIBLE

                    }else if(TextUtils.isEmpty(pw)){ // 비밀번호 입력란이 공백일 때
                        binding.tvPwDuplicate.text = "* 8~20자리 영문 대,소문자,특수문자 모두 포함하여 입력해 주세요"
                        binding.tvPwDuplicate.setTextColor(ContextCompat.getColor(this, R.color.red))
                        binding.tvPwDuplicate.visibility = View.VISIBLE

                    }else if(TextUtils.isEmpty(pwConfirm)){ // 비밀번호확인 입력란이 공백일 때
                        binding.tvNotSame.visibility = View.GONE

                    }else if (!regex.matches(pw)){
                        binding.tvPwDuplicate.text = "* 8~20자리 영문 대,소문자,특수문자 모두 포함하여 입력해 주세요"
                        binding.tvPwDuplicate.setTextColor(ContextCompat.getColor(this, R.color.red))
                        binding.etPassword.setBackgroundResource(R.drawable.bg_edit_dupl)
                        binding.tvPwDuplicate.visibility = View.VISIBLE
                        binding.etPasswordConfirm.setBackgroundResource(R.drawable.bg_edit)
                        binding.tvNotSame.visibility = View.GONE

                    }else if (regex.matches(pw) && pw == pwConfirm){ // 위 조건을 만족할 때
                        binding.tvPwDuplicate.text = "사용이 가능한 비밀번호입니다."
                        binding.tvPwDuplicate.setTextColor(ContextCompat.getColor(this, R.color.brand_color))
                        binding.etPasswordConfirm.setBackgroundResource(R.drawable.bg_edit)
                        binding.tvPwDuplicate.visibility = View.VISIBLE
                        binding.tvNotSame.visibility = View.GONE

                    }

                    Log.i("tests", "id: $id, pw: $pw, pwConfirm: $pwConfirm, name: $name")
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}