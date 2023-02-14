package com.zrq.videodemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.DownloadedAdapter
import com.zrq.videodemo.adapter.DownloadingAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.bean.ContentData
import com.zrq.videodemo.bean.Download
import com.zrq.videodemo.databinding.FragmentDownloadBinding
import com.zrq.videodemo.db.bean.DownloadItem
import com.zrq.videodemo.utils.Constants.DOWN_FINISH
import com.zrq.videodemo.utils.Constants.PAGE_DOWNLOADED
import com.zrq.videodemo.utils.OtherUtils

class DownloadFragment : BaseFragment<FragmentDownloadBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDownloadBinding {
        return FragmentDownloadBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: DownloadedAdapter
    private val list = mutableListOf<DownloadItem>()

    override fun initData() {
        mainModel.localVideo.clear()
        mainModel.localVideo.addAll(OtherUtils.listFiles(requireContext()))
        list.clear()
        list.addAll(mainModel.localVideo)
        mainModel.setSearchHintText("下载完成页")
        mAdapter = DownloadedAdapter(requireContext(), list) { pos ->
            val chapterList = mutableListOf<Chapter>()
            list.forEach { chapterList.add(Chapter(it.chapterPath, it.chapterTitle)) }
            mainModel.contentData = ContentData(
                "", chapterList, "",
                "", "", "", "", list[pos].title,
                "", "", "", pos
            )
            mainModel.contentData?.pos = pos
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.action_global_playerFragment)
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    override fun initEvent() {
        mBinding.apply {
            tvDownloading.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(R.id.downloadingFragment)
            }
        }
    }

    override fun setNowPage(): String = PAGE_DOWNLOADED
}
