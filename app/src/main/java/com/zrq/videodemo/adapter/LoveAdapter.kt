package com.zrq.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrq.videodemo.databinding.ItemHomeIconBinding
import com.zrq.videodemo.db.bean.Love

class LoveAdapter(
    private val context: Context,
    private val list: MutableList<Love>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemHomeIconBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemHomeIconBinding> {
        return VH(ItemHomeIconBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemHomeIconBinding>, position: Int) {
        val love = list[position]
        holder.binding.apply {
            Glide.with(context)
                .load(love.cover)
                .into(ivIcon)
            tvTitle.text = love.title
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}