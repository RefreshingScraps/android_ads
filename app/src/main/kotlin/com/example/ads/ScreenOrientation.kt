package com.example.ads

import android.content.Context

class ScreenOrientation {
    companion object{
        @JvmStatic
        fun getScreenOrientation(context: Context): Int {
            // 获取当前屏幕方向
            return context.resources.configuration.orientation
        }
    }
}