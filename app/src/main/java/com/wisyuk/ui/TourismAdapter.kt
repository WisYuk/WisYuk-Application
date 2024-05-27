package com.wisyuk.ui

import android.app.Activity
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wisyuk.data.response.ListTourismItem
import com.wisyuk.databinding.ItemTourismBinding
import com.wisyuk.ui.detail_home.DetailActivity

class TourismAdapter : ListAdapter<ListTourismItem, TourismAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTourismBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tourism = getItem(position)
        if (tourism != null) {
            holder.bind(tourism)
        }
    }
    class MyViewHolder(private val binding: ItemTourismBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListTourismItem) {
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .into(binding.ivItemPhoto)

            binding.tvItemName.text = item.name
            binding.tvItemDescription.text = item.description
            binding.tvItemDate.text = item.createdAt // ?

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(USER_ID, item.id)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "photo"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvItemDescription, "description")
                    )

                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListTourismItem>() {
            override fun areItemsTheSame(
                oldItem: ListTourismItem,
                newItem: ListTourismItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListTourismItem,
                newItem: ListTourismItem
            ): Boolean {
                return oldItem == newItem
            }
        }

        private const val USER_ID = "userId"

    }


}