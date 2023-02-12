package com.zrq.videodemo.db.bean

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
data class Love(

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "video_id")
    val videoId: String,

    @ColumnInfo(name = "cover")
    val cover: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
