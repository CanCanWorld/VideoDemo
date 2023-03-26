package com.zrq.videodemo.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.tencent.mmkv.MMKV
import com.zrq.videodemo.R
import com.zrq.videodemo.databinding.DialogSpeedBinding
import com.zrq.videodemo.utils.Constants.SPEED

class SpeedDialog(
    context: Context,
    private val activity: Activity,
) : Dialog(context, R.style.SingleDialog) {

    private lateinit var mBinding: DialogSpeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        mBinding = DialogSpeedBinding.inflate(LayoutInflater.from(context))
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
            rb125.setOnClickListener {
                MMKV.defaultMMKV().encode(SPEED, 1.25f)
            }
            rb15.setOnClickListener {
                MMKV.defaultMMKV().encode(SPEED, 1.5f)
            }
            rb175.setOnClickListener {
                MMKV.defaultMMKV().encode(SPEED, 1.75f)
            }
            rb2.setOnClickListener {
                MMKV.defaultMMKV().encode(SPEED, 2f)
            }
            rb25.setOnClickListener {
                MMKV.defaultMMKV().encode(SPEED, 2.5f)
            }
            rb3.setOnClickListener {
                MMKV.defaultMMKV().encode(SPEED, 3f)
            }
            rb10.setOnClickListener {
                MMKV.defaultMMKV().encode(SPEED, 10f)
                Toast.makeText(context, "起飞", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun show() {
        super.show()
        when (MMKV.defaultMMKV().decodeFloat(SPEED, 1.5f)) {
            1.25f -> {
                mBinding.rb125.isChecked = true
            }
            1.5f -> {
                mBinding.rb15.isChecked = true
            }
            1.75f -> {
                mBinding.rb175.isChecked = true
            }
            2f -> {
                mBinding.rb2.isChecked = true
            }
            2.5f -> {
                mBinding.rb25.isChecked = true
            }
            3f -> {
                mBinding.rb3.isChecked = true
            }
            10f -> {
                mBinding.rb10.isChecked = true
            }
            else -> {}
        }
    }

    companion object {
        const val TAG = "SpeedDialog"
    }
}