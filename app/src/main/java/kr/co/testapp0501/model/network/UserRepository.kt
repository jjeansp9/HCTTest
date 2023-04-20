package kr.co.testapp0501.model.network

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kr.co.testapp0501.NormalUser
import kr.co.testapp0501.SocialUser
import kr.co.testapp0501.view.activities.LoginActivity
import kr.co.testapp0501.view.activities.MainActivity
import retrofit2.Call
import retrofit2.Response

class UserRepository {

//    fun addUser(id : Long?, name : String) : LiveData<User>{
//
//        Log.i("mm", id.toString()+name)
//
//        val userLiveData = MutableLiveData<User>()
//        val user = User(id, name)
//
//        apiService.addUser(user).enqueue(object : retrofit2.Callback<User> {
//
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//
//                if (response.isSuccessful){
//                    userLiveData.value = response.body()
//                }else{
//                    // Handle error
//                }
//            }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Log.e("UserRepository Error", "${t.message}")
//            }
//        })
//
//        return userLiveData
//    }

    fun addNormalUser(context: Context, normalUser: NormalUser) : LiveData<NormalUser>{
        val userLiveData = MutableLiveData<NormalUser>()
        Log.i("addNormalUser", "id: ${normalUser.id} , pw: ${normalUser.pw} , name: ${normalUser.name} , gender: ${normalUser.gender}")

        userLiveData.value = normalUser

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        apiService.getNormalUser("normaluser").enqueue(object : retrofit2.Callback<NormalUser> {
            override fun onResponse(call: Call<NormalUser>, response: Response<NormalUser>) {

                val item = response.body()

                if (item?.id != null) {
                    return
                } else {
                    apiService.addNormalUser(normalUser).enqueue(object : retrofit2.Callback<NormalUser> {
                        override fun onResponse(call: Call<NormalUser>, response: Response<NormalUser>) {

                            if (response.isSuccessful) {
                                val item = response.body()
                                Log.i("UserRepository registerUser()", item?.id.toString())
                            } else {
                                // Handle error
                            }
                        }
                        override fun onFailure(call: Call<NormalUser>, t: Throwable) {
                            Log.e("UserRepository Error", "${t.message}")
                        }
                    })

                    context.startActivity(Intent(context, MainActivity::class.java))
                    (context as LoginActivity).finish()
                }
            }
            override fun onFailure(call: Call<NormalUser>, t: Throwable) {
            }
        })
        return userLiveData
    }



    fun addUser(context: Context, token : String, platform: String) : LiveData<SocialUser>{
        Log.i("UserRepository Token", token)

        val userLiveData = MutableLiveData<SocialUser>()

        if (platform == "kakao"){
            UserApiClient.instance.me { user, throwable ->
                if (user != null) {
//                    Log.i("kakaoLogin", "카카오 고유ID : " + user.id)
//                    Log.i("kakaoLogin", "카카오 닉네임 : " + user.kakaoAccount!!.profile!!.nickname)
//                    Log.i("kakaoLogin", "카카오 이메일 : " + user.kakaoAccount!!.email)
//                    Log.i("kakaoLogin", "카카오 성별 : " + user.kakaoAccount!!.gender)
//                    Log.i("kakaoLogin", "카카오 연령대 : " + user.kakaoAccount!!.ageRange)
//                    Log.i(
//                        "kakaoLogin",
//                        "카카오 프로필사진 : " + user.kakaoAccount!!.profile!!.profileImageUrl
//                    )

                    val user = SocialUser(user.id.toString(), user.kakaoAccount!!.profile!!.nickname)
                    userLiveData.value = SocialUser(user.id, user.name)

                    Log.i("UserRepository addUser()", "kakao: " + user.id.toString())

                    context.startActivity(Intent(context, MainActivity::class.java))
                    (context as LoginActivity).finish()

                    // TODO 서버로 데이터보내기 테스트 해봐야 함
                    // 회원정보 서버로 보내기
                    registerUser(context, user)

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

                    val user = SocialUser(nidProfileResponse.profile!!.id!!, nidProfileResponse.profile!!.nickname)
                    userLiveData.value = SocialUser(user.id, user.name)

                    //Log.i("UserRepository addUser()", "naver: " + user.id.toString())

                    context.startActivity(Intent(context, MainActivity::class.java))
                    (context as LoginActivity).finish()

                    // TODO 서버로 데이터보내기 테스트 해봐야 함
                    // 회원정보 서버로 보내기
                    registerUser(context, user)
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

    // retrofit2 를 사용하여 http 통신
    private fun registerUser(context: Context,user: SocialUser?) {

        val apiService: ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

        apiService.getUser("user").enqueue(object : retrofit2.Callback<SocialUser> {
            override fun onResponse(call: Call<SocialUser>, response: Response<SocialUser>) {

                val item = response.body()

                if (item?.id != null) {
                    return
                } else {
                    apiService.addUser(user!!).enqueue(object : retrofit2.Callback<SocialUser> {
                        override fun onResponse(call: Call<SocialUser>, response: Response<SocialUser>) {

                            if (response.isSuccessful) {
                                val item = response.body()
                                Log.i("UserRepository registerUser()", item?.id.toString())
                            } else {
                                // Handle error
                            }
                        }
                        override fun onFailure(call: Call<SocialUser>, t: Throwable) {
                            Log.e("UserRepository Error", "${t.message}")
                        }
                    })

                    context.startActivity(Intent(context, MainActivity::class.java))
                    (context as LoginActivity).finish()
                }
            }
            override fun onFailure(call: Call<SocialUser>, t: Throwable) {
            }
        })

    }
}




























