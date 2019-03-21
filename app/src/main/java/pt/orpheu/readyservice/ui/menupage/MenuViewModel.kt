package pt.orpheu.readyservice.ui.menupage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.orpheu.readyservice.api.ApiService
import pt.orpheu.readyservice.model.*
import pt.orpheu.readyservice.repository.OrdersRepository
import javax.inject.Inject

class MenuViewModel @Inject constructor(val apiService: ApiService, val ordersRepository: OrdersRepository) : ViewModel(){

    private val state: MutableLiveData<LoadingState> = MutableLiveData()

    val submenus : MutableLiveData<MenuDetails> = MutableLiveData()

    fun getLoadingStateLiveData(): LiveData<LoadingState> = state

    fun getMenusLiveData(): LiveData<MenuDetails> = submenus

    fun init(id: Long){
        viewModelScope.launch {
            try {
                state.value = LOADING
                submenus.value = apiService.getMenuDetails(id)
                state.value = LOADED
            }catch (e:Exception){
                state.value = ERROR(e.message ?: "error: something went wrong")
            }
        }
    }

    fun orderProduct(item: Item){
        viewModelScope.launch {
            try {
                state.value = LOADING
                ordersRepository.orderItem(ItemOrder(1, item))
                state.value = LOADED
            }catch (e:Exception){
                state.value = ERROR(e.message ?: "error: something went wrong")
            }
        }
    }

}