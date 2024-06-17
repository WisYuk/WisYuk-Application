package com.wisyuk.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wisyuk.data.response.RecommendationsItem
import com.wisyuk.databinding.ItemTourismBinding
import com.wisyuk.ui.home.detail_home.DetailActivity

class RecommendationAdapter(private val goAt: String) : ListAdapter<RecommendationsItem, RecommendationAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTourismBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, goAt)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tourism = getItem(position)
        if (tourism != null) {
            holder.bind(tourism)
        }
    }
    class MyViewHolder(private val binding: ItemTourismBinding, private val goAt: String) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: RecommendationsItem) {
            Glide.with(itemView.context)
                .load(item.details.image)
                .into(binding.ivItemPhoto)

            binding.tvItemName.text = item.details.name
            binding.tvItemDescription.text = item.details.description
            binding.tvSubtitle.text = item.details.city
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.TOURISM, item)
                intent.putExtra(DetailActivity.EXTRA_GOAT, goAt)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "photo"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvSubtitle, "date"),
                        Pair(binding.tvItemDescription, "description")
                    )

                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }

    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationsItem>() {
            override fun areItemsTheSame(
                oldItem: RecommendationsItem,
                newItem: RecommendationsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RecommendationsItem,
                newItem: RecommendationsItem
            ): Boolean {
                return oldItem == newItem
            }
        }

    }
}