package kr.co.testapp0501.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivitySignUpSnsBinding
import kr.co.testapp0501.model.UserRepository
import kr.co.testapp0501.model.user.SocialUser
import kr.co.testapp0501.view.DatePickerFragment

class SignUpSnsActivity : AppCompatActivity() {

    private val binding : ActivitySignUpSnsBinding by lazy { ActivitySignUpSnsBinding.inflate(layoutInflater) }
    var gender = "M"
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val snsType = intent.getStringExtra("snsType")
        val snsId = intent.getStringExtra("snsId")
        Log.i("SignUpSnsActivity", snsType + snsId)

        // 생년월일 입력란을 클릭하면 캘린더 오픈
        binding.txtCal.setOnClickListener{
            showDatePicker(it)
        }

        clickedGender() // 성별 선택

        binding.btnRequestNum.setOnClickListener{clickedRequestNum()} // 폰번호 인증요청 클릭
        binding.btnNext.setOnClickListener{clickedNext(snsType!!, snsId!!)} // 다음버튼 클릭

    }

    // 폰번호 인증요청 클릭
    private fun clickedRequestNum(){

    }

    // 다음 버튼 클릭 [ 클릭하면 회원가입하기 위해 모두 입력했으면 정보들을 서버로 데이터를 보냄 ]
    private fun clickedNext(snsType : String, snsId : String){
        val name = binding.etName.text.toString().trim()
        val phoneNumber = binding.etPhoneNum.text.toString().trim()
        val birth = binding.txtCal.text.toString().trim()

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }else if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "휴대폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(birth)){
            Toast.makeText(this, "생년월일을 선택해주세요.", Toast.LENGTH_SHORT).show()
        }else{
            val user = SocialUser(snsType, snsId, name, phoneNumber, birth, gender)

            userRepository.snsRegisterUser(this, user)
            Log.i("SignUpSnsActivity",
                "snsType: ${user.snsType}," +
                        " snsId: ${user.snsId}, " +
                        "snsName: ${user.name}, " +
                        "snsPhoneNum: ${user.phoneNumber}, " +
                        "snsBirth: ${user.birth}, " +
                        "snsGender: ${user.sex}"
            )
        }
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

    // 성별 선택
    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor", "ResourceType")
    private fun clickedGender(){

        // 성별 버튼 클릭 [남자]
        binding.btnMan.setOnClickListener{
            it.setBackgroundResource(R.drawable.bg_textbox_gender_man)
            binding.btnMan.setTextColor(ContextCompat.getColor(this, R.color.gender_select))
            binding.btnGirl.setBackgroundResource(R.drawable.bg_gender_girl_white)
            binding.btnGirl.setTextColor(ContextCompat.getColor(this, R.color.gender_un_select))
            gender = "M"
            Log.i("...", "test")

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


}