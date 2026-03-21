package com.example.ads

/**
 * 自定义注解：标记的方法仅在 Debug 包下执行，Release 包下跳过执行
 * 适用场景：调试日志、测试接口、开发辅助功能等
 */
@Target(AnnotationTarget.FUNCTION) // 注解仅作用于方法
@Retention(AnnotationRetention.RUNTIME) // 运行时保留（AspectJ需要）
annotation class DebugOnly