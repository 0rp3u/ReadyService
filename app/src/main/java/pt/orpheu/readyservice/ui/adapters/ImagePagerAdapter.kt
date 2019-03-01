package pt.orpheu.readyservice.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.model.ItemOrder

class ImagePagerAdapter(
    context: Context,
    imagesUrls: List<String> = listOf(),
    val clickCallback: ()->Unit = {}
) : PagerAdapter() {
    var mLayoutInflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private var data = imagesUrls.toMutableList()

    fun addItems(items: List<String>){
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun clear(){
        data = mutableListOf()
        notifyDataSetChanged()
    }

    fun setData(items: List<String>){
        clear()
        addItems(items)
    }


    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.image_page, container, false)

        val imageView = itemView.findViewById(R.id.imageView) as ImageView

        Glide.with(imageView.context)
            .load(data[position])
            .apply(RequestOptions().placeholder(R.drawable.ic_main_food).centerCrop())
            .into(imageView)

        container.addView(itemView)
        imageView.setOnClickListener {
            clickCallback()
        }

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}