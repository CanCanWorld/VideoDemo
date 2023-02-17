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

class ThemeDialog(
    context: Context,
    private val activity: Activity,
    private val onThemeChangeCallback: ()-> Unit
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
                lp.width = display.width * 4 / 5
                window!!.attributes = lp
                setCanceledOnTouchOutside(true)
            }
        }


    }

    private fun initEvent() {
        mBinding.apply {
            rbPink.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "pink")
                onThemeChangeCallback()
            }
            rbRed.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "red")
                onThemeChangeCallback()
            }
            rbPurple.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "purple")
                onThemeChangeCallback()
            }
            rbTeal.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "teal")
                onThemeChangeCallback()
            }
            rbGold.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "gold")
                onThemeChangeCallback()
            }
            rbGrey.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "grey")
                onThemeChangeCallback()
            }
            rbBlank.setOnClickListener {
                MMKV.defaultMMKV().encode("theme_type", "black")
                onThemeChangeCallback()
            }
        }
    }

    override fun show() {
        super.show()
        when (MMKV.defaultMMKV().decodeString("theme_type", "pink")) {
            "pink" -> {
                mBinding.rbPink.isChecked = true
            }
            "red" -> {
                mBinding.rbRed.isChecked = true
            }
            "purple" -> {
                mBinding.rbPurple.isChecked = true
            }
            "teal" -> {
                mBinding.rbTeal.isChecked = true
            }
            "grey" -> {
                mBinding.rbGrey.isChecked = true
            }
            "black" -> {
                mBinding.rbBlank.isChecked = true
            }
            else -> {}
        }
    }

    companion object {
        const val TAG = "ThemeDialog"
    }
}