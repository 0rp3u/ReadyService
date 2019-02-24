package pt.orpheu.readyservice.ui.itemoptionsdialog

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import pt.orpheu.readyservice.R
import javax.inject.Inject
import pt.orpheu.readyservice.databinding.DialogItemOptionsBinding
import pt.orpheu.readyservice.base.BaseDialog
import pt.orpheu.readyservice.model.Item


class ItemOptionsDialog : BaseDialog<DialogItemOptionsBinding, ItemOptionsViewModel>() {
    companion object {
        const val DIALOG_TAG = "ITEM_OPTIONS_DIALOG"
        const val ITEM = "ITEM"
        
        fun newInstance(item: Item): ItemOptionsDialog {
            val dialog = ItemOptionsDialog()
            val arguments = Bundle()
            arguments.putParcelable(ITEM, item)
            dialog.arguments = arguments
            return dialog
        }
    }


    val item: Item by lazy { arguments!!.getParcelable<Item>(ITEM)}


    override fun defineTitle() = item.name

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun layoutToInflate() = R.layout.dialog_item_options

    override fun defineViewModel() = ViewModelProviders
        .of(this, viewModelFactory)
        .get(ItemOptionsViewModel::class.java)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.itemOptionsViewModel = viewModel
        dataBinding.item = item

        viewModel.getCurrentCount().observe(this, Observer {
            dataBinding.count.setText("$it")
        })

        dataBinding.addToOrderBtn.setOnClickListener {
            viewModel.orderItem()
            dismiss()
        }

        viewModel.getCurrentCount().observe(this, Observer {
            dataBinding.addToOrderBtn.text = when {
                viewModel.isNewOrderItem && it == 0-> "Cancel"
                viewModel.isNewOrderItem && it > 0 -> "Add to order"
                viewModel.isNewOrderItem.not() && it == 0-> "Remove from order"
                viewModel.isNewOrderItem.not() && it > 0 -> "Update order"
                else -> dataBinding.addToOrderBtn.text
            }

            dataBinding.total.text = "${it.times(item.price)} $"
        })




        viewModel.init(item)
    }
}