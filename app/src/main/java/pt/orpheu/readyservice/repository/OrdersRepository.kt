package pt.orpheu.readyservice.repository

import androidx.lifecycle.LiveData
import pt.orpheu.readyservice.model.Item
import pt.orpheu.readyservice.model.ItemOrder
import pt.orpheu.readyservice.model.Order

interface OrdersRepository {

    fun getCurrentOrderLiveData(): LiveData<Order?>

    fun getAlreadyOrderedLiveData(): LiveData<List<ItemOrder>?>

    suspend fun getOrderItem(item: Item): ItemOrder?

    suspend fun deleteItem(itemId: Long)

    suspend fun updateItem(itemOrder: ItemOrder)

    suspend fun orderItem(itemOrder: ItemOrder)

    suspend fun closeCurrentOrder(): Unit

    suspend fun emptyCurrentOrder(): Unit

    suspend fun emptyAllOrders()
}