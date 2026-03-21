package com.example.ads.dynamiclogic
/**
 * 动态加载的核心类：提供待执行的逻辑
 */
class DynamicLogic {
    // 定义所有图标别名的类名（对应AndroidManifest中的activity-alias name）
    private var ALIAS_1 = "com.example.ads.IconAlias1"
    private var ALIAS_2 = "com.example.ads.IconAlias2"

    /**
     * 切换到指定图标
     * @param context 上下文
     * @param targetAlias 目标别名类名（如ALIAS_1、ALIAS_2）
     */
    fun changeAppIcon(context: android.content.Context, targetAlias: String) {
        // 1. 禁用所有别名
        disableComponent(context, ALIAS_1)
        disableComponent(context, ALIAS_2)

        // 2. 启用目标别名
        enableComponent(context, targetAlias)

        // 3. 提示用户（图标切换可能延迟，需重启桌面生效）
        android.widget.Toast.makeText(context, "图标已切换，桌面可能延迟更新", android.widget.Toast.LENGTH_SHORT).show()
    }

    /**
     * 启用组件（Activity/Alias）
     */
    private fun enableComponent(context: android.content.Context, className: String) {
        val componentName = android.content.ComponentName(context.packageName, className)
        context.packageManager.setComponentEnabledSetting(
            componentName,
            android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            android.content.pm.PackageManager.DONT_KILL_APP // 不杀死应用，避免闪退
        )
    }

    /**
     * 禁用组件（Activity/Alias）
     */
    private fun disableComponent(context: android.content.Context, className: String) {
        val componentName = android.content.ComponentName(context.packageName, className)
        context.packageManager.setComponentEnabledSetting(
            componentName,
            android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            android.content.pm.PackageManager.DONT_KILL_APP
        )
    }
}
