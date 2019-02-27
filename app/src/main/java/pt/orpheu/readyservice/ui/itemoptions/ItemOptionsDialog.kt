package pt.orpheu.readyservice.ui.itemoptions

import android.os.Bundle
import androidx.fragment.app.DialogFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.animatedDialogTheme)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.itemOptionsViewModel = viewModel
        dataBinding.item = item

        dataBinding.close.setOnClickListener{ dismiss() }


        dataBinding.addToOrderBtn.setOnClickListener {
            viewModel.orderItem()
            dismiss()
        }

        viewModel.getEditOtionStateLiveData().observe(this, Observer { state->
            dataBinding.addToOrderBtn.text = when(state) {
                EditOptionState.NO_CHANGE  -> "Close"
                EditOptionState.ADDING_NEW -> "Add to order"
                EditOptionState.REMOVING   -> "Remove from order"
                EditOptionState.UPDATING   -> "Update order"
                else -> dataBinding.addToOrderBtn.text
            }
            viewModel.getCurrentCount().let {
                dataBinding.total.text = getString(R.string.money_text, it.times(item.price))
                val text = "$it"
                dataBinding.count.setText(text)
                dataBinding.count.setSelection(text.length)
            }
        })


        viewModel.init(item)
    }
}