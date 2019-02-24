package pt.orpheu.readyservice.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pt.orpheu.readyservice.ui.itemoptionsdialog.ItemOptionsDialog

@Module
abstract class DialogBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeItemOptionsDialog(): ItemOptionsDialog

}