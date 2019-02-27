package pt.orpheu.readyservice.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("hide")
    fun hide(view: View, hide: Boolean) {
        view.visibility = if(hide) View.GONE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter(value = ["placeholder", "image", "circle"], requireAll = false)
    fun image(imageView: ImageView, placeholder: Drawable?, image: String?, circle: Boolean?) {
        Glide.with(imageView.context)
            .load(image?: return)
            .apply(if(circle == true) RequestOptions.circleCropTransform() else RequestOptions()
                .placeholder(placeholder)
                .error(placeholder)
                .centerInside()
            )
            .into(imageView)
    }
}