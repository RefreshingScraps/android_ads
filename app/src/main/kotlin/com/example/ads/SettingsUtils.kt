package com.example.ads

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SettingsUtils {
    companion object {
        // 定义SP文件名（可自定义）
        @JvmStatic
        val SP_NAME: String = "app_settings"
        @JvmStatic
        var sp: SharedPreferences? = null

        // 初始化SharedPreferences
        @JvmStatic
        private fun getSP(context: Context): SharedPreferences? {
            if (sp == null) {
                // MODE_PRIVATE：只有当前应用可访问
                sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            }
            return sp
        }

        // 保存布尔类型设置（如开关状态）
        @JvmStatic
        fun saveSetting(context: Context, key: String?, value: Boolean) {
            getSP(context)!!.edit { putBoolean(key, value) }
        }

        // 读取布尔类型设置（带默认值）
        @JvmStatic
        fun getSetting(context: Context, key: String?, defaultValue: Boolean): Boolean {
            return getSP(context)!!.getBoolean(key, defaultValue)
        }
    }
}