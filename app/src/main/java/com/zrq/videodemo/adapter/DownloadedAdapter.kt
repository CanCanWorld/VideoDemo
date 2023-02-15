package com.zrq.videodemo.adapter

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.videodemo.databinding.ItemDownloadedBinding
import com.zrq.videodemo.db.bean.DownloadItem

class DownloadedAdapter(
    private val context: Context,
    private val list: MutableList<DownloadItem>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemDownloadedBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemDownloadedBinding> {
        return VH(ItemDownloadedBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemDownloadedBinding>, position: Int) {
        val data = list[position]
        holder.binding.apply {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(data.chapterPath)
            ivCover.setImageBitmap(mediaMetadataRetriever.getFrameAtTime(1))
            tvTitle.text = data.title
            tvChapter.text = data.chapterTitle
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}