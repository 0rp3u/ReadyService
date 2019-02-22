package pt.orpheu.readyservice.database


import androidx.room.Database
import androidx.room.RoomDatabase
import pt.orpheu.readyservice.database.dao.OrderDao
import pt.orpheu.readyservice.database.dao.OrderedItemsDao
import pt.orpheu.readyservice.database.entity.OrderEntity
import pt.orpheu.readyservice.database.entity.OrderedItemEntity

@Database(entities = [ OrderEntity::class, OrderedItemEntity::class], version = 1)
abstract class ReadyServiceDatabase : RoomDatabase() {

    abstract fun getOrderDao() : OrderDao

    abstract fun getOrderedItemsDao() : OrderedItemsDao
}