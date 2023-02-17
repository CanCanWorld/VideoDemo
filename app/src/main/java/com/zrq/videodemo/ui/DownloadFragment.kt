package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.DownloadedAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.bean.ContentData
import com.zrq.videodemo.bean.Download
import com.zrq.videodemo.databinding.FragmentDownloadBinding
import com.zrq.videodemo.utils.Constants.PAGE_DOWNLOADED
import com.zrq.videodemo.utils.OtherUtils

class DownloadFragment : BaseFragment<FragmentDownloadBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDownloadBinding {
        return FragmentDownloadBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: DownloadedAdapter
    private val list = mutableListOf<Download>()
    private var isEdit = false
    private val selectList = mutableListOf<Download>()

    @SuppressLint("SetTextI18n")
    override fun initData() {
        mainModel.reloadLocalVideo()
        list.clear()
        mainModel.localVideo.forEach {
            list.add(Download(it))
        }
        mainModel.setSearchHintText("下载完成页")
        mAdapter = DownloadedAdapter(requireContext(), list, { pos ->
            val chapterList = mutableListOf<Chapter>()
            list.forEach { chapterList.add(Chapter(it.downloadItem.chapterPath, it.downloadItem.chapterTitle)) }
            mainModel.contentData = ContentData(
                "未知", chapterList, list[pos].downloadItem.cover,
                "未知", "未知", "未知", "未知", list[pos].downloadItem.title,
                "未知", "未知", "未知", pos
            )
            mainModel.contentData?.pos = pos
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.action_global_playerFragment)
        }, { pos, isSelect ->
            mBinding.apply {
                list[pos].isSelect = isSelect
                if (isSelect) {
                    if (!selectList.contains(list[pos]))
                        selectList.add(list[pos])
                } else {
                    selectList.remove(list[pos])
                }
                if (selectList.size == 0) {
                    tvDelete.text = "删除"
                    tvDelete.isEnabled = false
                    tvDelete.setTextColor(requireActivity().resources.getColor(R.color.grey__))
                } else {
                    tvDelete.text = "删除(${selectList.size})"
                    tvDelete.isEnabled = true
                    tvDelete.setTextColor(requireActivity().resources.getColor(R.color.pink))
                }
                Log.d(TAG, "initData: ${selectList.size}-->${list.size}")
                checkbox.isChecked = selectList.size == list.size
            }
        })
        mBinding.apply {
            recyclerView.adapter = mAdapter
            val queryAll = mainModel.db?.downloadDao()?.queryAll() ?: mutableListOf()
            val downNum = queryAll.size
            if (downNum == 0) {
                RlDownloading.visibility = View.GONE
            } else {
                RlDownloading.visibility = View.VISIBLE
                val path = queryAll[0].cover
                Glide.with(this@DownloadFragment)
                    .load(path)
                    .into(ivCover)
                tvDownloadInfo.text = downNum.toString() + "个视频正在下载"
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initEvent() {
        mBinding.apply {
            RlDownloading.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(R.id.downloadingFragment)
            }

            ivBack.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .popBackStack()
            }
            tvDelete.setOnClickListener {
                val num = selectList.size
                selectList.forEach {
                    mainModel.localVideo.remove(it.downloadItem)
                    OtherUtils.deleteFile(it.downloadItem.chapterPath)
                    list.remove(it)
                }
                selectList.clear()
                list.forEach { it.isEdit = false }
                rlEdit.visibility = View.GONE
                mAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "删除" + num + "项数据成功", Toast.LENGTH_SHORT).show()
            }
            checkbox.setOnClickListener {
                list.forEach {
                    selectList.clear()
                    if (checkbox.isChecked) {
                        selectList.addAll(list)
                    }
                    it.isSelect = checkbox.isChecked
                }
                mAdapter.notifyDataSetChanged()
            }

            tvEdit.setOnClickListener {
                if (isEdit) {
                    list.forEach { it.isEdit = false }
                    rlEdit.visibility = View.GONE
                } else {
                    list.forEach { it.isEdit = true }
                    rlEdit.visibility = View.VISIBLE
                }
                isEdit = !isEdit
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun setNowPage(): String = PAGE_DOWNLOADED

    private companion object {
        const val TAG = "DownloadFragment"
    }
}
