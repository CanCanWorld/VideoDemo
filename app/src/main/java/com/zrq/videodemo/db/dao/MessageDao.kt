package com.zrq.videodemo.db.dao

import androidx.room.*
import com.zrq.videodemo.db.bean.Message

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMsg(vararg msg: Message)

    @Query("Select * from message")
    fun queryAll(): MutableList<Message>

    @Query("Select * from message where title like :title limit 1")
    fun findByTitle(title: String): Message

    @Update
    fun update(msg: Message)

    @Delete
    fun delete(msg: Message)

}