package com.zrq.videodemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.zrq.videodemo.R
import com.zrq.videodemo.bean.Result
import com.zrq.videodemo.databinding.FragmentSearchBinding
import com.zrq.videodemo.utils.Constants.BASE_URL
import com.zrq.videodemo.utils.Constants.SEARCH
import com.zrq.videodemo.utils.HttpUtil

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun initData() {
    }

    override fun initEvent() {
        mBinding.apply {
            tvSearch.setOnClickListener {
                val keyword = etSearch.text.toString()
                val url = "$BASE_URL$SEARCH/$keyword/1/10"
                HttpUtil.httpGet(url) { success, msg ->
                    if (success) {
                        val result = Gson().fromJson(msg, Result::class.java)
                        mainModel.searchResult.postValue(result.data)
                    }
                }
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(R.id.resultFragment)
            }
        }
    }

}
