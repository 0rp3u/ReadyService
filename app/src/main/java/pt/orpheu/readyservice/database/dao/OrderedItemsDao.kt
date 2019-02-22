package pt.orpheu.readyservice.database.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import pt.orpheu.readyservice.database.entity.OrderedItemEntity

@Dao
interface OrderedItemsDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(order: OrderedItemEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(order: List<OrderedItemEntity>)

    @Update(onConflict = REPLACE)
    suspend fun update(order: OrderedItemEntity)

    @Query("DELETE FROM ordered_items WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT ordered_items.* FROM ordered_items, orders WHERE orders.current = 1 and ordered_items.order_id = orders.id")
    suspend fun gerCurrentOrderItems(): List<OrderedItemEntity>?

    @Query("SELECT ordered_items.*  FROM ordered_items, orders WHERE orders.current = 1 and ordered_items.order_id = orders.id")
    fun gerCurrentOrderItemsLiveData(): LiveData<List<OrderedItemEntity>>

    @Query("SELECT ordered_items.* FROM ordered_items, orders WHERE orders.current = 0 and ordered_items.order_id = orders.id")
    suspend fun gerAlreadyOrderedItems(): List<OrderedItemEntity>?

}