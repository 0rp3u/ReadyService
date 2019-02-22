package pt.orpheu.readyservice.ui.itemdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.base.BaseActivity
import pt.orpheu.readyservice.databinding.ActivityItemDetailsBinding
import pt.orpheu.readyservice.model.Item
import pt.orpheu.readyservice.ui.adapters.ImagePagerAdapter
import pt.orpheu.readyservice.ui.menupage.MenuFragment
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

    }
}
