package com.zrq.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.videodemo.databinding.ItemRecordBinding

class RecordAdapter(
    private val context: Context,
    private val list: MutableList<String>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemRecordBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemRecordBinding> {
        return VH(ItemRecordBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemRecordBinding>, position: Int) {
        holder.binding.apply {
            tvRecord.text = list[position]
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}