package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zrq.videodemo.adapter.DownloadAllAdapter
import com.zrq.videodemo.bean.Download
import com.zrq.videodemo.databinding.FragmentDownloadBinding
import com.zrq.videodemo.db.bean.DownloadItem
import com.zrq.videodemo.utils.Constants.DOWN_FINISH
import com.zrq.videodemo.utils.Constants.DOWN_ING
import com.zrq.videodemo.utils.Constants.DOWN_STOP
import com.zrq.videodemo.utils.Constants.PAGE_DOWN
import com.zrq.videodemo.utils.DownloadUtil
import com.zrq.videodemo.view.MyPrepareView
import com.zrq.videodemo.view.MyStandardVideoController
import xyz.doikki.videocontroller.component.*


class DownloadFragment : BaseFragment<FragmentDownloadBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDownloadBinding {
        return FragmentDownloadBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: DownloadAllAdapter
    private val list = mutableListOf<Download>()

    override fun initData() {
        mainModel.db?.downloadDao()?.let {
            list.clear()
            it.queryAll().forEach { item ->
                list.add(Download(item))
            }
        }
        mainModel.localVideo.forEach {
            list.add(Download(it, DOWN_FINISH))
        }
        mainModel.setSearchHintText("下载页")
        mAdapter = DownloadAllAdapter(requireContext(), list) {
            val item = list[it].downloadItem
            when (list[it].isRunning) {
                DOWN_ING -> {
                    DownloadUtil.pause(requireContext(), item.taskId)
                    list[it].isRunning = DOWN_STOP
                }
                DOWN_STOP -> {
                    DownloadUtil.downloadOne(requireContext(), item.title, item.chapterTitle, item.chapterPath)
                    list[it].isRunning = DOWN_ING
                }
                DOWN_FINISH -> {
                    mBinding.videoView.visibility = View.VISIBLE
                    playLocalVideo(item)
                }
                else -> {}
            }
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun initEvent() {

        mainModel.onBackPress = {
            val back = !mBinding.videoView.onBackPressed()
            back
        }
        DownloadUtil.runningListener = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        percent = task.percent
                    }
                }
                if (it.isRunning == DOWN_STOP) it.isRunning = DOWN_ING
            }
            mAdapter.notifyDataSetChanged()
        }
    }


    private fun playLocalVideo(item: DownloadItem) {

        mBinding.videoView.setUrl(item.chapterPath)
        val controller = MyStandardVideoController(requireContext())

        val completeView = CompleteView(requireContext())
        val errorView = ErrorView(requireContext())
        val prepareView = MyPrepareView(requireContext())
        prepareView.setClickStart()
        val titleView = TitleView(requireContext())
        titleView.setTitle("${item.title}-${item.chapterPath}")
        controller.addControlComponent(completeView, errorView, prepareView, titleView)
        controller.addControlComponent(VodControlView(requireContext()))
        controller.addControlComponent(GestureView(requireContext()))
        controller.setCanChangePosition(true)

        mBinding.videoView.setVideoController(controller)
        mBinding.videoView.start()
    }

    override fun setNowPage(): String {
        return PAGE_DOWN
    }


    override fun onPause() {
        super.onPause()
        mBinding.videoView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.videoView.release()
    }

}
