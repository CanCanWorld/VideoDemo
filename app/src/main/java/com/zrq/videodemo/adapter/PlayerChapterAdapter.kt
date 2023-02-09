package com.zrq.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.databinding.ItemPlayerChapterBinding

class PlayerChapterAdapter(
    private val context: Context,
    private val list: MutableList<Chapter>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemPlayerChapterBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemPlayerChapterBinding> {
        return VH(ItemPlayerChapterBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemPlayerChapterBinding>, position: Int) {
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