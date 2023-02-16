package com.zrq.videodemo.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.HistoryAdapter
import com.zrq.videodemo.databinding.DialogHistoryBinding
import com.zrq.videodemo.utils.OtherUtils

class HistoryDialog(
    context: Context,
    private val activity: Activity,
    private val list: MutableList<String>
) : Dialog(context, R.style.SingleDialog) {

    private lateinit var mBinding: DialogHistoryBinding
    private var mAdapter: HistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER)
        mBinding = DialogHistoryBinding.inflate(LayoutInflater.from(context))
        initData()
        initEvent()
        setContentView(mBinding.root)

        if (window != null) {
            val lp = window?.attributes
            if (lp != null) {
                lp.width = OtherUtils.getWindowWidth(activity) / 8 * 7
                lp.height = OtherUtils.getWindowHeight(activity) / 3 * 2
                window!!.attributes = lp
                setCanceledOnTouchOutside(true)
            }
        }


    }

    private fun initData() {
        mAdapter = HistoryAdapter(context, list) {
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onItemNotify(l: MutableList<String>) {
        list.clear()
        list.addAll(l)
        mAdapter?.notifyDataSetChanged()
    }


    private fun initEvent() {
        mBinding.apply {
            tvEnsure.setOnClickListener {
                dismiss()
            }
        }
    }
}