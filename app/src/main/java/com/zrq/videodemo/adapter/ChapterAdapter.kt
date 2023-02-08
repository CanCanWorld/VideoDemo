package com.zrq.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.databinding.ItemChapterBinding

class ChapterAdapter(
    private val context: Context,
    private val list: MutableList<Chapter>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemChapterBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemChapterBinding> {
        return VH(ItemChapterBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemChapterBinding>, position: Int) {
        val chapter = list[position]
        holder.binding.apply {
            tvTitle.text = chapter.title
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}