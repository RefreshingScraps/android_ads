package com.example.ads

import android.content.pm.ApplicationInfo
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature

/**
 * 处理 @OnlyInDebug 注解的切面类
 * 核心逻辑：Debug包执行方法，Release包跳过执行
 */
@Aspect // 标记为切面类
class DebugOnlyAspect {

    /**
     * 定义切点：匹配所有被 @OnlyInDebug 注解标记的方法
     */
    @Pointcut("execution(@com.example.ads.OnlyInDebug * *(..))")
    fun onlyInDebugPointcut() {}

    /**
     * 环绕通知：拦截方法执行，判断是否执行原方法
     */
    @Around("onlyInDebugPointcut()") // 关联切点
    @Throws(Throwable::class)
    fun aroundOnlyInDebugMethod(joinPoint: ProceedingJoinPoint): Any? {
        // 1. 获取方法信息（可选，用于日志）
        val methodSignature = joinPoint.signature as MethodSignature
        val methodName = methodSignature.method.name
        val className = joinPoint.target.javaClass.simpleName

        // 2. 判断是否为Debug包
        val isDebug = (ApplicationInfo().flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return if (isDebug) {
            // 3. Debug包：执行原方法
            println("[DebugOnly] 执行 $className.$methodName() (Debug模式)")
            joinPoint.proceed() // 执行原方法
        } else {
            // 4. Release包：跳过执行
            println("[DebugOnly] 跳过 $className.$methodName() (Release模式)")
            null // 返回默认值，不执行原方法
        }
    }
}
