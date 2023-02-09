package com.zrq.videodemo.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.zrq.videodemo.R
import com.zrq.videodemo.bean.Result
import com.zrq.videodemo.databinding.FragmentSearchBinding
import com.zrq.videodemo.utils.Constants.BASE_URL
import com.zrq.videodemo.utils.Constants.PAGE_SEARCH
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
                mainModel.keyword = keyword
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(R.id.resultFragment)
            }

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    tvSearch.callOnClick()
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
                }
                false
            }

        }
    }

    override fun setNowPage(): String = PAGE_SEARCH

}
