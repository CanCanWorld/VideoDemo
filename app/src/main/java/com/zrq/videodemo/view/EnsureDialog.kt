package com.zrq.videodemo.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.zrq.videodemo.R
import com.zrq.videodemo.databinding.DialogEnsureBinding
import com.zrq.videodemo.utils.OtherUtils

class EnsureDialog(
    context: Context,
    private val activity: Activity,
    private val callback: (Boolean) -> Unit
) : Dialog(context, R.style.SingleDialog) {

    private lateinit var mBinding: DialogEnsureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        mBinding = DialogEnsureBinding.inflate(LayoutInflater.from(context))
        initData()
        initEvent()
        setContentView(mBinding.root)

        if (window != null) {
            val lp = window?.attributes
            if (lp != null) {
                lp.width = OtherUtils.getWindowWidth(activity) / 8 * 7
                window!!.attributes = lp
                setCanceledOnTouchOutside(true)
            }
        }


    }

    private fun initData() {
        mBinding.apply {
        }
    }

    private fun initEvent() {
        mBinding.apply {
            tvEnsure.setOnClickListener {
                dismiss()
                callback(true)
            }
            tvCancel.setOnClickListener {
                dismiss()
                callback(false)
            }
        }
    }
}