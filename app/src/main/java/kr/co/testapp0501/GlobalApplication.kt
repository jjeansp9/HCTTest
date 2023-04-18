package kr.co.testapp0501

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화
        KakaoSdk.init(this, "61ff44cdc04cde9c9d23991b91d0c014")
    }
}