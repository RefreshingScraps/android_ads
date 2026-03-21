package com.example.ads

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ads.Init.getSettings
import com.example.ads.SettingsList.On
import com.example.ads.SettingsList.SettingKeys
import java.util.Random


class SettingsUIFragment : Fragment() {
    private val mMyAdapter: MyAdapter = this.MyAdapter()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private val mSettingsList = ArrayList<SettingsList>()
    private val mItems = ArrayList<Any?>() // 使用Object列表，可以放视频和广告
    // 标记：防止重复刷新
    private var isRefreshing : Boolean = false
    class Settings // Optional: Add constructor for better initialization
        (// Use lowercase for variable names (coding convention)
        var settingText: String?, var settingKey: String?, var on: Boolean?
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_uisettings, container, false)
        initHomeFragmentView(view)
        // 设置下拉刷新
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        On = getSettings(context, SettingKeys)
        setupSwipeRefresh()
        return view
    }

    private fun setupSwipeRefresh() {
        // 设置刷新进度条颜色
        swipeRefreshLayout?.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_orange_light,
            android.R.color.holo_green_light
        )

        // 设置下拉刷新监听
        swipeRefreshLayout?.setOnRefreshListener{
            // 防止重复触发刷新
            if (isRefreshing) return@setOnRefreshListener
            isRefreshing = true
            // 建议使用协程，这里用Handler简化示例
            Handler(Looper.getMainLooper()).postDelayed({
                On = getSettings(context, SettingKeys)
                swipeRefreshLayout!!.isRefreshing = false
            }, 500) // 0.5秒延迟模拟加载
        }
    }

    private fun refreshSettingList(){
        // 构建混合数据
        mItems.clear()
        for (i in SettingsList.SettingsList.indices) {
            mItems.add(
                Settings(
                    SettingsList.SettingsList[i],
                    SettingKeys[i],
                    On[i]
                )
            )

            // 每3个设置项插入一个广告
            if (i % 4 == 1) {
                mItems.add(AdItem(AdPlatform.entries[Random().nextInt(AdPlatform.entries.size)]))
                //                mItems.add(new AdItem(AdPlatform.OSET));
            }
        }
        mMyAdapter.notifyDataSetChanged()
    }
    fun initHomeFragmentView(v: View) {
        val mRecyclerView = v.findViewById<RecyclerView?>(R.id.recycler_view)
        refreshSettingList()
        mRecyclerView!!.setAdapter(mMyAdapter)
        mRecyclerView.setLayoutManager(LinearLayoutManager(context))
    }

    // 广告数据类
    internal class AdItem(var platform: AdPlatform)
    internal inner class MyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
        override fun getItemViewType(position: Int): Int {
            return if (mItems[position] is Settings) {
                TYPE_SETTING
            } else {
                TYPE_AD
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == TYPE_SETTING) {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list, parent, false)
                return SettingsViewHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ad_list, parent, false) // 创建广告布局
                return AdViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is SettingsViewHolder) {
                val settings = mItems[position] as Settings
                holder.bind(settings)
            } else if (holder is AdViewHolder && position < holder.adHasLoaded.size) {
                if (!holder.adHasLoaded[position]!!) {
                    val adItem = mItems[position] as AdItem
                    holder.bind(context, adItem, position)
                }
            }
        }

        override fun getItemCount(): Int {
            return mItems.size
        }
    }

    internal class SettingsViewHolder(settingItem: View) : RecyclerView.ViewHolder(settingItem) {
        // 1. 获取Switch控件实例
        var switchControl: SwitchCompat = settingItem.findViewById(R.id.switch_control)
        fun bind(setting: Settings) {
            switchControl.text = setting.settingText
            switchControl.isChecked = setting.on == true

            // 2. 设置开关状态监听
            switchControl.setOnCheckedChangeListener { buttonView, isChecked ->
                // 保存开关状态到SP
                SettingsUtils.saveSetting(buttonView.context, setting.settingKey, isChecked)
            }
        }
    }

    internal class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var adContainer: ViewGroup?
        var adHasLoaded = ArrayList<Boolean?>()

        init {
            for (i in SettingsList.SettingsList.indices) {
                adHasLoaded.add(i, false)
            }
            adContainer = itemView.findViewById(R.id.FeedAdContainer)
        }

        fun bind(context: Context? ,adItem: AdItem, position: Int) {
            if (adContainer != null) {
                adContainer!!.removeAllViews()
                loadAdByType.loadFeedAd(context as Activity,adContainer!!, adItem.platform)
                adHasLoaded[position] = true
            }
        }
    }

    // Optional: Add lifecycle method to handle memory cleanup
    override fun onDestroyView() {
        super.onDestroyView()
        mSettingsList.clear()
    }

    companion object {
        private const val TYPE_SETTING = 0
        private const val TYPE_AD = 1
    }
}