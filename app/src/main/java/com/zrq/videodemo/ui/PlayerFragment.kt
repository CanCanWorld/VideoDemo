package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.zrq.videodemo.adapter.PlayerChapterAdapter
import com.zrq.videodemo.adapter.RecordAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.bean.Content
import com.zrq.videodemo.databinding.FragmentPlayerBinding
import com.zrq.videodemo.db.bean.Message
import com.zrq.videodemo.utils.Constants.PAGE_PLAYER
import xyz.doikki.videocontroller.StandardVideoController
import java.text.SimpleDateFormat
import java.util.*

class PlayerFragment : BaseFragment<FragmentPlayerBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    private lateinit var chapterAdapter: PlayerChapterAdapter
    private lateinit var recordAdapter: RecordAdapter
    private val chapterList = mutableListOf<Chapter>()
    private val recordList = mutableListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {

        chapterAdapter = PlayerChapterAdapter(requireContext(), chapterList) {
            changeChapter(it)
        }
        recordAdapter = RecordAdapter(requireContext(), recordList) {
        }
        mBinding.apply {
            rvChapter.adapter = chapterAdapter
            rvMsg.adapter = recordAdapter
        }
        mainModel.content?.let {
            initView(it)
            mBinding.apply {

                chapterList.clear()
                chapterList.addAll(it.data.chapterList)
                chapterAdapter.notifyDataSetChanged()
                videoView.setUrl(it.data.chapterList[it.pos].chapterPath)
                val controller = StandardVideoController(requireContext())
                controller.addDefaultControlComponent(it.data.title, false)
                videoView.setVideoController(controller)
                videoView.start()


                mainModel.db?.messageDao()?.let { dao ->
                    dao.insertMsg(Message(mainModel.keywords.first, Date().time, chapterList[it.pos].title))
                    dao.queryAll().forEach { msg ->
                        recordList.add(
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(msg.currentTime) +
                                    "观看了：" +
                                    msg.title +
                                    msg.chapter
                        )
                    }
                    recordAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(content: Content) {
        mBinding.apply {
            tvTitle.text = content.data.title
            mainModel.setSearchHintText(content.data.title + "：" + content.data.chapterList[content.pos].title)
            rvChapter.scrollToPosition(content.pos)
        }
    }

    private fun changeChapter(pos: Int) {
        if (mainModel.content?.pos == pos) return
        mainModel.content?.pos = pos
        mBinding.videoView.release()
        mBinding.videoView.setUrl(chapterList[pos].chapterPath)
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
