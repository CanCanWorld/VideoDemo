package com.zrq.videodemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tencent.mmkv.MMKV
import com.zrq.videodemo.databinding.ActivityMainBinding
import xyz.doikki.videocontroller.StandardVideoController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        MMKV.initialize(this)
        initEvent()
    }

    private fun initEvent() {
        mBinding.apply {
        }
    }

    private lateinit var mBinding: ActivityMainBinding
}