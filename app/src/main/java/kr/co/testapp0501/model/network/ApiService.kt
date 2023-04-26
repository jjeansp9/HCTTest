package kr.co.testapp0501.model.network

import kr.co.testapp0501.model.users.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // 일반 회원가입 ID 중복체크
    @GET("com/avad/api/member/check/{memberId}")
    fun checkId(@Path("memberId") memberId : CheckId): Call<UserResponse>

    // 일반 회원가입
    @Headers("Content-Type: application/json")
    @POST("com/avad/api/member/self")
    fun addNormalUser(@Body normaluser: NormalUser): Call<UserResponse>

    // 일반 로그인
    @Headers("Content-Type: application/json")
    @POST("com/avad/api/member/self/login")
    fun normalLogin(@Body user: NormalLogin): Call<UserResponse>

    // 소셜로 회원가입
    @POST("com/avad/api/member/sns")
    fun addSnsUser(@Body user: SocialUser): Call<UserResponse>

    // 소셜로 로그인
    @POST("com/avad/api/member/sns/login")
    fun snsLogin(@Query("user") user: String?): Call<UserResponse>


}