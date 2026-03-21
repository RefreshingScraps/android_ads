package com.example.ads

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.File
import java.io.FileOutputStream



class Converter {
    companion object {
        fun getFileFromAssets(context: Context, assetsFilePath: String): File? {
            val assetManager = context.assets
            // 1. 定义本地存储路径（内部存储，仅应用可访问，无需权限）
            val localFile = File(context.filesDir, getFileNameFromPath(assetsFilePath))

            // 如果本地已存在该文件，直接返回（避免重复复制）
            if (localFile.exists()) {
                return localFile
            }

            try {
                // 2. 打开assets文件输入流
                assetManager.open(assetsFilePath).use { inputStream ->
                    // 3. 写入本地文件
                    FileOutputStream(localFile).use { outputStream ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (inputStream.read(buffer).also { length = it } != -1) {
                            outputStream.write(buffer, 0, length)
                        }
                        outputStream.flush()
                    }
                }
                return localFile
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }
        private fun getFileNameFromPath(filePath: String): String {
            return filePath.substring(filePath.lastIndexOf("/") + 1)
        }

        fun getAssetsImage(context: Context, url: String): Bitmap? {
            val am = context.assets
            try {
                am.open(url).use { inputStream ->
                    return BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }
    }
}