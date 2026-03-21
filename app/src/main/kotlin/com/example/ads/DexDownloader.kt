package com.example.ads

import android.content.Context
import com.example.ads.DevUtil.Companion.d
import com.example.ads.DevUtil.Companion.e
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Dex下载工具类：下载云端Dex到应用私有目录
 */
object DexDownloader {
    private val TAG = "DexDownloader"

    /**
     * 下载Dex文件
     * @param context 上下文
     * @param dexUrl Dex文件的云端链接
     * @return 下载后的Dex文件路径，失败返回null
     */
    fun downloadDex(context: Context, dexUrl: String): File? {
        return try {
            // 1. 创建应用私有目录（无需权限，仅本应用可访问）
            val dexDir = File(context.filesDir, "dex")
            if (!dexDir.exists()) {
                dexDir.mkdirs()
            }
            val dexFile = File(dexDir, "DynamicLogic.dex")
            if (dexFile.exists()) {
                dexFile.delete() // 删除旧文件
            }

            // 2. 建立网络连接
            val url = URL(dexUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 10000
            connection.requestMethod = "GET"

            // 3. 读取输入流并写入文件
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream: InputStream = connection.inputStream
                val outputStream = FileOutputStream(dexFile)
                val buffer = ByteArray(1024)
                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                d(TAG, "Dex文件下载成功：${dexFile.absolutePath}")
                dexFile
            } else {
                e(TAG, "下载失败：响应码${connection.responseCode}")
                null
            }
        } catch (e: Exception) {
            e(TAG, "下载异常：${e.message}")
            null
        }
    }
}