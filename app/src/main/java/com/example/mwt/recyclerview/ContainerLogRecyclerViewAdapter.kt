package com.example.mwt.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.db.containerdb.ContainersEntity
import com.example.mwt.db.dailylogdb.DailyLogEntity
import com.example.mwt.fragments.timer.DrinkingTimerViewModel
import com.example.mwt.util.inflate
import kotlinx.android.synthetic.main.layout_list_water_container_drinked.view.*
import kotlinx.android.synthetic.main.log_edit_frame.view.*


class ContainerLogRecyclerViewAdapter (private val viewModel: DrinkingTimerViewModel):
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
                progressBar.progress = ((dailyLogEntity.amount.toFloat() / dailyLogEntity.size.toFloat()) *100.0).toInt()
                itemView.setOnLongClickListener {
                    showDialogEdit(itemView, dailyLogEntity)
                    true
                }
            }
        }

        private fun showDialogEdit(view: View, dailyLogEntity: DailyLogEntity) {

            val mBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            val mView: View = LayoutInflater.from(view.context).inflate(R.layout.log_edit_frame, null)
            mBuilder.setView(mView)
            val dialog: AlertDialog = mBuilder.create()

            mView.daily_log_seekBar.progress = ((dailyLogEntity.amount.toFloat() / dailyLogEntity.size.toFloat())*100f).toInt()
            mView.container_name_log.text = dailyLogEntity.name

            mView.daily_log_cancel_btn.setOnClickListener {
                dialog.dismiss()
            }

            mView.daily_log_delete_btn.setOnClickListener{
                viewModel.deletePost(dailyLogEntity)
                dialog.dismiss()
            }

            mView.daily_log_refresh_btn.setOnClickListener {

                val dailyLogEntityToReplace = DailyLogEntity(dailyLogEntity.name,
                        ((mView.daily_log_seekBar.progress.toFloat() / 100f) * dailyLogEntity.size).toInt(),
                        dailyLogEntity.size,
                        dailyLogEntity.date)

                dailyLogEntityToReplace.id = dailyLogEntity.id

                viewModel.updatePost(dailyLogEntityToReplace)
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}