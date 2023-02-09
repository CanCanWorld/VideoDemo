package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.zrq.videodemo.adapter.PlayerChapterAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.databinding.FragmentPlayerBinding
import com.zrq.videodemo.utils.Constants.PAGE_PLAYER
import xyz.doikki.videocontroller.StandardVideoController

class PlayerFragment : BaseFragment<FragmentPlayerBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: PlayerChapterAdapter
    private val list = mutableListOf<Chapter>()

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun initData() {

        mAdapter = PlayerChapterAdapter(requireContext(), list) {
            changeChapter(it)
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }

        mainModel.content?.let {
            mBinding.apply {
                if (isAdded) {
                    Glide.with(requireActivity())
                        .load(it.data.cover)
                        .into(ivCover)
                }
                tvTitle.text = it.data.title
                tvDesc.text = "简述：" + it.data.descs.trim() +
                        "\n导演：" + it.data.director +
                        "\n演员：" + it.data.actor +
                        "\n地区：" + it.data.region +
                        "\n发布时间：" + it.data.releaseTime
                mainModel.setSearchHintText(it.data.title + "播放页")
                recyclerView.scrollToPosition(it.pos)

                list.clear()
                list.addAll(it.data.chapterList)
                mAdapter.notifyDataSetChanged()
                videoView.setUrl(it.data.chapterList[it.pos].chapterPath)
                val controller = StandardVideoController(requireContext())
                controller.addDefaultControlComponent(it.data.title, false)
                videoView.setVideoController(controller)
                videoView.start()
            }
        }
    }

    private fun changeChapter(pos: Int) {
        if (mainModel.content?.pos == pos) return
        mainModel.content?.pos = pos
        mBinding.videoView.release()
        mBinding.videoView.setUrl(list[pos].chapterPath)
        mBinding.videoView.start()
    }

    override fun initEvent() {

        mainModel.onBackPress = {
            val back = !mBinding.videoView.onBackPressed()
            back
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.videoView.resume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.videoView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.videoView.release()
    }

    override fun setNowPage(): String = PAGE_PLAYER
}
