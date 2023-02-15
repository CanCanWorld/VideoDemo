package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.DownloadedAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.bean.ContentData
import com.zrq.videodemo.databinding.FragmentDownloadBinding
import com.zrq.videodemo.db.bean.DownloadItem
import com.zrq.videodemo.utils.Constants.PAGE_DOWNLOADED
import com.zrq.videodemo.utils.OtherUtils

class DownloadFragment : BaseFragment<FragmentDownloadBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDownloadBinding {
        return FragmentDownloadBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: DownloadedAdapter
    private val list = mutableListOf<DownloadItem>()

    @SuppressLint("SetTextI18n")
    override fun initData() {

        list.clear()
        list.addAll(mainModel.localVideo)
        mainModel.setSearchHintText("下载完成页")
        mAdapter = DownloadedAdapter(requireContext(), list) { pos ->
            val chapterList = mutableListOf<Chapter>()
            list.forEach { chapterList.add(Chapter(it.chapterPath, it.chapterTitle)) }
            mainModel.contentData = ContentData(
                "未知", chapterList, list[pos].cover,
                "未知", "未知", "未知", "未知", list[pos].title,
                "未知", "未知", "未知", pos
            )
            mainModel.contentData?.pos = pos
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.action_global_playerFragment)
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
            val queryAll = mainModel.db?.downloadDao()?.queryAll() ?: mutableListOf()
            val downNum = queryAll.size
            if (downNum == 0) {
                RlDownloading.visibility = View.GONE
            } else {
                RlDownloading.visibility = View.VISIBLE
                val path = mainModel.db!!.downloadDao()!!.queryAll()[0].cover
                Glide.with(this@DownloadFragment)
                    .load(path)
                    .into(ivCover)
                tvDownloadInfo.text = downNum.toString() + "个视频正在下载\n点击查看下载进度"
            }
        }
    }

    override fun initEvent() {
        mBinding.apply {
            RlDownloading.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(R.id.downloadingFragment)
            }
        }
    }

    override fun setNowPage(): String = PAGE_DOWNLOADED

    private companion object {
        const val TAG = "DownloadFragment"
    }
}
