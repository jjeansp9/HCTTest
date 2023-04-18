package kr.co.testapp0501.model.network

import kr.co.testapp0501.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/")
    fun addUser(@Body user: User): Call<User>

}