package com.example.ads

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.ViewGroup
import android.content.Context

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val broadcastCenter = ViewModelProvider(this)[SplashBroadcastCenter::class.java]
        // 观察消息变化（自动感知生命周期，无需手动注销）
        broadcastCenter.messageEvent.observe(this) { message ->
            // 处理接收到的消息
            //Log.d("Receiver", "收到消息：$message")
            if(message == "FINISH_ACTIVITY") {
                findViewById<ViewGroup?>(R.id.SplashAdContainer)!!.removeAllViews()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun onSplashAdLoaded(context: Context, view: ViewGroup){
            // 将 context 强转为 SplashActivity 实例
            //val activity = context as? SplashActivity ?: return
            context.let {  }
            view.let {  }
        }
        @JvmStatic
        fun goToMainActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            // 将 context 强转为 SplashActivity 实例
            val activity = context as? SplashActivity ?: return
            // 通过实例调用 send() 方法
            activity.goToMainActivity()
        }
    }

    fun goToMainActivity(){
        ViewModelProvider(this)[SplashBroadcastCenter::class.java].sendMessage("FINISH_ACTIVITY")
    }
}