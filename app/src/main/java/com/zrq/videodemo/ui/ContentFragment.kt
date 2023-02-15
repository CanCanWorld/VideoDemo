package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.ChapterAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.bean.Content
import com.zrq.videodemo.databinding.FragmentContentBinding
import com.zrq.videodemo.db.bean.Love
import com.zrq.videodemo.utils.Constants.BASE_URL
import com.zrq.videodemo.utils.Constants.CONTENT
import com.zrq.videodemo.utils.Constants.DOWN_COMPLETE
import com.zrq.videodemo.utils.Constants.DOWN_RUN
import com.zrq.videodemo.utils.Constants.PAGE_CONTENT
import com.zrq.videodemo.utils.HttpUtil
import com.zrq.videodemo.utils.OtherUtils
import com.zrq.videodemo.view.DownloadBottomDialog

class ContentFragment : BaseFragment<FragmentContentBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentContentBinding {
        return FragmentContentBinding.inflate(inflater, container, false)
    }

    private val list = mutableListOf<Chapter>()
    private lateinit var mAdapter: ChapterAdapter
    private var isLove = false
    private var downloadBottomDialog: DownloadBottomDialog? = null

    override fun initData() {
        mAdapter = ChapterAdapter(requireContext(), list) {
            mainModel.contentData?.pos = it
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.action_global_playerFragment)
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }

        if (mainModel.contentData?.chapterList == null) {
            loadContent {
                refreshUi()
            }
        } else {
            refreshUi()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshUi() {
        Handler(Looper.getMainLooper()).post {
            loadUi()
            checkContentData()
            list.clear()
            mainModel.contentData?.let { list.addAll(it.chapterList) }
            mAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadUi() {
        mainModel.contentData?.let { content ->
            mBinding.apply {
                if (isAdded) {
                    Glide.with(requireActivity())
                        .load(content.cover)
                        .into(ivCover)
                }
                tvTitle.text = content.title
                mainModel.setSearchHintText("详情：" + content.title)
                tvDesc.text = "简述：" + content.descs.trim() +
                        "\n导演：" + content.director +
                        "\n演员：" + content.actor +
                        "\n地区：" + content.region +
                        "\n发布时间：" + content.releaseTime
                mainModel.db?.loveDao()?.let { dao ->
                    val loves = dao.queryAllByTitle(content.title)
                    if (loves.size == 0) {
                        isLove = false
                        ivLove.setImageResource(R.drawable.ic_zhuifanshu)
                    } else {
                        isLove = true
                        ivLove.setImageResource(R.drawable.ic_yizhuifan)
                    }
                    ivLove.visibility = View.VISIBLE
                    btnDownload.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun loadContent(callback: () -> Unit) {
        val url = "$BASE_URL$CONTENT/${mainModel.videoId}"
        HttpUtil.httpGet(url) { success, msg ->
            if (success) {
                val content = Gson().fromJson(msg, Content::class.java)
                mainModel.contentData = content.data
                callback()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkContentData() {
        mainModel.contentData?.let { data ->
            mainModel.localVideo.forEach { local ->
                if (local.title == data.title) {
                    data.chapterList.forEach {
                        if (local.chapterTitle == it.title) {
                            it.state = DOWN_COMPLETE
                            it.chapterPath = local.chapterPath
                        }
                    }
                }
            }
            mainModel.db?.downloadDao()?.queryAll()?.forEach { download ->
                if (download.title == data.title) {
                    data.chapterList.forEach {
                        if (download.chapterTitle == it.title) {
                            it.state = DOWN_RUN
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainModel.contentData = null
    }

    override fun initEvent() {
        mBinding.ivLove.setOnClickListener {
            mainModel.db?.loveDao()?.let { dao ->
                mainModel.contentData?.let { data ->
                    if (isLove) {
                        val loves = dao.queryAllByTitle(data.title)
                        loves.forEach {
                            dao.delete(it)
                        }
                        mBinding.ivLove.setImageResource(R.drawable.ic_zhuifanshu)
                    } else {
                        dao.insertLike(Love(data.title, data.videoId, data.cover))
                        mBinding.ivLove.setImageResource(R.drawable.ic_yizhuifan)
                    }
                    isLove = !isLove
                }
            }
        }

        mBinding.btnDownload.setOnClickListener {
            if (downloadBottomDialog == null) {
                mainModel.contentData?.let {
                    downloadBottomDialog = DownloadBottomDialog(requireContext(), requireActivity(), mainModel, it) {
                        refreshUi()
                    }
                }
            }

            downloadBottomDialog!!.behavior.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                peekHeight = OtherUtils.getWindowHeight(requireActivity())
            }
            downloadBottomDialog!!.show()
        }

    }

    override fun setNowPage(): String = PAGE_CONTENT

    private companion object {
        const val TAG = "ContentFragment"
    }
}
