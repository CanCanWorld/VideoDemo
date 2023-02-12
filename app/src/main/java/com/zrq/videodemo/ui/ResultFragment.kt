package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.ResultAdapter
import com.zrq.videodemo.bean.DataItem
import com.zrq.videodemo.bean.Result
import com.zrq.videodemo.databinding.FragmentResultBinding
import com.zrq.videodemo.utils.Constants.BASE_URL
import com.zrq.videodemo.utils.Constants.PAGE_COUNT
import com.zrq.videodemo.utils.Constants.PAGE_RESULT
import com.zrq.videodemo.utils.Constants.SEARCH
import com.zrq.videodemo.utils.HttpUtil

class ResultFragment : BaseFragment<FragmentResultBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentResultBinding {
        return FragmentResultBinding.inflate(inflater, container, false)
    }

    private val list = mutableListOf<DataItem>()
    private lateinit var mAdapter: ResultAdapter
    private var page = 1
    private var count = 0
    private var keyword = ""
    private var isRefresh = false

    override fun initData() {
        mAdapter = ResultAdapter(requireContext(), list) {
            mainModel.videoId = list[it].videoId
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.action_global_contentFragment)
            mainModel.clearBottomFocus()
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
        Log.d(TAG, "initData: ${mainModel.keywords}")

        keyword = mainModel.keywords.first
        mainModel.setSearchHintText("搜索页：$keyword")

        loadVideo()
    }

    override fun initEvent() {
        mainModel.onBackPress = {
            if (mainModel.nowPage == PAGE_RESULT)
                if (mainModel.keywords.size > 0)
                    mainModel.keywords.pop()
            true
        }
        mBinding.apply {

            refreshLayout.setOnRefreshListener {
                isRefresh = true
                page = 1
                loadVideo()
            }
            refreshLayout.setOnLoadMoreListener {
                if (PAGE_COUNT * page > count) {
                    mBinding.refreshLayout.finishLoadMoreWithNoMoreData()
                } else {
                    page++
                    loadVideo()
                }
            }
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 20)
                        mainModel.clearBottomFocus()
                }
            })
        }
        mainModel.apply {
            onRefreshClickListener = {
                if (mainModel.nowPage == PAGE_RESULT)
                    mBinding.refreshLayout.autoRefresh()
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadVideo() {
        val url = "$BASE_URL$SEARCH/$keyword/$page/$PAGE_COUNT"
        HttpUtil.httpGet(url) { success, msg ->
            if (success) {
                Handler(Looper.getMainLooper()).post {
                    val result = Gson().fromJson(msg, Result::class.java)
                    if (page == 1) {
                        count = result.count
                        list.clear()
                        if (isRefresh) {
                            Toast.makeText(requireContext(), "刷新成功，总共" + count + "条数据", Toast.LENGTH_SHORT).show()
                            isRefresh = false
                        }
                    }
                    result.data?.let { list.addAll(it) }
                    mAdapter.notifyDataSetChanged()
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            }
            mBinding.refreshLayout.finishLoadMore()
            mBinding.refreshLayout.finishRefresh()
        }
    }

    override fun setNowPage(): String = PAGE_RESULT

    companion object {
        private const val TAG = "ResultFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

}
