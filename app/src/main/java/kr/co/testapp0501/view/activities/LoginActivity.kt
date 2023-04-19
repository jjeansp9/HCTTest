package kr.co.testapp0501.view.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityLoginBinding
import kr.co.testapp0501.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {

    private val binding : ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var userViewModel: UserViewModel

    private val clientId = "3nhXN4DGA4kNKDzAwXzW" // 네이버 로그인 식별 아이디
    private val clientSecret = "M8AMLuZlf_" // 네이버 로그인 식별 패스워드
    private val nidOAuthLogin = NidOAuthLogin()

    var mGoogleSignInClient: GoogleSignInClient? = null

    // 어떤것으로 로그인했는지 구분하기 위한 변수
    val kakao = "kakao"
    val naver = "naver"
    val google = "google"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        Log.d("keyHash", " KeyHash :" + Utility.getKeyHash(this)) // 카카오 SDK용 키해시 값
        NaverIdLoginSDK.initialize(this, clientId, clientSecret, "Test") // 네이버 클라이언트 등록

        normalLogin()
        clickedBackGround()

        binding.kakaoLogin.setOnClickListener{login(kakao)}
        binding.naverLogin.setOnClickListener{login(naver)}
        binding.googleLogin.setOnClickListener{login(google)}
    }

    // 파라미터 값에 맞는 플랫폼으로 로그인 실행
    private fun login(platform : String){
        userViewModel.startLogin(this, platform)
        userViewModel.addUser(this, platform).observe(this){ user ->
            Log.i("MainActivity User", user.name+user.id)
        }
    }

    // 키보드가 열린 상태일 때
    // 키보드 외의 바깥 영역을 클릭하면 키보드 내려가게하는 메소드
    @SuppressLint("ClickableViewAccessibility")
    private fun clickedBackGround(){
        binding.loginRoot.setOnTouchListener(OnTouchListener { v, event ->
            val inputManager: InputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                this.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            false
        })
    }

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    private fun normalLogin(){
        binding.imgLogin.setOnTouchListener{ view, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    view.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_un_click))
                    true
                }
                MotionEvent.ACTION_UP -> {
                    view.setBackgroundColor( ContextCompat.getColor(this, R.color.btn_click))

                    var email = binding.etInputEmail.text.toString()
                    var password = binding.etInputPassword.text.toString()

                    if (email.replace(" ","") == ""){
                        Toast.makeText(this, "아이디를 입력해 주세요", Toast.LENGTH_SHORT).show()

                    }else if(password.replace(" ","") == ""){
                        Toast.makeText(this, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show()

                    }else{
                        startActivity(Intent(this, MainActivity::class.java))
                        binding.etInputEmail.text = Editable.Factory.getInstance().newEditable("")
                        binding.etInputPassword.text = Editable.Factory.getInstance().newEditable("")
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    }
                    true

                }
                else -> false
            }
        }
    }


    fun test(){
        UserApiClient.instance.unlink { throwable: Throwable? ->
            if (throwable != null) {
                Log.e("kakaoUnlink", "카카오 회원탈퇴 실패", throwable)
                Toast.makeText(this, "로그인을 한 상태에서 진행해주세요", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Log.i("kakaoUnlink", "카카오 회원탈퇴 성공")
                Toast.makeText(this, "회원탈퇴 성공", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }
}