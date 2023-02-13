package com.zrq.videodemo.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zrq.videodemo.MainModel
import com.zrq.videodemo.R
import com.zrq.videodemo.adapter.DownloadAdapter
import com.zrq.videodemo.bean.Content
import com.zrq.videodemo.databinding.BottomDownloadBinding
import com.zrq.videodemo.db.bean.DownloadItem
import com.zrq.videodemo.utils.Constants
import com.zrq.videodemo.utils.DownloadUtil
import com.zrq.videodemo.utils.OtherUtils

class DownloadBottomDialog(
    private val ctx: Context,
    private val activity: Activity,
    private val mainModel: MainModel,
    private val content: Content,
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
        val data = content.data
        mAdapter = DownloadAdapter(ctx, data.chapterList) {
            mainModel.db?.downloadDao()?.let { dao ->
                val taskId = DownloadUtil.downloadOne(ctx, data.title, data.chapterList[it].title, data.chapterList[it].chapterPath)
                if (taskId == -1L) {
                    Toast.makeText(context, "创建失败", Toast.LENGTH_SHORT).show()
                    return@DownloadAdapter
                }
                data.chapterList[it].state = Constants.DOWN_ING
                mAdapter.notifyItemChanged(it)
                val item = DownloadItem(
                    taskId, data.title, data.chapterList[it].title,
                    data.chapterList[it].chapterPath, data.cover, 0
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

            }
            tvCheckDownload.setOnClickListener {
                dismiss()
                Navigation.findNavController(activity, R.id.fragment_container)
                    .navigate(R.id.downloadFragment)
            }
        }
    }

    private companion object {
        private const val TAG = "DownloadBottomDialog"
    }


}