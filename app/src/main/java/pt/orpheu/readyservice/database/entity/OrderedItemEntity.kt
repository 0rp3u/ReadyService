package pt.orpheu.readyservice.database.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "ordered_items",
    foreignKeys= [ForeignKey(
        entity = OrderEntity::class,
        parentColumns = ["id"],
        childColumns = ["order_id"],
        onDelete = CASCADE)],
    indices= [Index(value=["order_id"])]
)
data class OrderedItemEntity(

    @ColumnInfo(name = "item_id")
    var itemId: Long,

    @ColumnInfo(name = "order_id")
    var orderId: Long,

    @ColumnInfo(name = "item_count")
    var itemCount: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
)