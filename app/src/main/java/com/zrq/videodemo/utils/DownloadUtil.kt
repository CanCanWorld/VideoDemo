package com.zrq.videodemo.utils

import android.content.Context
import android.util.Log
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.m3u8.M3U8VodOption
import com.arialyy.aria.core.task.DownloadTask
import java.io.File


object DownloadUtil {

    private val listeners = mutableListOf<DownloadListener>()

    fun addListener(listener: DownloadListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: DownloadListener) {
        listeners.remove(listener)
    }

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

    fun start(ctx: Context, taskId: Long) {
        Aria.download(ctx).load(taskId).reStart()
    }

    fun pause(ctx: Context, taskId: Long) {
        Aria.download(ctx).load(taskId).stop()
    }


    @Download.onWait
    fun onWait(task: DownloadTask) {
        Log.d(TAG, "wait ==> " + task.downloadEntity.fileName)
        listeners.forEach {
            it.onWait(task)
        }
    }

    @Download.onPre
    fun onPre(task: DownloadTask) {
        Log.d(TAG, "onPre")
        listeners.forEach {
            it.onPre(task)
        }
    }

    @Download.onTaskStart
    fun taskStart(task: DownloadTask) {
        Log.d(TAG, "onStart")
        listeners.forEach {
            it.onStart(task)
        }
    }

    @Download.onTaskRunning
    fun running(task: DownloadTask) {
        Log.d(TAG, "running ${task.percent}")
        listeners.forEach {
            it.onRun(task)
        }
    }

    @Download.onTaskResume
    fun taskResume(task: DownloadTask) {
        Log.d(TAG, "resume")
        listeners.forEach {
            it.onResume(task)
        }
    }

    @Download.onTaskStop
    fun taskStop(task: DownloadTask) {
        Log.d(TAG, "stop")
        listeners.forEach {
            it.onStop(task)
        }
    }

    @Download.onTaskCancel
    fun taskCancel(task: DownloadTask) {
        Log.d(TAG, "cancel")
        listeners.forEach {
            it.onCancel(task)
        }
    }

    @Download.onTaskFail
    fun taskFail(task: DownloadTask) {
        Log.d(TAG, "fail")
        listeners.forEach {
            it.onFail(task)
        }
    }

    @Download.onTaskComplete
    fun taskComplete(task: DownloadTask) {
        Log.d(TAG, "taskComplete: ")
        listeners.forEach {
            it.onComplete(task)
        }

    }

    private const val TAG = "DownloadUtil"
}