package pt.orpheu.readyservice.ui.alreadyordered

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.base.BaseFragment
import javax.inject.Inject
import pt.orpheu.readyservice.databinding.FragmentAlreadyOrderedDrawerBinding
import pt.orpheu.readyservice.ui.adapters.OrderItemsRecyclerViewAdapter
import cn.pedant.SweetAlert.SweetAlertDialog
import pt.orpheu.readyservice.ui.itemoptions.ItemOptionsDialog


class AlreadyOrderedFragment : BaseFragment<FragmentAlreadyOrderedDrawerBinding, AlreadyOrderedViewModel>() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun layoutToInflate() = R.layout.fragment_already_ordered_drawer

    override fun defineViewModel() = ViewModelProviders
        .of(this, viewModelFactory)
        .get(AlreadyOrderedViewModel::class.java)


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

            dialog.show(fragmentManager, ItemOptionsDialog.DIALOG_TAG)
        }

        recyclerView.adapter = currentOrderRecyclerViewAdapter

        viewModel.getAlreadyOrderedLiveData().observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty())
                currentOrderRecyclerViewAdapter.clear()
            else
                currentOrderRecyclerViewAdapter.setData(it)
        })

    }


    private fun setupOptions(){

        viewModel.getAlreadyOrderedLiveData().observe(viewLifecycleOwner, Observer {
            if(it?.isNotEmpty() !=null) {
                dataBinding.total.text =
                    getString(R.string.money_text, it.fold(0.0) { acc: Double, itemOrder ->
                        acc.plus(itemOrder.count.times(itemOrder.item.price))
                    })
            }
        })


        dataBinding.payBtn.setOnClickListener {
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("This will finalize your current order")
                .setConfirmText("Yes,finalize it!")
                .setConfirmClickListener { sDialog ->
                    viewModel.ordersPaid()
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