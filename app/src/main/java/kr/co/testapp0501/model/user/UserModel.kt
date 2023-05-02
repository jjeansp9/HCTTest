package kr.co.testapp0501.model.user

import android.content.Context
import android.content.SharedPreferences

class UserModel constructor(val context: Context) {

    // sns Data 저장
    fun saveSnsData(type:String, id:String){
        context.getSharedPreferences("snsData", Context.MODE_PRIVATE).edit().apply{
            putString("type", type)
            putString("id", id)
            commit()
        }
    }
    // sns Data 불러오기
    fun loadSnsData(): SocialLogin{
        val pref: SharedPreferences = context.getSharedPreferences("snsData", Context.MODE_PRIVATE)
        var type: String = pref.getString("type", "") as String
        var id: String = pref.getString("id", "") as String // 저장된 데이터가 없으면 ""

        return SocialLogin(type, id)
    }

    // normal Data 저장
    fun saveNormalData(id:String, pw:String){
        context.getSharedPreferences("normalData", Context.MODE_PRIVATE).edit().apply{
            putString("id", id)
            putString("pw", pw)
            commit()
        }
    }

    // normal Data 불러오기
    fun loadNormalData(): NormalLogin{
        val pref: SharedPreferences = context.getSharedPreferences("normalData", Context.MODE_PRIVATE)
        var id: String = pref.getString("id", "") as String
        var pw: String = pref.getString("pw", "") as String // 저장된 데이터가 없으면 ""

        return NormalLogin(id, pw)
    }

}