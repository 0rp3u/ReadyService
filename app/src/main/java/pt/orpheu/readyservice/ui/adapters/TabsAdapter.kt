package pt.orpheu.readyservice.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pt.orpheu.readyservice.model.Menu
import pt.orpheu.readyservice.ui.menupage.MenuFragment

class TabsAdapter(
    fm: FragmentManager,
    menus: List<Menu> = listOf()
) : FragmentPagerAdapter(fm) {


    private var data = menus.toMutableList()

    fun addItems(items: List<Menu>){
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun clear(){
        data = mutableListOf()
        notifyDataSetChanged()
    }

    fun setData(items: List<Menu>){
        clear()
        addItems(items)
    }

    override fun getItem(i: Int): Fragment {
        return MenuFragment.newInstance(data[i].id)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "${data[position].name}"
    }

    override fun getCount(): Int = data.size
}
