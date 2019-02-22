package pt.orpheu.readyservice.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @ColumnInfo(name = "current") var current: Boolean = true,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long = 0
)