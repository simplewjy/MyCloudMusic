package com.simple.mycloudmusic.module.home.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.simple.mycloudmusic.R
import com.simple.mycloudmusic.module.base.BaseActivity
import com.simple.mycloudmusic.module.home.fragment.BlankFragment
import com.simple.mycloudmusic.module.utils.beanCopy
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Description app的主页面
 * Created by wjy
 * On 2021/5/13
 */
class HomeActivity : BaseActivity() {

    private val mPageAdapter by lazy {
        ViewPagerStateAdapter(
            this
        )
    }

    private val a = "a"
    private val b = "a"
    private val c = "a"
    val d = "a"

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        this.javaClass.beanCopy()
        initTab()

    }

    /**
     * 初始化底部菜单栏
     */
    private fun initTab() {
        //初始化tab
        val tabFound = tab_home.newTab()
        tabFound.setIcon(R.drawable.discovery_unselect)
        tabFound.text = resources.getString(R.string.found)
        tabFound.view.tag = resources.getString(R.string.found)
        val tabSprinkler = tab_home.newTab()
        tabSprinkler.setIcon(R.mipmap.icon_cloud_music)
        tabSprinkler.text = resources.getString(R.string.sprinkler)
        tabSprinkler.view.tag = resources.getString(R.string.sprinkler)
        val tabMine = tab_home.newTab()
        tabMine.setIcon(R.mipmap.icon_cloud_music)
        tabMine.text = resources.getString(R.string.mine)
        tabMine.view.tag = resources.getString(R.string.mine)
        val tabKaraoke = tab_home.newTab()
        tabKaraoke.setIcon(R.mipmap.icon_cloud_music)
        tabKaraoke.text = resources.getString(R.string.karaoke)
        tabKaraoke.view.tag = resources.getString(R.string.karaoke)
        val tabCloudVillage = tab_home.newTab()
        tabCloudVillage.setIcon(R.mipmap.icon_cloud_music)
        tabCloudVillage.text = resources.getString(R.string.cloud_village)
        tabCloudVillage.view.tag = resources.getString(R.string.cloud_village)

        tab_home.addTab(tabFound)
        tab_home.addTab(tabSprinkler)
        tab_home.addTab(tabMine)
        tab_home.addTab(tabKaraoke)
        tab_home.addTab(tabCloudVillage)

        vp_home.adapter = mPageAdapter

        tab_home.setScrollPosition(0, 0f, true)
        setTabState()

        //添加页签选中监听
        tab_home.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                vp_home.currentItem = tab.position
//                setTabState()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        //注册页面变化的回调接口
        vp_home.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tab_home.setScrollPosition(position, 0f, true)
                setTabState()
            }
        })
    }

    /**
     * 设置选中状态或非选中状态
     */
    private fun setTabState() {
        for (i in 0 until tab_home.tabCount) {
            val currentTab = tab_home.getTabAt(i)
            setItemState(currentTab, currentTab?.isSelected)
        }
    }

    private fun setItemState(currentTab: TabLayout.Tab?, select: Boolean?) {
        when (currentTab?.view?.tag) {
            R.string.found -> {
                currentTab.setIcon(
                    if (select == true) {
                        R.drawable.discovery_select
                    } else {
                        R.drawable.discovery_unselect
                    }
                )
            }
            R.string.karaoke -> {
                currentTab.setIcon(
                    if (select == true) {
                        R.drawable.discovery_select
                    } else {
                        R.drawable.discovery_select
                    }
                )
            }
            R.string.mine -> {
                currentTab.setIcon(
                    if (select == true) {
                        R.drawable.discovery_select
                    } else {
                        R.drawable.discovery_select
                    }
                )
            }
            R.string.sprinkler -> {
                currentTab.setIcon(
                    if (select == true) {
                        R.drawable.discovery_select
                    } else {
                        R.drawable.discovery_select
                    }
                )
            }
            R.string.cloud_village -> {
                currentTab.setIcon(
                    if (select == true) {
                        R.drawable.discovery_select
                    } else {
                        R.drawable.discovery_select
                    }
                )
            }
            else -> {
                //do nothing
            }
        }
    }


    internal class ViewPagerStateAdapter :
        FragmentStateAdapter {

        private val mFragments = arrayListOf<BlankFragment>(
            BlankFragment(),
            BlankFragment(),
            BlankFragment(),
            BlankFragment(),
            BlankFragment()
        )

        constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity) {

        }

        constructor(fragment: Fragment) : super(fragment) {

        }

        override fun getItemCount(): Int {
            return mFragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return mFragments[position]
        }

    }
}