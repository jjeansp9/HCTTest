package kr.co.testapp0501.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
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
import kr.co.testapp0501.Logger
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivityLoginBinding
import kr.co.testapp0501.model.user.NormalLogin
import kr.co.testapp0501.model.user.SocialLogin
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

    var users = UserModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 디바이스에 저장된 값 불러오기 [ 아래 코드 실행하면 자동로그인 되어 로그인화면 그냥 넘어감 ]
        val loadUserInfo: NormalLogin= users.loadNormalData()
        val loadSnsUserInfo: SocialLogin= users.loadSnsData()

//        Log.i("LoginActivity normal", loadUserInfo.id + ", " + loadUserInfo.pw)
//        Log.i("LoginActivity sns", loadSnsUserInfo.snsType + "," + loadSnsUserInfo.snsId)
//
//        // 디바이스에 저장된 ID값이 있다면 로그인 화면을 생략하고, 그룹 화면으로 이동
//        when {
//            loadUserInfo.id != "" && loadUserInfo.pw != "" -> { // 일반 회원가입을 이미 했다면 자동로그인 [ 로그인화면 넘어가기 ]
//                startActivity(Intent(this@LoginActivity, GroupActivity::class.java))
//                finish()
//            }
//            loadSnsUserInfo.snsType != "" && loadSnsUserInfo.snsId != "" -> { // 소셜 회원가입을 이미 했다면 자동로그인 [ 로그인화면 넘어가기 ]
//                startActivity(Intent(this@LoginActivity, GroupActivity::class.java))
//                finish()
//            }
//        }

        setContentView(binding.root)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        Log.d("keyHash", " KeyHash :" + Utility.getKeyHash(this)) // 카카오 SDK용 키해시 값
        NaverIdLoginSDK.initialize(this, clientId, clientSecret, "Test") // 네이버 클라이언트 등록


        clickedBackGround()

        // 일반 회원가입
        binding.layoutSignUp.setOnClickListener{startActivity(Intent(this, SignUpActivity::class.java))}

        normalLogin() // 일반 로그인
        binding.kakaoLogin.setOnClickListener{login(kakao)} // 카카오 로그인
        binding.naverLogin.setOnClickListener{login(naver)} // 네이버 로그인
        binding.googleLogin.setOnClickListener{login(google)} // 구글 로그인

    }

    // 파라미터 값에 맞는 플랫폼으로 로그인 실행
    private fun login(platform : String){
        userViewModel.startLogin(this, platform)
//        userViewModel.addSnsUser(this, platform).observe(this){ user ->
//            Log.i("MainActivity User", platform + ", " + user.snsId)
//        }
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
        binding.imgLogin.setOnTouchListener{ view, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    view.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_un_click))

                    true
                }
                MotionEvent.ACTION_UP -> {
                    view.setBackgroundColor( ContextCompat.getColor(this, R.color.btn_click))

                    var email = binding.etInputId.text.toString().trim()
                    var password = binding.etInputPw.text.toString().trim()

                    if (TextUtils.isEmpty(email)){
                        Toast.makeText(this, "아이디를 입력해 주세요", Toast.LENGTH_SHORT).show()

                    }else if(TextUtils.isEmpty(password)){
                        Toast.makeText(this, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show()

                    }else{
                        val id = binding.etInputId.text.toString().trim()
                        val pw = binding.etInputPw.text.toString().trim()
                        val login = NormalLogin(id, pw)

                        // 입력한 id, pw 값을 서버로 보내기
                        userViewModel.normalLogin(this, login).observe(this){ token ->
                            Log.i("LoginActivity Login", token+"")
                            if (token != null){

                                val intent = Intent(this, GroupActivity::class.java)
                                intent.putExtra("token", token)
                                startActivity(intent)

                                binding.etInputId.text = Editable.Factory.getInstance().newEditable("")
                                binding.etInputPw.text = Editable.Factory.getInstance().newEditable("")
                                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this, "입력하신 정보가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                            }
                        }

                        //startActivity(Intent(this, GroupActivity::class.java)) // 임시
                        //Toast.makeText(this, "입력하신 정보가 일치하지 않아도 임시로 화면 넘어가기", Toast.LENGTH_SHORT).show()
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