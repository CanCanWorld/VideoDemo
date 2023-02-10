package com.zrq.videodemo.db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "current_time")
    val currentTime: Long,
    @ColumnInfo(name = "chapter")
    val chapter: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
