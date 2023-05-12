package kr.co.testapp0501.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import kr.co.testapp0501.databinding.ActivityLoginBinding
import kr.co.testapp0501.model.UserRepository
import kr.co.testapp0501.model.user.NormalLogin
import kr.co.testapp0501.model.user.UserModel
import kr.co.testapp0501.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {

    private val binding : ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var userViewModel: UserViewModel

    private val clientId = "3nhXN4DGA4kNKDzAwXzW" // 네이버 로그인 식별 아이디
    private val clientSecret = "M8AMLuZlf_" // 네이버 로그인 식별 패스워드
    private val nidOAuthLogin = NidOAuthLogin()

    private var mGoogleSignInClient: GoogleSignInClient? = null

    // 어떤버튼을 클릭해서 로그인했는지 구분하기 위한 변수
    private val kakao = "kakao"
    private val naver = "naver"
    private val google = "google"

    private val userRepository = UserRepository()

    private var users = UserModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("keyHash", " KeyHash :" + Utility.getKeyHash(this)) // 카카오 SDK용 키해시 값
        NaverIdLoginSDK.initialize(this, clientId, clientSecret, "Test") // 네이버 클라이언트 등록
        autoLogin() // 자동로그인
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setContentView(binding.root)
        clickedBackGround()

        // 일반 회원가입
        binding.tvSignUp.setOnClickListener{startActivity(Intent(this, SignUpActivity::class.java))}

        binding.imgLogin.setOnClickListener{normalLogin()} // 일반 로그인
        binding.kakaoLogin.setOnClickListener{login(kakao)} // 카카오 로그인
        binding.naverLogin.setOnClickListener{login(naver)} // 네이버 로그인
        binding.googleLogin.setOnClickListener{login(google)} // 구글 로그인


        Log.i("naverToken",NaverIdLoginSDK.getAccessToken().toString() +"," + NaverIdLoginSDK.getRefreshToken() + ", " + NaverIdLoginSDK.getExpiresAt())
    }

    // 카카오, 네이버, 일반 자동로그인
    private fun autoLogin(){

        val loadUserInfo: NormalLogin= users.loadNormalData()
        val loadSnsId=  users.loadSnsData()
        val loadLoginType=  users.loadLoginType()
        Log.i("login data", loadUserInfo.id + ", " + loadSnsId)

        // 토큰 존재여부 [ 없으면 회원가입 또는 로그인 진행 ]
        if (AuthApiClient.instance.hasToken()) { // 카카오 토큰 존재여부
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        //로그인 필요
                    }
                    else {
                        //기타 에러
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    userRepository.snsLogin(this, kakao, loadSnsId)
                    return@accessTokenInfo
                }
            }

        }else if (NaverIdLoginSDK.getRefreshToken()!= null){ // 네이버 토큰 존재여부
            userRepository.snsLogin(this, naver, loadSnsId)
            return


        }else if (loadUserInfo.id != "default" && loadUserInfo.pw != "default"){
            // 디바이스에 저장된 ID값이 있다면 로그인 화면을 생략하고, 그룹 화면으로 이동
            // 일반 회원가입을 이미 했다면 자동로그인 [ 로그인화면 넘어가기 ]
            val login = NormalLogin(loadUserInfo.id, loadUserInfo.pw, "")
            userViewModel.normalLogin(this, login).observe(this){
                if (it == 500){ // code 500: 서버 내부 오류
                    Toast.makeText(this, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }
            Log.i("LoginActivity Login", "id: $loadUserInfo.id, pw: $loadUserInfo.pw")
            return
        }else{

        }
    }

    // 파라미터 값에 맞는 플랫폼으로 로그인 실행
    private fun login(platform : String){
        userViewModel.startLogin(this, platform)
    }

    // 키보드가 열린 상태일 때
    // 키보드 외의 바깥 영역을 클릭하면 키보드 내려가게하는 메소드
    @SuppressLint("ClickableViewAccessibility")
    private fun clickedBackGround(){
        binding.loginRoot.setOnTouchListener(OnTouchListener { v, event ->

            if(v.hasFocus()){
                val inputManager: InputMethodManager =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    this.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                false

            }else{ true }
        })
    }

    // 일반회원 로그인
    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    private fun normalLogin(){
        var id = binding.etInputId.text.toString().trim()
        var password = binding.etInputPw.text.toString().trim()

        if (TextUtils.isEmpty(id)){
            Toast.makeText(this, "아이디를 입력해 주세요", Toast.LENGTH_SHORT).show()

        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show()

        }else{
            val id = binding.etInputId.text.toString().trim()
            val pw = binding.etInputPw.text.toString().trim()
            val login = NormalLogin(id, pw, "")

            // 입력한 id, pw 값을 서버로 보내기
            userViewModel.normalLogin(this, login).observe(this){
                if (it == 500){ // code 500: 서버 내부 오류
                    Toast.makeText(this, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                }
            }

            //startActivity(Intent(this, GroupActivity::class.java)) // 임시
            //Toast.makeText(this, "입력하신 정보가 일치하지 않아도 임시로 화면 넘어가기", Toast.LENGTH_SHORT).show()
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