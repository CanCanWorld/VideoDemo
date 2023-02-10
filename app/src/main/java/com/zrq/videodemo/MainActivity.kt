package com.zrq.videodemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tencent.mmkv.MMKV
import com.zrq.videodemo.databinding.ActivityMainBinding
import com.zrq.videodemo.db.MyDatabase
import com.zrq.videodemo.utils.Constants.PAGE_SEARCH
import com.zrq.videodemo.utils.StatusBarUtil
import xyz.doikki.videocontroller.StandardVideoController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        MMKV.initialize(this)
        StatusBarUtil.transparencyBar(this)
        mainModel = ViewModelProvider(this)[MainModel::class.java]
        initEvent()
        initDB()
    }

    private fun initDB() {
        mainModel.db = Room.databaseBuilder(applicationContext, MyDatabase::class.java, "VideoDB")
            //允许在主线程上操作数据库
            .allowMainThreadQueries()
            //数据库创建和打开事件会回调到这里，可以再次操作数据库
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d(TAG, "onCreate: ")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Log.d(TAG, "onOpen: ")
                }
            })
            .build()
    }

    private fun initEvent() {
        mainModel.setSearchHintText = {
            mBinding.etSearch.hint = it
        }
        mainModel.setSearchText = {
            mBinding.etSearch.setText(it)
        }
        mBinding.apply {
            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mainModel.keywords.push(etSearch.text.toString())
                    Navigation.findNavController(this@MainActivity, R.id.fragment_container)
                        .navigate(R.id.action_global_resultFragment)
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
                }
                false
            }
            ivHome.setOnClickListener {
                if (mainModel.nowPage == PAGE_SEARCH) return@setOnClickListener
                Navigation.findNavController(this@MainActivity, R.id.fragment_container)
                    .navigate(R.id.action_global_searchFragment)
            }
            ivRefresh.setOnClickListener {
                mainModel.onRefreshClickListener()
            }
        }
    }

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mainModel: MainModel

    override fun onBackPressed() {
        if (mainModel.onBackPress())
            super.onBackPressed()
    }

    private companion object {
        private const val TAG = "MainActivity"
    }
}