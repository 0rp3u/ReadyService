package pt.orpheu.readyservice.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pt.orpheu.readyservice.ui.currentorder.CurrentOrderFragment
import pt.orpheu.readyservice.ui.menupage.MenuFragment

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMenuFragment(): MenuFragment

    @ContributesAndroidInjector
    abstract fun contributeCurrentOrderFragment(): CurrentOrderFragment

}