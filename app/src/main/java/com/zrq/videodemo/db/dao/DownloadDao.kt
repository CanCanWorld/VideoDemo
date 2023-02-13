package com.zrq.videodemo.db.dao

import androidx.room.*
import com.zrq.videodemo.db.bean.DownloadItem

@Dao
interface DownloadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(vararg item: DownloadItem)

    @Query("Select * from downloaditem")
    fun queryAll(): MutableList<DownloadItem>

    @Query("Select * from downloaditem where task_id = :taskId")
    fun queryAllByTaskId(taskId: Long): MutableList<DownloadItem>

    @Update
    fun update(item: DownloadItem)

    @Delete
    fun delete(item: DownloadItem)
}