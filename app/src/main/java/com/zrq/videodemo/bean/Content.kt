package com.zrq.videodemo.bean

import androidx.annotation.Keep
import com.zrq.videodemo.utils.Constants

@Keep
data class Content(
    val code: Int,
    val count: Int,
    val data: Data,
    val msg: String,
    var pos: Int,
)

@Keep
data class Data(
    val actor: String,
    val chapterList: MutableList<Chapter>,
    val cover: String,
    val descs: String,
    val director: String,
    val region: String,
    val releaseTime: String,
    val title: String,
    val updateTime: String,
    val videoId: String,
    val videoType: String
)

@Keep
data class Chapter(
    val chapterPath: String,
    val title: String,
    var state: Int = Constants.DOWN_NON,
)