package com.refapp.tester.views

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.refapp.tester.R
import com.refapp.tester.models.StringResult
import com.refapp.tester.services.NetworkInformationFactory
import kotlinx.android.synthetic.main.fragment_route_details.*

class RouteDetailsFragment() : Fragment(), IStringResultTaskListener  {
    private lateinit var taskWorker : RouteDetailTask
    override lateinit var progressBar: ProgressBar
    override lateinit var currentContext :Context

    override fun onAttach(context: Context){
        super.onAttach(context)
        currentContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_route_details, container, false)
        val btnSearch = view.findViewById<Button>(R.id.rdfButtonSearch)
        btnSearch.setOnClickListener{
            taskWorker = RouteDetailTask(this)
            this.hideKeyboard()
            val searchText = rdfTextEditSearch.text.toString()
            taskWorker.execute(searchText)
        }
        return view
    }

    override fun onTaskCompleted(result: StringResult) {
        rdfTextViewResults.text = ""
        rdfTextViewResults.text  = if (result.isError){
            result.debugMessage
        } else {
            result.result
        }
    }
}

class RouteDetailTask(private var taskListener: IStringResultTaskListener) : AsyncTask<String, StringResult, StringResult>() {
    private lateinit var factory: NetworkInformationFactory

    override fun onPreExecute() {
        super.onPreExecute()
        factory = NetworkInformationFactory(taskListener.currentContext)
        //tell progress bar to come up
    }

    override fun doInBackground(vararg arg: String?): StringResult {
        val hostname = arg[0]
        return when (hostname.isNullOrEmpty()) {
            true -> StringResult("", true, "Error - hostname is blank")
            false -> factory.getNetworkRouteInfo(hostname)
        }
    }

    override fun onPostExecute(result: StringResult) {
        super.onPostExecute(result)
        taskListener.onTaskCompleted(result)
    }
}

