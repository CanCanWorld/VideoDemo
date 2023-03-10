package com.zrq.videodemo.bean

import androidx.annotation.Keep
import com.zrq.videodemo.db.bean.DownloadItem
import com.zrq.videodemo.utils.Constants

@Keep
data class Download(
    val downloadItem: DownloadItem,
    var state: Int = Constants.DOWN_STOP,
    var isEdit: Boolean = false,
    var isSelect: Boolean = false
)