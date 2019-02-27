package pt.orpheu.readyservice.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pt.orpheu.readyservice.ui.alreadyordered.AlreadyOrderedFragment
import pt.orpheu.readyservice.ui.currentorder.CurrentOrderFragment
import pt.orpheu.readyservice.ui.itemoptions.ItemOptionsBottomSheetFragment
import pt.orpheu.readyservice.ui.menupage.MenuFragment

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMenuFragment(): MenuFragment

    @ContributesAndroidInjector
    abstract fun contributeCurrentOrderFragment(): CurrentOrderFragment

    @ContributesAndroidInjector
    abstract fun contributeAlreadyOrderedFragment(): AlreadyOrderedFragment

    @ContributesAndroidInjector
    abstract fun contributeItemOptionsBottomSheetFragment(): ItemOptionsBottomSheetFragment
}
