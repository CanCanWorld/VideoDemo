package com.zrq.videodemo.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import com.zrq.videodemo.MainModel
import com.zrq.videodemo.db.bean.DownloadItem
import com.zrq.videodemo.db.bean.Message
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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

    fun messageToString(msg: Message): String {
        val sdfYear = SimpleDateFormat("yyyy", Locale.CHINA)
        val that = SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(msg.currentTime).toLong()
        val nowDay = SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Date()).toLong()
        if (sdfYear.format(msg.currentTime) == sdfYear.format(Date().time)) {
            val string = when (nowDay - that) {
                0L -> "今天"
                1L -> "昨天"
                2L -> "前天"
                else -> SimpleDateFormat("MM月dd日", Locale.CHINA).format(msg.currentTime)
            }
            return string + SimpleDateFormat(" HH时mm分", Locale.CHINA).format(msg.currentTime) +
                    "观看了《" + msg.title + "》" + msg.chapter
        } else
            return SimpleDateFormat("yyyy年MM月dd日 HH时mm分", Locale.CHINA).format(msg.currentTime) +
                    "观看了《" + msg.title + "》" + msg.chapter
    }

}