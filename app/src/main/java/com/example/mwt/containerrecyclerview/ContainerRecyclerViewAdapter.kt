package com.example.mwt.containerrecyclerview

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.db.containerdb.ContainersEntity
import com.example.mwt.fragments.tracker.TrackerViewModel
import com.example.mwt.inflate
import com.example.mwt.livedata.setInt
import com.example.mwt.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custom_dialog_option_tracker_frame.view.*
import kotlinx.android.synthetic.main.custom_dialog_tracker_frame.view.*
import kotlinx.android.synthetic.main.layout_list_item.view.*

class ContainerRecyclerViewAdapter (private val viewModel: TrackerViewModel, private var preference: SharedPreferences) :
        ListAdapter<ContainersEntity, RecyclerView.ViewHolder>(diffCallback) {


    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<ContainersEntity>() {

            override fun areItemsTheSame(oldItem: ContainersEntity, newItem: ContainersEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ContainersEntity, newItem: ContainersEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == super.getItemCount()) R.layout.list_item_add else R.layout.layout_list_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(viewType)

        return when (viewType) {
            R.layout.layout_list_item -> ContainerViewHolder(view)
            R.layout.list_item_add -> AddContainerViewHolder(view)
            else -> throw IllegalArgumentException("Unknown view type $viewType.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContainerViewHolder -> {
                val item = getItem(position)
                holder.bind(item)
            }
            is AddContainerViewHolder -> holder.bind()
        }
    }

    private fun createSnackBar(mView: View, stringId: Int, duration: Int) {
        Snackbar.make(mView, stringId,
                duration)
                .show()
    }

    inner class ContainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        fun bind(container: ContainersEntity) {
            with(itemView) {
                item_name.text = container.name
                item_size.text = container.size.toString()
                itemView.setOnClickListener {
                    preference.setInt(SHARED_PREFERENCE_NUMERATOR_DAILY, item_size.text.toString().toInt() + preference.getInt(SHARED_PREFERENCE_NUMERATOR_DAILY, DEFAULT_NUMERATOR))
                }
                itemView.setOnLongClickListener {
                    showDialog(itemView)
                    true
                }
            }
        }

        private fun showDialog(view: View) {

            val mBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            val mView: View = LayoutInflater.from(view.context).inflate(R.layout.custom_dialog_option_tracker_frame, null)
            mBuilder.setView(mView)
            val dialog: AlertDialog = mBuilder.create()
            var visibilityEdit: Boolean = false

            mView.edit_drop.setOnClickListener{
                if(visibilityEdit){
                    it.background = mView.getResources().getDrawable(R.drawable.ic_arrow_drop_down)
                    mView.constraintLayoutEdit.visibility = View.GONE
                } else {
                    it.background = mView.getResources().getDrawable(R.drawable.ic_arrow_drop_up)
                    mView.constraintLayoutEdit.visibility = View.VISIBLE
                }
                visibilityEdit = !visibilityEdit
            }


            dialog.show()
        }
    }

    inner class AddContainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private fun showDialog(view: View) {

            val mBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            val mView: View = LayoutInflater.from(view.context).inflate(R.layout.custom_dialog_tracker_frame, null)
            mBuilder.setView(mView)
            val dialog: AlertDialog = mBuilder.create()

            mView.add_btn.setOnClickListener {
                if (mView.editContainerName.text.isNotBlank() && mView.editContainerSize.text.isNotBlank()) {
                    createSnackBar(mView, R.string.snackbar_success, Snackbar.LENGTH_SHORT)
                    Handler().postDelayed({
                        dialog.dismiss()
                        viewModel.savePost(ContainersEntity(mView.editContainerName.text.toString(), mView.editContainerSize.text.toString().toInt()))
                    }, 1500)
                } else {
                    createSnackBar(mView, R.string.snackbar_fail, Snackbar.LENGTH_SHORT)
                }
            }

            mView.cancel_btn.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        fun bind() {
            with(itemView) {
                itemView.setOnClickListener {
                    showDialog(itemView)
                }
            }
        }
    }


}