package com.zrq.videodemo.utils

import com.arialyy.aria.core.task.DownloadTask

class DownloadListener {
    var onWait: (DownloadTask) -> Unit = {}
    var onPre: (DownloadTask) -> Unit = {}
    var onStart: (DownloadTask) -> Unit = {}
    var onRun: (DownloadTask) -> Unit = {}
    var onResume: (DownloadTask) -> Unit = {}
    var onStop: (DownloadTask) -> Unit = {}
    var onCancel: (DownloadTask) -> Unit = {}
    var onFail: (DownloadTask) -> Unit = {}
    var onComplete: (DownloadTask) -> Unit = {}
}