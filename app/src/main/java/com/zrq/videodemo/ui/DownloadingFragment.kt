package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.DownloadingAdapter
import com.zrq.videodemo.bean.Download
import com.zrq.videodemo.databinding.FragmentDownloadingBinding
import com.zrq.videodemo.utils.Constants.DOWN_FAIL
import com.zrq.videodemo.utils.Constants.DOWN_PRE
import com.zrq.videodemo.utils.Constants.DOWN_RUN
import com.zrq.videodemo.utils.Constants.DOWN_STOP
import com.zrq.videodemo.utils.Constants.PAGE_DOWNLOADING
import com.zrq.videodemo.utils.DownloadListener
import com.zrq.videodemo.utils.DownloadUtil
import com.zrq.videodemo.utils.OtherUtils

class DownloadingFragment : BaseFragment<FragmentDownloadingBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDownloadingBinding {
        return FragmentDownloadingBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: DownloadingAdapter
    private val list = mutableListOf<Download>()
    private val downloadListener = DownloadListener()

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
            when (list[it].state) {
                DOWN_RUN -> {
                    DownloadUtil.pause(requireContext(), item.taskId)
                    list[it].state = DOWN_STOP
                }
                DOWN_STOP -> {
                    DownloadUtil.downloadOne(requireContext(), item.title, item.chapterTitle, item.chapterPath)
                    list[it].state = DOWN_RUN
                }
            }
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initEvent() {
        mBinding.apply {
            ivBack.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .popBackStack()
            }
        }
        downloadListener.onRun = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        Log.d(TAG, "onRun: ${task.percent}")
                        percent = task.percent
                        it.state = DOWN_RUN
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        downloadListener.onStop = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        it.state = DOWN_STOP
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        downloadListener.onPre = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        it.state = DOWN_PRE
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        downloadListener.onResume = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        it.state = DOWN_RUN
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        downloadListener.onFail = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        it.state = DOWN_FAIL
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        downloadListener.onComplete = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        mainModel.db?.downloadDao()?.delete(this)

                        val downloadingFiles = mainModel.db?.downloadDao()?.queryAll() ?: mutableListOf()
                        downloadingFiles.forEach {
                            mainModel.localVideo.removeIf { f -> f.title == it.title && f.chapterTitle == it.chapterTitle }
                        }
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        DownloadUtil.addListener(downloadListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        DownloadUtil.addListener(downloadListener)
    }

    override fun setNowPage(): String = PAGE_DOWNLOADING

    private companion object {
        const val TAG = "DownloadingFragment"
    }

}
