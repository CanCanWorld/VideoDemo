package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.ChapterAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.bean.Content
import com.zrq.videodemo.databinding.FragmentContentBinding
import com.zrq.videodemo.utils.Constants.BASE_URL
import com.zrq.videodemo.utils.Constants.CONTENT
import com.zrq.videodemo.utils.Constants.PAGE_CONTENT
import com.zrq.videodemo.utils.HttpUtil

class ContentFragment : BaseFragment<FragmentContentBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentContentBinding {
        return FragmentContentBinding.inflate(inflater, container, false)
    }

    private val list = mutableListOf<Chapter>()
    private lateinit var mAdapter: ChapterAdapter

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun initData() {

        mAdapter = ChapterAdapter(requireContext(), list) {
            mainModel.content?.pos = it
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.action_global_playerFragment)
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }

        val url = "$BASE_URL$CONTENT/${mainModel.videoId}"
        HttpUtil.httpGet(url) { success, msg ->
            if (success) {
                val content = Gson().fromJson(msg, Content::class.java)
                mainModel.content = content
                Handler(Looper.getMainLooper()).post {
                    mBinding.apply {
                        if (isAdded) {
                            Glide.with(requireActivity())
                                .load(content.data.cover)
                                .into(ivCover)
                        }
                        tvTitle.text = content.data.title
                        mainModel.setSearchHintText(content.data.title+"详情页")
                        tvDesc.text = "简述：" + content.data.descs.trim() +
                                "\n导演：" + content.data.director +
                                "\n演员：" + content.data.actor +
                                "\n地区：" + content.data.region +
                                "\n发布时间：" + content.data.releaseTime
                        list.clear()
                        list.addAll(content.data.chapterList)
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun initEvent() {
    }

    override fun setNowPage(): String = PAGE_CONTENT

}
