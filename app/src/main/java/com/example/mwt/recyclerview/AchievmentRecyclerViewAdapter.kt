package com.example.mwt.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.db.achievementdb.AchievementsEntity
import com.example.mwt.util.inflate
import kotlinx.android.synthetic.main.layout_list_achievment_item.view.*

class AchievmentRecyclerViewAdapter:
        ListAdapter<AchievementsEntity, RecyclerView.ViewHolder>(diffCallback) {

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<AchievementsEntity>() {

            override fun areItemsTheSame(oldItem: AchievementsEntity, newItem: AchievementsEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AchievementsEntity, newItem: AchievementsEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return  R.layout.layout_list_achievment_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(viewType)
        return AchievementViewHolder (view)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AchievementViewHolder -> {
                val item = getItem(position)
                holder.bind(item)
            }
        }
    }

    inner class AchievementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(achievementsEntity: AchievementsEntity) {
            with(itemView) {
                goal_item_header.text = achievementsEntity.itemHeader
                goal_item_info.text = achievementsEntity.itemDateValue
            }
        }
    }
}