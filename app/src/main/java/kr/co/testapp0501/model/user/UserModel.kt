package kr.co.testapp0501.model.user

import android.content.Context
import android.content.SharedPreferences

class UserModel constructor(val context: Context) {

    // login type 저장
    fun saveLoginType(type:String){
        context.getSharedPreferences("loginType", Context.MODE_PRIVATE).edit().apply{
            putString("type", type)
            apply()
        }
    }

    // login type 불러오기
    fun loadLoginType(): String{
        val pref: SharedPreferences = context.getSharedPreferences("loginType", Context.MODE_PRIVATE)
        var type: String = pref.getString("type", "default") as String

        return type
    }


    // sns id 저장
    fun saveSnsId(id:String){
        context.getSharedPreferences("snsId", Context.MODE_PRIVATE).edit().apply{
            putString("id", id)
            apply()
        }
    }

    // sns id 불러오기
    fun loadSnsId(): String{
        val pref: SharedPreferences = context.getSharedPreferences("snsId", Context.MODE_PRIVATE)
        var id: String = pref.getString("id", "default") as String

        return id
    }

    // normal Data 저장
    fun saveNormalData(id:String, pw:String){
        context.getSharedPreferences("normalData", Context.MODE_PRIVATE).edit().apply{
            putString("id", id)
            putString("pw", pw)
            apply()
        }
    }

    // normal Data 불러오기
    fun loadNormalData(): NormalLogin{
        val pref: SharedPreferences = context.getSharedPreferences("normalData", Context.MODE_PRIVATE)
        var id: String = pref.getString("id", "default") as String
        var pw: String = pref.getString("pw", "default") as String // 저장된 데이터가 없으면 ""

        return NormalLogin(id, pw)
    }

}