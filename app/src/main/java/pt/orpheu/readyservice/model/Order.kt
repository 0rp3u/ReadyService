package pt.orpheu.readyservice.model

data class Order(
    val id: Long,
    val current: Boolean,
    val items: List<ItemOrder>
)


