package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.zrq.videodemo.bean.ContentData
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
    private var contentCache: ContentData? = null

    override fun initData() {
        mAdapter = ChapterAdapter(requireContext(), list) {
            mainModel.contentData?.pos = it
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.action_global_playerFragment)
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }

        loadContent()
        val queryAll = mainModel.db?.loveDao()?.queryAll()
        Log.d(TAG, "queryAll: $queryAll")

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun loadContent() {
        val url = "$BASE_URL$CONTENT/${mainModel.videoId}"
        HttpUtil.httpGet(url) { success, msg ->
            if (success) {
                val content = Gson().fromJson(msg, Content::class.java)
                mainModel.contentData = content.data
                Handler(Looper.getMainLooper()).post {
                    mBinding.apply {
                        if (isAdded) {
                            Glide.with(requireActivity())
                                .load(content.data.cover)
                                .into(ivCover)
                        }
                        tvTitle.text = content.data.title
                        mainModel.setSearchHintText("详情：" + content.data.title)
                        tvDesc.text = "简述：" + content.data.descs.trim() +
                                "\n导演：" + content.data.director +
                                "\n演员：" + content.data.actor +
                                "\n地区：" + content.data.region +
                                "\n发布时间：" + content.data.releaseTime
                        list.clear()
                        contentCache = content.data
                        checkList()
                        mAdapter.notifyDataSetChanged()

                        mainModel.db?.loveDao()?.let { dao ->
                            val loves = dao.queryAllByTitle(content.data.title)
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
        }
    }

    private fun checkList() {
        contentCache?.let { data ->
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
            list.addAll(data.chapterList)
        }
    }

    override fun onResume() {
        super.onResume()
        checkList()
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
                        Log.d(TAG, "dismiss: ")
                        checkList()
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
