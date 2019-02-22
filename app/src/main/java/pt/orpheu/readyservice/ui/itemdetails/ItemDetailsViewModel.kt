package pt.orpheu.readyservice.ui.itemdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.orpheu.readyservice.api.ApiService
import pt.orpheu.readyservice.model.*
import javax.inject.Inject

class ItemDetailsViewModel @Inject constructor(val apiService: ApiService) : ViewModel(){

    private val state: MutableLiveData<LoadingState> = MutableLiveData()

    val item : MutableLiveData<Item> = MutableLiveData()

    fun getLoadingStateLiveData(): LiveData<LoadingState> = state

    fun getMenusLiveData(): LiveData<Item> = item

    fun init(id: Int){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                state.value = LOADING

                //item.value = apiService.getMenuDetails(id)
                state.value = LOADED
            }catch (e:Exception){
                state.value = ERROR(e.message ?: "error: something went wrong")
            }
        }
    }

}