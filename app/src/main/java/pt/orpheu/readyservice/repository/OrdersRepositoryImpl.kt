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

    private val currentOrderMld : MediatorLiveData<Order> = MediatorLiveData()

    private val allOrderedItemsdMld : MediatorLiveData<List<ItemOrder>?> = MediatorLiveData()

    init {
        setupAllOrderedMediatorLiveData()
        setupCurrentOrderMediatorLiveData()
    }


    override fun getCurrentOrderLiveData() = currentOrderMld

    override fun getAlreadyOrderedLiveData() = allOrderedItemsdMld

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



    override suspend fun closeCurrentOrder() = withContext(Dispatchers.IO) {
        try {
            database.beginTransaction()

            orderDao.getCurrentOrder()?.let {
                orderDao.update(it.apply { it.current = false })
            }

            database.setTransactionSuccessful()

        }finally { database.endTransaction() }
    }

    override suspend fun emptyCurrentOrder() = withContext(Dispatchers.IO) {
        try {
            database.beginTransaction()

            orderDao.getCurrentOrder()?.let {
                orderDao.delete(it.id)
            }

            database.setTransactionSuccessful()

        }finally { database.endTransaction() }
    }

    override suspend fun emptyAllOrders() = withContext(Dispatchers.IO) {
        try {
            database.beginTransaction()

            database.clearAllTables() //we only save order Info so it's fine.

            database.setTransactionSuccessful()

        }finally { database.endTransaction() }
    }


    private fun setupCurrentOrderMediatorLiveData(){
        val orderingItems =  database.getOrderedItemsDao().gerCurrentOrderItemsLiveData()

        currentOrderMld.addSource(orderingItems) {
            // coroutine context needed for the non blocking api call,
            // liveData access has to be on the main thread
            GlobalScope.launch(Dispatchers.Main) {
                currentOrderMld.postValue(
                    if(!it.isNullOrEmpty()) {
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

    private fun setupAllOrderedMediatorLiveData(){
        val alreadyOrderedItems =  database.getOrderedItemsDao().getAlreadyOrderedItemsLiveData()

        allOrderedItemsdMld.addSource(alreadyOrderedItems) {
            // coroutine context needed for the non blocking api call,
            // liveData access has to be on the main thread
            GlobalScope.launch(Dispatchers.Main) {
                allOrderedItemsdMld.postValue(
                    if(!it.isNullOrEmpty()) {
                        it.fold(mutableListOf<ItemOrder>()) { acc, dbItem ->
                            (acc.find { it.item.id == dbItem.itemId }
                                ?: ItemOrder(0, apiService.getItemDetails(dbItem.itemId) ))
                                .apply { count += dbItem.itemCount }
                                .let { acc.remove(it); acc.add(it)}
                            acc
                        }
                    } else null
                )
            }
        }
    }
}