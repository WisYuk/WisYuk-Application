package com.wisyuk.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wisyuk.data.response.Details
import com.wisyuk.data.response.ListTourismItem
import com.wisyuk.data.response.RecommendationsItem
import com.wisyuk.databinding.ItemTourismBinding
import com.wisyuk.ui.home.detail_home.DetailActivity
import com.wisyuk.ui.home.detail_home.DetailActivity.Companion.EXTRA_GOAT
import com.wisyuk.ui.home.detail_home.DetailActivity.Companion.TOURISM
import com.wisyuk.utils.Utils.dateFormatted
import com.wisyuk.utils.Utils.dateFormattedGoAt

class TourismAdapter : ListAdapter<ListTourismItem, TourismAdapter.MyViewHolder>(DIFF_CALLBACK) {

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
        fun bind(item: ListTourismItem) {
            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.ivItemPhoto)

            binding.tvItemName.text = item.name
            binding.tvItemDescription.text = item.description
            if (item.goAt != null) {
                binding.tvSubtitle.text = item.goAt.dateFormatted()
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(TOURISM, convertToRecommendationsItem(item))
                    intent.putExtra(EXTRA_GOAT, item.goAt.dateFormattedGoAt())

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
            } else {
                binding.tvSubtitle.text = item.createdAt?.dateFormatted() // ?
            }

        }

        fun convertToRecommendationsItem(listTourismItem: ListTourismItem): RecommendationsItem {
            val details = Details(
                image = listTourismItem.image,
                city = "Unknown", // Assuming city is not available in ListTourismItem, you might need to handle this appropriately
                latitude = listTourismItem.latitude ?: "",
                name = listTourismItem.name,
                rating = 0.0, // Assuming rating is not available in ListTourismItem, you might need to handle this appropriately
                description = listTourismItem.description,
                id = listTourismItem.id,
                category = listTourismItem.category ?: "",
                longitude = listTourismItem.longitude ?: ""
            )

            return RecommendationsItem(
                details = details,
                id = listTourismItem.id
            )
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

    }


}