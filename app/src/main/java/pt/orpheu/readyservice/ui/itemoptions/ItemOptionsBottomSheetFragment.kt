package pt.orpheu.readyservice.ui.itemoptions

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.base.BaseFragment
import pt.orpheu.readyservice.databinding.BottomSheetItemOptionsBinding
import pt.orpheu.readyservice.model.Item
import javax.inject.Inject



class ItemOptionsBottomSheetFragment : BaseFragment<BottomSheetItemOptionsBinding, ItemOptionsViewModel>() {
    companion object {
        const val ITEM = "ITEM"

        fun newInstance(item: Item): ItemOptionsBottomSheetFragment {
            val instance = ItemOptionsBottomSheetFragment()
            val arguments = Bundle()
            arguments.putParcelable(ITEM, item)
            instance.arguments = arguments
            return instance
        }
    }


    val item: Item by lazy { arguments!!.getParcelable<Item>(ITEM)}


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun layoutToInflate() = R.layout.bottom_sheet_item_options

    override fun defineViewModel() = ViewModelProviders
        .of(this, viewModelFactory)
        .get(ItemOptionsViewModel::class.java)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.itemOptionsViewModel = viewModel
        dataBinding.item = item



        dataBinding.addToOrderBtn.setOnClickListener {
            viewModel.orderItem()

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