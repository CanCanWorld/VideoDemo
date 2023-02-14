package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zrq.videodemo.adapter.PlayerChapterAdapter
import com.zrq.videodemo.adapter.RecordAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.bean.ContentData
import com.zrq.videodemo.databinding.FragmentPlayerBinding
import com.zrq.videodemo.db.bean.Message
import com.zrq.videodemo.utils.Constants.PAGE_PLAYER
import com.zrq.videodemo.view.MyPrepareView
import com.zrq.videodemo.view.MyStandardVideoController
import xyz.doikki.videocontroller.component.*
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
    private var isAutoPause = false

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
        mBinding.apply {
            loadChapter()
            val controller = MyStandardVideoController(requireContext())

            val completeView = CompleteView(requireContext())
            val errorView = ErrorView(requireContext())
            val prepareView = MyPrepareView(requireContext())
            prepareView.setClickStart()
            val titleView = TitleView(requireContext())
            mainModel.contentData?.let {
                titleView.setTitle("${it.title}-${it.chapterList[it.pos].title}")
            }
            controller.addControlComponent(completeView, errorView, prepareView, titleView)
            controller.addControlComponent(VodControlView(requireContext()))
            controller.addControlComponent(GestureView(requireContext()))
            controller.setCanChangePosition(true)

            videoView.setVideoController(controller)
            videoView.start()

            doMessageDb()

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadChapter() {
        mainModel.contentData?.let {
            initView(it)
            chapterList.clear()
            chapterList.addAll(it.chapterList)
            chapterAdapter.notifyDataSetChanged()
            mBinding.videoView.setUrl(it.chapterList[it.pos].chapterPath)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun doMessageDb() {
        recordList.clear()
        mainModel.contentData?.let {
            mainModel.db?.messageDao()?.let { dao ->
                dao.insertMsg(Message(it.title, Date().time, chapterList[it.pos].title))
                dao.queryAllByTitle(it.title)
                    .asReversed()
                    .forEach { msg ->
                        val sdfYear = SimpleDateFormat("yyyy", Locale.CHINA)
                        val that = SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(msg.currentTime).toLong()
                        val nowDay = SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Date()).toLong()
                        if (sdfYear.format(msg.currentTime) == sdfYear.format(Date().time)) {
                            val string = when (nowDay - that) {
                                0L -> "今天"
                                1L -> "昨天"
                                2L -> "前天"
                                else -> SimpleDateFormat("MM月dd日", Locale.CHINA).format(msg.currentTime)
                            }
                            recordList.add(
                                string + SimpleDateFormat(" HH时mm分", Locale.CHINA).format(msg.currentTime) +
                                        "观看了《" +
                                        msg.title + "》" +
                                        msg.chapter
                            )
                        } else
                            recordList.add(
                                SimpleDateFormat("yyyy年MM月dd日 HH时mm分", Locale.CHINA).format(msg.currentTime) +
                                        "观看了《" +
                                        msg.title + "》" +
                                        msg.chapter
                            )
                    }
                recordAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(content: ContentData) {
        mBinding.apply {
            tvTitle.text = content.title
            mainModel.setSearchHintText(content.title + "：" + content.chapterList[content.pos].title)
            rvChapter.scrollToPosition(content.pos)
        }
    }

    private fun changeChapter(pos: Int) {
        if (mainModel.contentData?.pos == pos) return
        mainModel.contentData?.pos = pos
        mBinding.videoView.release()
        mBinding.videoView.setUrl(chapterList[pos].chapterPath)
        mBinding.videoView.start()
        doMessageDb()
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
        if (isAutoPause) {
            mBinding.videoView.start()
        }
    }

    private companion object {
        const val TAG = "PlayerFragment"
    }

    override fun onPause() {
        super.onPause()
        if (mBinding.videoView.isPlaying) {
            mBinding.videoView.pause()
            isAutoPause = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.videoView.release()
    }

    override fun setNowPage(): String = PAGE_PLAYER
}
