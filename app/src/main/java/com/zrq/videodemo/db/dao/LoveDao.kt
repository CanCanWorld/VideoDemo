package com.zrq.videodemo.db.dao

import androidx.room.*
import com.zrq.videodemo.db.bean.Love

@Dao
interface LoveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLike(vararg love: Love)

    @Query("Select * from love")
    fun queryAll(): MutableList<Love>

    @Query("Select * from love where title = :title")
    fun queryAllByTitle(title: String): MutableList<Love>

    @Update
    fun update(love: Love)

    @Delete
    fun delete(love: Love)
}