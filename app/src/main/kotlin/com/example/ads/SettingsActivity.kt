package com.example.ads

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {
    private var viewPager: ViewPager2? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var fragmentList: MutableList<Fragment>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initView()
        initFragments()
        setupViewPager()
        setupBottomNavigation()
    }

    private fun initView() {
        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottomNavigation)
    }

    private fun initFragments() {
        fragmentList = ArrayList()
        fragmentList?.add(SettingsUIFragment())
        fragmentList?.add(VideoFragment())
        fragmentList?.add(ProfileFragment())
    }

    private fun setupViewPager() {
        val adapter = fragmentList?.let { ViewPagerAdapter(this, it) }
        viewPager!!.setAdapter(adapter)
        viewPager!!.setOffscreenPageLimit(3) // 预加载所有页面

        // 禁用左右滑动切换（如果需要的话）
        // viewPager.setUserInputEnabled(false);

        // ViewPager页面切换监听
        viewPager!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 同步更新底部导航选中项
                when (position) {
                    0 -> bottomNavigationView!!.selectedItemId = R.id.navigation_home
                    1 -> bottomNavigationView!!.selectedItemId = R.id.navigation_video
                    2 -> bottomNavigationView!!.selectedItemId = R.id.navigation_mine
                }
            }
        })
    }

    private fun setupBottomNavigation() {
        bottomNavigationView!!.setOnItemSelectedListener { item: MenuItem? ->
            val itemId = item!!.itemId
            when (itemId) {
                R.id.navigation_home -> {
                    viewPager!!.setCurrentItem(0, true)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_video -> {
                    viewPager!!.setCurrentItem(1, true)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_mine -> {
                    viewPager!!.setCurrentItem(2, true)
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }

        // 长按提示
        bottomNavigationView!!.setOnItemReselectedListener { _: MenuItem? -> }
    }
    //    @Override
    //    public void onBackPressed() {
    //        // 如果不是首页，返回首页
    //        if (viewPager.getCurrentItem() != 0) {
    //            viewPager.setCurrentItem(0, true);
    //            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    //        } else {
    //            super.onBackPressed();
    //        }
    //    }
}