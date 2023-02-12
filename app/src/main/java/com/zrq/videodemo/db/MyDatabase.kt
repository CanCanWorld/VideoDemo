package com.zrq.videodemo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zrq.videodemo.db.bean.Love
import com.zrq.videodemo.db.bean.Message
import com.zrq.videodemo.db.dao.LoveDao
import com.zrq.videodemo.db.dao.MessageDao

@Database(entities = [Message::class, Love::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao?
    abstract fun loveDao(): LoveDao?
}