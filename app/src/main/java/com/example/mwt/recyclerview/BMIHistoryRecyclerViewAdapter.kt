package com.example.mwt.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.db.bmiRecordsdb.BMIRecordEntity
import com.example.mwt.util.inflate
import kotlinx.android.synthetic.main.layout_list_bmi_history_item.view.*

class BMIHistoryRecyclerViewAdapter :
        ListAdapter<BMIRecordEntity, RecyclerView.ViewHolder>(diffCallback) {

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<BMIRecordEntity>() {

            override fun areItemsTheSame(oldItem: BMIRecordEntity, newItem: BMIRecordEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BMIRecordEntity, newItem: BMIRecordEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.layout_list_bmi_history_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(viewType)
        return BMIRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BMIRecordViewHolder -> {
                val item = getItem(position)
                holder.bind(item)
            }
        }
    }

    inner class BMIRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bmiRecordEntity: BMIRecordEntity) {
            with(itemView) {
                bmi_amount.text = bmiRecordEntity.bmi.toString()
                bmi_date.text = bmiRecordEntity.date
            }
        }
    }
}