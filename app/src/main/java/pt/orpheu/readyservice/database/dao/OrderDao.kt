package pt.orpheu.readyservice.database.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import pt.orpheu.readyservice.database.entity.OrderEntity

@Dao
interface OrderDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(order: OrderEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(message: List<OrderEntity>)

    @Update(onConflict = REPLACE)
    suspend fun update(order: OrderEntity)

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT orders.* FROM orders WHERE current = 1")
    suspend fun getCurrentOrder(): OrderEntity?

}