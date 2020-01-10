package com.refapp.tester.views

import android.content.Context
import android.widget.ProgressBar
import com.refapp.tester.models.StringResult

interface IStringResultTaskListener {
    var progressBar: ProgressBar
    var currentContext: Context
    fun onTaskCompleted(result: StringResult)
}