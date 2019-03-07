package pt.orpheu.readyservice.ui.itemdetails

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.*
import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.base.BaseActivity
import pt.orpheu.readyservice.databinding.ActivityItemDetailsBinding
import pt.orpheu.readyservice.extensions.hide
import pt.orpheu.readyservice.extensions.show
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

    private val landscape by lazy{ resources.configuration.orientation ==  Configuration.ORIENTATION_LANDSCAPE }

    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(dataBinding.bottomSheetContainer) }

    private var showingDetails = false


    override fun layoutToInflate() = R.layout.activity_item_details

    override fun defineViewModel() = ViewModelProviders
        .of(this, viewModelFactory)
        .get(ItemDetailsViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val item: Item = intent?.extras?.getParcelable(EXTRA_ITEM) ?: return

        setupAppBar()

        if (savedInstanceState == null) setupBottomSheetFragment(item)

        setupViewImagePager(item)

        setupFAB()

        hideDetails()

        dataBinding.description.text = item.description



        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) {}
            override fun onStateChanged(p0: View, state: Int) { onBottomSheetStateChanged(state) }
        })

    }

    private fun showDetails(){
        if(!landscape) return
        showingDetails = true
        if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        dataBinding.toolbar.show(R.anim.slide_in_top)
        dataBinding.description.show(R.anim.fade_in)
        dataBinding.floatingActionButton.hide()
    }

    private fun hideDetails(){
        if(!landscape) return
        showingDetails = false
        if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        dataBinding.toolbar.hide(R.anim.slide_out_top)
        dataBinding.description.hide(R.anim.fade_out)
        dataBinding.floatingActionButton.show()
    }

    private fun switchToLandscape(){
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun switchToPortrait(){
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }


    private fun setupViewImagePager(item: Item){
        dataBinding.imageViewpager.adapter = ImagePagerAdapter(this,  item.img_urls){
            Log.d("view pager o click", "${bottomSheetBehavior.state}")
            when(bottomSheetBehavior.state){
                BottomSheetBehavior.STATE_HIDDEN -> showDetails()
                else -> hideDetails()
            }
        }

    }

    private fun setupFAB(){
        dataBinding.floatingActionButton.apply { if(landscape) show() else hide() }
        dataBinding.floatingActionButton.setOnClickListener { showDetails() }
    }

    private fun setupAppBar(){
        dataBinding.backBtn.setOnClickListener { onBackPressed() }
        dataBinding.fullscreenSwitchBtn.setOnClickListener { if(landscape) switchToPortrait() else switchToLandscape() }
    }


    private fun setupBottomSheetFragment(item: Item){
        val fragment = ItemOptionsBottomSheetFragment.newInstance(item)
        supportFragmentManager.transaction(allowStateLoss = true) {
            replace(R.id.bottom_sheet_container, fragment)
        }
    }

    private fun onBottomSheetStateChanged(state: Int){
        (supportFragmentManager.findFragmentById(R.id.bottom_sheet_container) as ItemOptionsBottomSheetFragment)
        .onBottomSheetStateChanged(state)

        if(state == BottomSheetBehavior.STATE_HIDDEN && showingDetails) hideDetails()

    }

    fun closeBottomSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}
