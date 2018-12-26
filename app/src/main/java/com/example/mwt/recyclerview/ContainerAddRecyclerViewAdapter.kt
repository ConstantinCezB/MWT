package com.example.mwt.recyclerview

import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.db.containeradddb.ContainersAddEntity
import com.example.mwt.fragments.timer.DrinkingTimerViewModel
import com.example.mwt.inflate
import kotlinx.android.synthetic.main.layout_list_water_container_drinked.view.*


class ContainerAddRecyclerViewAdapter (private val viewModel: DrinkingTimerViewModel, private var preference: SharedPreferences):
        ListAdapter<ContainersAddEntity, RecyclerView.ViewHolder>(diffCallback){

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<ContainersAddEntity>() {

            override fun areItemsTheSame(oldItem: ContainersAddEntity, newItem: ContainersAddEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ContainersAddEntity, newItem: ContainersAddEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
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

        fun bind(containersAddEntity: ContainersAddEntity) {
            with(itemView) {
                container_name.text = containersAddEntity.name
                water_drank.text = containersAddEntity.amount.toString()
                container_max.text = containersAddEntity.size.toString()
                date_drank_container.text = containersAddEntity.date
            }
        }
    }
}