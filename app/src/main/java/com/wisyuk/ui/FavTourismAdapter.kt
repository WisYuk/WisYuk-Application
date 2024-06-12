package com.wisyuk.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wisyuk.data.response.FavTourismItem
import com.wisyuk.databinding.ItemTourismBinding
import com.wisyuk.ui.home.detail_home.DetailActivity
import com.wisyuk.utils.Utils.dateFormatted

class FavTourismAdapter : ListAdapter<FavTourismItem, FavTourismAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTourismBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tourism = getItem(position)
        if (tourism != null) {
            holder.bind(tourism)
        }
    }
    class MyViewHolder(private val binding: ItemTourismBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: FavTourismItem) {

            binding.tvItemName.text = item.name
            binding.tvItemDate.text = item.goAt.dateFormatted() // ?

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)

                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavTourismItem>() {
            override fun areItemsTheSame(
                oldItem: FavTourismItem,
                newItem: FavTourismItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FavTourismItem,
                newItem: FavTourismItem
            ): Boolean {
                return oldItem == newItem
            }
        }

    }


}