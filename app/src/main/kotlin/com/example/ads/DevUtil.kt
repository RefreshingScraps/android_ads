package com.example.ads

import android.content.pm.ApplicationInfo
import android.util.Log

class DevUtil {
    companion object{
        @JvmStatic
        fun isDebug(): Boolean{
            return (ApplicationInfo().flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        }
        @JvmStatic
        @DebugOnly
        fun d(tag: String, message: String){
            Log.d(tag, message)
        }
        @JvmStatic
        @DebugOnly
        fun e(tag: String, message: String){
            Log.e(tag, message)
        }
        @JvmStatic
        @DebugOnly
        fun i(tag: String, message: String){
            Log.i(tag, message)
        }
        @JvmStatic
        @DebugOnly
        fun v(tag: String, message: String){
            Log.v(tag, message)
        }
        @JvmStatic
        @DebugOnly
        fun w(tag: String, message: String){
            Log.w(tag, message)
        }
    }
}