package kr.co.testapp0501.view.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivitySignUpSnsBinding
import kr.co.testapp0501.model.UserRepository
import kr.co.testapp0501.model.users.NormalUser
import kr.co.testapp0501.model.users.SocialUser
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

        clickedNext(snsType!!, snsId!!)

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

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor", "ResourceType")
    private fun clickedNext(snsType : String, snsId : String){

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

        // 다음 버튼 클릭 [ 클릭하면 회원가입하기 위해 모두 입력했으면 정보들을 서버로 데이터를 보냄 ]
        binding.btnNext.setOnTouchListener{ view, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    view.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_un_click))
                    true
                }
                MotionEvent.ACTION_UP -> {
                    view.setBackgroundColor( ContextCompat.getColor(this, R.color.btn_click))


                    val name = binding.etName.text.toString().trim()
                    val phoneNumber = binding.etPhoneNum.text.toString().trim()
                    val birth = binding.txtCal.text.toString().trim()

                    val user = SocialUser(snsType, snsId, name, phoneNumber, birth, gender)

                    userRepository.registerUser(this, user)
                    true
                }
                else -> false
            }
        }
    }


}