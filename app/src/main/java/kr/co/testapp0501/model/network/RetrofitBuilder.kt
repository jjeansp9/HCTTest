package kr.co.testapp0501.model.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {

    companion object{

        // Retrofit 객체를 만들어서 리턴해주는 기능메소드
        fun getRetrofitInstance(): Retrofit? {
            val builder = Retrofit.Builder()
            builder.baseUrl(BASE_URL)
            builder.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))

            return builder.build()
        }
    }
}