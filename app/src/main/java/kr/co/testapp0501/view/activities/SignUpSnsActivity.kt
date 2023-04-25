package kr.co.testapp0501.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.ActivitySignUpSnsBinding
import kr.co.testapp0501.model.users.SocialUser

class SignUpSnsActivity : AppCompatActivity() {

    private val binding : ActivitySignUpSnsBinding by lazy { ActivitySignUpSnsBinding.inflate(layoutInflater) }

    private lateinit var userInfo : SocialUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userInfo = intent.getSerializableExtra("user") as SocialUser
        Log.i("SignUpSnsActivity", userInfo.snsType + userInfo.snsId)
    }
}