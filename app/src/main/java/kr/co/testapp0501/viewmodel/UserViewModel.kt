package kr.co.testapp0501.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import kr.co.testapp0501.model.UserRepository
import kr.co.testapp0501.model.user.CheckId
import kr.co.testapp0501.model.user.NormalLogin
import kr.co.testapp0501.model.user.NormalUser
import kr.co.testapp0501.model.user.SocialUser

class UserViewModel : ViewModel() {

    private val userRepository = UserRepository()
    fun addSnsUser(context: Context, platform: String): LiveData<SocialUser>{
        return userRepository.addSnsUser(context, "", platform)
    }

    // 액티비티에서 전달받은 platform 문자열에 에 해당하는 메소드 동작
    fun startLogin(context: Context, platform : String){
        if (platform == "kakao"){
            kakaoLogin(context, platform)
        }else if (platform == "naver"){
            naverLogin(context)
        }else if (platform == "google"){
            //googleLogin()
        }
    }

    // 카카오 로그인 메소드
    private fun kakaoLogin(context: Context, platform: String){
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("UserViewModel kakao", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("UserViewModel kakao", "카카오계정으로 로그인 성공 ${token.accessToken}")
                userRepository.addSnsUser(context, token.accessToken, platform).toString()
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context = context)) {
            UserApiClient.instance.loginWithKakaoTalk(context = context) { token, error ->
                if (error != null) {
                    Log.e("UserViewModel kakao", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context = context, callback = callback)
                } else if (token != null) {
                    Log.i("UserViewModel kakao", "카카오톡으로 로그인 성공! ${token.accessToken}")
                    userRepository.addSnsUser(context, token.accessToken, platform).toString()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context = context, callback = callback)
        }
    }

    // TODO : 최초 앱 접속했을 때는 네이버 버튼 눌러서 회원가입. 이미 했다면 자동로그인 개선하기
    // 네이버 로그인 메소드
    private fun naverLogin(context: Context){

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                Log.i("naverLogin", "네이버 토큰 : " + NaverIdLoginSDK.getAccessToken())
                Log.i("naverLogin", "네이버 Refresh토큰 : " + NaverIdLoginSDK.getRefreshToken())
                Log.i("naverLogin", "네이버 만료시간 : " + NaverIdLoginSDK.getExpiresAt())
                Log.i("naverLogin", "네이버 토큰타입 : " + NaverIdLoginSDK.getTokenType())
                Log.i("naverLogin", "네이버 상태 : " + NaverIdLoginSDK.getState())

                userRepository.addSnsUser(context, NaverIdLoginSDK.getAccessToken().toString(), "naver")
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(context,"errorCode:$errorCode, errorDesc:$errorDescription",
                    Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(context, callback = oauthLoginCallback)
    }

    // 구글 로그인 메소드
    private fun googleLogin(context: Context){
//        val mGoogleSignInClient: GoogleSignInClient
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//
//        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
//
//        val signInIntent: Intent = mGoogleSignInClient!!.signInIntent
//        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == AppCompatActivity.RESULT_OK) {
//                val data: Intent? = result.data
//                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                userRepository.handleSignInResult(task)
//
//            }
//        }
//
//        resultLauncher.launch(signInIntent)
    }

    private val _text = MutableLiveData<String>()

    val text: LiveData<String>
        get() = _text

    private val _normalUser = MutableLiveData<NormalUser>()

    val normalUser: LiveData<NormalUser>
        get() = _normalUser

    private val _checkId = MutableLiveData<String>()

    fun checkId(checkId: CheckId): LiveData<String>{
        return userRepository.checkId(checkId)
    }

    // 액티비티에서 회원가입 et에 입력한 값을 얻어와 userRepository 로 넘기기
    fun addNormalUser(context: Context, normalUser: NormalUser): LiveData<Int>{
        return userRepository.addNormalUser(context, normalUser)
    }

    // 액티비티에서 로그인 et에 입력한 값을 얻어와 userRepository 로 넘기기
    fun normalLogin(context: Context, login: NormalLogin): LiveData<Int>{
        return userRepository.normalLogin(context, login)
    }



}