package kr.co.testapp0501.model.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import kr.co.testapp0501.User
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class UserRepository {
    private val apiService : ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

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

    fun addUser(token : String, platform: String) : LiveData<User>{
        Log.i("UserRepository Token", token)

        val userLiveData = MutableLiveData<User>()

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

                    val user = User(user.id, user.kakaoAccount!!.profile!!.nickname)
                    userLiveData.value = User(user.id, user.name)

                    Log.i("UserRepository addUser()", user.id.toString())

                    apiService.getUser("users").enqueue(object : retrofit2.Callback<User>{
                        override fun onResponse(call: Call<User>, response: Response<User>) {

                            val item = response.body()

                            if (item?.id != null){
                                return

                            }else{
                                registerUser(item)


                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                        }
                    })
                }
            }
        }


        return userLiveData
    }

    private fun registerUser(user: User?){

        val user = User(user?.id, user?.name)

        apiService.addUser(user).enqueue(object : retrofit2.Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {

                if (response.isSuccessful){
                    val item =response.body()
                    Log.i("UserRepository registerUser()", item?.id.toString())
                }else{
                    // Handle error
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("UserRepository Error", "${t.message}")
            }
        })
    }

}



























