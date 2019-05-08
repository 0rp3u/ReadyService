package pt.orpheu.readyservice.ui.currentorder

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.base.BaseFragment
import javax.inject.Inject
import pt.orpheu.readyservice.databinding.FragmentCurrentOrderDrawerBinding
import pt.orpheu.readyservice.ui.adapters.OrderItemsRecyclerViewAdapter
import cn.pedant.SweetAlert.SweetAlertDialog
import pt.orpheu.readyservice.ui.itemoptions.ItemOptionsDialog


class CurrentOrderFragment : BaseFragment<FragmentCurrentOrderDrawerBinding, CurrentOrderViewModel>() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun layoutToInflate() = R.layout.fragment_current_order_drawer

    override fun defineViewModel() = ViewModelProviders
        .of(this, viewModelFactory)
        .get(CurrentOrderViewModel::class.java)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.currentOrderViewModel = viewModel
        setupRecyclerView()

        setupOptions()

    }



    private fun setupRecyclerView() {
        val recyclerView = dataBinding.recyclerView
        val currentOrderRecyclerViewAdapter = OrderItemsRecyclerViewAdapter(requireContext()) {
            val dialog = ItemOptionsDialog.newInstance(it.item)

            dialog.show(requireFragmentManager(), ItemOptionsDialog.DIALOG_TAG)
        }

        recyclerView.adapter = currentOrderRecyclerViewAdapter

        viewModel.getCurrentOrderLiveData().observe(this, Observer {
            if(it?.items.isNullOrEmpty())
                currentOrderRecyclerViewAdapter.clear()
            else
                currentOrderRecyclerViewAdapter.setData(it!!.items)
        })

    }


    private fun setupOptions(){

        viewModel.getCurrentOrderLiveData().observe(this, Observer {
            if(it?.items?.isNotEmpty() !=null) {
                dataBinding.total.text =
                    getString(R.string.money_text, it.items.fold(0.0) { acc: Double, itemOrder ->
                        acc.plus(itemOrder.count.times(itemOrder.item.price))
                    })
            }
        })

        dataBinding.emptyBtn.setOnClickListener {

            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("This will empty your current order, you won't be able to recover it")
                .setConfirmText("Yes,empty it!")
                .setConfirmClickListener { sDialog ->
                    viewModel.emptyCurrentOrder()
                    sDialog.dismissWithAnimation()
                }
                .show()
        }

        dataBinding.orderBtn.setOnClickListener {
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("This will finalize your current order")
                .setConfirmText("Yes,finalize it!")
                .setConfirmClickListener { sDialog ->
                    viewModel.closeCurrentOrder()
                    sDialog
                        .setTitleText("finalized!")
                        .setContentText("Your current order has been finalized, we hope you enjoy it")
                        .setConfirmText("OK")
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                }
                .show()

        }

    }
}