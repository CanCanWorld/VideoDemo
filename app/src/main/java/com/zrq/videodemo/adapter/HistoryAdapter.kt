package com.zrq.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.videodemo.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val context: Context,
    private val list: MutableList<String>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemHistoryBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemHistoryBinding> {
        return VH(ItemHistoryBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemHistoryBinding>, position: Int) {
        holder.binding.apply {
            tvTitle.text = list[position]
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}