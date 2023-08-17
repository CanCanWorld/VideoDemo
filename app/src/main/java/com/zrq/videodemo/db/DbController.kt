package com.zrq.videodemo.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.lang.ref.WeakReference


object DbController {

    private var context: WeakReference<Context>? = null

    fun initDb(ctx: Context): MyDatabase {
        context = WeakReference(ctx)
        return Room.databaseBuilder(ctx, MyDatabase::class.java, "VideoDB")
            //允许在主线程上操作数据库
            .allowMainThreadQueries()
            //数据库创建和打开事件会回调到这里，可以再次操作数据库
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                }
            })
            .build()

    }


}