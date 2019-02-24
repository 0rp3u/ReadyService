package pt.orpheu.readyservice.ui.itemoptionsdialog

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

class ItemOptionsViewModel @Inject constructor(private val ordersRepository: OrdersRepository) : ViewModel(){
    private val state: MutableLiveData<LoadingState> = MutableLiveData()

    var isNewOrderItem : Boolean = false

    private lateinit var itemToOrder : Item

    private val itemCount :MutableLiveData<Int> = MutableLiveData(1)

    fun getCurrentCount() : LiveData<Int> = itemCount

    fun getLoadingStateLiveData(): LiveData<LoadingState> = state



    fun init(item: Item){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                state.value = LOADING

                itemToOrder = item
                val dbOrderItem = ordersRepository.getOrderItem(item)
                isNewOrderItem = dbOrderItem == null
                itemCount.value = dbOrderItem?.count ?: 1

                state.value = LOADED
            }catch (e:Exception){
                state.value = ERROR(e.message ?: "error: something went wrong")
            }
        }
    }

    fun orderItem(){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                state.value = LOADING

                when {
                    isNewOrderItem && itemCount.value!! > 0 -> ordersRepository.orderItem(ItemOrder(itemCount.value ?: 1, itemToOrder))
                    itemCount.value!! > 0 -> ordersRepository.updateItem(ItemOrder(itemCount.value ?: 1, itemToOrder))
                    !isNewOrderItem && itemCount.value!! == 0 -> ordersRepository.deleteItem(itemToOrder.id)
                }

                state.value = LOADED
            }catch (e:Exception){
                state.value = ERROR(e.message ?: "error: something went wrong")
            }
        }
    }

    fun minusCount(){
        itemCount.postValue((itemCount.value!! - 1).let { if(it >= 0) it else 0 })
    }

    fun plusCount(){
        itemCount.postValue((itemCount.value!! + 1).let { if(it <100) it else 100 })
    }

    fun editedCount(count: CharSequence){
        val value = try{ "$count".toInt() } catch (e:Exception) { 0 }
        if(value  != itemCount.value)
            itemCount.postValue(value)
    }
}