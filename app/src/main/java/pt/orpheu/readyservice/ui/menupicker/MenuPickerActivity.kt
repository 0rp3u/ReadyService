package pt.orpheu.readyservice.ui.menupicker

import android.os.Bundle
import android.view.Gravity
import pt.orpheu.readyservice.R
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import pt.orpheu.readyservice.base.BaseActivity
import pt.orpheu.readyservice.databinding.ActivityMenuPickerBinding
import javax.inject.Inject
import androidx.viewpager.widget.ViewPager
import com.dekoservidoni.omfm.OneMoreFabMenu
import pt.orpheu.readyservice.model.*
import pt.orpheu.readyservice.ui.adapters.ImagePagerAdapter
import pt.orpheu.readyservice.ui.adapters.OrderItemsRecyclerViewAdapter
import pt.orpheu.readyservice.ui.adapters.TabsAdapter


class MenuPickerActivity : BaseActivity<ActivityMenuPickerBinding, MenuPickerViewModel>(), OneMoreFabMenu.OptionsClick  {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun layoutToInflate() = R.layout.activity_menu_picker

    override fun defineViewModel() = ViewModelProviders
            .of(this, viewModelFactory)
            .get(MenuPickerViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getLoadingStateLiveData().observe(this, Observer { loadingState ->
            loadingState?.let {
                when(it){
                    is LOADED -> hideLoading()
                    is LOADING -> showLoading()
                    is ERROR -> hideLoading()
                }
            }
        })

        initPagers()

        dataBinding.fab.setOptionsClick(this)

        viewModel.init()
    }


    override fun onOptionClick(optionId: Int?) {
        when(optionId) {
            R.id.option1 -> dataBinding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.option2 -> dataBinding.drawerLayout.openDrawer(GravityCompat.END)
            R.id.option3 -> Toast.makeText(this,"Requesting staff, please wait",Toast.LENGTH_SHORT).show()
        }
    }



    private fun showLoading(){
        dataBinding.loading.root.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.blink)
        dataBinding.loading.loadImg.animation = anim
        dataBinding.loading.loadImg.startAnimation(anim)
    }

    private fun hideLoading(){
        dataBinding.loading.root.visibility = View.GONE
        dataBinding.loading.loadImg.clearAnimation()
    }

    private fun initPagers(){
        val imageViewPager = dataBinding.topImageViewpager
        val tabLayout = dataBinding.tabs
        val menuPager = dataBinding.viewpager

        imageViewPager.offscreenPageLimit = 4
        menuPager.offscreenPageLimit = 4


        menuPager.adapter = TabsAdapter(supportFragmentManager)
        imageViewPager.adapter = ImagePagerAdapter(this)
        tabLayout.setupWithViewPager(menuPager)



        /* Synchronize with menu pager  */
        imageViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            private var mScrollState = ViewPager.SCROLL_STATE_IDLE

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) { return }
                menuPager.scrollTo(imageViewPager.scrollX, menuPager.scrollY)

                //since tablayout is setup with menupager, we have to synchronize the scroll position manually
                tabLayout.setScrollPosition(position, positionOffset, true)
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {
                mScrollState = state
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    menuPager.setCurrentItem(imageViewPager.currentItem, false)
                }
            }
        })


        /* Synchronize with image pager  */
        menuPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            private var mScrollState = ViewPager.SCROLL_STATE_IDLE

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) { return }
                imageViewPager.scrollTo(menuPager.scrollX, imageViewPager.scrollY)
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {
                mScrollState = state
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    imageViewPager.setCurrentItem(menuPager.currentItem, false)
                }
            }
        })

        viewModel.getMenusLiveData().observe(this, Observer{
            (menuPager.adapter as TabsAdapter).setData(it)
            (imageViewPager.adapter as ImagePagerAdapter).setData( it.map { it.img_url })
        })


    }
}