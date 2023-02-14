package com.zrq.videodemo.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import com.zrq.videodemo.MainModel
import com.zrq.videodemo.db.bean.DownloadItem
import java.io.File

object OtherUtils {

    const val TAG = "OtherUtils"

    fun getWindowWidth(context: Activity): Int {
        val display = context.windowManager.defaultDisplay
        val dm = DisplayMetrics()
        display.getRealMetrics(dm)
        return dm.widthPixels
    }

    fun getWindowHeight(context: Activity): Int {
        val display = context.windowManager.defaultDisplay
        val dm = DisplayMetrics()
        display.getRealMetrics(dm)
        return dm.heightPixels
    }


    fun listFiles(ctx: Context): MutableList<DownloadItem> {
        val list = mutableListOf<DownloadItem>()
        val path = ctx.getExternalFilesDir(null)!!.absolutePath
        val dir = File(path)
        if (dir.isDirectory) {
            val files = dir.listFiles()
            for (file in files!!) {
                if (file.isDirectory) {
                    val files2 = file.listFiles()
                    for (file2 in files2!!) {
                        Log.d(TAG, "file2: ${file2.name}")
                        if (file2.name.endsWith(".mp4")) {
                            val item = DownloadItem(-1, file.name, file2.name.removeSuffix(".mp4"), file2.absolutePath, "", 100)
                            list.add(item)
                        }
                    }
                }
                Log.d(TAG, "file: ${file.name}")
            }
        }
        return list
    }

}