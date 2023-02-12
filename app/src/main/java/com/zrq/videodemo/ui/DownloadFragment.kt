package com.zrq.videodemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zrq.videodemo.adapter.DownloadAllAdapter
import com.zrq.videodemo.bean.DownloadItem
import com.zrq.videodemo.databinding.FragmentDownloadBinding
import com.zrq.videodemo.utils.Constants.PAGE_DOWN
import com.zrq.videodemo.utils.DownloadUtil


class DownloadFragment : BaseFragment<FragmentDownloadBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDownloadBinding {
        return FragmentDownloadBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: DownloadAllAdapter
    private val list = mutableListOf<DownloadItem>()

    override fun initData() {
        list.clear()
        list.addAll(mainModel.downloadItems)
        mAdapter = DownloadAllAdapter(requireContext(), list) {

        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    override fun initEvent() {
        DownloadUtil.mCallback = {
            list[0].progress = it
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun setNowPage(): String {
        return PAGE_DOWN
    }

}
