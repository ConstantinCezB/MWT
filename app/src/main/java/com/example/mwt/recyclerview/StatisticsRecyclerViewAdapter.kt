package com.example.mwt.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.db.dateprogressdb.DateProgressEntity
import com.example.mwt.util.inflate
import kotlinx.android.synthetic.main.layout_list_statistics_item.view.*

class StatisticsRecyclerViewAdapter:
        ListAdapter<DateProgressEntity, RecyclerView.ViewHolder>(diffCallback) {

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<DateProgressEntity>() {

            override fun areItemsTheSame(oldItem: DateProgressEntity, newItem: DateProgressEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DateProgressEntity, newItem: DateProgressEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return  R.layout.layout_list_statistics_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(viewType)
        return StatisticsViewHolder (view)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StatisticsViewHolder -> {
                val item = getItem(position)
                holder.bind(item)
            }
        }
    }

    inner class StatisticsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dateProgressEntity: DateProgressEntity) {
            with(itemView) {
                date_statistics.text = dateProgressEntity.date
                water_drank_statistics.text = dateProgressEntity.progress.toString()
            }
        }
    }
}