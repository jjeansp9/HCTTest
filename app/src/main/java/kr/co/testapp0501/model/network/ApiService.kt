package kr.co.testapp0501.model.network

import kr.co.testapp0501.model.users.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("com/avad/api/member/checkId")
    fun checkId(@Body id : CheckId): Call<UserResponse>

    // 일반 회원가입
    @Headers("Content-Type: application/json")
    @POST("com/avad/api/member/join")
    fun addNormalUser(@Body normaluser: NormalUser): Call<UserResponse>

    // 일반 로그인
    @Headers("Content-Type: application/json")
    @POST("com/avad/api/member/login")
    fun normalLogin(@Body user: NormalLogin): Call<UserResponse>


    // 소셜로그인 http 통신
    @POST("/")
    fun addUser(@Body user: SocialUser): Call<SocialUser>
    @GET("/")
    fun getUser(@Query("user") user: String?): Call<SocialUser>

}