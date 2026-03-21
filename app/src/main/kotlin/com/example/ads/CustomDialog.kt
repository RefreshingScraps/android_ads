package com.example.ads

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 4. 核心修复：在 lifecycleScope 协程中调用挂起函数
        lifecycleScope.launch { // 绑定 Activity 生命周期的协程作用域
            // 调用示例
            val retrofit = Retrofit.Builder()
                .baseUrl("https://dialog.freshingair.dpdns.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(NetworkApiService::class.java)
            try {
                // 调用挂起函数获取数据
                val response = service.getMediaData("https://dialog.freshingair.dpdns.org/")

                // 解析数据并使用（注意：UI 操作可直接在 lifecycleScope 中执行）
                if (response.code == 200) {
                    null
                } else {
                    println("请求失败：${response.message}")
                }
            } catch (e: Exception) {
                // 捕获网络异常（如无网络、超时、解析失败等）
                println("网络请求出错：${e.message}")
            } finally {

            }
        }
    }
}