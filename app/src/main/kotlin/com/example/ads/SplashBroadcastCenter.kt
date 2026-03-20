package com.example.ads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// 1. 创建一个单例的LiveData持有者（ViewModel或独立单例）
class SplashBroadcastCenter : ViewModel() {
    // 定义一个MutableLiveData作为消息载体
    private val _messageEvent = MutableLiveData<String>()
    // 对外暴露不可变的LiveData，防止外部直接修改
    val messageEvent: LiveData<String> = _messageEvent

    // 发送消息的方法
    fun sendMessage(message: String) {
        _messageEvent.postValue(message) // 支持在子线程调用
    }
}