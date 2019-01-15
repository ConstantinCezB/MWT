package com.example.mwt.fragments.bmi

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mwt.R
import com.example.mwt.util.showContent
import kotlinx.android.synthetic.main.bmi_fragment.view.*
import java.util.*

class BMIFragment: Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bmi_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mDateListenor =

        view.constraintLayoutInput.setOnClickListener {
            view.constraintLayoutInputToDrop.showContent(view.bmi_input_edit_drop)
        }

        view.constraintLayoutActivity.setOnClickListener {
            view.constraintLayoutActivityToDrop.showContent(view.bmi_activity_edit_drop)
        }

        view.constraintLayoutChart.setOnClickListener {
            view.constraintLayoutChartToDrop.showContent(view.bmi_chart_edit_drop)
        }

        view.constraintLayoutLog.setOnClickListener {
            view.constraintLayoutLogToDrop.showContent(view.bmi_log_edit_drop)
        }

        view.datePickerDialogPrompt.setOnClickListener {
            showDateDialog()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun showDateDialog () {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        dialog.show()
    }


}


