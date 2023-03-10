package com.zrq.videodemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.zrq.videodemo.MainModel

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    protected lateinit var mBinding: T
    protected lateinit var mainModel: MainModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainModel = ViewModelProvider(requireActivity())[MainModel::class.java]
        mBinding = providedViewBinding(inflater, container)
        mainModel.nowPage = setNowPage()
        mainModel.setSearchText("")
        initData()
        initEvent()
        mainModel.clearBottomFocus()
        return mBinding.root
    }

    abstract fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    abstract fun initData()

    abstract fun initEvent()

    abstract fun setNowPage(): String

}
