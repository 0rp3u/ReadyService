package pt.orpheu.readyservice.repository

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
import pt.orpheu.readyservice.model.Item
import pt.orpheu.readyservice.model.ItemOrder
import pt.orpheu.readyservice.model.Order
import javax.inject.Inject

class OrdersRepositoryImpl
@Inject constructor(
    private val apiService: ApiService,
    private val database: ReadyServiceDatabase,
    private val orderedItemsDao: OrderedItemsDao,
    private val orderDao: OrderDao
): OrdersRepository {

    private val ld : MediatorLiveData<Order> = MediatorLiveData()

    init {
        val orderedItems =  database.getOrderedItemsDao().gerCurrentOrderItemsLiveData()
        ld.addSource(orderedItems) {
            // coroutine context needed for the non blocking api call,
            // liveData access has to be on the main thread
            GlobalScope.launch(Dispatchers.Main) {
                ld.postValue(
                    if(it.isNotEmpty()) {
                        Order(
                            it.first().orderId,
                            true,
                            it.map{ ItemOrder(it.itemCount, apiService.getItemDetails(it.itemId)) }
                        )
                    } else null
                )
            }
        }
    }


    override fun getCurrentOrderLiveData() = ld

    override suspend fun getOrderItem(item: Item) : ItemOrder? = withContext(Dispatchers.IO){
        return@withContext orderedItemsDao.getOrderItem(item.id)
            ?.let { ItemOrder(it.itemCount, item) }
    }


    override suspend fun deleteItem(itemId: Long) = withContext(Dispatchers.IO){
        try {
            database.beginTransaction()
            val itemOrderEntry = orderedItemsDao.getOrderItem(itemId)
            orderedItemsDao.delete(itemOrderEntry?.id ?: return@withContext)
            database.setTransactionSuccessful()

        }finally { database.endTransaction() }
    }

    override suspend fun updateItem(itemOrder: ItemOrder) = withContext(Dispatchers.IO){
        try {
            database.beginTransaction()

            val itemOrderEntry = orderedItemsDao.getOrderItem(itemOrder.item.id)
            itemOrderEntry?.itemCount = itemOrder.count
            orderedItemsDao.update(itemOrderEntry ?: return@withContext)

            database.setTransactionSuccessful()

        }finally { database.endTransaction() }
    }


    override suspend fun orderItem(itemOrder: ItemOrder) = withContext(Dispatchers.IO) {
        try {
            database.beginTransaction()

            val order = orderDao.getCurrentOrder()
                ?: OrderEntity(true).apply { orderDao.insert(this).let { this.id = it } }

            orderedItemsDao.insert(
                OrderedItemEntity(
                    itemOrder.item.id,
                    order.id,
                    itemOrder.count
                )
            )

            database.setTransactionSuccessful()

        }finally { database.endTransaction() }
    }

    override suspend fun emptyCurrentOrder() = withContext(Dispatchers.IO) {
        try {
            database.beginTransaction()

            orderDao.getCurrentOrder()?.let { orderDao.delete(it.id) }

            database.setTransactionSuccessful()

        }finally { database.endTransaction() }
    }

}