package pt.orpheu.readyservice.ui.itemdetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.base.BaseActivity
import pt.orpheu.readyservice.databinding.ActivityItemDetailsBinding
import pt.orpheu.readyservice.model.Item
import pt.orpheu.readyservice.ui.adapters.ImagePagerAdapter
import pt.orpheu.readyservice.ui.itemoptions.ItemOptionsBottomSheetFragment
import javax.inject.Inject

class ItemDetailsActivity : BaseActivity<ActivityItemDetailsBinding, ItemDetailsViewModel>() {

    companion object {
        const val EXTRA_ITEM = "EXTRA_ITEM"

    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun layoutToInflate() = R.layout.activity_item_details

    override fun defineViewModel() = ViewModelProviders
        .of(this, viewModelFactory)
        .get(ItemDetailsViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent!!.extras!!.getParcelable<Item>(EXTRA_ITEM)

        dataBinding.imageViewpager.adapter = ImagePagerAdapter(this,  item.img_urls)
        dataBinding.description.text = item.description
        if (savedInstanceState == null) {
            setupBottomFragment(item)

        }

        BottomSheetBehavior.from(dataBinding.bottomSheetContainer).setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) {}

            override fun onStateChanged(p0: View, p1: Int) {

            }

        })
    }

    private fun setupBottomFragment(item: Item){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.bottom_sheet_container, ItemOptionsBottomSheetFragment.newInstance(item))
            .commitNow()

    }

}
