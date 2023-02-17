package com.zrq.videodemo.adapter

import android.content.Context
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.videodemo.bean.Download
import com.zrq.videodemo.databinding.ItemDownloadedBinding

class DownloadedAdapter(
    private val context: Context,
    private val list: MutableList<Download>,
    private val onItemClickListener: (Int) -> Unit,
    private val onItemSelectListener: (Int, Boolean) -> Unit,
) : RecyclerView.Adapter<VH<ItemDownloadedBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemDownloadedBinding> {
        return VH(ItemDownloadedBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemDownloadedBinding>, position: Int) {
        val data = list[position].downloadItem
        holder.binding.apply {
            try {
                val mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(data.chapterPath)
                ivCover.setImageBitmap(mediaMetadataRetriever.getFrameAtTime(1))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            tvTitle.text = data.title
            tvChapter.text = data.chapterTitle
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
            llRoot.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}