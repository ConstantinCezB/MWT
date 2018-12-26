package com.example.mwt.fragments.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mwt.R
import com.example.mwt.recyclerview.ContainerAddRecyclerViewAdapter
import kotlinx.android.synthetic.main.tracker_fragment.*
import org.koin.android.viewmodel.ext.android.getViewModel

class DrinkingTimerFragment : Fragment() {

    private lateinit var viewModel: DrinkingTimerViewModel
    private lateinit var containerDrankRecyclerViewAdapter: ContainerAddRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.drinking_timer_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()

        containerDrankRecyclerViewAdapter = ContainerAddRecyclerViewAdapter().also(recyclerTimerView::setAdapter)

        viewModel.getAllPosts().observe(viewLifecycleOwner, Observer {

        })
    }
}