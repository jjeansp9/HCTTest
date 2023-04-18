package kr.co.testapp0501

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화
        KakaoSdk.init(this, "d476cd737ca181d3209deb2bb1fb6de9")
    }
}