package com.example.mwt.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.db.containeradddb.ContainersAddEntity
import com.example.mwt.util.inflate
import kotlinx.android.synthetic.main.layout_list_water_container_drinked.view.*


class ContainerAddRecyclerViewAdapter:
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

        fun bind(containersAddEntity: ContainersAddEntity) {
            with(itemView) {
                container_name.text = containersAddEntity.name
                water_drank.text = String.format("%s %s", resources.getString(R.string.water_drank), containersAddEntity.amount.toString())
                container_max.text = String.format("%s %s", resources.getString(R.string.water_drank_container_max), containersAddEntity.size.toString())
                date_drank_container.text = containersAddEntity.date
                progressBar.progress = (containersAddEntity.amount / containersAddEntity.size) *100
            }
        }
    }
}