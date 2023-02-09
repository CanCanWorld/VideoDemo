package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.ChapterAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.databinding.FragmentPlayerBinding
import com.zrq.videodemo.utils.Constants.PAGE_PLAYER
import xyz.doikki.videocontroller.StandardVideoController

class PlayerFragment : BaseFragment<FragmentPlayerBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: ChapterAdapter
    private val list = mutableListOf<Chapter>()

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {

        mAdapter = ChapterAdapter(requireContext(), list) {
            Log.d("TAG", "play: $it")
            changeChapter(it)
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }

        mainModel.content?.let {
            mBinding.apply {
                Glide.with(this@PlayerFragment)
                    .load(it.data.cover)
                    .into(ivCover)
                tvTitle.text = it.data.title
                tvDesc.text = it.data.descs
                tvActor.text = it.data.actor

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
