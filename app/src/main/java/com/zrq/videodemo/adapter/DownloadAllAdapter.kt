package com.zrq.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.videodemo.bean.DownloadItem
import com.zrq.videodemo.databinding.ItemDownloadAllBinding

class DownloadAllAdapter(
    private val context: Context,
    private val list: MutableList<DownloadItem>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemDownloadAllBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemDownloadAllBinding> {
        return VH(ItemDownloadAllBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemDownloadAllBinding>, position: Int) {
        val data = list[position]
        holder.binding.apply {
            Glide.with(context)
                .load(data.cover)
                .into(ivCover)
            tvTitle.text = data.title
            tvChapter.text = data.chapter.title
            tvProgress.text = data.progress.toString()
            progressbar.progress = data.progress
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}