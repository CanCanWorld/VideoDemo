package com.zrq.videodemo.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.DownloadingAdapter
import com.zrq.videodemo.bean.Download
import com.zrq.videodemo.databinding.FragmentDownloadingBinding
import com.zrq.videodemo.utils.Constants.DOWN_FAIL
import com.zrq.videodemo.utils.Constants.DOWN_PRE
import com.zrq.videodemo.utils.Constants.DOWN_RUN
import com.zrq.videodemo.utils.Constants.DOWN_STOP
import com.zrq.videodemo.utils.Constants.PAGE_DOWNLOADING
import com.zrq.videodemo.utils.DownloadListener
import com.zrq.videodemo.utils.DownloadUtil

class DownloadingFragment : BaseFragment<FragmentDownloadingBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDownloadingBinding {
        return FragmentDownloadingBinding.inflate(inflater, container, false)
    }

    private lateinit var mAdapter: DownloadingAdapter
    private val list = mutableListOf<Download>()
    private val downloadListener = DownloadListener()
    private var isEdit = false
    private val selectList = mutableListOf<Download>()

    @SuppressLint("SetTextI18n")
    override fun initData() {
        mainModel.db?.downloadDao()?.let {
            list.clear()
            it.queryAll().forEach { item ->
                list.add(Download(item))
            }

            selectList.clear()
        }
        mainModel.setSearchHintText("下载页")
        mAdapter = DownloadingAdapter(requireContext(), list, {
            val item = list[it].downloadItem
            when (list[it].state) {
                DOWN_RUN -> {
                    DownloadUtil.pause(requireContext(), item.taskId)
                    list[it].state = DOWN_STOP
                }
                DOWN_STOP -> {
                    DownloadUtil.downloadOne(requireContext(), item.title, item.chapterTitle, item.chapterPath)
                    list[it].state = DOWN_RUN
                }
            }
        }, { pos, isSelect ->
            mBinding.apply {
                list[pos].isSelect = isSelect
                if (isSelect) {
                    if (!selectList.contains(list[pos]))
                        selectList.add(list[pos])
                } else {
                    selectList.remove(list[pos])
                }
                if (selectList.size == 0) {
                    tvDelete.text = "删除"
                    tvDelete.isEnabled = false
                    tvDelete.setTextColor(requireActivity().resources.getColor(R.color.grey__))
                } else {
                    tvDelete.text = "删除(${selectList.size})"
                    tvDelete.isEnabled = true
                    tvDelete.setTextColor(requireActivity().resources.getColor(R.color.pink))
                }
                Log.d(TAG, "initData: ${selectList.size}-->${list.size}")
                checkbox.isChecked = selectList.size == list.size
            }
        })
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initEvent() {
        mBinding.apply {
            ivBack.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .popBackStack()
            }
            tvEdit.setOnClickListener {
                if (isEdit) {
                    list.forEach { it.isEdit = false }
                    rlEdit.visibility = View.GONE
                } else {
                    list.forEach { it.isEdit = true }
                    rlEdit.visibility = View.VISIBLE
                }
                isEdit = !isEdit
                mAdapter.notifyDataSetChanged()
            }
            tvDelete.setOnClickListener {
                val num = selectList.size
                selectList.forEach {
                    DownloadUtil.delete(requireContext(), it.downloadItem.taskId)
                    mainModel.db?.downloadDao()?.delete(it.downloadItem)
                    list.remove(it)
                }
                selectList.clear()
                list.forEach { it.isEdit = false }
                rlEdit.visibility = View.GONE
                mAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "删除" + num + "项数据成功", Toast.LENGTH_SHORT).show()
            }
            checkbox.setOnClickListener {
                list.forEach {
                    selectList.clear()
                    if (checkbox.isChecked) {
                        selectList.addAll(list)
                    }
                    it.isSelect = checkbox.isChecked
                }
                mAdapter.notifyDataSetChanged()
            }
        }
        downloadListener.onRun = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        Log.d(TAG, "onRun: ${task.percent}")
                        percent = task.percent
                        it.state = DOWN_RUN
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        downloadListener.onStop = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        it.state = DOWN_STOP
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        downloadListener.onPre = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        it.state = DOWN_PRE
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        downloadListener.onResume = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        it.state = DOWN_RUN
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        downloadListener.onFail = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        it.state = DOWN_FAIL
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        downloadListener.onComplete = { task ->
            list.forEach {
                it.downloadItem.apply {
                    if (taskId == task.entity.id) {
                        mainModel.db?.downloadDao()?.delete(this)
                        list.remove(it)

                        val downloadingFiles = mainModel.db?.downloadDao()?.queryAll() ?: mutableListOf()
                        downloadingFiles.forEach {
                            mainModel.localVideo.removeIf { f -> f.title == it.title && f.chapterTitle == it.chapterTitle }
                        }
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        }
        DownloadUtil.addListener(downloadListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        DownloadUtil.addListener(downloadListener)
    }

    override fun setNowPage(): String = PAGE_DOWNLOADING

    private companion object {
        const val TAG = "DownloadingFragment"
    }

}
