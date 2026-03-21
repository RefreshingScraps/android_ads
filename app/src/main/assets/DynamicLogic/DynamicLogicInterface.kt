package com.example.ads.dynamiclogic

import android.content.Context

/**
 * 图标修改接口（宿主和DEX共用，用于跨类加载器调用）
 */
interface DynamicLogicInterface {
    /**
     * 切换应用图标
     * @param context 上下文（需传入宿主的ApplicationContext）
     * @param targetAlias 目标图标
     * @return 是否切换成功
     */
    fun changeAppIcon(context: android.content.Context, targetAlias: String): Boolean
}
