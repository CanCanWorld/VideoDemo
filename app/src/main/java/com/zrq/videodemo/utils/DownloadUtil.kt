package com.zrq.videodemo.utils

import android.content.Context
import android.util.Log
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.m3u8.M3U8VodOption
import com.arialyy.aria.core.task.DownloadTask
import com.zrq.videodemo.db.bean.DownloadItem
import java.io.File


object DownloadUtil {

    var runningListener: (DownloadTask) -> Unit = { }

    init {
        Aria.download(this).register()  //注册aria
    }

    fun downloadOne(ctx: Context, dirName: String, fileName: String, m3u8: String): Long {
        Log.d(TAG, "downloadOne: $m3u8")
        val dirPath = ctx.getExternalFilesDir(null)!!.absolutePath + File.separator + dirName
        val filePath = dirPath + File.separator + fileName + ".mp4"
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdir()
        }
        Log.d(TAG, "path: $filePath")
        val option = M3U8VodOption().apply {
            setUseDefConvert(false)
            setBandWidthUrlConverter(MyConvert.MyBandWidthDefConverter())
            setVodTsUrlConvert(MyConvert.TSConvert())
            merge(true)
        }
        return Aria.download(ctx)
            .load(m3u8)
            .setFilePath(filePath)
            .m3u8VodOption(option)
            .create()
    }

    fun start(ctx: Context, taskId: Long){
        Aria.download(ctx).load(taskId).reStart()
    }

    fun pause(ctx: Context, taskId: Long) {
        Aria.download(ctx).load(taskId).stop()
    }


    @Download.onWait
    fun onWait(task: DownloadTask) {
        Log.d(TAG, "wait ==> " + task.downloadEntity.fileName)
    }

    @Download.onPre
    fun onPre(task: DownloadTask?) {
        Log.d(TAG, "onPre")
    }

    @Download.onTaskStart
    fun taskStart(task: DownloadTask?) {
        Log.d(TAG, "onStart")
    }

    @Download.onTaskRunning
    fun running(task: DownloadTask) {
        Log.d(TAG, "running ${task.percent}")
        runningListener(task)
    }

    @Download.onTaskResume
    fun taskResume(task: DownloadTask?) {
        Log.d(TAG, "resume")
    }

    @Download.onTaskStop
    fun taskStop(task: DownloadTask?) {
        Log.d(TAG, "stop")
    }

    @Download.onTaskCancel
    fun taskCancel(task: DownloadTask?) {
        Log.d(TAG, "cancel")
    }

    @Download.onTaskFail
    fun taskFail(task: DownloadTask?) {
        Log.d(TAG, "fail")
    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask) {
    }

    private const val TAG = "DownloadUtil"

}