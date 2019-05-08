package pt.orpheu.readyservice.ui.menupage

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.base.BaseFragment
import pt.orpheu.readyservice.databinding.FragmentMenuBinding
import pt.orpheu.readyservice.model.*
import javax.inject.Inject
import android.content.Intent
import pt.orpheu.readyservice.ui.adapters.ItemPageAdapter
import pt.orpheu.readyservice.ui.itemdetails.ItemDetailsActivity
import pt.orpheu.readyservice.ui.itemoptions.ItemOptionsDialog


class MenuFragment : BaseFragment<FragmentMenuBinding, MenuViewModel>(){

    companion object {
        const val EXTRA_MENU_ID = "EXTRA_MENU_ID"

        fun newInstance(menuId: Long): Fragment {
            return MenuFragment()
                .apply { arguments = Bundle().apply { putLong("EXTRA_MENU_ID", menuId); } }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun layoutToInflate() = R.layout.fragment_menu

    override fun defineViewModel() = ViewModelProviders
        .of(this, viewModelFactory)
        .get(MenuViewModel::class.java)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getLoadingStateLiveData().observe(this, Observer { loadingState ->
            loadingState?.let {
                when(it){
                    is LOADED  -> hideLoading()
                    is LOADING -> showLoading()
                    is ERROR   -> hideLoading()
                }
            }
        })


        viewModel.init(arguments!!.getLong(EXTRA_MENU_ID))

        viewModel.getMenusLiveData().observe(this, Observer {
            dataBinding.myRecyclerView.adapter =
                ItemPageAdapter(
                    context!!,
                    it.subMenus.flatMap { it.items },
                    {
                        val intent = Intent(activity, ItemDetailsActivity::class.java)
                            .apply { putExtra(ItemDetailsActivity.EXTRA_ITEM, it) }
                        startActivity(intent)
                    },
                    {
                        val dialog = ItemOptionsDialog.newInstance(it)
                        dialog.show(requireFragmentManager(), ItemOptionsDialog.DIALOG_TAG)
                    }
                )
        })

    }

    private fun showLoading(){
        dataBinding.loading.root.visibility = View.VISIBLE
        dataBinding.loading.loadImg.startAnimation(
            AnimationUtils.loadAnimation(context, pt.orpheu.readyservice.R.anim.blink)
        )
    }

    private fun hideLoading() {
        dataBinding.loading.root.visibility = View.GONE
        dataBinding.loading.loadImg.clearAnimation()
    }

}
