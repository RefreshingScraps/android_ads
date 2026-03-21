package com.example.ads

// 数据模型（与 Worker 返回的 JSON 对应）
data class NetworkResponse(
    val code: Int,
    val message: String,
    val data: CustomDialogData
)