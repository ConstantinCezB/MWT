package com.example.mwt.fragments.trackerLog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.mwt.fragments.trackerLog.log.DrinkingLogFragment
import com.example.mwt.fragments.trackerLog.tracker.TrackerFragment


class PagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TrackerFragment()
            1 -> DrinkingLogFragment()
            else -> TrackerFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Tracker"
            1 -> "Log"
            else -> "Tracker"
        }
    }
}