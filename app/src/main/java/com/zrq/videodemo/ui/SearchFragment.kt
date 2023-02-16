package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.Navigation
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.LoveAdapter
import com.zrq.videodemo.databinding.FragmentSearchBinding
import com.zrq.videodemo.db.bean.Love
import com.zrq.videodemo.utils.Constants
import com.zrq.videodemo.utils.Constants.PAGE_SEARCH
import com.zrq.videodemo.utils.HttpUtil
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: LoveAdapter
    private val list = mutableListOf<Love>()

    override fun initData() {
        mainModel.db?.loveDao()?.let {
            list.clear()
            list.addAll(it.queryAll())
        }
        mAdapter = LoveAdapter(requireContext(), list) {
            mainModel.videoId = list[it].videoId
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.action_global_contentFragment)
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
        mainModel.setSearchHintText("首页")
        loadTextByTime()
    }

    override fun initEvent() {
        mBinding.apply {
            tvText.setOnClickListener { loadTextByTime() }
            tvSearch.setOnClickListener {
                if (etSearch.text.toString() == "") {
                    Toast.makeText(requireContext(), "请输入关键字", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val keyword = etSearch.text.toString()
                mainModel.keywords.push(keyword)
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(R.id.action_global_resultFragment)
            }

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    tvSearch.callOnClick()
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
                }
                false
            }
            ivSetting.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(R.id.settingFragment)
            }
        }
    }

    private fun loadTextByTime() {
        val hour = SimpleDateFormat("HH", Locale.CHINA).format(Date()).toInt()
        Log.d(TAG, "loadTextByTime: $hour")
        when (hour) {
            in 6..10 -> {
                loadText(1)
            }
            in 11..20 -> {
                loadText(2)
            }
            else -> {
                loadText(3)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadText(type: Int) {
        when (type) {
            1 -> {
                HttpUtil.httpGetOnUi(Constants.GET_TEXT_1) { success, msg ->
                    if (success) {
                        try {
                            Log.d(TAG, "loadText: 1")
                            val text = JSONObject(msg).getJSONObject("result").getString("content")
                            mBinding.tvText.text = "「 $text 」"
                        } catch (e: Exception) {
                            e.printStackTrace()
                            loadText(4)
                        }
                    }
                }
            }
            2 -> {
                Log.d(TAG, "loadText: 2")
                HttpUtil.httpGetOnUi(Constants.GET_TEXT_2) { success, msg ->
                    if (success) {
                        try {
                            val text = JSONObject(msg).getJSONObject("result").getString("dialogue")
                            mBinding.tvText.text = "「 $text 」"
                        } catch (e: Exception) {
                            loadText(4)
                        }
                    }
                }
            }
            3 -> {
                Log.d(TAG, "loadText: 3")
                HttpUtil.httpGetOnUi(Constants.GET_TEXT_3) { success, msg ->
                    if (success) {
                        try {
                            val text = JSONObject(msg).getJSONObject("result").getString("content")
                            mBinding.tvText.text = "「 $text 」"
                        } catch (e: Exception) {
                            loadText(4)
                        }
                    }
                }
            }
            4 -> {
                Log.d(TAG, "loadText: 4")
                HttpUtil.httpGetOnUi(Constants.GET_TEXT) { success, msg ->
                    if (success) {
                        mBinding.tvText.text = "「 $msg 」"
                    }
                }
            }
            else -> {}
        }
    }

    override fun setNowPage(): String = PAGE_SEARCH

    private companion object {
        const val TAG = "SearchFragment"
    }

}
