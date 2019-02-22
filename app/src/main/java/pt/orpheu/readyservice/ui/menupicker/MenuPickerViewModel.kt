package pt.orpheu.readyservice.ui.menupicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import pt.orpheu.readyservice.api.ApiService
import pt.orpheu.readyservice.database.dao.OrderedItemsDao
import pt.orpheu.readyservice.database.entity.OrderedItemEntity
import pt.orpheu.readyservice.model.*
import pt.orpheu.readyservice.repository.OrdersRepository
import javax.inject.Inject

class MenuPickerViewModel @Inject constructor(private val apiService: ApiService, private val ordersRepository: OrdersRepository) : ViewModel(){

    private val state: MutableLiveData<LoadingState> = MutableLiveData()

    private val menus: MutableLiveData<List<Menu>> = MutableLiveData()

    private var currentOrder = ordersRepository.getCurrentOrderLiveData()

    fun getMenusLiveData(): LiveData<List<Menu>> = menus

    fun getLoadingStateLiveData(): LiveData<LoadingState> = state

    fun getCurrentOrderLiveData() = currentOrder

    fun init(){
        GlobalScope.launch(Dispatchers.Main) {
            try{
                state.value = LOADING
                menus.value = apiService.getMenus()

                currentOrder = ordersRepository.getCurrentOrderLiveData()

                state.value = LOADED
            }catch (e:Exception){
                state.value = ERROR(e.message ?: "error: something went wrong")
            }
        }
    }


}