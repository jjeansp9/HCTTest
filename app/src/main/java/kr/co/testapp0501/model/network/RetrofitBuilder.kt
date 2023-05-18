package kr.co.testapp0501.model.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitBuilder {

    companion object{

        fun getRetrofitInstanceFirst(): Retrofit? {
            val okHttpClient = OkHttpClient.Builder()
                .build()

            val builder = Retrofit.Builder()
            builder.baseUrl(ApiService.BASE_URL_FIRST)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            return builder.build()
        }

        fun getRetrofitInstanceSecond(): Retrofit? {
            val okHttpClient = OkHttpClient.Builder()
                .build()

            val builder = Retrofit.Builder()
            builder.baseUrl(ApiService.BASE_URL_SECOND)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            return builder.build()
        }
    }
}