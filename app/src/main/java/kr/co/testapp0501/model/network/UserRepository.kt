package kr.co.testapp0501.model.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.testapp0501.User
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class UserRepository {
    private val apiService : ApiService = RetrofitBuilder.getRetrofitInstance()!!.create(ApiService::class.java)

    fun addUser(id : String, name : String) : LiveData<User>{

        Log.i("mm", id+name)

        val userLiveData = MutableLiveData<User>()
        val user = User(id, name)

        apiService.addUser(user).enqueue(object : retrofit2.Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {

                if (response.isSuccessful){
                    userLiveData.value = response.body()
                }else{
                    // Handle error
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("UserRepository Error", "${t.message}")
            }
        })

        return userLiveData
    }

    fun getAccessToken(token : String){
        Log.i("UserRepository Token", token)

    }

}



























