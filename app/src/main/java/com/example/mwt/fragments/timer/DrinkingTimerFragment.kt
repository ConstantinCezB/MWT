package com.example.mwt.fragments.timer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mwt.R
import com.example.mwt.recyclerview.ContainerLogRecyclerViewAdapter
import com.example.mwt.util.SHARED_PREFERENCE_FILE
import kotlinx.android.synthetic.main.drinking_timer_fragment.*
import kotlinx.android.synthetic.main.drinking_timer_fragment.view.*
import org.koin.android.viewmodel.ext.android.getViewModel

class DrinkingTimerFragment : Fragment() {

    private lateinit var viewModel: DrinkingTimerViewModel
    private lateinit var containerDrankRecyclerViewAdapter: ContainerLogRecyclerViewAdapter
    private var preference: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drinking_timer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = context?.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        view.recyclerDailyLogView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

        containerDrankRecyclerViewAdapter = ContainerLogRecyclerViewAdapter(viewModel, preference!!).also(recyclerDailyLogView::setAdapter)

        viewModel.getAllPosts().observe(viewLifecycleOwner, Observer {
            containerDrankRecyclerViewAdapter.submitList(it)
            showEmptyLogDisplay(it.size)
        })
    }

    private fun showEmptyLogDisplay (adapterSize: Int){
        if (adapterSize == 0) display_no_log.visibility = View.VISIBLE
        else display_no_log.visibility = View.GONE
    }
}