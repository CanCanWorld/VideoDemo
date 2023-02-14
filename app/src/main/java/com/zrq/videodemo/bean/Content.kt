package com.zrq.videodemo.bean

import androidx.annotation.Keep
import com.zrq.videodemo.utils.Constants

@Keep
data class Content(
    val code: Int,
    val count: Int,
    val data: ContentData,
    val msg: String,
)

@Keep
data class ContentData(
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
    val videoType: String,
    var pos: Int,
)

@Keep
data class Chapter(
    var chapterPath: String,
    val title: String,
    var state: Int = Constants.DOWN_NON,
)