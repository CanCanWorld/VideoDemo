package com.zrq.videodemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.videodemo.R
import com.zrq.videodemo.bean.Download
import com.zrq.videodemo.databinding.ItemDownloadingBinding
import com.zrq.videodemo.utils.Constants.DOWN_FAIL
import com.zrq.videodemo.utils.Constants.DOWN_COMPLETE
import com.zrq.videodemo.utils.Constants.DOWN_PRE
import com.zrq.videodemo.utils.Constants.DOWN_RUN
import com.zrq.videodemo.utils.Constants.DOWN_STOP

class DownloadingAdapter(
    private val context: Context,
    private val list: MutableList<Download>,
    private val onItemClickListener: (Int) -> Unit
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
                else -> {
                    tvDownloadInfo.text = "下载暂停"
                    tvDownloadInfo.setTextColor(context.resources.getColor(R.color.grey_))
                }
            }
            progressbar.progress = data.percent
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}