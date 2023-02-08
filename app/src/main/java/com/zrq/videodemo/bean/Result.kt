package com.zrq.videodemo.bean

import androidx.annotation.Keep

@Keep
data class Result(
    val msg: String = "",
    val code: Int = 0,
    val data: MutableList<DataItem>?,
    val count: Int = 0
)

@Keep
data class DataItem(
    val actor: String = "",
    val cover: String = "",
    val descs: String = "",
    val releaseTime: String = "",
    val director: String = "",
    val videoType: String = "",
    val videoId: String = "",
    val updateTime: String = "",
    val title: String = "",
    val region: String = ""
)