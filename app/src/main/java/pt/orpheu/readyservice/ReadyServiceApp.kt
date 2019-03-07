package pt.orpheu.readyservice

import android.os.StrictMode
import com.squareup.leakcanary.LeakCanary
import dagger.android.support.DaggerApplication
import pt.orpheu.readyservice.di.component.DaggerAppComponent


class ReadyServiceApp : DaggerApplication() {

    override fun applicationInjector() = DaggerAppComponent.builder().application(this).build()


    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            setupLeakCanary()
            enabledStrictMode()
        }
    }

    private fun setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

    private fun enabledStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .permitDiskReads()
                .penaltyLog()
                .penaltyDeath()
                .build())
    }

}