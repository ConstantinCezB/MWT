package com.example.mwt.fragments.tracker

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mwt.R
import com.example.mwt.containerrecyclerview.ContainerRecyclerViewAdapter
import kotlinx.android.synthetic.main.tracker_fragment.view.*
import android.content.Context.MODE_PRIVATE
import com.example.mwt.livedata.intLiveData
import com.example.mwt.util.*
import kotlinx.android.synthetic.main.tracker_fragment.*

class TrackerFragment : Fragment() {

    private lateinit var viewModel: TrackerViewModel
    private lateinit var containerRecyclerViewAdapter: ContainerRecyclerViewAdapter
    private var preference: SharedPreferences? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tracker_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, MODE_PRIVATE)
        view.recyclerContainerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        preference?.intLiveData(SHARED_PREFERENCE_NUMERATOR_DAILY, DEFAULT_NUMERATOR)?.observe(this, Observer{
            view.drinking_progress_bar.setProgress((it.toFloat() / preference!!.getInt(SHARED_PREFERENCE_DENOMINATOR_DAILY, DEFAULT_DENOMINATOR).toFloat() * 100).toInt(), true)
            view.numerator_daily.text = it.toString()
            view.purcentage_daily.text = (it.toFloat() / preference!!.getInt(SHARED_PREFERENCE_DENOMINATOR_DAILY, DEFAULT_DENOMINATOR).toFloat() * 100).toString() + "%"
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TrackerViewModel::class.java)

        containerRecyclerViewAdapter = ContainerRecyclerViewAdapter(viewModel, preference!!).also(recyclerContainerView::setAdapter)

        viewModel.getAllPosts().observe(viewLifecycleOwner, Observer(containerRecyclerViewAdapter::submitList))
    }
}
