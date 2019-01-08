package com.example.mwt.recyclerview

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
import com.example.mwt.db.MWTDatabase
import com.example.mwt.db.dailylogdb.DailyLogEntity
import com.example.mwt.db.containerdb.ContainersEntity
import com.example.mwt.fragments.tracker.TrackerViewModel
import com.example.mwt.util.inflate
import com.example.mwt.util.setInt
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.custom_dialog_option_tracker_frame.view.*
import kotlinx.android.synthetic.main.layout_list_item.view.*
import kotlinx.coroutines.experimental.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import java.util.*
import android.widget.SeekBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custom_dialog_tracker_frame.view.*

class ContainerRecyclerViewAdapter (private val viewModel: TrackerViewModel, private var preference: SharedPreferences, private val modeFavorite: Boolean) :
        ListAdapter<ContainersEntity, RecyclerView.ViewHolder>(diffCallback), KoinComponent {


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
        return if (modeFavorite) super.getItemCount()
        else super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (modeFavorite) {
            R.layout.layout_list_item
        } else {
            if(position == super.getItemCount()) R.layout.list_item_add else R.layout.layout_list_item
        }
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
            is ContainerRecyclerViewAdapter.ContainerViewHolder -> {
                val item = getItem(position)
                holder.bind(item)
            }
            is ContainerRecyclerViewAdapter.AddContainerViewHolder -> holder.bind()
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
                    addAmount(container)
                }
                itemView.setOnLongClickListener {
                    showDialogEdit(itemView, container)
                    true
                }
            }
        }

        private fun addAmount(container: ContainersEntity){
            val amountToAdd = ((viewModel.progress.toFloat() / 100.toFloat()) * container.size.toFloat()).toInt()
            preference.setInt(SHARED_PREFERENCE_NUMERATOR_DAILY,
                    amountToAdd + preference
                            .getInt(SHARED_PREFERENCE_NUMERATOR_DAILY, DEFAULT_NUMERATOR))
            launch {
                get<MWTDatabase>().dailyLogDao().save(
                        DailyLogEntity(
                                container.name,
                                amountToAdd,
                                container.size,
                                Calendar.getInstance().getTimeAndDate()))
            }
        }

        private fun showDialogEdit(view: View, container: ContainersEntity) {

            val mBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            val mView: View = LayoutInflater.from(view.context).inflate(R.layout.custom_dialog_option_tracker_frame, null)
            mBuilder.setView(mView)
            val dialog: AlertDialog = mBuilder.create()
            var visibilityEdit = false

            mView.editContainerNameEditScreen.text.append(container.name)

            mView.editContainerSizeEditScreen.text.append(container.size.toString())

            if (container.favorite) mView.add_to_favorite_buttom.text = "Rem Fav"

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

            mView.delete_btn.setOnClickListener {
                viewModel.deletePost(container)
                dialog.dismiss()
            }

            mView.cancel_edit_btn.setOnClickListener {
                dialog.dismiss()
            }

            mView.edit_container_accept_btn.setOnClickListener{
                val containerToEdit = ContainersEntity(mView.editContainerNameEditScreen.text.toString(),
                        mView.editContainerSizeEditScreen.text.toString().toInt(), favorite = container.favorite)

                containerToEdit.id = container.id
                viewModel.updatePost(containerToEdit)

                dialog.dismiss()
            }

            mView.add_to_favorite_buttom.setOnClickListener{
                if (container.favorite){
                    container.favorite = false
                    viewModel.updatePost(container)
                    mView.add_to_favorite_buttom.text = "Favorite"
                } else {
                    container.favorite = true
                    viewModel.updatePost(container)
                    mView.add_to_favorite_buttom.text = "Rem Fav"
                }
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
                        viewModel.savePost(ContainersEntity(mView.editContainerName.text.toString(),
                                mView.editContainerSize.text.toString().toInt()))
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