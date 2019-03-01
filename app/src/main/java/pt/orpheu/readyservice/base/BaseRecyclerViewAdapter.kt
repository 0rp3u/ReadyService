package pt.orpheu.readyservice.base

import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerViewAdapter<T, VH: RecyclerView.ViewHolder>(
    items: List<T> = listOf()
) : RecyclerView.Adapter<VH>() {

    private var data = items.toMutableList()

    override fun getItemCount(): Int = data.size

    fun addItems(items: List<T>){
        data.addAll(items)
        notifyItemRangeInserted(data.size - items.size, items.size)
    }

    fun clear(){
        data = mutableListOf()
        notifyDataSetChanged()
    }

    fun setData(items: List<T>){
        data = items.toMutableList()
        notifyDataSetChanged()
    }

    fun getData(position: Int) = data[position]
}


