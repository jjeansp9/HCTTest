package kr.co.testapp0501.model

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import kr.co.testapp0501.model.user.*
import kr.co.testapp0501.view.activity.GroupActivity
import kr.co.testapp0501.view.activity.LoginActivity
import kr.co.testapp0501.view.activity.SignUpActivity
import kr.co.testapp0501.view.activity.SignUpSnsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    // 회원가입 할 때 아이디 중복체크
    fun checkId(checkId: CheckId) : LiveData<String>{
        val userLiveData = MutableLiveData<String>()

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        apiService.checkId(checkId).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.i("UserRepository checkId()", response.code().toString())
                userLiveData.value = response.code().toString()

                if (response.isSuccessful) {
                    val item = response.body()
                    Log.i("UserRepository checkId()", item?.msg.toString())
                } else {
                    // Handle error
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

            }
        })
        return userLiveData
    }

    // 일반 회원가입
    fun addNormalUser(context: Context, normalUser: NormalUser) : LiveData<Int>{
        val userLiveData = MutableLiveData<Int>()
        Log.i("addNormalUser", "id: ${normalUser.id} , pw: ${normalUser.pw} , name: ${normalUser.name} , gender: ${normalUser.sex}")
        var users = UserModel(context)

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        apiService.addNormalUser(normalUser).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.i("UserRepository addNormalUser()", ">>>>>>"+response.code())

                // Http status 201 : Success
                // Http status 409 : 아이디 중복
                // Http status 400 : 비밀번호 형식 등 확인
                // Http status 500 : 서버 내부오류
                userLiveData.value = response.code()

                if (response.isSuccessful) {
                    val item = response.body()
                    Log.i("UserRepository addNormalUser()", item?.msg.toString())

                    // 자동으로 로그인하기 위해 디바이스에 [id, pw] 저장
                    users.saveLoginType("defalut")
                    users.saveNormalData(normalUser.id, normalUser.pw)

                    (context as SignUpActivity).finish()
                    Toast.makeText(context, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle error
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("UserRepository Error", "${t.message}")
            }
        })
        return userLiveData
    }

    // 일반 로그인
    fun normalLogin(context: Context, login: NormalLogin) : LiveData<Int>{
        val userLiveData = MutableLiveData<Int>()

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        apiService.normalLogin(login).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.i("UserRepository normalLogin()", ">>>>>>"+response.code())
                userLiveData.value = response.code()
                if (response.isSuccessful) { // 일반로그인에 성공한 경우

                    Log.i("UserRepository normalLogin()", "message: "+ response.body()?.msg.toString() +"\n jwtToken: " + response.body()?.data?.jwtToken)

                    val intent = Intent(context, GroupActivity::class.java)
                    intent.putExtra("token", response.body()?.data?.jwtToken)
                    context.startActivity(intent)
                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()

                } else { // 일반로그인에 실패한 경우
                    userLiveData.value = null
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.i("UserRepository normalLogin()", ">>>>>>"+Log.getStackTraceString(t))

            }
        })
        return userLiveData
    }

    // 소셜로그인 [ 카카오, 네이버, 구글 ]
    fun addSnsUser(context: Context, token : String, platform: String) : LiveData<SocialUser>{
        Log.i("UserRepository Token", token)

        val userLiveData = MutableLiveData<SocialUser>()

        if (platform == "kakao"){
            UserApiClient.instance.me { user, throwable ->
                if (token != null || token != "") {
//                    Log.i("kakaoLogin", "카카오 고유ID : " + user.id)
//                    Log.i("kakaoLogin", "카카오 닉네임 : " + user.kakaoAccount!!.profile!!.nickname)
//                    Log.i("kakaoLogin", "카카오 이메일 : " + user.kakaoAccount!!.email)
//                    Log.i("kakaoLogin", "카카오 성별 : " + user.kakaoAccount!!.gender)
//                    Log.i("kakaoLogin", "카카오 연령대 : " + user.kakaoAccount!!.ageRange)

                    val snsType = platform
                    val snsId = user?.id.toString()

                    snsLogin(context,platform, snsId).observe(context){ it ->
                        when (it) {
                            200 -> { // 서버에 저장된 회원 정보가 있다면
                                snsLogin(context, platform, snsId)
                            }
                            404 -> { // 서버에 저장된 정보가 없다면

                                // sns로 로그인 사용자의 추가 정보를 얻기 위해, Intent 객체 생성 후 데이터 전달
                                val intent = Intent(context, SignUpSnsActivity::class.java)
                                intent.putExtra("snsType", snsType) // snsType [ kakao ]
                                intent.putExtra("snsId", snsId) // sns Id
                                context.startActivity(intent)
                            }
                            500 -> { // 서버 내부오류
                                Toast.makeText(context, "잠시 후 다시 시도해 주세요", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }else if(platform == "naver"){
            val nidOAuthLogin = NidOAuthLogin()
            nidOAuthLogin.callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(nidProfileResponse: NidProfileResponse) {
                    Log.i("naverInfo", "네이버 ID : " + nidProfileResponse.profile!!.id)
//                    Log.i("naverInfo", "네이버 닉네임 : " + nidProfileResponse.profile!!.nickname)
//                    Log.i("naverInfo", "네이버 프로필이미지 : " + nidProfileResponse.profile!!.profileImage)
//                    Log.i("naverInfo", "네이버 이메일 : " + nidProfileResponse.profile!!.email)
//                    Log.i("naverInfo", "네이버 성별 : " + nidProfileResponse.profile!!.gender)
//                    Log.i("naverInfo", "네이버 연령대 : " + nidProfileResponse.profile!!.age)
                    val snsId = nidProfileResponse.profile!!.id.toString()

                    snsLogin(context,platform, snsId).observe(context){ it ->
                        when (it) {
                            200 -> { // 서버에 저장된 회원 정보가 있다면
                                snsLogin(context, platform, snsId)
                            }
                            404 -> { // 서버에 저장된 정보가 없다면

                                val intent = Intent(context, SignUpSnsActivity::class.java)
                                intent.putExtra("snsType", platform) // snsType [ naver ]
                                intent.putExtra("snsId", snsId) // sns Id
                                context.startActivity(intent)
                            }
                            500 -> { // 서버 내부오류
                                Toast.makeText(context, "잠시 후 다시 시도해 주세요", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                override fun onFailure(i: Int, s: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Log.i("UserRepository naver error", "errorCode: $errorCode, errorDesc: $errorDescription")
                }
                override fun onError(i: Int, s: String) {
                    Toast.makeText(context, "onError", Toast.LENGTH_SHORT).show()
                }
            })
        }else if(platform == "google"){

        }

        return userLiveData
    }

    // 구글로그인 회원정보
//    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//            Log.i("googleID", (account.id)!!)
//            Log.i("googleAccount", account.account.toString())
//            Log.i("googleEmail", (account.email)!!)
//            Log.i("googleDisplayName", (account.displayName)!!)
//            Log.i("googleGivenName", (account.givenName)!!)
//            Log.i("googleFamilyName", (account.familyName)!!)
//            Log.i("googlePhotoUrl", account.photoUrl.toString())
//            Log.i("googleAccessToken", account?.idToken.toString())
//            Log.i("googleAccessToken", account.isExpired.toString())
//
//
//            Glide.with(this).load(account.photoUrl).into(binding.googleImage)
//            binding.googleName.text = account.displayName
//            Toast.makeText(this@MainActivity, "구글 로그인 완료", Toast.LENGTH_SHORT).show()
//
//            //updateUI(account);
//        } catch (e: ApiException) {
//            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
//            //updateUI(null);
//        }
//    }

    // sns 회원가입 통신
    fun snsRegisterUser(context: Context, user: SocialUser?) {

        var users = UserModel(context)

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        apiService.addSnsUser(user!!).enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.i("UserRepository snsSignUp()", ">>>>>>"+response.code())
                if (response.isSuccessful) {
                    Log.i("UserRepository snsSignUp()", response.body()?.msg.toString())

                    // 자동으로 로그인하기 위해 디바이스에 [type, id] 저장
                    users.saveLoginType(user.snsType)
                    users.saveSnsId(user.snsId)

                    val intent = Intent(context, GroupActivity::class.java)
                    intent.putExtra("token", response.body()?.data?.jwtToken)
                    context.startActivity(intent)

                    (context as SignUpSnsActivity).finish()

                    Toast.makeText(context, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle error
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("UserRepository Error", "${t.message}")
            }
        })
    }

    // sns 로그인
    fun snsLogin(context: Context, snsType: String, snsId: String) : LiveData<Int>{
        val idLiveData = MutableLiveData<Int>()
        val users = UserModel(context)
        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        Log.i("UserRepository snsSignIn()", snsId)

        apiService.snsLogin(snsId).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.i("UserRepository snsSignIn()", ">>>>>>"+response.code())
                idLiveData.value = response.code()
                if (response.isSuccessful){
                    Log.i("UserRepository snsSignIn()", response.body()?.msg.toString())

                    // 자동으로 로그인하기 위해 디바이스에 [type, id] 저장
                    users.saveLoginType(snsType)
                    users.saveSnsId(snsId)

                    val intent = Intent(context, GroupActivity::class.java)
                    intent.putExtra("token", response.body()?.data?.jwtToken)
                    context.startActivity(intent)

                    (context as LoginActivity).finish()
                }else{

                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            }

        })
        return idLiveData
    }
}

private fun <T> LiveData<T>.observe(context: Context, function: (T) -> Unit) {}





























