package kr.co.testapp0501;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 카카오 개발자사이트에서 [ 내 애플리케이션 ] 탭에 들어가면 나의 네이티브앱 키 를 확인할 수 있음
        KakaoSdk.init(this, "61ff44cdc04cde9c9d23991b91d0c014");
    }
}
