package com.refapp.tester.views

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import com.eygsl.ctmob.speedup.R
import com.refapp.tester.models.NetworkToolsOption
import com.refapp.tester.models.StringResult
import com.refapp.tester.services.NetworkInformationFactory
import kotlinx.android.synthetic.main.fragment_network_tools.*

class NetworkToolsFragment : Fragment(), IStringResultTaskListener {
    private lateinit var taskWorker: NetworkToolsTask
    private lateinit var btnSearch: Button
    override lateinit var currentContext: Context
    override lateinit var progressBar: ProgressBar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_network_tools, container, false)
        btnSearch = view.findViewById(R.id.ntfButtonSearch)
        progressBar = view.findViewById(R.id.ntfProgressBar)
        btnSearch.setOnClickListener {
            val selectedOption = when (ntfRadioGroup.checkedRadioButtonId) {
                R.id.ntfRadioButtonPing -> NetworkToolsOption.Ping
                else -> NetworkToolsOption.Nslookup
            }
            taskWorker = NetworkToolsTask(this, selectedOption)
            this.hideKeyboard()
            val searchText = ntfTextEditSearch.text.toString()
            ntfTextViewResults.text = ""
            btnSearch.isEnabled = false
            taskWorker.execute(searchText)
        }
        return view
    }

    override fun onTaskCompleted(result: StringResult) {
        btnSearch.isEnabled = true
        ntfTextViewResults.text = if (result.isError) {
            result.debugMessage
        } else {
            result.result
        }
    }
}

class NetworkToolsTask(
    private var taskListener: IStringResultTaskListener,
    private var selectedOption: NetworkToolsOption
) : AsyncTask<String, StringResult, StringResult>() {
    private lateinit var factory: NetworkInformationFactory

    override fun onPreExecute() {
        super.onPreExecute()
        factory = NetworkInformationFactory(taskListener.currentContext)
        //tell progress bar to come up
        taskListener.progressBar.isIndeterminate = true
        taskListener.progressBar.visibility = View.VISIBLE

    }

    override fun doInBackground(vararg arg: String?): StringResult {
        val hostname = arg[0]
        return if (!hostname.isNullOrEmpty()) {
            when (selectedOption) {
                NetworkToolsOption.Ping -> {
                    factory.getNetworkPingResult(hostname)
                }
                NetworkToolsOption.Traceroute -> {
                    factory.getNetworkTracerouteResult(hostname)
                }
                else -> {
                    factory.getNslookupResult(hostname)
                }
            }
        } else {
            StringResult("", true, "hostname is blank or empty")
        }
    }

    override fun onPostExecute(result: StringResult) {
        super.onPostExecute(result)
        taskListener.progressBar.visibility = View.INVISIBLE
        taskListener.onTaskCompleted(result)
    }
}
