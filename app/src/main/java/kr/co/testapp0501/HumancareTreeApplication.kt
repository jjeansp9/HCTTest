package kr.co.testapp0501

import android.app.Application
import kr.co.testapp0501.common.di.AppDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HumancareTreeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(applicationContext)
            modules(AppDiModule)
        }
    }
}