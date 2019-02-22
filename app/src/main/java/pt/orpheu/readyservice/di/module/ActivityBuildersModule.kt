package pt.orpheu.readyservice.di.module


import dagger.Module
import dagger.android.ContributesAndroidInjector
import pt.orpheu.readyservice.ui.itemdetails.ItemDetailsActivity
import pt.orpheu.readyservice.ui.menupicker.MenuPickerActivity
import pt.orpheu.readyservice.ui.servicepicker.ServicePickerActivity

@Module
abstract class ActivityBuildersModule {

    //@ContributesAndroidInjector(modules = [ FragmentBuildersModule::class, DialogBuildersModule::class])
    @ContributesAndroidInjector
    abstract fun contributeServicePickerActivity(): ServicePickerActivity

    @ContributesAndroidInjector
    abstract fun contributeMenuPickerActivity(): MenuPickerActivity

    @ContributesAndroidInjector
    abstract fun contributeItemDetailsActivity(): ItemDetailsActivity

}