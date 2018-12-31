package com.example.mwt.fragments.tracker

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mwt.R
import com.example.mwt.recyclerview.ContainerRecyclerViewAdapter
import kotlinx.android.synthetic.main.tracker_fragment.view.*
import android.content.Context.MODE_PRIVATE
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.recyclerview.ContainerFavoriteRecyclerViewAdapter
import com.example.mwt.util.intLiveData
import com.example.mwt.util.stringLiveData
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.tracker_fragment.*
import org.koin.android.viewmodel.ext.android.getViewModel

class TrackerFragment : Fragment() {

    private lateinit var viewModel: TrackerViewModel
    private lateinit var containerFavoriteRecyclerViewAdapter: ContainerFavoriteRecyclerViewAdapter
    private lateinit var containerRecyclerViewAdapter: ContainerRecyclerViewAdapter
    private var preference: SharedPreferences? = null
    private var visibilityEdit: Boolean = false



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tracker_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE)
        view.recyclerFavoriteContainerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.containerRecyclerView.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)

        preference?.intLiveData(SHARED_PREFERENCE_NUMERATOR_DAILY, DEFAULT_NUMERATOR)?.observe(this, Observer{
            view.drinking_progress_bar.setProgress((it.toFloat() / preference!!.getInt(SHARED_PREFERENCE_DENOMINATOR_DAILY, DEFAULT_DENOMINATOR).toFloat() * 100).toInt(), true)
            view.numerator_daily.text = it.toString()
            val percentage = (it.toFloat() / preference!!.getInt(SHARED_PREFERENCE_DENOMINATOR_DAILY, DEFAULT_DENOMINATOR).toFloat() * 100)
            view.percentage_daily.text =  String.format("%2.1f%%", percentage)

        })

        preference?.stringLiveData(TIME_INTERVAL_PREVIOUS_WORKER_DATE, DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE)?.observe(this,
                Observer {
                    view.current_day_text.text = it
                })

        view.pull_up_tab.setOnClickListener{
            if(visibilityEdit){
                pull_up.background = ContextCompat.getDrawable(context!!, R.drawable.ic_invert_colors_black_24dp)
                view.containersRecycler.visibility = View.GONE
                view.favorite_category_title.visibility = View.GONE
            } else {
                pull_up.background = ContextCompat.getDrawable(context!!, R.drawable.ic_invert_colors_off_black_24dp)
                view.containersRecycler.visibility = View.VISIBLE
                view.favorite_category_title.visibility = View.VISIBLE
            }
            visibilityEdit = !visibilityEdit
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

        containerFavoriteRecyclerViewAdapter = ContainerFavoriteRecyclerViewAdapter(viewModel, preference!!).also(recyclerFavoriteContainerView::setAdapter)

        containerRecyclerViewAdapter = ContainerRecyclerViewAdapter(viewModel, preference!!).also(containerRecyclerView::setAdapter)

        viewModel.getFavoriteContainers().observe(viewLifecycleOwner, Observer {
            containerFavoriteRecyclerViewAdapter.submitList(it)
        })

        viewModel.getNonFavoriteContainers().observe(viewLifecycleOwner, Observer{
            containerRecyclerViewAdapter.submitList(it)
        })
    }
}
