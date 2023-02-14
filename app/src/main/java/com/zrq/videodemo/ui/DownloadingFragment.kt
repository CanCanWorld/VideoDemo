package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zrq.videodemo.adapter.DownloadingAdapter
import com.zrq.videodemo.bean.Download
import com.zrq.videodemo.databinding.FragmentDownloadingBinding
import com.zrq.videodemo.utils.Constants
import com.zrq.videodemo.utils.Constants.PAGE_DOWNLOADING
import com.zrq.videodemo.utils.DownloadUtil

class DownloadingFragment : BaseFragment<FragmentDownloadingBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDownloadingBinding {
        return FragmentDownloadingBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: DownloadingAdapter
    private val list = mutableListOf<Download>()

    override fun initData() {
        mainModel.db?.downloadDao()?.let {
            list.clear()
            it.queryAll().forEach { item ->
                list.add(Download(item))
            }
        }
        mainModel.setSearchHintText("下载页")
        mAdapter = DownloadingAdapter(requireContext(), list) {
            val item = list[it].downloadItem
            when (list[it].isRunning) {
                Constants.DOWN_ING -> {
                    DownloadUtil.pause(requireContext(), item.taskId)
                    list[it].isRunning = Constants.DOWN_STOP
                }
                Constants.DOWN_STOP -> {
                    DownloadUtil.downloadOne(requireContext(), item.title, item.chapterTitle, item.chapterPath)
                    list[it].isRunning = Constants.DOWN_ING
                }
            }
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initEvent() {
        DownloadUtil.runningListener = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        percent = task.percent
                    }
                }
                if (it.isRunning == Constants.DOWN_STOP) it.isRunning = Constants.DOWN_ING
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun setNowPage(): String = PAGE_DOWNLOADING

}
