package pt.orpheu.readyservice.extensions

import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

fun View.hide(@AnimRes animation_id: Int? = null){
    if (animation_id == null ){ visibility = View.GONE; return }
    if (visibility == View.GONE) return

    val anim = AnimationUtils.loadAnimation(this.context, animation_id)

    anim.setAnimationListener(object: Animation.AnimationListener{
        override fun onAnimationRepeat(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
            Log.d("Hide animation", "ended")
            visibility = View.GONE
        }

        override fun onAnimationStart(p0: Animation?) {
            Log.d("Hide animation", "started")
        }
    })

    anim.fillAfter = false
    startAnimation(anim)
}

fun View.show(@AnimRes animation_id: Int? = null){
    if (animation_id == null ){ visibility = View.VISIBLE; return }
    if (visibility == View.VISIBLE) return

    val anim = AnimationUtils.loadAnimation(this.context, animation_id)
    anim.setAnimationListener(object: Animation.AnimationListener{
        override fun onAnimationRepeat(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
            Log.d("Show animation", "ended")
            visibility = View.VISIBLE
        }

        override fun onAnimationStart(p0: Animation?) {
            Log.d("Show animation", "started")
        }
    })
    anim.fillAfter = false

    startAnimation(anim)
}