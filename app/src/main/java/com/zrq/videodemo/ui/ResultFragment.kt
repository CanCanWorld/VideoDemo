package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.ResultAdapter
import com.zrq.videodemo.bean.DataItem
import com.zrq.videodemo.databinding.FragmentResultBinding

class ResultFragment : BaseFragment<FragmentResultBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentResultBinding {
        return FragmentResultBinding.inflate(inflater, container, false)
    }

    private val list = mutableListOf<DataItem>()
    private lateinit var mAdapter: ResultAdapter

    override fun initData() {
        mAdapter = ResultAdapter(requireContext(), list) {
            mainModel.videoId = list[it].videoId
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.contentFragment)
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initEvent() {
        mainModel.searchResult.observe(this) {
            list.clear()
            list.addAll(it)
            mAdapter.notifyDataSetChanged()
        }
    }

}
