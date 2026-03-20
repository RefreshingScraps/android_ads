package com.example.ads

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Init.adSDKisLoaded[AdPlatform.ADMOB] = false
        Init.adSDKisLoaded[AdPlatform.BAIDU] = false
        Init.adSDKisLoaded[AdPlatform.BEIZI] = false
        Init.adSDKisLoaded[AdPlatform.CSJ] = false
    }
}