package kr.co.testapp0501.view.activities

import android.annotation.SuppressLint
import android.content.ContentValues
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
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
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

    private val clientId = "2RREVK5H8Ovzs4Z8LijQ" // 네이버 로그인 식별 아이디
    private val clientSecret = "x8vBpMJJO8" // 네이버 로그인 식별 패스워드
    private val nidOAuthLogin = NidOAuthLogin()

    var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        Log.d("keyHash", " KeyHash :" + Utility.getKeyHash(this)) // 카카오 SDK용 키해시 값
        NaverIdLoginSDK.initialize(this, clientId, clientSecret, "Test") // 네이버 클라이언트 등록

        normalLogin()
        clickedBackGround()

        binding.kakaoLogin.setOnClickListener{kakaoLogin()}
        binding.naverLogin.setOnClickListener{}
        binding.googleLogin.setOnClickListener{}

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickedBackGround(){
        binding.loginRoot.setOnTouchListener(OnTouchListener { v, event ->
            hideKeyboard()
            false
        })
    }

    fun hideKeyboard() {
        val inputManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun kakaoLogin(){
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공! ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
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
}