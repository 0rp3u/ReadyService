package pt.orpheu.readyservice.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<T : ViewDataBinding, VM : ViewModel> : DaggerAppCompatActivity() {

    companion object {
        const val DEFAULT_CONTAINER_ID = 0
    }

    protected val viewModel: VM by lazy { defineViewModel() }
    protected val dataBinding: T by lazy { DataBindingUtil.setContentView<T>(this, layoutToInflate())}

    @IdRes
    open fun containerId() = DEFAULT_CONTAINER_ID

    open fun initialFragment(): Fragment? = null

    @LayoutRes
    abstract fun layoutToInflate(): Int

    abstract fun defineViewModel() : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //dataBinding.lifecycleOwner = this

        if (savedInstanceState == null) {
            defineInitialFragment()
        }
    }

    private fun defineInitialFragment() {
        val initialFragment = initialFragment() ?: return

        if (containerId() != DEFAULT_CONTAINER_ID) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(containerId(), initialFragment)
                    .commitNow()
        }
    }

}