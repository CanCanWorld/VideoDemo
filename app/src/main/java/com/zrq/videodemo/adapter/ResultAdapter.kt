package com.zrq.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.videodemo.bean.DataItem
import com.zrq.videodemo.databinding.ItemResultBinding

class ResultAdapter(
    private val context: Context,
    private val list: MutableList<DataItem>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemResultBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemResultBinding> {
        return VH(ItemResultBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemResultBinding>, position: Int) {
        val dataItem = list[position]
        holder.binding.apply {
            Glide.with(context)
                .load(dataItem.cover)
                .into(ivCover)
            tvTitle.text = dataItem.title
            tvDesc.text = dataItem.descs.trim()
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}