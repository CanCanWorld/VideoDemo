package com.zrq.videodemo.bean

data class Content(
    val code: Int,
    val count: Int,
    val data: Data,
    val msg: String,
    var pos: Int
)

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

data class Chapter(
    val chapterPath: String,
    val title: String
)