package com.zrq.videodemo.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.tencent.mmkv.MMKV
import com.zrq.videodemo.R
import com.zrq.videodemo.databinding.DialogThemeBinding
import com.zrq.videodemo.utils.Constants.THEME_BLUE
import com.zrq.videodemo.utils.Constants.THEME_GREEN
import com.zrq.videodemo.utils.Constants.THEME_PINK
import com.zrq.videodemo.utils.Constants.THEME_PURPLE
import com.zrq.videodemo.utils.Constants.THEME_RED
import com.zrq.videodemo.utils.Constants.THEME_TYPE
import com.zrq.videodemo.utils.Constants.THEME_YELLOW

class ThemeDialog(
    context: Context,
    private val activity: Activity,
    private val onThemeChangeCallback: () -> Unit
) : Dialog(context, R.style.SingleDialog) {

    private lateinit var mBinding: DialogThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        mBinding = DialogThemeBinding.inflate(LayoutInflater.from(context))
        initEvent()
        setContentView(mBinding.root)

        val display = activity.windowManager.defaultDisplay
        if (window != null) {
            val lp = window?.attributes
            if (lp != null) {
                lp.width = display.width * 3 / 4
                window!!.attributes = lp
                setCanceledOnTouchOutside(true)
            }
        }


    }

    private fun initEvent() {
        mBinding.apply {
            rbPink.setOnClickListener {
                MMKV.defaultMMKV().encode(THEME_TYPE, THEME_PINK)
                onThemeChangeCallback()
            }
            rbRed.setOnClickListener {
                MMKV.defaultMMKV().encode(THEME_TYPE, THEME_RED)
                onThemeChangeCallback()
            }
            rbPurple.setOnClickListener {
                MMKV.defaultMMKV().encode(THEME_TYPE, THEME_PURPLE)
                onThemeChangeCallback()
            }
            rbGreen.setOnClickListener {
                MMKV.defaultMMKV().encode(THEME_TYPE, THEME_GREEN)
                onThemeChangeCallback()
            }
            rbYellow.setOnClickListener {
                MMKV.defaultMMKV().encode(THEME_TYPE, THEME_YELLOW)
                onThemeChangeCallback()
            }
            rbBlue.setOnClickListener {
                MMKV.defaultMMKV().encode(THEME_TYPE, THEME_BLUE)
                onThemeChangeCallback()
            }
        }
    }

    override fun show() {
        super.show()
        when (MMKV.defaultMMKV().decodeString(THEME_TYPE, THEME_PINK)) {
            THEME_PINK -> {
                mBinding.rbPink.isChecked = true
            }
            THEME_RED -> {
                mBinding.rbRed.isChecked = true
            }
            THEME_PURPLE -> {
                mBinding.rbPurple.isChecked = true
            }
            THEME_GREEN -> {
                mBinding.rbGreen.isChecked = true
            }
            THEME_YELLOW -> {
                mBinding.rbYellow.isChecked = true
            }
            THEME_BLUE -> {
                mBinding.rbBlue.isChecked = true
            }
            else -> {}
        }
    }

    companion object {
        const val TAG = "ThemeDialog"
    }
}