package com.example.ads

import android.content.Context
import com.example.ads.Converter.Companion.getFileFromAssets
import com.example.ads.DevUtil.Companion.d
import com.example.ads.DevUtil.Companion.e
import com.kuaishou.weapon.p0.jni.A
import dalvik.system.DexClassLoader
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.reflect.Method

/**
 * Dex加载工具类：加载外部Dex并调用其中的方法
 */
object DexLoader {
    private const val TAG = "DexLoader"
    private var dexFile: File? = File("")
    @OptIn(DelicateCoroutinesApi::class)
    fun getAndLoadDex(context: Context, dexUrl: String, targetClassName: String, methodName: String){
        // 协程中执行下载+加载（避免主线程网络操作）
        GlobalScope.launch(Dispatchers.Main) {
            // 1. 下载Dex（IO线程）
            dexFile = withContext(Dispatchers.IO) {
                DexDownloader.downloadDex(context, dexUrl)
            }
            // 2. 加载并调用Dex
            if (dexFile != null) {
                d(TAG, "开始加载Dex文件：${dexFile!!.absolutePath}")
                loadAndInvokeDex(context, targetClassName, methodName)
            } else {
                e(TAG, "Dex文件下载失败")
            }
        }
    }
    @DebugOnly
    fun getAssetsDexAndLoad(context: Context,
                            dexName: String,
                            targetClassName: String,
                            methodName: String,
                            paramTypes: Array<Class<*>>? = null,
                            paramValues: Array<Any?>? = null){
        dexFile=getFileFromAssets(context, dexName)
        loadAndInvokeDex(context,targetClassName,methodName, paramTypes, paramValues)
    }
    // 待加载的类名（包名+类名）
    //com.example.ads.dynamiclogic.DynamicLogic.DynamicLogic

    /**
     * 加载Dex并调用指定方法
     * @param context 上下文
     * @param targetClassName 目标类名
     * @param methodName 方法名
     * @param dexFile 下载后的Dex文件
     */
    fun loadAndInvokeDex(context: Context,
                         targetClassName: String,
                         methodName: String,
                         paramTypes: Array<Class<*>>? = null,
                         paramValues: Array<Any?>? = null) {
        try {
            dexFile!!.setReadOnly()
            // 1. 创建DexClassLoader
            // 参数说明：
            // - dexPath：Dex文件路径
            // - optimizedDirectory：优化后的Dex存储目录（必须是应用私有目录）
            // - librarySearchPath：so库路径（无so则传null）
            // - parent：父类加载器（应用默认类加载器）
            val optimizedDir = File(context.filesDir, "dex_opt")
            if (!optimizedDir.exists()) {
                optimizedDir.mkdirs()
            }
            val classLoader = DexClassLoader(
                dexFile!!.absolutePath,
                optimizedDir.absolutePath,
                null,
                context.classLoader
            )

            // 2. 加载目标类
            val dynamicClass = classLoader.loadClass(targetClassName)

            // 3. 创建类实例（无参构造）
            val dynamicInstance = dynamicClass.getDeclaredConstructor().newInstance()
            var dynamicMethod: Method?
            var dynamicResult: Any?
            // 5. 反射调用
            if(paramTypes == null){
                dynamicMethod = dynamicClass.getMethod(methodName)
                // dynamicMethod.isAccessible = true
                dynamicResult = dynamicMethod.invoke(dynamicInstance)
            } else {
                dynamicMethod = dynamicClass.getMethod(methodName, *paramTypes)
                // dynamicMethod.isAccessible = true
                // 处理参数值（可空不影响invoke，invoke支持null参数）
                val methodParamValues = paramValues ?: emptyArray<Any?>()
                dynamicResult = dynamicMethod.invoke(dynamicInstance, *methodParamValues)
            }
            d(TAG, "调用${methodName}结果：$dynamicResult")

        } catch (e: ClassNotFoundException) {
            e(TAG, "类未找到：${e.message}")
        } catch (e: NoSuchMethodException) {
            e(TAG, "方法未找到：${e.message}")
        } catch (e: Exception) {
            e(TAG, "加载/调用Dex失败：${e.message}")
        }
    }
}