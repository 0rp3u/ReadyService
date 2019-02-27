package pt.orpheu.readyservice.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pt.orpheu.readyservice.di.ViewModelKey
import pt.orpheu.readyservice.di.factory.ViewModelFactory
import pt.orpheu.readyservice.ui.alreadyordered.AlreadyOrderedViewModel
import pt.orpheu.readyservice.ui.currentorder.CurrentOrderViewModel
import pt.orpheu.readyservice.ui.itemdetails.ItemDetailsViewModel
import pt.orpheu.readyservice.ui.itemoptions.ItemOptionsViewModel
import pt.orpheu.readyservice.ui.menupage.MenuViewModel
import pt.orpheu.readyservice.ui.menupicker.MenuPickerViewModel
import pt.orpheu.readyservice.ui.servicepicker.ServicePickerViewModel

@Module
abstract class ViewModelModule {


    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ServicePickerViewModel::class)
    abstract fun bindServicePickerViewModel(servicePickerViewModel: ServicePickerViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MenuPickerViewModel::class)
    abstract fun bindMenuPickerViewModel(menuPickerViewModel: MenuPickerViewModel) : ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MenuViewModel::class)
    abstract fun bindMenuViewModel(menuViewModel: MenuViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ItemDetailsViewModel::class)
    abstract fun bindItemDetailsViewModel(itemDetailsViewModel: ItemDetailsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CurrentOrderViewModel::class)
    abstract fun bindCurrentOrderViewModel(currentOrderViewModel: CurrentOrderViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlreadyOrderedViewModel::class)
    abstract fun bindAlreadyOrderedViewModel(alreadyOrderedViewModel: AlreadyOrderedViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ItemOptionsViewModel::class)
    abstract fun bindItemOptionsViewModel(itemOptionsViewModel: ItemOptionsViewModel) : ViewModel

}