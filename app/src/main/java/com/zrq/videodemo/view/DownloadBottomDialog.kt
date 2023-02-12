package com.zrq.videodemo.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zrq.videodemo.MainModel
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.DownloadAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.bean.Content
import com.zrq.videodemo.bean.DownloadItem
import com.zrq.videodemo.databinding.BottomDownloadBinding
import com.zrq.videodemo.utils.DownloadUtil
import com.zrq.videodemo.utils.OtherUtils

class DownloadBottomDialog(
    private val ctx: Context,
    private val activity: Activity,
    private val mainModel: MainModel,
    private val content: Content,
) : BottomSheetDialog(ctx) {

    private lateinit var mBinding: BottomDownloadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = BottomDownloadBinding.inflate(LayoutInflater.from(context))
        initData()
        initEvent()
        setContentView(mBinding.root)

        if (window != null) {
            val lp = window?.attributes
            if (lp != null) {
                lp.width = OtherUtils.getWindowWidth(activity)
                lp.height = OtherUtils.getWindowHeight(activity)
                window!!.attributes = lp
            }
        }
        val layoutParams = mBinding.root.layoutParams
        layoutParams.width = OtherUtils.getWindowWidth(activity)
        layoutParams.height = OtherUtils.getWindowHeight(activity) / 3 * 2
        mBinding.root.layoutParams = layoutParams

        setCanceledOnTouchOutside(true)
    }

    private lateinit var mAdapter: DownloadAdapter

    @SuppressLint("SetTextI18n")
    private fun initData() {
        val data = content.data
        mAdapter = DownloadAdapter(ctx, data.chapterList) {
            mainModel.downloadItems.add(DownloadItem(data.chapterList[it], data.title, data.cover, 0))
            DownloadUtil.downloadOne(ctx, data.title, data.chapterList[it].title, data.chapterList[it].chapterPath)
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    private fun initEvent() {
        mBinding.apply {
            ivClose.setOnClickListener {
                dismiss()
            }
            tvDownloadAll.setOnClickListener {

            }
            tvCheckDownload.setOnClickListener {
                dismiss()
                Navigation.findNavController(activity, R.id.fragment_container)
                    .navigate(R.id.downloadFragment)
            }
        }
    }

    private companion object {
        private const val TAG = "DownloadBottomDialog"
    }


}