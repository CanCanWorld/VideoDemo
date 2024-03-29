package com.zrq.videodemo.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zrq.videodemo.MainModel
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.DownloadAdapter
import com.zrq.videodemo.bean.ContentData
import com.zrq.videodemo.databinding.BottomDownloadBinding
import com.zrq.videodemo.db.bean.DownloadItem
import com.zrq.videodemo.utils.Constants.DOWN_COMPLETE
import com.zrq.videodemo.utils.Constants.DOWN_RUN
import com.zrq.videodemo.utils.DownloadUtil
import com.zrq.videodemo.utils.OtherUtils

class DownloadBottomDialog(
    private val ctx: Context,
    private val activity: Activity,
    private val mainModel: MainModel,
    private val content: ContentData,
    private val onDismiss: () -> Unit
) : BottomSheetDialog(ctx) {

    private lateinit var mBinding: BottomDownloadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = BottomDownloadBinding.inflate(LayoutInflater.from(context))
        initData()
        initEvent()
        setContentView(mBinding.root)

        if (window != null) {
            val lp = window?.attributes
            if (lp != null) {
                lp.width = OtherUtils.getWindowWidth(activity)
                lp.height = OtherUtils.getWindowHeight(activity)
                window!!.attributes = lp
            }
        }
        val layoutParams = mBinding.root.layoutParams
        layoutParams.width = OtherUtils.getWindowWidth(activity)
        layoutParams.height = OtherUtils.getWindowHeight(activity) / 3 * 2
        mBinding.root.layoutParams = layoutParams

        setCanceledOnTouchOutside(true)
    }

    private lateinit var mAdapter: DownloadAdapter

    private fun initData() {
        mAdapter = DownloadAdapter(ctx, content.chapterList) {
            when (content.chapterList[it].state) {
                DOWN_RUN -> {
                    Toast.makeText(ctx, "已在下载列表中", Toast.LENGTH_SHORT).show()
                    return@DownloadAdapter
                }
                DOWN_COMPLETE -> {
                    Toast.makeText(ctx, "已下载完成", Toast.LENGTH_SHORT).show()
                    return@DownloadAdapter
                }
                else -> {}
            }
            mainModel.db?.downloadDao()?.let { dao ->
                val taskId = DownloadUtil.downloadOne(ctx, content.title, content.chapterList[it].title, content.chapterList[it].chapterPath)
                if (taskId == -1L) {
                    Toast.makeText(context, "创建失败", Toast.LENGTH_SHORT).show()
                    return@DownloadAdapter
                }
                content.chapterList[it].state = DOWN_RUN
                mAdapter.notifyItemChanged(it)
                val item = DownloadItem(
                    taskId, content.title, content.chapterList[it].title,
                    content.chapterList[it].chapterPath, content.cover, 0
                )
                dao.insertItem(item)
            }
        }
        mBinding.apply {
            recyclerView.adapter = mAdapter
        }
    }

    private fun initEvent() {
        mBinding.apply {
            ivClose.setOnClickListener {
                dismiss()
            }
            tvDownloadAll.setOnClickListener {
                Toast.makeText(ctx, "一个个点吧，这个没搞", Toast.LENGTH_SHORT).show()
            }
            tvCheckDownload.setOnClickListener {
                dismiss()
                Navigation.findNavController(activity, R.id.fragment_container)
                    .navigate(R.id.downloadFragment)
            }
        }
    }

    override fun dismiss() {
        onDismiss()
        super.dismiss()
        Log.d(TAG, "dismiss: ")
    }

    private companion object {
        private const val TAG = "DownloadBottomDialog"
    }


}