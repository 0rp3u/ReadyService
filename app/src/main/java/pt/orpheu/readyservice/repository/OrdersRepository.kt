package pt.orpheu.readyservice.repository

import androidx.lifecycle.LiveData
import pt.orpheu.readyservice.model.ItemOrder
import pt.orpheu.readyservice.model.Order

interface OrdersRepository {

    fun getCurrentOrderLiveData(): LiveData<Order?>


    suspend fun orderItem(itemOrder: ItemOrder)

    suspend fun emptyCurrentOrder()

}