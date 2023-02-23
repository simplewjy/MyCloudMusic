package com.simple.mycloudmusic.module.home.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.simple.mycloudmusic.R
import com.simple.mycloudmusic.http.repository.RemoteService
import com.simple.mycloudmusic.module.base.BaseActivity
import com.simple.mycloudmusic.module.home.fragment.*
import com.simple.mycloudmusic.module.utils.beanCopy
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        super.initView()
        this.javaClass.beanCopy()
        initTab()
    }

    override fun initData() {
        super.initData()
        lifecycleScope.launch {
//            RemoteService.loginByPhone("17856013748", "wjy19961124")
            RemoteService.checkMusic("33894312")
        }
    }

    /**
     * 初始化底部菜单栏
     */
    private fun initTab() {
        //初始化tab
        val tabDiscovery = tab_home.newTab()
        tabDiscovery.setIcon(R.drawable.discovery_unselect)
        tabDiscovery.text = resources.getString(R.string.discovery)
        tabDiscovery.view.tag = resources.getString(R.string.discovery)
        val tabSprinkler = tab_home.newTab()
        tabSprinkler.setIcon(R.drawable.sprinkler_unselect)
        tabSprinkler.text = resources.getString(R.string.sprinkler)
        tabSprinkler.view.tag = resources.getString(R.string.sprinkler)
        val tabMine = tab_home.newTab()
        tabMine.setIcon(R.drawable.mine_unselect)
        tabMine.text = resources.getString(R.string.mine)
        tabMine.view.tag = resources.getString(R.string.mine)
        val tabKaraoke = tab_home.newTab()
        tabKaraoke.setIcon(R.drawable.follow_unselect)
        tabKaraoke.text = resources.getString(R.string.follow)
        tabKaraoke.view.tag = resources.getString(R.string.follow)
        val tabCloudVillage = tab_home.newTab()
        tabCloudVillage.setIcon(R.drawable.cloud_village_unselect)
        tabCloudVillage.text = resources.getString(R.string.community)
        tabCloudVillage.view.tag = resources.getString(R.string.community)

        tab_home.addTab(tabDiscovery)
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
            setItemState(currentTab)
        }
    }

    private fun setItemState(currentTab: TabLayout.Tab?) {
        val select = currentTab?.isSelected
        when (currentTab?.view?.tag) {
            resources.getString(R.string.discovery) -> {
                currentTab.setIcon(
                    if (select == true) {
                        R.drawable.discovery_select
                    } else {
                        R.drawable.discovery_unselect
                    }
                )
            }
            resources.getString(R.string.follow) -> {
                currentTab.setIcon(
                    if (select == true) {
                        R.drawable.follow_select
                    } else {
                        R.drawable.follow_unselect
                    }
                )
            }
            resources.getString(R.string.mine) -> {
                currentTab.setIcon(
                    if (select == true) {
                        R.drawable.mine_select
                    } else {
                        R.drawable.mine_unselect
                    }
                )
            }
            resources.getString(R.string.sprinkler) -> {
                currentTab.setIcon(
                    if (select == true) {
                        R.drawable.sprinkler_select
                    } else {
                        R.drawable.sprinkler_unselect
                    }
                )
            }
            resources.getString(R.string.community) -> {
                currentTab.setIcon(
                    if (select == true) {
                        R.drawable.cloud_village_select
                    } else {
                        R.drawable.cloud_village_unselect
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

        private val mFragments = arrayListOf(
            DiscoveryFragment(),
            SprinklerFragment(),
            MineFragment(),
            FollowFragment(),
            CommunityFragment()
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