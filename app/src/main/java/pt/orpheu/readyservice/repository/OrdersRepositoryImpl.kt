package pt.orpheu.readyservice.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.orpheu.readyservice.api.ApiService
import pt.orpheu.readyservice.database.ReadyServiceDatabase
import pt.orpheu.readyservice.database.dao.OrderDao
import pt.orpheu.readyservice.database.dao.OrderedItemsDao
import pt.orpheu.readyservice.database.entity.OrderEntity
import pt.orpheu.readyservice.database.entity.OrderedItemEntity
import pt.orpheu.readyservice.model.ItemOrder
import pt.orpheu.readyservice.model.Order
import java.lang.Exception
import javax.inject.Inject

class OrdersRepositoryImpl
@Inject constructor(
    private val apiService: ApiService,
    private val database: ReadyServiceDatabase,
    private val orderedItemsDao: OrderedItemsDao,
    private val orderDao: OrderDao
): OrdersRepository {

    val ld : MediatorLiveData<Order> = MediatorLiveData()


    override fun getCurrentOrderLiveData(): LiveData<Order?>{

        val orderedItems =  database.getOrderedItemsDao().gerCurrentOrderItemsLiveData()
        ld.addSource(orderedItems) {
            GlobalScope.launch(Dispatchers.Main) {
                ld.postValue(
                    if(it.isNotEmpty()) {
                        Order(
                            it.first().orderId,
                            true,
                            it.map{
                                ItemOrder(
                                    it.itemCount,
                                    apiService.getItemDetails(it.itemId)
                                )
                            }
                        )
                    } else null
                )
            }
        }
        return ld
    }


    override suspend fun orderItem(itemOrder: ItemOrder) = withContext(Dispatchers.IO) {
        //database.beginTransaction()
        val order = orderDao.getCurrentOrder()
            ?: OrderEntity(true).apply { orderDao.insert(this).let { this.id = it }}

        val currentOrderedItems = orderedItemsDao.gerCurrentOrderItems()

        val itemOrderEntry = currentOrderedItems?.find { it.itemId == itemOrder.item.id }
        if(currentOrderedItems == null || itemOrderEntry == null) {
            orderedItemsDao.insert(
                OrderedItemEntity(
                    itemOrder.item.id,
                    order.id,
                    itemOrder.count
                )
            )
            //database.endTransaction()
            return@withContext
        }

        itemOrderEntry.apply {
            itemCount += itemOrder.count

        }

        orderedItemsDao.update(itemOrderEntry)

        //database.endTransaction()

    }

    override suspend fun emptyCurrentOrder() = withContext(Dispatchers.IO) {
        try {
            database.beginTransaction()

            val current = orderDao.getCurrentOrder()

            current?.let { orderDao.delete(it.id) }

            database.setTransactionSuccessful()
            database.endTransaction()

        } catch (e: Exception) {
            database.endTransaction()
        }
    }

}