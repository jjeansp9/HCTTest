package kr.co.testapp0501

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화
        KakaoSdk.init(this, "90ef9a5c7702c7ffbdfad2f666330477")
    }
}