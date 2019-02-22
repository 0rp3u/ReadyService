package pt.orpheu.readyservice.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerAppCompatDialogFragment


abstract class BaseDialog<T : ViewDataBinding, VM : ViewModel> : DaggerAppCompatDialogFragment() {

    protected val viewModel: VM by lazy { defineViewModel() }
    protected lateinit var dataBinding: T

    @LayoutRes
    abstract fun layoutToInflate(): Int

    abstract fun defineViewModel() : VM


    abstract fun defineTitle(): String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.setTitle(defineTitle())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dataBinding = DataBindingUtil.inflate(layoutInflater, layoutToInflate(), container, false)
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

}