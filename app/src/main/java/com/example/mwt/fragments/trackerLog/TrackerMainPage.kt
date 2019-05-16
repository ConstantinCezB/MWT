package com.example.mwt.fragments.trackerLog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.mwt.R
import kotlinx.android.synthetic.main.fragment_tracker_main_page.view.*


class TrackerMainPage : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tracker_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.pagerTracker.adapter = PagerAdapter((context as FragmentActivity).supportFragmentManager)

    }


}