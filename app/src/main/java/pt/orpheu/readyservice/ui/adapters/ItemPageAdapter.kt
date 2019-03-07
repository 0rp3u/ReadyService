package pt.orpheu.readyservice.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.databinding.ListItemCardBinding
import pt.orpheu.readyservice.model.Item

class ItemPageAdapter internal constructor(
    val context: Context,
    val items: List<Item>,
    val clickCallback: (Item)->Unit,
    val longClickCallback: (Item)->Unit)
    : RecyclerView.Adapter<ItemVH>() {

    lateinit var dataBinding : ListItemCardBinding

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemVH {

        dataBinding = DataBindingUtil.inflate(
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.list_item_card,
            viewGroup,
            false
        )
        return ItemVH(dataBinding.root)
    }

    override fun onBindViewHolder(ItemVH: ItemVH, i: Int) {
        Glide.with(context)
            .load(items[i].img_urls.first())
            .apply(
                RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.ic_main_food)
            )
            .into(dataBinding.image)

        dataBinding.title.text = items[i].name
        dataBinding.description.text = items[i].description
        dataBinding.price.text =  context.getString(R.string.money_text, items[i].price)

        dataBinding.root.setOnClickListener {
            clickCallback(items[i])
        }

        dataBinding.root.setOnLongClickListener {
            longClickCallback(items[i])
            true
        }
    }
}

class ItemVH internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)