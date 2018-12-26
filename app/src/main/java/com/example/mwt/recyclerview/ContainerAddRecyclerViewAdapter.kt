package com.example.mwt.recyclerview

import android.content.SharedPreferences
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.db.containeradddb.ContainersAddEntity
import com.example.mwt.fragments.timer.DrinkingTimerViewModel


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}