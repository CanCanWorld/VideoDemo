package com.zrq.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.videodemo.bean.Download
import com.zrq.videodemo.databinding.ItemDownloadedBinding

class DownloadingAdapter(
    private val context: Context,
    private val list: MutableList<Download>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemDownloadedBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemDownloadedBinding> {
        return VH(ItemDownloadedBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemDownloadedBinding>, position: Int) {
        val data = list[position].downloadItem
        holder.binding.apply {
            Glide.with(context)
                .load(data.cover)
                .into(ivCover)
            tvTitle.text = data.title
            tvChapter.text = data.chapterTitle
            tvProgress.text = data.percent.toString()
            progressbar.progress = data.percent
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}