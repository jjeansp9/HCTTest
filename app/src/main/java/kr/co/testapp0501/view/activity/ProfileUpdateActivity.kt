package kr.co.testapp0501.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseActivity
import kr.co.testapp0501.databinding.ActivityProfileBinding
import kr.co.testapp0501.databinding.ActivityProfileUpdateBinding
import kr.co.testapp0501.view.DatePickerFragment
import kr.co.testapp0501.viewmodel.ProfileViewModel

class ProfileUpdateActivity : BaseActivity<ActivityProfileUpdateBinding>(R.layout.activity_profile_update) {
    companion object{
        const val TAG = "profileUpdateActivity"
    }
    private var gender = "M"
    private lateinit var jwtToken: String
    private var memberSeq : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vmProfile = ProfileViewModel()
        viewDataBinding.lifecycleOwner = this

        jwtToken = intent.getStringExtra("jwtToken")!!
        memberSeq = intent.getIntExtra("memberSeq", memberSeq)

        selectInputChangeColor() // 선택된 et 색상 변경
        clickedGender() // 성별 선택
        setToolbar()
        birthClick() // datePicker
        setEtSpace()
    }

    override fun onResume() {
        super.onResume()
        loadProfileInfo(jwtToken, memberSeq)
    }

    // 회원정보 불러오기
    private fun loadProfileInfo(jwtToken: String, memberSeq: Int){
        viewDataBinding.vmProfile?.requestMemberInfo(jwtToken, memberSeq)

        viewDataBinding.vmProfile!!.profileGender.observe(this){
            gender = if (it == "M"){
                viewDataBinding.btnMan.setBackgroundResource(R.drawable.bg_textbox_gender_man)
                viewDataBinding.btnMan.setTextColor(ContextCompat.getColor(this, R.color.gender_select))
                viewDataBinding.btnGirl.setBackgroundResource(R.drawable.bg_gender_girl_white)
                viewDataBinding.btnGirl.setTextColor(ContextCompat.getColor(this, R.color.gender_un_select))
                "M"
            }else{
                viewDataBinding.btnGirl.setBackgroundResource(R.drawable.bg_textbox_gender_girl)
                viewDataBinding.btnGirl.setTextColor(ContextCompat.getColor(this, R.color.gender_select))
                viewDataBinding.btnMan.setBackgroundResource(R.drawable.bg_gender_man_white)
                viewDataBinding.btnMan.setTextColor(ContextCompat.getColor(this, R.color.gender_un_select))
                "F"
            }
        }
    }

    // date picker clicked
    private fun birthClick(){
        viewDataBinding.txtCal.setOnClickListener{ showDatePicker(it) }
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
        viewDataBinding.txtCal.text = "$dateMessage" // 선택한 날짜를 화면에 보여주기
    }

    // 버튼 [ 성별 선택, 휴대폰 인증요청, 다음 버튼 ]
    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor", "ResourceType")
    private fun clickedGender(){

        // 성별 버튼 클릭 [남자]
        viewDataBinding.btnMan.setOnClickListener{
            it.setBackgroundResource(R.drawable.bg_textbox_gender_man)
            viewDataBinding.btnMan.setTextColor(ContextCompat.getColor(this, R.color.gender_select))
            viewDataBinding.btnGirl.setBackgroundResource(R.drawable.bg_gender_girl_white)
            viewDataBinding.btnGirl.setTextColor(ContextCompat.getColor(this, R.color.gender_un_select))
            gender = "M"

        }
        // 성별 버튼 클릭 [여자]
        viewDataBinding.btnGirl.setOnClickListener{
            it.setBackgroundResource(R.drawable.bg_textbox_gender_girl)
            viewDataBinding.btnGirl.setTextColor(ContextCompat.getColor(this, R.color.gender_select))
            viewDataBinding.btnMan.setBackgroundResource(R.drawable.bg_gender_man_white)
            viewDataBinding.btnMan.setTextColor(ContextCompat.getColor(this, R.color.gender_un_select))
            gender = "F"
        }

    }

    // editText 클릭시 배경색 변경
    private fun selectInputChangeColor(){
        var selectedEditText: EditText? = null
        // list에 editText 저장
        val editTextList = listOf(
            viewDataBinding.etName,
            viewDataBinding.etPhoneNum,
            viewDataBinding.etResponseNum
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
//        viewDataBinding.profileUpdateRoot.setOnClickListener {
//            selectedEditText?.apply {
//                clearFocus()
//                selectedEditText = null
//            }
//        }
    }

    // EditText에 스페이스바 입력 안되게 설정
    private fun setEtSpace(){
        val editTextList = arrayOf(
            viewDataBinding.etName,
            viewDataBinding.etPhoneNum,
            viewDataBinding.etResponseNum
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

    override fun initObservers() {
    }

    private fun setToolbar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val tv = findViewById<TextView>(R.id.tv_toolber_title)
        tv.visibility = View.VISIBLE
        tv.text = "정보"
        val complete = findViewById<TextView>(R.id.album_upload_create) // 게시글 작성 완료버튼
        complete.visibility = View.VISIBLE
        complete.setOnClickListener{
            profileUpdateComplete()
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    // 프로필 수정 완료버튼 클릭
    private fun profileUpdateComplete(){

        if (viewDataBinding.progressBar.visibility == View.GONE){
            viewDataBinding.progressBar.visibility = View.VISIBLE

            val name= viewDataBinding.etName.text.toString().trim()
            val birth: String= viewDataBinding.txtCal.text.toString()
            val phoneNum= viewDataBinding.etPhoneNum.text.toString().trim()

            viewDataBinding.vmProfile?.profileChanged(jwtToken, memberSeq, name, birth, gender, phoneNum)?.observe(this){
                if (it == 200){
                    finish()
                    viewDataBinding.progressBar.visibility = View.GONE
                    Toast.makeText(this, R.string.profile_update_success, Toast.LENGTH_SHORT).show()
                }else if (it == 400) {

                }else if (it == 404) {

                }else if (it == 500) {

                }
            }
        }

    }

    // 휴대폰번호 인증요청 클릭
    private fun phoneNumConfirmRequest(){
        viewDataBinding.btnRequestNum.setOnClickListener{

        }
    }
}






























