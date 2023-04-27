package kr.co.testapp0501.model.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitBuilder {

    companion object{

        fun getRetrofitInstance(): Retrofit? {
            val builder = Retrofit.Builder()
            builder.baseUrl(ApiService.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            return builder.build()
        }
    }
}