package pt.orpheu.readyservice

import dagger.android.support.DaggerApplication
import pt.orpheu.readyservice.di.component.DaggerAppComponent


class ReadyServiceApp : DaggerApplication() {

    override fun applicationInjector() = DaggerAppComponent.builder().application(this).build()

}