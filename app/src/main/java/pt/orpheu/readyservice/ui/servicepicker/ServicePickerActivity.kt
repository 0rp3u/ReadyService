package pt.orpheu.readyservice.ui.servicepicker

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import pt.orpheu.readyservice.R
import pt.orpheu.readyservice.databinding.ActivityServicePickerBinding
import pt.orpheu.readyservice.ui.menupicker.MenuPickerActivity
import pt.orpheu.readyservice.base.BaseActivity
import javax.inject.Inject
import android.view.animation.AnimationUtils



class ServicePickerActivity : BaseActivity<ActivityServicePickerBinding, ServicePickerViewModel>() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun layoutToInflate() = R.layout.activity_service_picker

    override fun defineViewModel() = ViewModelProviders
            .of(this, viewModelFactory)
            .get(ServicePickerViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_TransparentStatusTheme)
        super.onCreate(savedInstanceState)

        dataBinding.takeAway.startAnimation(
                AnimationUtils.loadAnimation(applicationContext, R.anim.slide_from_left)
        )

        dataBinding.restaurant.startAnimation(
                AnimationUtils.loadAnimation(applicationContext, R.anim.slide_from_right)
        )

        dataBinding.takeAwayImg.setOnClickListener {
            startActivity(Intent(this, MenuPickerActivity::class.java))
        }

        dataBinding.restaurantImg.setOnClickListener {
            startActivity(Intent(this, MenuPickerActivity::class.java))
        }
    }
}
