package com.example.mwt.fragments.timer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.recyclerview.ContainerLogRecyclerViewAdapter
import kotlinx.android.synthetic.main.drinking_timer_fragment.*
import kotlinx.android.synthetic.main.drinking_timer_fragment.view.*
import kotlinx.android.synthetic.main.tracker_fragment.*
import org.koin.android.viewmodel.ext.android.getViewModel

class DrinkingTimerFragment : Fragment() {

    private lateinit var viewModel: DrinkingTimerViewModel
    private lateinit var containerDrankRecyclerViewAdapter: ContainerLogRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drinking_timer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerDailyLogView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

        containerDrankRecyclerViewAdapter = ContainerLogRecyclerViewAdapter(viewModel).also(recyclerDailyLogView::setAdapter)

        viewModel.getAllPosts().observe(viewLifecycleOwner, Observer {
            containerDrankRecyclerViewAdapter.submitList(it)
            showEmptyLogDisplay(containerDrankRecyclerViewAdapter.itemCount)
        })
    }

    private fun showEmptyLogDisplay (adapterSize: Int){
        if (adapterSize == 0) display_no_log.visibility = View.VISIBLE
        else display_no_log.visibility = View.GONE
    }
}