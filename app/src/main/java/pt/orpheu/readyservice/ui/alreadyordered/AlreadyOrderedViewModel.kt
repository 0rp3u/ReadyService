package pt.orpheu.readyservice.ui.alreadyordered

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.orpheu.readyservice.model.*
import pt.orpheu.readyservice.repository.OrdersRepository
import javax.inject.Inject

class AlreadyOrderedViewModel @Inject constructor(private val ordersRepository: OrdersRepository) : ViewModel(){

    private val state: MutableLiveData<LoadingState> = MutableLiveData()

    private var alreadyOrdered = ordersRepository.getAlreadyOrderedLiveData()

    val listEmpty = MediatorLiveData<Boolean>()

    fun getLoadingStateLiveData(): LiveData<LoadingState> = state

    fun getAlreadyOrderedLiveData() = alreadyOrdered

    fun getCurrentOrderIsEmptyLiveData(): LiveData<Boolean> = listEmpty

    init{
        listEmpty.addSource(getAlreadyOrderedLiveData()) {
            listEmpty.postValue(it.isNullOrEmpty())
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

    fun  ordersPaid(){
        viewModelScope.launch {
            try {
                state.value = LOADING
                ordersRepository.emptyAllOrders()
                state.value = LOADED
            }catch (e:Exception){
                state.value = ERROR(e.message ?: "error: something went wrong")
            }
        }
    }

}