package com.zrq.videodemo

import android.Manifest
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.arialyy.aria.core.Aria
import com.tencent.mmkv.MMKV
import com.zrq.videodemo.databinding.ActivityMainBinding
import com.zrq.videodemo.db.DbController
import com.zrq.videodemo.utils.Constants.PAGE_DOWNLOADED
import com.zrq.videodemo.utils.Constants.PAGE_DOWNLOADING
import com.zrq.videodemo.utils.Constants.PAGE_SEARCH
import com.zrq.videodemo.utils.Constants.THEME_BLUE
import com.zrq.videodemo.utils.Constants.THEME_GREEN
import com.zrq.videodemo.utils.Constants.THEME_PINK
import com.zrq.videodemo.utils.Constants.THEME_PURPLE
import com.zrq.videodemo.utils.Constants.THEME_RED
import com.zrq.videodemo.utils.Constants.THEME_TYPE
import com.zrq.videodemo.utils.Constants.THEME_YELLOW
import com.zrq.videodemo.utils.OtherUtils
import com.zrq.videodemo.utils.StatusBarUtil
import xyz.doikki.videoplayer.exo.ExoMediaPlayerFactory
import xyz.doikki.videoplayer.player.VideoViewConfig
import xyz.doikki.videoplayer.player.VideoViewManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MMKV.initialize(this)
        setBaseTheme()
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        requestPermissions()
        mainModel = ViewModelProvider(this)[MainModel::class.java]
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        initData()
        initEvent()
    }

    private fun initData() {
        initDB()
        VideoViewManager.setConfig(
            VideoViewConfig.newBuilder()
                .setPlayerFactory(ExoMediaPlayerFactory.create())
                .build()
        )
        Aria.init(this)

        mainModel.reloadLocalVideo = {
            val listFiles = OtherUtils.listFiles(this)
            val downloadingFiles = mainModel.db?.downloadDao()?.queryAll() ?: mutableListOf()
            downloadingFiles.forEach {
                listFiles.removeIf { f -> f.title == it.title && f.chapterTitle == it.chapterTitle }
            }
            mainModel.localVideo.clear()
            mainModel.localVideo.addAll(listFiles)
        }
    }

    private fun initDB() {
        mainModel.db = DbController.initDb(this)
    }

    private fun initEvent() {
        mainModel.apply {
            clearBottomFocus = { clearFocus() }
            setSearchHintText = {
                mBinding.etSearch.hint = it
            }
            setSearchText = {
                mBinding.etSearch.setText(it)
            }
        }
        mBinding.apply {
            val that = this@MainActivity
            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearFocus()
                    if (etSearch.text.toString() == "") {
                        Toast.makeText(that, "请输入关键字", Toast.LENGTH_SHORT).show()
                        return@setOnEditorActionListener false
                    }
                    mainModel.keywords.push(etSearch.text.toString())
                    Navigation.findNavController(that, R.id.fragment_container)
                        .navigate(R.id.action_global_resultFragment)
                }
                false
            }
            ivBack.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(that, R.anim.anim_translate))
                onBackPressed()
            }
            ivDownload.setOnClickListener {
                if (mainModel.nowPage == PAGE_DOWNLOADED || mainModel.nowPage == PAGE_DOWNLOADING) {
                    Toast.makeText(that, "当前正在下载页", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Navigation.findNavController(that, R.id.fragment_container)
                    .navigate(R.id.downloadFragment)
            }
            ivHome.setOnClickListener {
                if (mainModel.nowPage == PAGE_SEARCH) {
                    Toast.makeText(that, "当前正在主页", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Navigation.findNavController(that, R.id.fragment_container)
                    .popBackStack(R.id.searchFragment, false)
                mainModel.keywords.clear()
            }
            ivRefresh.setOnClickListener {
                it.startAnimation(AnimationUtils.loadAnimation(that, R.anim.anim_rotation_360))
                mainModel.onRefreshClickListener()
            }
        }
    }

    private fun clearFocus() {
        mBinding.etSearch.clearFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    private fun setBaseTheme() {
        val themeType = MMKV.defaultMMKV().decodeString(THEME_TYPE)
        val id: Int =
            when (themeType) {
                THEME_RED -> R.style.Theme_Red
                THEME_PINK -> R.style.Theme_Pink
                THEME_PURPLE -> R.style.Theme_Purple
                THEME_BLUE -> R.style.Theme_Blue
                THEME_GREEN -> R.style.Theme_Green
                THEME_YELLOW -> R.style.Theme_Yellow
                else -> R.style.Theme_Pink
            }
        StatusBarUtil.setStatusBarColor(
            this, when (themeType) {
                THEME_RED -> R.color.red
                THEME_PINK -> R.color.pink
                THEME_PURPLE -> R.color.purple
                THEME_BLUE -> R.color.blue
                THEME_GREEN -> R.color.green
                THEME_YELLOW -> R.color.yellow
                else -> R.color.pink
            }
        )
        setTheme(id)
    }


    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mainModel: MainModel

    override fun onBackPressed() {
        if (mainModel.onBackPress())
            super.onBackPressed()
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
    }

    private companion object {
        private const val TAG = "MainActivity"


        val PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Aria.download(this).unRegister()
    }

}