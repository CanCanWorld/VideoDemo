package com.zrq.videodemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.videodemo.R
import com.zrq.videodemo.bean.Download
import com.zrq.videodemo.databinding.ItemDownloadingBinding
import com.zrq.videodemo.utils.Constants.DOWN_FAIL
import com.zrq.videodemo.utils.Constants.DOWN_PRE
import com.zrq.videodemo.utils.Constants.DOWN_RUN
import com.zrq.videodemo.utils.Constants.DOWN_STOP
import com.zrq.videodemo.utils.Constants.DOWN_WAIT

class DownloadingAdapter(
    private val context: Context,
    private val list: MutableList<Download>,
    private val onItemClickListener: (Int) -> Unit,
    private val onItemSelectListener: (Int, Boolean) -> Unit,
) : RecyclerView.Adapter<VH<ItemDownloadingBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemDownloadingBinding> {
        return VH(ItemDownloadingBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH<ItemDownloadingBinding>, position: Int) {
        val data = list[position].downloadItem
        holder.binding.apply {
            Glide.with(context)
                .load(data.cover)
                .into(ivCover)
            tvTitle.text = data.title
            tvChapter.text = data.chapterTitle
            when (list[position].state) {
                DOWN_FAIL -> {
                    tvDownloadInfo.text = "下载失败"
                    tvDownloadInfo.setTextColor(context.resources.getColor(R.color.red))
                }
                DOWN_RUN -> {
                    tvDownloadInfo.text = "下载中..${data.percent}%"
                    tvDownloadInfo.setTextColor(context.resources.getColor(R.color.pink))
                }
                DOWN_PRE -> {
                    tvDownloadInfo.text = "加载中.."
                    tvDownloadInfo.setTextColor(context.resources.getColor(R.color.pink))
                }
                DOWN_STOP -> {
                    tvDownloadInfo.text = "下载暂停"
                    tvDownloadInfo.setTextColor(context.resources.getColor(R.color.grey_))
                }
                DOWN_WAIT -> {
                    tvDownloadInfo.text = "等待其他"
                    tvDownloadInfo.setTextColor(context.resources.getColor(R.color.grey_))
                }
                else -> {
                    tvDownloadInfo.text = "下载暂停"
                    tvDownloadInfo.setTextColor(context.resources.getColor(R.color.grey_))
                }
            }
            progressbar.progress = data.percent
            radioBtn.isChecked = list[position].isSelect
            val select = list[position].isEdit
            if (select) {
                radioBtn.visibility = View.VISIBLE
                selectBtn.visibility = View.VISIBLE
            } else {
                radioBtn.visibility = View.GONE
                selectBtn.visibility = View.GONE
            }
            selectBtn.setOnClickListener { radioBtn.isChecked = !radioBtn.isChecked }
            radioBtn.setOnCheckedChangeListener { _, isChecked ->
                onItemSelectListener(position, isChecked)
            }
            llRoot.setOnClickListener {
                onItemClickListener(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}