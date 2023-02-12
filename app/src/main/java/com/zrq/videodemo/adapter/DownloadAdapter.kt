package com.zrq.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.databinding.ItemDownloadBinding

class DownloadAdapter(
    private val context: Context,
    private val list: MutableList<Chapter>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemDownloadBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemDownloadBinding> {
        return VH(ItemDownloadBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemDownloadBinding>, position: Int) {
        val love = list[position]
        holder.binding.apply {
            tvTitle.text = love.title
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}