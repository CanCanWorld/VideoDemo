package com.zrq.videodemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zrq.videodemo.databinding.FragmentContentBinding

class ContentFragment : BaseFragment<FragmentContentBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentContentBinding {
        return FragmentContentBinding.inflate(inflater, container, false)
    }

    override fun initData() {
    }

    override fun initEvent() {
    }

}
