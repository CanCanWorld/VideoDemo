package com.zrq.videodemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zrq.videodemo.databinding.FragmentPlayerBinding

class PlayerFragment : BaseFragment<FragmentPlayerBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    override fun initData() {
    }

    override fun initEvent() {
    }

}
