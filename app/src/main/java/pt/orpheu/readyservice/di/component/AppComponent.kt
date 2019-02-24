package pt.orpheu.readyservice.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import pt.orpheu.readyservice.ReadyServiceApp
import pt.orpheu.readyservice.di.module.*
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ActivityBuildersModule::class,
    FragmentBuildersModule::class,
    DialogBuildersModule::class,
    AndroidSupportInjectionModule::class,
    ApiModule::class,
    DatabaseModule::class,
    RepositoryModule::class,
    ViewModelModule::class
])
interface AppComponent : AndroidInjector<ReadyServiceApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(readyServiceApp: ReadyServiceApp)

}