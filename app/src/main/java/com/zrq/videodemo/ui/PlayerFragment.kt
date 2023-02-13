package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zrq.videodemo.adapter.PlayerChapterAdapter
import com.zrq.videodemo.adapter.RecordAdapter
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.bean.Content
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
                val controller = MyStandardVideoController(requireContext())

                val completeView = CompleteView(requireContext())
                val errorView = ErrorView(requireContext())
                val prepareView = MyPrepareView(requireContext())
                prepareView.setClickStart()
                val titleView = TitleView(requireContext())
                titleView.setTitle(it.data.title)
                controller.addControlComponent(completeView, errorView, prepareView, titleView)
                controller.addControlComponent(VodControlView(requireContext()))
                controller.addControlComponent(GestureView(requireContext()))
                controller.setCanChangePosition(true)

                videoView.setVideoController(controller)
                videoView.start()

                doMessageDb()


            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun doMessageDb() {
        recordList.clear()
        mainModel.content?.let {
            mainModel.db?.messageDao()?.let { dao ->
                dao.insertMsg(Message(it.data.title, Date().time, chapterList[it.pos].title))
                dao.queryAllByTitle(it.data.title)
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
    }

    private companion object {
        const val TAG = "PlayerFragment"
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
