package com.example.mwt.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.db.dailylogdb.DailyLogEntity
import com.example.mwt.util.inflate
import kotlinx.android.synthetic.main.layout_list_water_container_drinked.view.*


class ContainerAddRecyclerViewAdapter:
        ListAdapter<DailyLogEntity, RecyclerView.ViewHolder>(diffCallback){

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<DailyLogEntity>() {

            override fun areItemsTheSame(oldItem: DailyLogEntity, newItem: DailyLogEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DailyLogEntity, newItem: DailyLogEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return  R.layout.layout_list_water_container_drinked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(viewType)
        return ContainerDrankViewHolder (view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContainerDrankViewHolder -> {
                val item = getItem(position)
                holder.bind(item)
            }
        }
    }

    inner class ContainerDrankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dailyLogEntity: DailyLogEntity) {
            with(itemView) {
                container_name.text = dailyLogEntity.name
                water_drank.text = String.format("%s %s", resources.getString(R.string.water_drank), dailyLogEntity.amount.toString())
                container_max.text = String.format("%s %s", resources.getString(R.string.water_drank_container_max), dailyLogEntity.size.toString())
                date_drank_container.text = dailyLogEntity.date
                progressBar.progress = (dailyLogEntity.amount / dailyLogEntity.size) *100
            }
        }
    }
}