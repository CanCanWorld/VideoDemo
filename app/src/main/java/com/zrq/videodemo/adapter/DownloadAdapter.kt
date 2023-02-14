package com.zrq.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrq.videodemo.R
import com.zrq.videodemo.bean.Chapter
import com.zrq.videodemo.databinding.ItemDownloadBinding
import com.zrq.videodemo.utils.Constants.DOWN_FAIL
import com.zrq.videodemo.utils.Constants.DOWN_COMPLETE
import com.zrq.videodemo.utils.Constants.DOWN_RUN
import com.zrq.videodemo.utils.Constants.DOWN_NON

class DownloadAdapter(
    private val context: Context,
    private val list: MutableList<Chapter>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<VH<ItemDownloadBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<ItemDownloadBinding> {
        return VH(ItemDownloadBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH<ItemDownloadBinding>, position: Int) {
        val item = list[position]
        holder.binding.apply {
            tvTitle.text = item.title
            when (item.state) {
                DOWN_NON -> {
                    ivState.visibility = View.GONE
                }
                DOWN_RUN -> {
                    ivState.visibility = View.VISIBLE
                    ivState.setImageResource(R.drawable.ic_xiazaizhong)
                }
                DOWN_COMPLETE -> {
                    ivState.visibility = View.VISIBLE
                    ivState.setImageResource(R.drawable.ic_xiazaiwancheng)
                }
                DOWN_FAIL -> {
                    ivState.visibility = View.VISIBLE
                    ivState.setImageResource(R.drawable.ic_xiazaishibai)
                }
                else -> {}
            }
            root.setOnClickListener { onItemClickListener(position) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}