package com.example.mwt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(
        @LayoutRes layoutId: Int,
        inflater: LayoutInflater = LayoutInflater.from(context),
        attachToRoot: Boolean = false
): View {
    return inflater.inflate(layoutId, this, attachToRoot)
}
