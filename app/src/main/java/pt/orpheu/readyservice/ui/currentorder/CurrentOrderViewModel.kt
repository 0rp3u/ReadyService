package pt.orpheu.readyservice.ui.currentorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.orpheu.readyservice.api.ApiService
import pt.orpheu.readyservice.model.*
import pt.orpheu.readyservice.repository.OrdersRepository
import javax.inject.Inject

class CurrentOrderViewModel @Inject constructor(private val ordersRepository: OrdersRepository) : ViewModel(){

    private val state: MutableLiveData<LoadingState> = MutableLiveData()

    private var currentOrder = ordersRepository.getCurrentOrderLiveData()

    val listEmpty = MediatorLiveData<Boolean>()

    fun getLoadingStateLiveData(): LiveData<LoadingState> = state

    fun getCurrentOrderLiveData() = currentOrder

    fun getCurrentOrderIsEmptyLiveData(): LiveData<Boolean> = listEmpty

    init{
        listEmpty.addSource(getCurrentOrderLiveData()) {
            listEmpty.postValue(it?.items.isNullOrEmpty())
        }
    }

    fun orderProduct(item: Item){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                state.value = LOADING
                ordersRepository.orderItem(ItemOrder(1, item))
                state.value = LOADED
            }catch (e:Exception){
                state.value = ERROR(e.message ?: "error: something went wrong")
            }
        }
    }

    fun emptyCurrentOrder(){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                state.value = LOADING
                ordersRepository.emptyCurrentOrder()
                state.value = LOADED
            }catch (e:Exception){
                state.value = ERROR(e.message ?: "error: something went wrong")
            }
        }
    }

}