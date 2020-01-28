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
import com.refapp.tester.services.DeviceInfoFactory
import com.refapp.tester.views.adapters.RvAdapterDeviceInfoList

class SettingFragment : Fragment() {
    private var factory : DeviceInfoFactory? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        factory = DeviceInfoFactory(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View = inflater.inflate(R.layout.fragment_setting, container, false)
        val activity: Context = activity as Context
        val rv = view.findViewById<RecyclerView>(R.id.deviceInfoRecycleView)
        rv.layoutManager = LinearLayoutManager(activity)
        val f = factory as DeviceInfoFactory
        val adapter = RvAdapterDeviceInfoList(f.getDeviceInfo())
        rv.adapter = adapter

        return view
    }


}
