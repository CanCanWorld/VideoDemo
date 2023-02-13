package com.zrq.videodemo.db.bean

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
data class DownloadItem(

    @ColumnInfo(name = "task_id")
    var taskId: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "chapter_title")
    val chapterTitle: String,

    @ColumnInfo(name = "chapter_path")
    val chapterPath: String,

    @ColumnInfo(name = "cover")
    val cover: String,

    @ColumnInfo(name = "percent")
    var percent: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)