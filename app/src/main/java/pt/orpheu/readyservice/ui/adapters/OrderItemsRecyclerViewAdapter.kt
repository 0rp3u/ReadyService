package pt.orpheu.readyservice.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.base.BaseRecyclerViewAdapter
import pt.orpheu.readyservice.databinding.ListOrderItemBinding
import pt.orpheu.readyservice.model.ItemOrder
import kotlin.coroutines.coroutineContext

class OrderItemsRecyclerViewAdapter(
    context: Context,
    itemOrders: List<ItemOrder> = listOf(),
    val clickCallback: (ItemOrder)->Unit
) : BaseRecyclerViewAdapter<ItemOrder, OrderItemViewHolder>(itemOrders) {

    private val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): OrderItemViewHolder {

        return OrderItemViewHolder(
            DataBindingUtil.inflate(
                inflator,
                R.layout.list_order_item,
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(orderItemViewHolder: OrderItemViewHolder, i: Int) {
        orderItemViewHolder.bind(getData(i), clickCallback)
    }
}
class OrderItemViewHolder(val itemBinding: ListOrderItemBinding) : RecyclerView.ViewHolder(itemBinding.root){


    fun bind(itemOrder: ItemOrder, listener: (ItemOrder) -> Unit) {
        Glide.with(itemView.context)
            .load(itemOrder.item.img_urls.first())
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_main_food)
            )
            .into(itemBinding.image)

        itemBinding.title.text = itemOrder.item.name
        itemBinding.count.text = "x${itemOrder.count}"
        itemBinding.price.text = itemView.context.getString(R.string.money_text, itemOrder.item.price)

        itemBinding.root.setOnClickListener {
            listener(itemOrder)
        }
    }
}




