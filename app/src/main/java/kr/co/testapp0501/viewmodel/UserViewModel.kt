package kr.co.testapp0501.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.testapp0501.User
import kr.co.testapp0501.model.network.ApiService
import kr.co.testapp0501.model.network.RetrofitBuilder
import kr.co.testapp0501.model.network.UserRepository

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()
    fun addUser(id : String, name : String): LiveData<User>{
        return userRepository.addUser(id, name)
    }

    fun getAccessToken(token : String) : String{
        return userRepository.getAccessToken(token).toString()
    }
}