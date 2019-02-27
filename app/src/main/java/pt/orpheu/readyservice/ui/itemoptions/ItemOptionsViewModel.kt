package pt.orpheu.readyservice.ui.itemoptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.orpheu.readyservice.model.*
import pt.orpheu.readyservice.repository.OrdersRepository
import javax.inject.Inject

class ItemOptionsViewModel @Inject constructor(private val ordersRepository: OrdersRepository) : ViewModel(){
    private val state: MutableLiveData<LoadingState> = MutableLiveData()

    private lateinit var itemToOrder : Item

    private var initialCount = 0
    private var itemCount : Int = 1

    private val editOptionState: MutableLiveData<EditOptionState> = MutableLiveData()

    fun getEditOtionStateLiveData() : LiveData<EditOptionState> = editOptionState

    fun getLoadingStateLiveData(): LiveData<LoadingState> = state

    fun getCurrentCount() = itemCount



    fun init(item: Item){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                state.value = LOADING

                itemToOrder = item
                ordersRepository.getOrderItem(item)?.count?.let {
                    itemCount = it
                    initialCount = it
                }

                updateEditOptionsState(initialCount, itemCount)


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

                when(editOptionState.value){
                    EditOptionState.ADDING_NEW -> {
                        ordersRepository.orderItem(ItemOrder(itemCount, itemToOrder))
                    }

                    EditOptionState.UPDATING ->{
                        ordersRepository.updateItem(ItemOrder(itemCount, itemToOrder))
                    }

                    EditOptionState.REMOVING ->  {
                        ordersRepository.deleteItem(itemToOrder.id)
                    }
                    EditOptionState.NO_CHANGE -> {}
                    null ->{}
                }

                initialCount = itemCount
                editOptionState.postValue(EditOptionState.NO_CHANGE)

                state.value = LOADED
            }catch (e:Exception){
                state.value = ERROR(e.message ?: "error: something went wrong")
            }
        }
    }

    fun minusCount(){
        if(itemCount > 0){
            itemCount -= 1
            updateEditOptionsState(initialCount, itemCount)
        }
    }

    fun plusCount(){
        if(itemCount < 100) {
            itemCount += 1
            updateEditOptionsState(initialCount, itemCount)
        }
    }

    fun editedCount(count: CharSequence){
        val value = try{ "$count".toInt() } catch (e:Exception) { 0 }
        if(value  != itemCount) {
            itemCount = value
            updateEditOptionsState(initialCount, itemCount)
        }
    }

    private fun updateEditOptionsState(initialCount :Int, newCount :Int) {

        if (initialCount == newCount)               editOptionState.postValue(EditOptionState.NO_CHANGE)
        else if (initialCount == 0 && newCount > 0) editOptionState.postValue(EditOptionState.ADDING_NEW)
        else if (initialCount > 0 && newCount != 0) editOptionState.postValue(EditOptionState.UPDATING)
        else if (initialCount > 0 && newCount == 0) editOptionState.postValue(EditOptionState.REMOVING)

    }

}