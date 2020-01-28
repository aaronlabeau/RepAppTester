package com.refapp.tester.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eygsl.ctmob.speedup.R
import com.refapp.tester.services.NetworkInformationFactory
import com.refapp.tester.views.adapters.RvAdapterNetworkInfoList

class NetworkFragment : Fragment() {
    private var factory : NetworkInformationFactory? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        factory = NetworkInformationFactory(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_network, container, false)
        val activity = activity as Context
        val rv = view.findViewById<RecyclerView>(R.id.networkInfoRecycleView)
        rv.layoutManager = LinearLayoutManager(activity)
        val f = factory as NetworkInformationFactory
        rv.adapter = RvAdapterNetworkInfoList(f.getNetworkInformation())
        return view
    }


}
