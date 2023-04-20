package kr.co.testapp0501.model.network

import kr.co.testapp0501.NormalUser
import kr.co.testapp0501.SocialUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // 일반로그인 http 통신
    @POST("/")
    fun addNormalUser(@Body user: NormalUser): Call<NormalUser>
    @GET("/")
    fun getNormalUser(@Query("user") user: String): Call<NormalUser>

    // 소셜로그인 http 통신
    @POST("/")
    fun addUser(@Body user: SocialUser): Call<SocialUser>
    @GET("/")
    fun getUser(@Query("user") user: String?): Call<SocialUser>

}