package kr.co.testapp0501.model.network

import kr.co.testapp0501.model.users.NormalUser
import kr.co.testapp0501.model.users.SocialUser
import kr.co.testapp0501.model.users.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // 일반로그인 http 통신
    @Headers("Content-Type: application/json")
    @POST("com/avad/api/member/join")
    fun addNormalUser(@Body normaluser: NormalUser): Call<UserResponse>

    @GET("/")
    fun getNormalUser(@Query("user") user: String): Call<NormalUser>


    // 소셜로그인 http 통신
    @POST("/")
    fun addUser(@Body user: SocialUser): Call<SocialUser>
    @GET("/")
    fun getUser(@Query("user") user: String?): Call<SocialUser>

}